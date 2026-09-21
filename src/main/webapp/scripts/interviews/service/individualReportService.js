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
    // manualReviewAgents lists the agents behind those unknown-level rules. It's not part of the
    // participant-facing report; callers with an assessor audience (the fired rules page) can surface it.
    this.build = function(firedRules, studyAgents) {
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

      _.each(firedRules, function(rule) {
        if (rule.level === 'probUnknown' || rule.level === 'possUnknown') {
          manualReviewNames.push(agentNameFor(rule));
          return;
        }
        if (rule.level !== 'probHigh' && rule.level !== 'probMedium' && rule.level !== 'probLow') {
          return;
        }
        var agentName = agentNameFor(rule);

        if (rule.level === 'probHigh') {
          high.push({
            agentName: agentName,
            studyAgent: isStudyAgent(rule),
            rationale: rule.rationale || null,
            level: rule.level,
            conditions: rule.conditions || []
          });
        } else {
          var text = rule.rationale;
          if (!text) {
            var template = DEFAULT_RATIONALE_TEXT[rule.level];
            text = template ? template.split('{agent}').join(agentName) : null;
          }
          if (text) {
            other.push({
              agentName: agentName,
              studyAgent: isStudyAgent(rule),
              level: rule.level,
              text: text,
              conditions: rule.conditions || []
            });
          }
        }
      });

      return {
        // The verdict itself is binary - either estimated above the safety threshold or not.
        // Lower-confidence findings are supplementary detail shown afterward, regardless of which
        // verdict applies - not a third competing result.
        verdictState: high.length > 0 ? 'flagged' : 'clear',
        highFindings: high,
        otherFindings: other,
        manualReviewAgents: _.uniq(manualReviewNames)
      };
    };
  }
})();
