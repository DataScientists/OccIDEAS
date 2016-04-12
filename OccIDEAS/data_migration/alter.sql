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

DELETE FROM occideas.Rule_AdditionalField where idAdditionalField=2;
DELETE FROM occideas.Rule_AdditionalField where idAdditionalField=4;
