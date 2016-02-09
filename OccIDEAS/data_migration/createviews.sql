DROP VIEW IF EXISTS ModuleRule;
CREATE VIEW ModuleRule AS 
SELECT concat(m.idNode, ':',nr.idRule, ':',r.agentId, ':',n.idNode)  as primaryKey,
 m.idNode as idModule, 
 nr.idRule,
 r.agentId as idAgent, 
 n.idNode
FROM Node n
INNER JOIN Node m ON n.topNodeId = m.idNode 
INNER JOIN Node_Rule nr ON n.idNode = nr.idNode
INNER JOIN Rule r ON nr.idRule = r.idRule