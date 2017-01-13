DROP TABLE IF EXISTS `Node_Language`;
DROP TABLE IF EXISTS `Language`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Language` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `language` varchar(20) NOT NULL,
  `description` varchar(2048) NOT NULL,
  `lastUpdated` datetime DEFAULT NULL,
  `flag` varchar(2048) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Node_Language` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `languageId` bigint(20) NOT NULL,
  `word` varchar(2048) DEFAULT NULL,
  `translation` varchar(2048) DEFAULT NULL,
  `lastUpdated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
    KEY `FK_Language` (`languageId`),
   CONSTRAINT `FK_Language` FOREIGN KEY (`languageId`) REFERENCES `Language` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

insert into `Language` (language,description,lastUpdated,flag) values ('GB','English',now(),'bfh-flag-GB');
insert into `Language` (language,description,lastUpdated,flag) values ('ES','Spanish',now(),'bfh-flag-ES');
insert into `Language` (language,description,lastUpdated,flag) values ('SA','Arabic',now(),'bfh-flag-SA');
insert into `Language` (language,description,lastUpdated,flag) values ('CN','Chinese',now(),'bfh-flag-CN');
insert into `Language` (language,description,lastUpdated,flag) values ('CZ','Czech',now(),'bfh-flag-CZ');
insert into `Language` (language,description,lastUpdated,flag) values ('DK','Danish',now(),'bfh-flag-DK');
insert into `Language` (language,description,lastUpdated,flag) values ('NL','Dutch',now(),'bfh-flag-NL');
insert into `Language` (language,description,lastUpdated,flag) values ('FI','Finnish',now(),'bfh-flag-FI');
insert into `Language` (language,description,lastUpdated,flag) values ('FR','French',now(),'bfh-flag-FR');
insert into `Language` (language,description,lastUpdated,flag) values ('DE','German',now(),'bfh-flag-DE');
insert into `Language` (language,description,lastUpdated,flag) values ('GR','Greek',now(),'bfh-flag-GR');
insert into `Language` (language,description,lastUpdated,flag) values ('IN','Hindi',now(),'bfh-flag-IN');
insert into `Language` (language,description,lastUpdated,flag) values ('HU','Hungarian',now(),'bfh-flag-HU');
insert into `Language` (language,description,lastUpdated,flag) values ('ID','Indonesian',now(),'bfh-flag-ID');
insert into `Language` (language,description,lastUpdated,flag) values ('IT','Italian',now(),'bfh-flag-IT');
insert into `Language` (language,description,lastUpdated,flag) values ('JP','Japanese',now(),'bfh-flag-JP');
insert into `Language` (language,description,lastUpdated,flag) values ('KR','Korean',now(),'bfh-flag-KR');
insert into `Language` (language,description,lastUpdated,flag) values ('NO','Norwegian',now(),'bfh-flag-NO');
insert into `Language` (language,description,lastUpdated,flag) values ('PL','Polish',now(),'bfh-flag-PL');
insert into `Language` (language,description,lastUpdated,flag) values ('PT','Portuguese',now(),'bfh-flag-PT');
insert into `Language` (language,description,lastUpdated,flag) values ('RO','Romanian',now(),'bfh-flag-RO');
insert into `Language` (language,description,lastUpdated,flag) values ('RU','Russian',now(),'bfh-flag-RU');
insert into `Language` (language,description,lastUpdated,flag) values ('SV','Swedish',now(),'bfh-flag-SV');
insert into `Language` (language,description,lastUpdated,flag) values ('KE','Swahili',now(),'bfh-flag-KE');
insert into `Language` (language,description,lastUpdated,flag) values ('TH','Thai',now(),'bfh-flag-TH');
insert into `Language` (language,description,lastUpdated,flag) values ('TR','Turkey',now(),'bfh-flag-TR');

DROP VIEW IF EXISTS NodeNodeLanguage;

DROP VIEW IF EXISTS NodeNodeLanguageMod;
CREATE VIEW NodeNodeLanguageMod AS 
select concat(n.idNode, ':',l.flag)  as primaryKey,
n.idNode,n.name,n.topNodeId,l.flag,l.description,nl.languageId,(select count(distinct nl1.word) from 
Node n1 left join 
Node_Language nl1 on n1.name = nl1.word
where n1.link = 0
and n1.type not like '%frequency%'
and n1.type != 'P_freetext'
and nl1.translation is not null
and case n.topNodeId when 0 then n1.topNodeId = n.idNode else n1.topNodeId = n.topNodeId end 
and nl1.languageId = nl.languageId) as current,
(select count(distinct n1.name) from
Node n1 
where n1.link = 0
and n1.type not like '%frequency%'
and n1.type != 'P_freetext'
and n1.deleted = 0
and case n.topNodeId when 0 then n1.topNodeId = n.idNode else n1.topNodeId = n.topNodeId end) as total from 
Node n left join 
Node_Language nl on n.name = nl.word
left join Language l on nl.languageId = l.id 
where n.link = 0
and n.type not like '%frequency%'
and n.type != 'P_freetext' 
and l.flag is not null
and n.node_discriminator = 'M'
group by n.idNode,nl.languageId,l.flag;

DROP VIEW IF EXISTS NodeNodeLanguageFrag;
CREATE VIEW NodeNodeLanguageFrag AS 
select concat(n.idNode, ':',l.flag)  as primaryKey,
n.idNode,n.name,n.topNodeId,l.flag,l.description,nl.languageId,(select count(distinct nl1.word) from 
Node n1 left join 
Node_Language nl1 on n1.name = nl1.word
where n1.link = 0
and n1.type not like '%frequency%'
and n1.type != 'P_freetext'
and nl1.translation is not null
and case n.topNodeId when 0 then n1.topNodeId = n.idNode else n1.topNodeId = n.topNodeId end 
and nl1.languageId = nl.languageId) as current,
(select count(distinct n1.name) from
Node n1 
where n1.link = 0
and n1.type not like '%frequency%'
and n1.type != 'P_freetext'
and n1.deleted = 0
and case n.topNodeId when 0 then n1.topNodeId = n.idNode else n1.topNodeId = n.topNodeId end) as total from 
Node n left join 
Node_Language nl on n.name = nl.word
left join Language l on nl.languageId = l.id 
where n.link = 0
and n.type not like '%frequency%'
and n.type != 'P_freetext' 
and l.flag is not null
and n.node_discriminator = 'F'
group by n.idNode,nl.languageId,l.flag;