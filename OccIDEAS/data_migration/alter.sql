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