-- Rules whose level is not probHigh/probMedium/probLow, for the occupational epidemiologist to review
-- (update the level, or remove the rule). Read-only.
-- Levels: 3 = probUnknown, 4 = possUnknown, 5 = noExposure.
-- Filtered to study agents (SYS_CONFIG type 'studyagent'); delete the marked lines to include every agent.
--
-- Query 1 - rules with linked answers: one row per rule per module. A rule's conditions can span more
-- than one module (e.g. a job module plus a task-module fragment), so such a rule appears once for each
-- module it touches. Starts from Node_Rule (the answer links, ~28k rows) rather than Rule (~1.5 million
-- rows, mostly noExposure with no answers linked), which is what keeps it fast.
--
-- Query 2 - rules with NO linked answers: counts per agent and level only. They have no conditions to
-- review (and so can't fire), which makes them candidates for removal as a group rather than one by one.

SET SESSION group_concat_max_len = 100000;

-- Query 1
SELECT r.idRule,
       a.name AS agent,
       CASE r.level WHEN 3 THEN 'probUnknown' WHEN 4 THEN 'possUnknown' WHEN 5 THEN 'noExposure' END AS level,
       IFNULL(nt.name, '?') AS module,
       GROUP_CONCAT(n.number ORDER BY n.number SEPARATOR ', ') AS node_numbers,
       GROUP_CONCAT(CONCAT(n.number, ' ', IFNULL(q.name, '?'), ' -> ', n.name,
                           IF(n.deleted = 1, ' [answer deleted]', ''))
                    ORDER BY n.number SEPARATOR ' | ') AS conditions,
       r.lastUpdated
FROM Node_Rule nr
JOIN Rule r ON r.idRule = nr.idRule
JOIN AgentInfo a ON a.idAgent = r.agentId
JOIN Node n ON n.idNode = nr.idNode
LEFT JOIN Node q ON q.idNode = CAST(n.parent_idNode AS UNSIGNED)
LEFT JOIN Node nt ON nt.idNode = n.topNodeId
WHERE r.deleted = 0
  AND r.level IN (3, 4, 5)
  AND r.agentId IN (SELECT CAST(value AS UNSIGNED) FROM SYS_CONFIG WHERE type = 'studyagent')  -- study agents only; delete this line for all agents
GROUP BY r.idRule, a.name, r.level, nt.idNode, nt.name, r.lastUpdated
ORDER BY a.name, r.level, module, r.idRule;

-- Query 2 (total minus linked per agent/level - much faster than NOT EXISTS over ~1.5 million rules)
SELECT a.name AS agent,
       CASE t.level WHEN 3 THEN 'probUnknown' WHEN 4 THEN 'possUnknown' WHEN 5 THEN 'noExposure' END AS level,
       t.total - IFNULL(l.linked, 0) AS rules_without_linked_answers
FROM (SELECT agentId, level, COUNT(*) AS total
      FROM Rule
      WHERE deleted = 0 AND level IN (3, 4, 5)
        AND agentId IN (SELECT CAST(value AS UNSIGNED) FROM SYS_CONFIG WHERE type = 'studyagent')  -- study agents only; delete this line for all agents
      GROUP BY agentId, level) t
LEFT JOIN (SELECT r.agentId, r.level, COUNT(DISTINCT r.idRule) AS linked
           FROM Node_Rule nr
           JOIN Rule r ON r.idRule = nr.idRule
           WHERE r.deleted = 0 AND r.level IN (3, 4, 5)
             AND r.agentId IN (SELECT CAST(value AS UNSIGNED) FROM SYS_CONFIG WHERE type = 'studyagent')  -- study agents only; delete this line for all agents
           GROUP BY r.agentId, r.level) l
       ON l.agentId = t.agentId AND l.level = t.level
JOIN AgentInfo a ON a.idAgent = t.agentId
WHERE t.total > IFNULL(l.linked, 0)
ORDER BY a.name, t.level;
