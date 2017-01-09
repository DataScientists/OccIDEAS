ALTER TABLE `occideas`.`Interview` 
ADD COLUMN `fragment_idNode` BIGINT(20) NULL DEFAULT NULL,
ADD COLUMN `referenceNumber` VARCHAR(255) NOT NULL;


CREATE TABLE `occideas`.`Interview_ManualAssessedRules` (
  `idInterview_ManualAssessedRules` INT NOT NULL AUTO_INCREMENT,
  `idinterview` INT NULL,
  `idRule` INT NULL,
  PRIMARY KEY (`idInterview_ManualAssessedRules`));

  CREATE TABLE `occideas`.`Interview_AutoAssessedRules` (
  `idInterview_AutoAssessedRules` INT NOT NULL AUTO_INCREMENT,
  `idinterview` INT NULL,
  `idRule` INT NULL,
  PRIMARY KEY (`idInterview_AutoAssessedRules`));
  
  
  CREATE TABLE `occideas`.`Interview_FiredRules` (
  `idInterview_FiredRules` INT NOT NULL AUTO_INCREMENT,
  `idinterview` INT NULL,
  `idRule` INT NULL,
  PRIMARY KEY (`idInterview_FiredRules`));

ALTER TABLE `occideas`.`Rule` 
ADD COLUMN `deleted` INT(11) NOT NULL DEFAULT 0 AFTER `type`;

CREATE TABLE `occideas`.`Participant` (
  `idParticipant` int(11) NOT NULL AUTO_INCREMENT,
  `reference` varchar(20) NOT NULL,
  `status` int(11) NOT NULL,
  `lastUpdated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`idparticipant`)
);
ALTER TABLE `occideas`.`Interview` 
ADD COLUMN `idParticipant` BIGINT(20) NOT NULL;

ALTER TABLE `occideas`.`Interview` 
ADD COLUMN `parentId` BIGINT(20);

ALTER TABLE `occideas`.`Note` 
ADD COLUMN `interviewId` BIGINT(20) ;

ALTER TABLE `occideas`.`Note` 
CHANGE COLUMN `lastUpdated` `lastUpdated` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;


DELETE FROM occideas.Rule_AdditionalField where idAdditionalField=2;
DELETE FROM occideas.Rule_AdditionalField where idAdditionalField=4;

ALTER TABLE `occideas`.`Interview_Display` 
ADD COLUMN `number` VARCHAR(2048);

ALTER TABLE `occideas`.`Interview_Question` 
CHANGE COLUMN `parentId` `parentModuleId` BIGINT(20) NOT NULL ;

ALTER TABLE `occideas`.`Interview_Answer` 
CHANGE COLUMN `topQuestionId` `topNodeId` BIGINT(20) NOT NULL ;

ALTER TABLE `occideas`.`Interview_Question` 
ADD COLUMN `isProcessed` TINYINT(4) NOT NULL DEFAULT 0 AFTER `deleted`;

ALTER TABLE `occideas`.`Interview_Answer` 
CHANGE COLUMN `modCount` `modCount` INT(11) NOT NULL DEFAULT 0 ;

ALTER TABLE `occideas`.`Interview_Question` 
CHANGE COLUMN `modCount` `modCount` INT(11) NOT NULL DEFAULT 0 ;

ALTER TABLE `occideas`.`Interview_Answer` 
CHANGE COLUMN `modCount` `modCount` INT(11) NULL DEFAULT 1 ;

ALTER TABLE `occideas`.`Interview_Answer` 
ADD COLUMN `interviewQuestionId` BIGINT(11) NULL ;

UPDATE Interview_Answer ia SET interviewQuestionId =(SELECT max(id) FROM Interview_Question iq WHERE iq.idinterview = ia.idinterview AND iq.question_Id=ia.parentQuestionId)
;
DELETE FROM occideas.Participant WHERE reference like 'HT%';
DELETE FROM occideas.Interview WHERE referenceNumber like 'HT%';

ALTER TABLE `occideas`.`Interview_Answer` DROP COLUMN `topIntQuestionId`;

ALTER TABLE app_user DROP COLUMN first_name;
ALTER TABLE app_user DROP COLUMN last_name;

ALTER TABLE APP_USER 
ADD COLUMN `first_name` VARCHAR(1024) NULL;
ALTER TABLE APP_USER 
ADD COLUMN `last_name` VARCHAR(1024) NULL;
ALTER TABLE APP_USER 
CHANGE COLUMN `email` `email` VARCHAR(1024) NULL;

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
and iq.nodeClass = 'M';

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

ALTER TABLE `occideas`.`Interview` 
ADD COLUMN `assessedStatus` VARCHAR(255) DEFAULT '';

DELETE FROM occideas.AUDIT_LOG WHERE id>0;
