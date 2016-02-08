DROP VIEW IF EXISTS ModuleRules;
CREATE VIEW ModuleRules AS 
SELECT m.idNode as idModule, nr.idRule, r.agentId as idAgent
FROM Node n
INNER JOIN Node m ON n.topNodeId = m.idNode 
INNER JOIN Node_Rule nr ON n.idNode = nr.idNode
INNER JOIN Rule r ON nr.idRule = r.idRule