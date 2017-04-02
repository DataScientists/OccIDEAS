/* fix links to interviews */

SELECT count(*) FROM Participant WHERE idParticipant>1000000;

UPDATE Participant SET idParticipant=(idParticipant-1000000) WHERE idParticipant>1000000;
UPDATE Interview SET idParticipant=(idParticipant-1000000) WHERE idParticipant>1000000;

SELECT count(*) FROM Participant WHERE idParticipant>1000000;

UPDATE Participant SET idParticipant=(idParticipant-1000000) WHERE idParticipant>1000000;
UPDATE Interview SET idParticipant=(idParticipant-1000000) WHERE idParticipant>1000000;

/* fix missing interviews */

UPDATE `occideasclean`.`Participant` SET `reference`='H005116' WHERE `idParticipant`='7';
UPDATE `occideasclean`.`Participant` SET `reference`='H002929' WHERE `idParticipant`='11';
UPDATE `occideasclean`.`Participant` SET `reference`='H000312' WHERE `idParticipant`='12';
UPDATE `occideasclean`.`Participant` SET `reference`='H007912' WHERE `idParticipant`='15';
UPDATE `occideasclean`.`Participant` SET `reference`='H001473' WHERE `idParticipant`='16';

UPDATE Interview SET parentId=0 WHERE parentId IS NULL;

/* Sys config */

DROP TABLE IF EXISTS SYS_CONFIG;
CREATE TABLE `SYS_CONFIG` (
`id` int(11) NOT NULL AUTO_INCREMENT,
`type` varchar(128) NOT NULL,
`name` varchar(128) NOT NULL,
`value` varchar(128) DEFAULT NULL,
`updatedDt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
`updatedBy` varchar(128) DEFAULT NULL,

PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into SYS_CONFIG (type,name,value,updatedBy) values ('config','activeIntro','15001','system');
INSERT INTO SYS_CONFIG (type,value,name,updatedBy) VALUES 
('studyagent',9,'Trichloroethylene','system'),
('studyagent',51,'Lead','system'),
('studyagent',77,'Styrene','system'),
('studyagent',116,'noise2','system'),
('studyagent',126,'IMPACT NOISE','system'),
('studyagent',133,'Mercury','system'),
('studyagent',134,'Toluene','system'),
('studyagent',135,'p-xylene','system'),
('studyagent',136,'Ethyl Benzene','system'),
('studyagent',137,'n-hexane','system'),
('studyagent',139,'Carbon Monoxide','system'),
('studyagent',140,'Carbon Disulphide','system'),
('studyagent',157,'Vibration','system');

INSERT INTO `SYS_CONFIG` 
(`type`, `name`, `value`, `updatedBy`) 
VALUES ('config', 'studyidprefix', 'H', 'admin');

INSERT INTO `SYS_CONFIG` 
(`type`, `name`, `value`,  `updatedBy`) 
VALUES ('config', 'studyidlength', 7, 'admin');

INSERT INTO  `SYS_CONFIG` 
(`type`, `name`, `value`, `updatedBy`) 
VALUES ('config', 'REPORT_EXPORT_CSV_DIR', '/opt/reports/', 'admin');

INSERT INTO  `SYS_CONFIG` 
(`type`, `name`, `value`, `updatedBy`) 
VALUES ('NOISEAGENT', 'Noise2', '116', 'admin');

INSERT INTO  `SYS_CONFIG` 
(`type`, `name`, `value`, `updatedBy`) 
VALUES ('VIBRATIONAGENT', 'vibration', '157', 'admin');


DROP TABLE IF EXISTS `AUDIT_LOG`;

/*Audit log table*/
create table AUDIT_LOG (
   `id` BIGINT NOT NULL AUTO_INCREMENT,
   `username` VARCHAR(30) NOT NULL,
   `user_type` VARCHAR(30) DEFAULT NULL,
   `action` VARCHAR(200) DEFAULT NULL,
   `method` VARCHAR(200) DEFAULT NULL,
   `arguments` BLOB DEFAULT NULL,
   `date`  datetime DEFAULT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=292 DEFAULT CHARSET=latin1;

/* Users */

DROP TABLE IF EXISTS `APP_USER_USER_PROFILE`;

DROP TABLE IF EXISTS `USER_PROFILE`;

DROP TABLE IF EXISTS `APP_USER`;

/*All User's gets stored in APP_USER table*/
create table APP_USER (
   `id` BIGINT NOT NULL AUTO_INCREMENT,
   `sso_id` VARCHAR(30) NOT NULL,
   `password` VARCHAR(100) NOT NULL,
   `first_name` VARCHAR(30) NOT NULL,
   `last_name`  VARCHAR(30) NOT NULL,
   `email` VARCHAR(30) NOT NULL,
   `state` VARCHAR(30) NOT NULL,  
   PRIMARY KEY (`id`),
   UNIQUE (`sso_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

/* USER_PROFILE table contains all possible roles */ 
create table USER_PROFILE(
   `id` BIGINT NOT NULL AUTO_INCREMENT,
   `type` VARCHAR(30) NOT NULL,
   PRIMARY KEY (`id`),
   UNIQUE (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

/* JOIN TABLE for MANY-TO-MANY relationship*/  
CREATE TABLE APP_USER_USER_PROFILE (
    `user_id` BIGINT NOT NULL,
    `user_profile_id` BIGINT NOT NULL,
    PRIMARY KEY (`user_id`, `user_profile_id`),
    CONSTRAINT FK_APP_USER FOREIGN KEY (`user_id`) REFERENCES APP_USER (`id`),
    CONSTRAINT FK_USER_PROFILE FOREIGN KEY (`user_profile_id`) REFERENCES USER_PROFILE (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/* Populate USER_PROFILE Table */
INSERT INTO USER_PROFILE(type)
VALUES ('INTERVIEWER');

INSERT INTO USER_PROFILE(type)
VALUES ('ASSESSOR');
 
INSERT INTO USER_PROFILE(type)
VALUES ('CONTDEV');

INSERT INTO USER_PROFILE(type)
VALUES ('READONLY');

INSERT INTO USER_PROFILE(type)
VALUES ('ADMIN');


/* Interviewer */
INSERT INTO APP_USER(sso_id, password, first_name, last_name, email, state)
VALUES ('interviewer','$2a$10$atfmQmuvGaogmp0CISDh.uYNcyjP0B88i8ersmcZ3Xn2nplvmZWsa', 'interviewer','interviewer','int@yahoo.com', 'Active');

/* Assessor */
INSERT INTO APP_USER(sso_id, password, first_name, last_name, email, state)
VALUES ('assessor','$2a$10$qx0VVOOfTWbElRv5Hc.H.e0ehlh9k0U3TVyKzITA4rezjrzZuT2Hi', 'assessor','assessor','int@yahoo.com', 'Active');

/* Content Developer */
INSERT INTO APP_USER(sso_id, password, first_name, last_name, email, state)
VALUES ('contdev','$2a$10$fKuJmQxyUQ4wCrcbUsK6xOWKAzacjOzXljz32zLPzAv63g4j9qHey', 'contdev','contdev','int@yahoo.com', 'Active');

/* Read Only Account */
INSERT INTO APP_USER(sso_id, password, first_name, last_name, email, state)
VALUES ('readonly','$2a$10$0.2nTVeqCm0WAzy3g.cw0.W9qT/GiCJOKYaWciW3kBVd6WZ/2JHWC', 'readonly','readonly','int@yahoo.com', 'Active');

/* Admin */
INSERT INTO APP_USER(sso_id, password, first_name, last_name, email, state)
VALUES ('admin','$2a$10$DvrQhzYrmVIUa2c8dQtSdOsmiE861DKiJ6VMMy4jDHbwUJXard3L6', 'admin','admin','int@yahoo.com', 'Active');

INSERT INTO `APP_USER_USER_PROFILE` (`user_id`, `user_profile_id`) VALUES ('1', '1');
INSERT INTO `APP_USER_USER_PROFILE` (`user_id`, `user_profile_id`) VALUES ('2', '2');
INSERT INTO `APP_USER_USER_PROFILE` (`user_id`, `user_profile_id`) VALUES ('3', '3');
INSERT INTO `APP_USER_USER_PROFILE` (`user_id`, `user_profile_id`) VALUES ('4', '4');
INSERT INTO `APP_USER_USER_PROFILE` (`user_id`, `user_profile_id`) VALUES ('5', '5');
INSERT INTO `APP_USER_USER_PROFILE` (`user_id`, `user_profile_id`) VALUES ('4', '3');


/* assessmentStatus */

ALTER TABLE `Interview` 
ADD COLUMN `assessedStatus` VARCHAR(255) DEFAULT '';

/* update views */

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
and im.idModule != (select value from SYS_CONFIG where name = 'activeintro' limit 1);

/*  fix nulls**/
UPDATE Interview_Question SET type='Q_linkedmodule' where nodeClass is Null and type is null and name like '\_%';
UPDATE Interview_Question SET type='Q_linkedajsm' where nodeClass is Null and type is null;


/* add reports table */
DROP TABLE IF EXISTS REPORT_HISTORY;
CREATE TABLE `REPORT_HISTORY` (
`id` int(11) NOT NULL AUTO_INCREMENT,
`type` varchar(128) NOT NULL,
`name` varchar(128) NOT NULL,
`path` varchar(128) DEFAULT NULL,
`status` varchar(128) NOT NULL,
`progress` varchar(128) NOT NULL,
`requestor` varchar(128) NOT NULL,
`jsonData` LONGTEXT DEFAULT NULL,
`updatedDt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
`updatedBy` varchar(128) DEFAULT NULL,

PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `REPORT_HISTORY` 
ADD COLUMN `startDt` TIMESTAMP NULL AFTER `updatedBy`,
ADD COLUMN `endDt` TIMESTAMP NULL AFTER `startDt`,
ADD COLUMN `duration` FLOAT NULL AFTER `endDt`,
ADD COLUMN `recordCount` INT(11) NULL AFTER `duration`;



