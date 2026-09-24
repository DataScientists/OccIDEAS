(function() {
  angular.module('occIDEASApp.Interviews').service('IndividualReportService',
    IndividualReportService);

  function IndividualReportService() {

    // Fallback wording used when a rule has no admin-authored rationale text yet.
    // Calibrated to the rule's actual confidence level, not to imply more certainty than exists.
    // No fallback exists for probHigh - an evidence claim shouldn't be fabricated generically,
    // so a probHigh finding simply shows no rationale line until an admin writes one.
    var DEFAULT_RATIONALE_TEXT = {
      probMedium: 'Your answers point to a possible link to {agent}. The evidence at this level of ' +
        'exposure isn’t conclusive, so this is noted rather than flagged as a concern.',
      probLow: 'Your answers show a low-probability link to {agent}. This is a minor signal in your ' +
        'answers, not a confirmed finding.'
    };

    // Builds the individual-facing summary: a binary "exposure indicated" verdict driven only by
    // PROBABLE_HIGH rules, plus a lower-key list of anything else noted. Only PROBABLE_HIGH/MEDIUM/LOW
    // are shown - NO_EXPOSURE is a clear finding with nothing to explain, and PROBABLE_UNKNOWN/
    // POSSIBLE_UNKNOWN mean the automated rules couldn't confidently classify the exposure at all
    // (that's what triggers a manual assessment) - surfacing those here with the same calibrated
    // language as a real low/medium finding would overstate what this automated screening determined,
    // and manual assessment is out of scope for this individual self-report. The full technical
    // breakdown (all levels, all conditions) remains available to the employer via the PDF/email report.
    //
    // Agent names come from the fired rule's own agent, not the study-agent list: that list only
    // holds the study's agents (SYS_CONFIG 'studyagent'), so a rule for any other agent has no entry
    // there. studyAgents is used only to flag such findings (studyAgent: false) - callers decide
    // whether to surface that (the fired rules page does; the participant report doesn't).
    //
    // options.collapseByAgent (the public view): for each agent only its highest-severity finding is
    // kept - a high finding trumps that agent's medium/low ones, and a medium trumps its low - since
    // "high" alongside "low probability link" for the same substance reads as a contradiction. If
    // more than one rule fires at that same top level (e.g. two probLow rules for the same agent),
    // they're merged into the one displayed finding rather than shown twice. Assessors leave it off
    // to see every fired rule individually. It doesn't affect the verdict (high already drives it)
    // or manualReviewAgents.
    //
    // manualReviewAgents lists the agents behind those unknown-level rules. It's not part of the
    // participant-facing report; callers with an assessor audience (the fired rules page) can surface it.
    //
    // options.allStudyAgents (the full study agent list, AgentsService.getStudyAgents) gives
    // notIdentifiedAgents: study agents with no high/medium/low finding and no unknown-level rule -
    // an unknown means the rules couldn't decide, which isn't the same as nothing identified.
    var SEVERITY_RANK = {probHigh: 3, probMedium: 2, probLow: 1};

    this.build = function(firedRules, studyAgents, options) {
      var collapseByAgent = !!(options && options.collapseByAgent);

      var studyAgentsById = {};
      _.each(studyAgents, function(agent) {
        studyAgentsById[agent.idAgent] = agent;
      });

      function agentNameFor(rule) {
        var studyAgent = studyAgentsById[rule.agentId];
        return (rule.agent && rule.agent.name) || (studyAgent && studyAgent.name) || 'Unknown';
      }

      function isStudyAgent(rule) {
        return !!studyAgentsById[rule.agentId];
      }

      var high = [];
      var other = [];
      var manualReviewNames = [];
      // Only used when collapseByAgent - tracks the one finding already shown per agent in each
      // bucket, so a second rule firing at the same top severity (e.g. two probLow rules for the
      // same agent) merges into it instead of showing that agent twice.
      var highByAgent = {};
      var otherByAgent = {};

      // Highest severity fired per agent, for collapseByAgent.
      var topRankByAgent = {};
      _.each(firedRules, function(rule) {
        var rank = SEVERITY_RANK[rule.level] || 0;
        if (rank > (topRankByAgent[rule.agentId] || 0)) {
          topRankByAgent[rule.agentId] = rank;
        }
      });

      _.each(firedRules, function(rule) {
        if (rule.level === 'probUnknown' || rule.level === 'possUnknown') {
          manualReviewNames.push(agentNameFor(rule));
          return;
        }
        if (rule.level !== 'probHigh' && rule.level !== 'probMedium' && rule.level !== 'probLow') {
          return;
        }
        if (collapseByAgent && SEVERITY_RANK[rule.level] < topRankByAgent[rule.agentId]) {
          return;
        }
        var agentName = agentNameFor(rule);

        if (rule.level === 'probHigh') {
          if (collapseByAgent && highByAgent[rule.agentId]) {
            highByAgent[rule.agentId].conditions = highByAgent[rule.agentId].conditions.concat(rule.conditions || []);
            return;
          }
          var highFinding = {
            agentName: agentName,
            studyAgent: isStudyAgent(rule),
            rationale: rule.rationale || null,
            level: rule.level,
            conditions: rule.conditions || []
          };
          high.push(highFinding);
          if (collapseByAgent) {
            highByAgent[rule.agentId] = highFinding;
          }
        } else {
          var text = rule.rationale;
          if (!text) {
            var template = DEFAULT_RATIONALE_TEXT[rule.level];
            text = template ? template.split('{agent}').join(agentName) : null;
          }
          if (text) {
            if (collapseByAgent && otherByAgent[rule.agentId]) {
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
            if (collapseByAgent) {
              otherByAgent[rule.agentId] = otherFinding;
            }
          }
        }
      });

      // Any rule at these levels means the agent wasn't cleared (unknown = couldn't be decided).
      var notClearedAgentIds = {};
      _.each(firedRules, function(rule) {
        if (SEVERITY_RANK[rule.level] || rule.level === 'probUnknown' || rule.level === 'possUnknown') {
          notClearedAgentIds[rule.agentId] = true;
        }
      });
      var notIdentified = _.sortBy(_.uniq(_.map(_.filter(options && options.allStudyAgents, function(agent) {
        return !notClearedAgentIds[agent.idAgent];
      }), 'name')), function(name) {
        return name.toLowerCase();
      });

      return {
        // The verdict itself is binary - either estimated above the safety threshold or not.
        // Lower-confidence findings are supplementary detail shown afterward, regardless of which
        // verdict applies - not a third competing result.
        verdictState: high.length > 0 ? 'flagged' : 'clear',
        highFindings: high,
        otherFindings: other,
        manualReviewAgents: _.uniq(manualReviewNames),
        notIdentifiedAgents: notIdentified
      };
    };
  }
})();
