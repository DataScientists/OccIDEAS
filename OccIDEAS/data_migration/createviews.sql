DROP VIEW IF EXISTS ModuleRule;
CREATE VIEW ModuleRule AS 
SELECT concat(m.idNode, ':',nr.idRule, ':',r.agentId, ':',n.idNode)  as primaryKey,
 m.idNode as idModule, 
 m.name as moduleName, 
 nr.idRule,
 r.level as ruleLevel,
 r.agentId as idAgent, 
 a.name as agentName, 
 n.idNode,
 n.number as nodeNumber
FROM Node n
INNER JOIN Node m ON n.topNodeId = m.idNode 
INNER JOIN Node_Rule nr ON n.idNode = nr.idNode
INNER JOIN Rule r ON nr.idRule = r.idRule
INNER JOIN AgentInfo a ON r.agentId = a.idAgent
WHERE m.deleted=0