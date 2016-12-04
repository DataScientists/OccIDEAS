ALTER TABLE `occideas`.`report_history` 
ADD COLUMN `startDt` TIMESTAMP NOT NULL AFTER `updateBy`,
ADD COLUMN `endDt` TIMESTAMP NULL AFTER `startDt`,
ADD COLUMN `duration` FLOAT NULL AFTER `endDt`,
ADD COLUMN `recordCount` INT(11) NULL AFTER `duration`;

--Populate existing data
UPDATE `occideas`.`report_history` SET `startDt`='2016-11-04 11:20:56', `duration`='60', recordCount='100';

