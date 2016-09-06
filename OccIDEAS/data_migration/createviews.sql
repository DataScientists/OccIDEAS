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
WHERE m.deleted=0 and n.deleted=0 and r.deleted=0;


DROP VIEW IF EXISTS InterviewIntroModule_Module;
CREATE VIEW InterviewIntroModule_Module AS 
SELECT 
concat(m.idNode,':',iq.id) AS 
primaryKey,
m.idNode as idModule, 
m.name as introModuleNodeName, 
iq.id as interviewPrimaryKey,
iq.idInterview as interviewId,
iq.name as interviewModuleName
from Node m  
INNER JOIN Interview_Question iq
where m.idNode = iq.link
and (iq.type='Q_linkedmodule' OR iq.type='M_IntroModule') 
AND iq.deleted=0;

DROP VIEW IF EXISTS InterviewModule_Fragment;
CREATE VIEW InterviewModule_Fragment AS 
SELECT 
concat(m.idNode,':',iq.id) AS 
primaryKey,
m.idNode as idFragment, 
m.name as fragmentNodeName, 
iq.id as interviewPrimaryKey,
iq.idInterview as interviewId,
iq.name as interviewFragmentName
from Node m  
INNER JOIN Interview_Question iq
where m.idNode = iq.link
and iq.type='Q_linkedajsm' AND iq.deleted=0;

DROP VIEW IF EXISTS Module_Fragment;
CREATE VIEW Module_Fragment AS 
SELECT concat(m.idNode, ':',n.idNode, ':',n.number, ':',n.link)  as primaryKey, 
m.idNode as moduleId, 
m.name as moduleName,
n.idNode as idNode,
 n.name as fragmentName,
 n.number as nodeNumber,
 n.link as fragmentId
FROM Node m
INNER JOIN Node n ON n.topNodeId = m.idNode
WHERE n.type = 'Q_linkedajsm';

