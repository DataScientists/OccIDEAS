(function() {
  angular.module('occIDEASApp.Interviews').service('IndividualReportService',
    IndividualReportService);

  function IndividualReportService() {

    // Assessor view only: fallback wording used when a rule has no admin-authored rationale text yet.
    // Follows the agreed scale (legal/health-data-position.md section 3): probMedium = likely below the
    // exposure limit, probLow = estimated to be well below it - never implying the exposure is harmless.
    // No fallback exists for probHigh - an evidence claim shouldn't be fabricated generically,
    // so a probHigh finding simply shows no rationale line until an admin writes one.
    var DEFAULT_RATIONALE_TEXT = {
      probMedium: 'Based on your answers, your exposure to {agent} is likely to be below the exposure ' +
        'limit. This is an estimate from what you told us, not a measurement.',
      probLow: 'Based on your answers, your exposure to {agent} is estimated to be well below the ' +
        'exposure limit. This doesn’t mean there is no exposure.'
    };

    // Public view: each finding is described per agent, never per rule, so the participant isn't
    // told which answers or questions triggered it (the occupational epidemiologist's advice - the
    // rule-specific Rule.rationale stays in the assessor view). The text is the agent's
    // publicDescription plus a sentence for the level. probHigh gets no level sentence - the verdict
    // above it already says the exposure is likely above the limit and recommends a health professional.
    var PUBLIC_LEVEL_TEXT = {
      probHigh: '',
      probMedium: ' Based on your answers, your exposure is likely to be below the exposure limit. ' +
        'This is an estimate from what you told us, not a measurement.',
      probLow: ' Based on your answers, your exposure is estimated to be well below the exposure limit. ' +
        'This doesn’t mean there is no exposure.'
    };

    function publicText(agentName, publicDescription, level) {
      return 'Your answers indicate that you may have been exposed to ' + agentName +
        (publicDescription ? ', ' + publicDescription : '') + '.' + PUBLIC_LEVEL_TEXT[level];
    }

    // Builds the individual-facing summary: a verdict driven by the highest level found (see
    // verdictState below), plus the findings behind it. Only PROBABLE_HIGH/MEDIUM/LOW
    // are shown - NO_EXPOSURE is a clear finding with nothing to explain, and PROBABLE_UNKNOWN/
    // POSSIBLE_UNKNOWN mean the automated rules couldn't confidently classify the exposure at all
    // (that's what triggers a manual assessment) - surfacing those here with the same calibrated
    // language as a real low/medium finding would overstate what this automated screening determined,
    // and manual assessment is out of scope for this individual self-report. The full technical
    // breakdown (all levels, all conditions) remains available to the employer via the PDF/email report.
    //
    // Agent names come from the fired rule's own agent, not the study-agent list: that list only
    // holds the study's agents (SYS_CONFIG 'studyagent'), so a rule for any other agent has no entry
    // there. studyAgents is used only to flag such findings (studyAgent: false), which the assessor
    // view badges. The public view leaves non-study agents out entirely (see publicStudyAgentIds).
    //
    // options.publicView: findings are worded per agent (see PUBLIC_LEVEL_TEXT) and collapsed - for
    // each agent only its highest-severity finding is kept - a high finding trumps that agent's medium/low ones, and a medium trumps its low - since
    // "high" alongside "low probability link" for the same substance reads as a contradiction. If
    // more than one rule fires at that same top level (e.g. two probLow rules for the same agent),
    // they're merged into the one displayed finding rather than shown twice. Assessors leave it off
    // to see every fired rule individually, each with its own Rule.rationale. It doesn't affect the verdict (each agent's top level is
    // always kept) or manualReviewAgents.
    //
    // manualReviewAgents lists the agents behind those unknown-level rules. It's not part of the
    // participant-facing report; callers with an assessor audience (the fired rules page) can surface it.
    //
    // options.allStudyAgents (the full study agent list, AgentsService.getStudyAgents) gives
    // notIdentifiedAgents: study agents with no high/medium/low finding and no unknown-level rule -
    // an unknown means the rules couldn't decide, which isn't the same as nothing identified.
    var SEVERITY_RANK = {probHigh: 3, probMedium: 2, probLow: 1};

    this.build = function(firedRules, studyAgents, options) {
      var publicView = !!(options && options.publicView);

      var studyAgentsById = {};
      _.each(studyAgents, function(agent) {
        studyAgentsById[agent.idAgent] = agent;
      });

      function agentNameFor(rule) {
        var studyAgent = studyAgentsById[rule.agentId];
        return (rule.agent && rule.agent.name) || (studyAgent && studyAgent.name) || 'Unknown';
      }

      function publicDescriptionFor(rule) {
        var studyAgent = studyAgentsById[rule.agentId];
        return (rule.agent && rule.agent.publicDescription) || (studyAgent && studyAgent.publicDescription) || null;
      }

      function isStudyAgent(rule) {
        return !!studyAgentsById[rule.agentId];
      }

      // The public view only reports study agents (options.allStudyAgents), in both the findings and
      // the verdict - the same list the "no exposures identified" section uses - so a rule firing for
      // an agent outside the study (e.g. a region-specific variant like Ocular UV EU) isn't shown to
      // the participant. The assessor view keeps every fired rule, badging the non-study ones. If the
      // study agent list is empty (lookup failed or none configured) nothing is filtered, rather than
      // the participant getting an empty report.
      var publicStudyAgentIds = null;
      if (publicView && options.allStudyAgents && options.allStudyAgents.length > 0) {
        publicStudyAgentIds = {};
        _.each(options.allStudyAgents, function(agent) {
          publicStudyAgentIds[agent.idAgent] = true;
        });
      }
      var reportedRules = publicStudyAgentIds ? _.filter(firedRules, function(rule) {
        return publicStudyAgentIds[rule.agentId];
      }) : firedRules;

      var high = [];
      var other = [];
      var manualReviewNames = [];
      // Only used in the public view - tracks the one finding already shown per agent in each
      // bucket, so a second rule firing at the same top severity (e.g. two probLow rules for the
      // same agent) merges into it instead of showing that agent twice.
      var highByAgent = {};
      var otherByAgent = {};

      // Highest severity fired per agent, for the public view.
      var topRankByAgent = {};
      _.each(reportedRules, function(rule) {
        var rank = SEVERITY_RANK[rule.level] || 0;
        if (rank > (topRankByAgent[rule.agentId] || 0)) {
          topRankByAgent[rule.agentId] = rank;
        }
      });

      _.each(reportedRules, function(rule) {
        if (rule.level === 'probUnknown' || rule.level === 'possUnknown') {
          manualReviewNames.push(agentNameFor(rule));
          return;
        }
        if (rule.level !== 'probHigh' && rule.level !== 'probMedium' && rule.level !== 'probLow') {
          return;
        }
        if (publicView && SEVERITY_RANK[rule.level] < topRankByAgent[rule.agentId]) {
          return;
        }
        var agentName = agentNameFor(rule);

        if (rule.level === 'probHigh') {
          if (publicView && highByAgent[rule.agentId]) {
            highByAgent[rule.agentId].conditions = highByAgent[rule.agentId].conditions.concat(rule.conditions || []);
            return;
          }
          var highFinding = {
            agentName: agentName,
            studyAgent: isStudyAgent(rule),
            rationale: publicView ? publicText(agentName, publicDescriptionFor(rule), rule.level)
              : (rule.rationale || null),
            level: rule.level,
            conditions: rule.conditions || []
          };
          high.push(highFinding);
          if (publicView) {
            highByAgent[rule.agentId] = highFinding;
          }
        } else {
          var text = publicView ? publicText(agentName, publicDescriptionFor(rule), rule.level) : rule.rationale;
          if (!text) {
            var template = DEFAULT_RATIONALE_TEXT[rule.level];
            text = template ? template.split('{agent}').join(agentName) : null;
          }
          if (text) {
            if (publicView && otherByAgent[rule.agentId]) {
              otherByAgent[rule.agentId].conditions = otherByAgent[rule.agentId].conditions.concat(rule.conditions || []);
              return;
            }
            var otherFinding = {
              agentName: agentName,
              studyAgent: isStudyAgent(rule),
              level: rule.level,
              text: text,
              conditions: rule.conditions || []
            };
            other.push(otherFinding);
            if (publicView) {
              otherByAgent[rule.agentId] = otherFinding;
            }
          }
        }
      });

      // Any rule at these levels means the agent wasn't cleared (unknown = couldn't be decided).
      var notClearedAgentIds = {};
      _.each(reportedRules, function(rule) {
        if (SEVERITY_RANK[rule.level] || rule.level === 'probUnknown' || rule.level === 'possUnknown') {
          notClearedAgentIds[rule.agentId] = true;
        }
      });
      var notIdentified = _.sortBy(_.uniq(_.map(_.filter(options && options.allStudyAgents, function(agent) {
        return !notClearedAgentIds[agent.idAgent];
      }), 'name')), function(name) {
        return name.toLowerCase();
      });

      // The verdict follows the highest level found across all agents: 'flagged' (probHigh - likely
      // above the exposure limit), 'medium' (probMedium - likely below it), 'low' (probLow - estimated
      // to be well below it) or 'clear' (nothing found). Findings below the headline level
      // (e.g. a low for another agent under a medium verdict) are shown as "also identified".
      var verdictState = 'clear';
      if (high.length > 0) {
        verdictState = 'flagged';
      } else if (_.some(other, {level: 'probMedium'})) {
        verdictState = 'medium';
      } else if (other.length > 0) {
        verdictState = 'low';
      }

      return {
        verdictState: verdictState,
        highFindings: high,
        otherFindings: other,
        manualReviewAgents: _.uniq(manualReviewNames),
        notIdentifiedAgents: notIdentified
      };
    };
  }
})();
