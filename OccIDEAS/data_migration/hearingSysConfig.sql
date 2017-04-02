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

