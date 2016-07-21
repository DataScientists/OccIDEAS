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