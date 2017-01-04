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


insert into `Language` (language,description,lastUpdated,flag) values ('AD','AD',now(),'bfh-flag-AD');
insert into `Language` (language,description,lastUpdated,flag) values ('AE','AE',now(),'bfh-flag-AE');
insert into `Language` (language,description,lastUpdated,flag) values ('AF','AF',now(),'bfh-flag-AF');
insert into `Language` (language,description,lastUpdated,flag) values ('AG','AG',now(),'bfh-flag-AG');
insert into `Language` (language,description,lastUpdated,flag) values ('AI','AI',now(),'bfh-flag-AI');
insert into `Language` (language,description,lastUpdated,flag) values ('AL','AL',now(),'bfh-flag-AL');
insert into `Language` (language,description,lastUpdated,flag) values ('AM','AM',now(),'bfh-flag-AM');
insert into `Language` (language,description,lastUpdated,flag) values ('AN','AN',now(),'bfh-flag-AN');
insert into `Language` (language,description,lastUpdated,flag) values ('AO','AO',now(),'bfh-flag-AO');
insert into `Language` (language,description,lastUpdated,flag) values ('AQ','AQ',now(),'bfh-flag-AQ');
insert into `Language` (language,description,lastUpdated,flag) values ('AR','AR',now(),'bfh-flag-AR');
insert into `Language` (language,description,lastUpdated,flag) values ('AS','AS',now(),'bfh-flag-AS');
insert into `Language` (language,description,lastUpdated,flag) values ('AT','AT',now(),'bfh-flag-AT');
insert into `Language` (language,description,lastUpdated,flag) values ('AU','AU',now(),'bfh-flag-AU');
insert into `Language` (language,description,lastUpdated,flag) values ('AW','AW',now(),'bfh-flag-AW');
insert into `Language` (language,description,lastUpdated,flag) values ('AX','AX',now(),'bfh-flag-AX');
insert into `Language` (language,description,lastUpdated,flag) values ('AZ','AZ',now(),'bfh-flag-AZ');
insert into `Language` (language,description,lastUpdated,flag) values ('BA','BA',now(),'bfh-flag-BA');
insert into `Language` (language,description,lastUpdated,flag) values ('BB','BB',now(),'bfh-flag-BB');
insert into `Language` (language,description,lastUpdated,flag) values ('BD','BD',now(),'bfh-flag-BD');
insert into `Language` (language,description,lastUpdated,flag) values ('BE','BE',now(),'bfh-flag-BE');
insert into `Language` (language,description,lastUpdated,flag) values ('BG','BG',now(),'bfh-flag-BG');
insert into `Language` (language,description,lastUpdated,flag) values ('BH','BH',now(),'bfh-flag-BH');
insert into `Language` (language,description,lastUpdated,flag) values ('BI','BI',now(),'bfh-flag-BI');
insert into `Language` (language,description,lastUpdated,flag) values ('BJ','BJ',now(),'bfh-flag-BJ');
insert into `Language` (language,description,lastUpdated,flag) values ('BL','BL',now(),'bfh-flag-BL');
insert into `Language` (language,description,lastUpdated,flag) values ('BM','BM',now(),'bfh-flag-BM');
insert into `Language` (language,description,lastUpdated,flag) values ('BN','BN',now(),'bfh-flag-BN');
insert into `Language` (language,description,lastUpdated,flag) values ('BO','BO',now(),'bfh-flag-BO');
insert into `Language` (language,description,lastUpdated,flag) values ('BR','BR',now(),'bfh-flag-BR');
insert into `Language` (language,description,lastUpdated,flag) values ('BS','BS',now(),'bfh-flag-BS');
insert into `Language` (language,description,lastUpdated,flag) values ('BT','BT',now(),'bfh-flag-BT');
insert into `Language` (language,description,lastUpdated,flag) values ('BW','BW',now(),'bfh-flag-BW');
insert into `Language` (language,description,lastUpdated,flag) values ('BY','BY',now(),'bfh-flag-BY');
insert into `Language` (language,description,lastUpdated,flag) values ('BZ','BZ',now(),'bfh-flag-BZ');
insert into `Language` (language,description,lastUpdated,flag) values ('CA','CA',now(),'bfh-flag-CA');
insert into `Language` (language,description,lastUpdated,flag) values ('CD','CD',now(),'bfh-flag-CD');
insert into `Language` (language,description,lastUpdated,flag) values ('CF','CF',now(),'bfh-flag-CF');
insert into `Language` (language,description,lastUpdated,flag) values ('CG','CG',now(),'bfh-flag-CG');
insert into `Language` (language,description,lastUpdated,flag) values ('CH','CH',now(),'bfh-flag-CH');
insert into `Language` (language,description,lastUpdated,flag) values ('CI','CI',now(),'bfh-flag-CI');
insert into `Language` (language,description,lastUpdated,flag) values ('CL','CL',now(),'bfh-flag-CL');
insert into `Language` (language,description,lastUpdated,flag) values ('CM','CM',now(),'bfh-flag-CM');
insert into `Language` (language,description,lastUpdated,flag) values ('CN','CN',now(),'bfh-flag-CN');
insert into `Language` (language,description,lastUpdated,flag) values ('CO','CO',now(),'bfh-flag-CO');
insert into `Language` (language,description,lastUpdated,flag) values ('CR','CR',now(),'bfh-flag-CR');
insert into `Language` (language,description,lastUpdated,flag) values ('CV','CV',now(),'bfh-flag-CV');
insert into `Language` (language,description,lastUpdated,flag) values ('CY','CY',now(),'bfh-flag-CY');
insert into `Language` (language,description,lastUpdated,flag) values ('CZ','CZ',now(),'bfh-flag-CZ');
insert into `Language` (language,description,lastUpdated,flag) values ('DJ','DJ',now(),'bfh-flag-DJ');
insert into `Language` (language,description,lastUpdated,flag) values ('DK','DK',now(),'bfh-flag-DK');
insert into `Language` (language,description,lastUpdated,flag) values ('DM','DM',now(),'bfh-flag-DM');
insert into `Language` (language,description,lastUpdated,flag) values ('DO','DO',now(),'bfh-flag-DO');
insert into `Language` (language,description,lastUpdated,flag) values ('DZ','DZ',now(),'bfh-flag-DZ');
insert into `Language` (language,description,lastUpdated,flag) values ('EC','EC',now(),'bfh-flag-EC');
insert into `Language` (language,description,lastUpdated,flag) values ('EE','EE',now(),'bfh-flag-EE');
insert into `Language` (language,description,lastUpdated,flag) values ('EG','EG',now(),'bfh-flag-EG');
insert into `Language` (language,description,lastUpdated,flag) values ('EH','EH',now(),'bfh-flag-EH');
insert into `Language` (language,description,lastUpdated,flag) values ('ER','ER',now(),'bfh-flag-ER');
insert into `Language` (language,description,lastUpdated,flag) values ('ES','ES',now(),'bfh-flag-ES');
insert into `Language` (language,description,lastUpdated,flag) values ('ET','ET',now(),'bfh-flag-ET');
insert into `Language` (language,description,lastUpdated,flag) values ('EU','EU',now(),'bfh-flag-EU');
insert into `Language` (language,description,lastUpdated,flag) values ('FI','FI',now(),'bfh-flag-FI');
insert into `Language` (language,description,lastUpdated,flag) values ('FJ','FJ',now(),'bfh-flag-FJ');
insert into `Language` (language,description,lastUpdated,flag) values ('FK','FK',now(),'bfh-flag-FK');
insert into `Language` (language,description,lastUpdated,flag) values ('FM','FM',now(),'bfh-flag-FM');
insert into `Language` (language,description,lastUpdated,flag) values ('FO','FO',now(),'bfh-flag-FO');
insert into `Language` (language,description,lastUpdated,flag) values ('FR','FR',now(),'bfh-flag-FR');
insert into `Language` (language,description,lastUpdated,flag) values ('FX','FX',now(),'bfh-flag-FX');
insert into `Language` (language,description,lastUpdated,flag) values ('GF','GF',now(),'bfh-flag-GF');
insert into `Language` (language,description,lastUpdated,flag) values ('GP','GP',now(),'bfh-flag-GP');
insert into `Language` (language,description,lastUpdated,flag) values ('MQ','MQ',now(),'bfh-flag-MQ');
insert into `Language` (language,description,lastUpdated,flag) values ('NC','NC',now(),'bfh-flag-NC');
insert into `Language` (language,description,lastUpdated,flag) values ('PF','PF',now(),'bfh-flag-PF');
insert into `Language` (language,description,lastUpdated,flag) values ('PM','PM',now(),'bfh-flag-PM');
insert into `Language` (language,description,lastUpdated,flag) values ('RE','RE',now(),'bfh-flag-RE');
insert into `Language` (language,description,lastUpdated,flag) values ('TF','TF',now(),'bfh-flag-TF');
insert into `Language` (language,description,lastUpdated,flag) values ('WF','WF',now(),'bfh-flag-WF');
insert into `Language` (language,description,lastUpdated,flag) values ('GA','GA',now(),'bfh-flag-GA');
insert into `Language` (language,description,lastUpdated,flag) values ('GB','GB',now(),'bfh-flag-GB');
insert into `Language` (language,description,lastUpdated,flag) values ('GD','GD',now(),'bfh-flag-GD');
insert into `Language` (language,description,lastUpdated,flag) values ('GE','GE',now(),'bfh-flag-GE');
insert into `Language` (language,description,lastUpdated,flag) values ('GG','GG',now(),'bfh-flag-GG');
insert into `Language` (language,description,lastUpdated,flag) values ('GH','GH',now(),'bfh-flag-GH');
insert into `Language` (language,description,lastUpdated,flag) values ('GL','GL',now(),'bfh-flag-GL');
insert into `Language` (language,description,lastUpdated,flag) values ('GM','GM',now(),'bfh-flag-GM');
insert into `Language` (language,description,lastUpdated,flag) values ('GN','GN',now(),'bfh-flag-GN');
insert into `Language` (language,description,lastUpdated,flag) values ('GQ','GQ',now(),'bfh-flag-GQ');
insert into `Language` (language,description,lastUpdated,flag) values ('GR','GR',now(),'bfh-flag-GR');
insert into `Language` (language,description,lastUpdated,flag) values ('GS','GS',now(),'bfh-flag-GS');
insert into `Language` (language,description,lastUpdated,flag) values ('GT','GT',now(),'bfh-flag-GT');
insert into `Language` (language,description,lastUpdated,flag) values ('GU','GU',now(),'bfh-flag-GU');
insert into `Language` (language,description,lastUpdated,flag) values ('GW','GW',now(),'bfh-flag-GW');
insert into `Language` (language,description,lastUpdated,flag) values ('GY','GY',now(),'bfh-flag-GY');
insert into `Language` (language,description,lastUpdated,flag) values ('HK','HK',now(),'bfh-flag-HK');
insert into `Language` (language,description,lastUpdated,flag) values ('HN','HN',now(),'bfh-flag-HN');
insert into `Language` (language,description,lastUpdated,flag) values ('HR','HR',now(),'bfh-flag-HR');
insert into `Language` (language,description,lastUpdated,flag) values ('HT','HT',now(),'bfh-flag-HT');
insert into `Language` (language,description,lastUpdated,flag) values ('HU','HU',now(),'bfh-flag-HU');
insert into `Language` (language,description,lastUpdated,flag) values ('ID','ID',now(),'bfh-flag-ID');
insert into `Language` (language,description,lastUpdated,flag) values ('IE','IE',now(),'bfh-flag-IE');
insert into `Language` (language,description,lastUpdated,flag) values ('IL','IL',now(),'bfh-flag-IL');
insert into `Language` (language,description,lastUpdated,flag) values ('IM','IM',now(),'bfh-flag-IM');
insert into `Language` (language,description,lastUpdated,flag) values ('IN','IN',now(),'bfh-flag-IN');
insert into `Language` (language,description,lastUpdated,flag) values ('IQ','IQ',now(),'bfh-flag-IQ');
insert into `Language` (language,description,lastUpdated,flag) values ('IS','IS',now(),'bfh-flag-IS');
insert into `Language` (language,description,lastUpdated,flag) values ('IT','IT',now(),'bfh-flag-IT');
insert into `Language` (language,description,lastUpdated,flag) values ('JE','JE',now(),'bfh-flag-JE');
insert into `Language` (language,description,lastUpdated,flag) values ('JM','JM',now(),'bfh-flag-JM');
insert into `Language` (language,description,lastUpdated,flag) values ('JO','JO',now(),'bfh-flag-JO');
insert into `Language` (language,description,lastUpdated,flag) values ('JP','JP',now(),'bfh-flag-JP');
insert into `Language` (language,description,lastUpdated,flag) values ('KE','KE',now(),'bfh-flag-KE');
insert into `Language` (language,description,lastUpdated,flag) values ('KG','KG',now(),'bfh-flag-KG');
insert into `Language` (language,description,lastUpdated,flag) values ('KH','KH',now(),'bfh-flag-KH');
insert into `Language` (language,description,lastUpdated,flag) values ('KI','KI',now(),'bfh-flag-KI');
insert into `Language` (language,description,lastUpdated,flag) values ('KM','KM',now(),'bfh-flag-KM');
insert into `Language` (language,description,lastUpdated,flag) values ('KN','KN',now(),'bfh-flag-KN');
insert into `Language` (language,description,lastUpdated,flag) values ('KP','KP',now(),'bfh-flag-KP');
insert into `Language` (language,description,lastUpdated,flag) values ('KR','KR',now(),'bfh-flag-KR');
insert into `Language` (language,description,lastUpdated,flag) values ('KV','KV',now(),'bfh-flag-KV');
insert into `Language` (language,description,lastUpdated,flag) values ('KW','KW',now(),'bfh-flag-KW');
insert into `Language` (language,description,lastUpdated,flag) values ('KY','KY',now(),'bfh-flag-KY');
insert into `Language` (language,description,lastUpdated,flag) values ('LA','LA',now(),'bfh-flag-LA');
insert into `Language` (language,description,lastUpdated,flag) values ('LC','LC',now(),'bfh-flag-LC');
insert into `Language` (language,description,lastUpdated,flag) values ('LK','LK',now(),'bfh-flag-LK');
insert into `Language` (language,description,lastUpdated,flag) values ('LR','LR',now(),'bfh-flag-LR');
insert into `Language` (language,description,lastUpdated,flag) values ('LS','LS',now(),'bfh-flag-LS');
insert into `Language` (language,description,lastUpdated,flag) values ('LT','LT',now(),'bfh-flag-LT');
insert into `Language` (language,description,lastUpdated,flag) values ('LU','LU',now(),'bfh-flag-LU');
insert into `Language` (language,description,lastUpdated,flag) values ('LV','LV',now(),'bfh-flag-LV');
insert into `Language` (language,description,lastUpdated,flag) values ('LY','LY',now(),'bfh-flag-LY');
insert into `Language` (language,description,lastUpdated,flag) values ('MA','MA',now(),'bfh-flag-MA');
insert into `Language` (language,description,lastUpdated,flag) values ('ME','ME',now(),'bfh-flag-ME');
insert into `Language` (language,description,lastUpdated,flag) values ('MG','MG',now(),'bfh-flag-MG');
insert into `Language` (language,description,lastUpdated,flag) values ('MH','MH',now(),'bfh-flag-MH');
insert into `Language` (language,description,lastUpdated,flag) values ('ML','ML',now(),'bfh-flag-ML');
insert into `Language` (language,description,lastUpdated,flag) values ('MM','MM',now(),'bfh-flag-MM');
insert into `Language` (language,description,lastUpdated,flag) values ('MP','MP',now(),'bfh-flag-MP');
insert into `Language` (language,description,lastUpdated,flag) values ('MR','MR',now(),'bfh-flag-MR');
insert into `Language` (language,description,lastUpdated,flag) values ('MS','MS',now(),'bfh-flag-MS');
insert into `Language` (language,description,lastUpdated,flag) values ('MT','MT',now(),'bfh-flag-MT');
insert into `Language` (language,description,lastUpdated,flag) values ('MU','MU',now(),'bfh-flag-MU');
insert into `Language` (language,description,lastUpdated,flag) values ('MV','MV',now(),'bfh-flag-MV');
insert into `Language` (language,description,lastUpdated,flag) values ('MW','MW',now(),'bfh-flag-MW');
insert into `Language` (language,description,lastUpdated,flag) values ('MZ','MZ',now(),'bfh-flag-MZ');
insert into `Language` (language,description,lastUpdated,flag) values ('NA','NA',now(),'bfh-flag-NA');
insert into `Language` (language,description,lastUpdated,flag) values ('NE','NE',now(),'bfh-flag-NE');
insert into `Language` (language,description,lastUpdated,flag) values ('NF','NF',now(),'bfh-flag-NF');
insert into `Language` (language,description,lastUpdated,flag) values ('NG','NG',now(),'bfh-flag-NG');
insert into `Language` (language,description,lastUpdated,flag) values ('NI','NI',now(),'bfh-flag-NI');
insert into `Language` (language,description,lastUpdated,flag) values ('NL','NL',now(),'bfh-flag-NL');
insert into `Language` (language,description,lastUpdated,flag) values ('NO','NO',now(),'bfh-flag-NO');
insert into `Language` (language,description,lastUpdated,flag) values ('NP','NP',now(),'bfh-flag-NP');
insert into `Language` (language,description,lastUpdated,flag) values ('NR','NR',now(),'bfh-flag-NR');
insert into `Language` (language,description,lastUpdated,flag) values ('NZ','NZ',now(),'bfh-flag-NZ');
insert into `Language` (language,description,lastUpdated,flag) values ('OM','OM',now(),'bfh-flag-OM');
insert into `Language` (language,description,lastUpdated,flag) values ('PA','PA',now(),'bfh-flag-PA');
insert into `Language` (language,description,lastUpdated,flag) values ('PE','PE',now(),'bfh-flag-PE');
insert into `Language` (language,description,lastUpdated,flag) values ('PG','PG',now(),'bfh-flag-PG');
insert into `Language` (language,description,lastUpdated,flag) values ('PH','PH',now(),'bfh-flag-PH');
insert into `Language` (language,description,lastUpdated,flag) values ('PK','PK',now(),'bfh-flag-PK');
insert into `Language` (language,description,lastUpdated,flag) values ('PL','PL',now(),'bfh-flag-PL');
insert into `Language` (language,description,lastUpdated,flag) values ('PN','PN',now(),'bfh-flag-PN');
insert into `Language` (language,description,lastUpdated,flag) values ('PS','PS',now(),'bfh-flag-PS');
insert into `Language` (language,description,lastUpdated,flag) values ('PT','PT',now(),'bfh-flag-PT');
insert into `Language` (language,description,lastUpdated,flag) values ('PW','PW',now(),'bfh-flag-PW');
insert into `Language` (language,description,lastUpdated,flag) values ('PY','PY',now(),'bfh-flag-PY');
insert into `Language` (language,description,lastUpdated,flag) values ('QA','QA',now(),'bfh-flag-QA');
insert into `Language` (language,description,lastUpdated,flag) values ('RS','RS',now(),'bfh-flag-RS');
insert into `Language` (language,description,lastUpdated,flag) values ('RU','RU',now(),'bfh-flag-RU');
insert into `Language` (language,description,lastUpdated,flag) values ('RW','RW',now(),'bfh-flag-RW');
insert into `Language` (language,description,lastUpdated,flag) values ('SA','SA',now(),'bfh-flag-SA');
insert into `Language` (language,description,lastUpdated,flag) values ('SB','SB',now(),'bfh-flag-SB');
insert into `Language` (language,description,lastUpdated,flag) values ('SC','SC',now(),'bfh-flag-SC');
insert into `Language` (language,description,lastUpdated,flag) values ('SD','SD',now(),'bfh-flag-SD');
insert into `Language` (language,description,lastUpdated,flag) values ('SE','SE',now(),'bfh-flag-SE');
insert into `Language` (language,description,lastUpdated,flag) values ('SG','SG',now(),'bfh-flag-SG');
insert into `Language` (language,description,lastUpdated,flag) values ('SH','SH',now(),'bfh-flag-SH');
insert into `Language` (language,description,lastUpdated,flag) values ('SI','SI',now(),'bfh-flag-SI');
insert into `Language` (language,description,lastUpdated,flag) values ('SK','SK',now(),'bfh-flag-SK');
insert into `Language` (language,description,lastUpdated,flag) values ('SM','SM',now(),'bfh-flag-SM');
insert into `Language` (language,description,lastUpdated,flag) values ('SN','SN',now(),'bfh-flag-SN');
insert into `Language` (language,description,lastUpdated,flag) values ('SO','SO',now(),'bfh-flag-SO');
insert into `Language` (language,description,lastUpdated,flag) values ('SR','SR',now(),'bfh-flag-SR');
insert into `Language` (language,description,lastUpdated,flag) values ('SS','SS',now(),'bfh-flag-SS');
insert into `Language` (language,description,lastUpdated,flag) values ('ST','ST',now(),'bfh-flag-ST');
insert into `Language` (language,description,lastUpdated,flag) values ('SV','SV',now(),'bfh-flag-SV');
insert into `Language` (language,description,lastUpdated,flag) values ('SY','SY',now(),'bfh-flag-SY');
insert into `Language` (language,description,lastUpdated,flag) values ('SZ','SZ',now(),'bfh-flag-SZ');
insert into `Language` (language,description,lastUpdated,flag) values ('TC','TC',now(),'bfh-flag-TC');
insert into `Language` (language,description,lastUpdated,flag) values ('TD','TD',now(),'bfh-flag-TD');
insert into `Language` (language,description,lastUpdated,flag) values ('TG','TG',now(),'bfh-flag-TG');
insert into `Language` (language,description,lastUpdated,flag) values ('TH','TH',now(),'bfh-flag-TH');
insert into `Language` (language,description,lastUpdated,flag) values ('TJ','TJ',now(),'bfh-flag-TJ');
insert into `Language` (language,description,lastUpdated,flag) values ('TM','TM',now(),'bfh-flag-TM');
insert into `Language` (language,description,lastUpdated,flag) values ('TN','TN',now(),'bfh-flag-TN');
insert into `Language` (language,description,lastUpdated,flag) values ('TP','TP',now(),'bfh-flag-TP');
insert into `Language` (language,description,lastUpdated,flag) values ('TR','TR',now(),'bfh-flag-TR');
insert into `Language` (language,description,lastUpdated,flag) values ('TT','TT',now(),'bfh-flag-TT');
insert into `Language` (language,description,lastUpdated,flag) values ('TV','TV',now(),'bfh-flag-TV');
insert into `Language` (language,description,lastUpdated,flag) values ('TW','TW',now(),'bfh-flag-TW');
insert into `Language` (language,description,lastUpdated,flag) values ('TZ','TZ',now(),'bfh-flag-TZ');
insert into `Language` (language,description,lastUpdated,flag) values ('UA','UA',now(),'bfh-flag-UA');
insert into `Language` (language,description,lastUpdated,flag) values ('UG','UG',now(),'bfh-flag-UG');
insert into `Language` (language,description,lastUpdated,flag) values ('US','US',now(),'bfh-flag-US');
insert into `Language` (language,description,lastUpdated,flag) values ('UY','UY',now(),'bfh-flag-UY');
insert into `Language` (language,description,lastUpdated,flag) values ('UZ','UZ',now(),'bfh-flag-UZ');
insert into `Language` (language,description,lastUpdated,flag) values ('VC','VC',now(),'bfh-flag-VC');
insert into `Language` (language,description,lastUpdated,flag) values ('VE','VE',now(),'bfh-flag-VE');
insert into `Language` (language,description,lastUpdated,flag) values ('VG','VG',now(),'bfh-flag-VG');
insert into `Language` (language,description,lastUpdated,flag) values ('VI','VI',now(),'bfh-flag-VI');
insert into `Language` (language,description,lastUpdated,flag) values ('VN','VN',now(),'bfh-flag-VN');
insert into `Language` (language,description,lastUpdated,flag) values ('VU','VU',now(),'bfh-flag-VU');
insert into `Language` (language,description,lastUpdated,flag) values ('WS','WS',now(),'bfh-flag-WS');
insert into `Language` (language,description,lastUpdated,flag) values ('YE','YE',now(),'bfh-flag-YE');
insert into `Language` (language,description,lastUpdated,flag) values ('ZA','ZA',now(),'bfh-flag-ZA');
insert into `Language` (language,description,lastUpdated,flag) values ('ZM','ZM',now(),'bfh-flag-ZM');
insert into `Language` (language,description,lastUpdated,flag) values ('BF','BF',now(),'bfh-flag-BF');
insert into `Language` (language,description,lastUpdated,flag) values ('CU','CU',now(),'bfh-flag-CU');
insert into `Language` (language,description,lastUpdated,flag) values ('DE','DE',now(),'bfh-flag-DE');
insert into `Language` (language,description,lastUpdated,flag) values ('IR','IR',now(),'bfh-flag-IR');
insert into `Language` (language,description,lastUpdated,flag) values ('KZ','KZ',now(),'bfh-flag-KZ');
insert into `Language` (language,description,lastUpdated,flag) values ('LB','LB',now(),'bfh-flag-LB');
insert into `Language` (language,description,lastUpdated,flag) values ('LI','LI',now(),'bfh-flag-LI');
insert into `Language` (language,description,lastUpdated,flag) values ('MC','MC',now(),'bfh-flag-MC');
insert into `Language` (language,description,lastUpdated,flag) values ('MD','MD',now(),'bfh-flag-MD');
insert into `Language` (language,description,lastUpdated,flag) values ('MK','MK',now(),'bfh-flag-MK');
insert into `Language` (language,description,lastUpdated,flag) values ('MN','MN',now(),'bfh-flag-MN');
insert into `Language` (language,description,lastUpdated,flag) values ('MO','MO',now(),'bfh-flag-MO');
insert into `Language` (language,description,lastUpdated,flag) values ('MX','MX',now(),'bfh-flag-MX');
insert into `Language` (language,description,lastUpdated,flag) values ('MY','MY',now(),'bfh-flag-MY');
insert into `Language` (language,description,lastUpdated,flag) values ('PR','PR',now(),'bfh-flag-PR');
insert into `Language` (language,description,lastUpdated,flag) values ('RO','RO',now(),'bfh-flag-RO');
insert into `Language` (language,description,lastUpdated,flag) values ('SL','SL',now(),'bfh-flag-SL');
insert into `Language` (language,description,lastUpdated,flag) values ('TO','TO',now(),'bfh-flag-TO');
insert into `Language` (language,description,lastUpdated,flag) values ('VA','VA',now(),'bfh-flag-VA');
insert into `Language` (language,description,lastUpdated,flag) values ('ZW','ZW',now(),'bfh-flag-ZW');

DROP VIEW IF EXISTS NodeNodeLanguage;

DROP VIEW IF EXISTS NodeNodeLanguageMod;
CREATE VIEW NodeNodeLanguageMod AS 
select concat(n.idNode, ':',l.flag)  as primaryKey,
n.idNode,n.topNodeId,l.flag,nl.languageId,(select count(distinct nl1.word) from 
Node n1 left join 
Node_Language nl1 on n1.name = nl1.word
where n1.link = 0
and n1.type not like '%frequency%'
and n1.type != 'P_freetext'
and nl1.translation is not null
and case n.topNodeId when 0 then n1.topNodeId = n.idNode else n1.topNodeId = n.topNodeId end 
and nl1.languageId = nl.languageId)+1 as current,
(select count(distinct n1.name) from
Node n1 
where n1.link = 0
and n1.type not like '%frequency%'
and n1.type != 'P_freetext'
and n1.nodeclass is not null
and n1.originalId = 0
and n1.deleted = 0
and case n.topNodeId when 0 then n1.topNodeId = n.idNode else n1.topNodeId = n.topNodeId end)+1 as total from 
Node n left join 
Node_Language nl on n.name = nl.word
left join Language l on nl.languageId = l.id 
where n.link = 0
and n.type not like '%frequency%'
and n.type != 'P_freetext' 
and l.flag is not null
group by n.idNode,nl.languageId,l.flag;

DROP VIEW IF EXISTS NodeNodeLanguageFrag;
CREATE VIEW NodeNodeLanguageFrag AS 
select concat(n.idNode, ':',l.flag)  as primaryKey,
n.idNode,n.topNodeId,l.flag,nl.languageId,(select count(distinct nl1.word) from 
Node n1 left join 
Node_Language nl1 on n1.name = nl1.word
where n1.link = 0
and n1.type not like '%frequency%'
and n1.type != 'P_freetext'
and nl1.translation is not null
and case n.topNodeId when 0 then n1.topNodeId = n.idNode else n1.topNodeId = n.topNodeId end 
and nl1.languageId = nl.languageId)+1 as current,
(select count(distinct n1.name) from
Node n1 
where n1.link = 0
and n1.type not like '%frequency%'
and n1.type != 'P_freetext'
and case n.topNodeId when 0 then n1.topNodeId = n.idNode else n1.topNodeId = n.topNodeId end)+1 as total from 
Node n left join 
Node_Language nl on n.name = nl.word
left join Language l on nl.languageId = l.id 
where n.link = 0
and n.type not like '%frequency%'
and n.type != 'P_freetext' 
and l.flag is not null
and n.node_discriminator = 'F'
group by n.idNode,nl.languageId,l.flag;