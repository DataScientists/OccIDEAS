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
m.type as moduleType, 
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
WHERE n.type = 'Q_linkedajsm'
AND n.deleted=0;

DROP VIEW IF EXISTS Module_IntroModule;
CREATE VIEW Module_IntroModule AS 
SELECT concat(m.idNode, ':',n.idNode, ':',n.number, ':',n.link)  as primaryKey, 
m.idNode as moduleId, 
m.name as moduleName,
n.idNode as idNode,
 n.name as moduleLinkName,
 n.number as nodeNumber,
 n.link as moduleLinkId
FROM Node m
INNER JOIN Node n ON n.topNodeId = m.idNode
WHERE n.type = 'Q_linkedmodule'
AND n.deleted=0;

DROP VIEW IF EXISTS AssessmentAnswerSummary;
CREATE VIEW AssessmentAnswerSummary AS
SELECT concat(p.idParticipant, ':',p.reference, ':',i.idinterview, ':',ia.answerId)  
as primaryKey,
p.idParticipant,p.reference,i.idinterview,ia.answerId,ia.name
,ia.answerFreetext,ia.type,p.status,i.assessedStatus,im.interviewModuleName
from Interview i,Participant p,
Interview_Answer ia,InterviewIntroModule_Module im
where i.idinterview = ia.idinterview
and i.idinterview = im.interviewId
and i.idParticipant = p.idParticipant 
and p.deleted = 0
and ia.deleted = 0
and im.moduleType != 'M_IntroModule';