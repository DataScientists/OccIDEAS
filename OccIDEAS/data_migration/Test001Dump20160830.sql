CREATE DATABASE  IF NOT EXISTS `occideas` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `occideas`;
-- MySQL dump 10.13  Distrib 5.5.50, for debian-linux-gnu (x86_64)
--
-- Host: 127.0.0.1    Database: occideas
-- ------------------------------------------------------
-- Server version	5.5.50-0ubuntu0.14.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Interview_DisplayAnswer`
--

DROP TABLE IF EXISTS `Interview_DisplayAnswer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Interview_DisplayAnswer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `interviewDisplayId` bigint(20) NOT NULL,
  `answerId` bigint(20) NOT NULL,
  `name` varchar(2048) DEFAULT NULL,
  `answerFreetext` varchar(2048) DEFAULT NULL,
  `nodeClass` varchar(235) DEFAULT NULL,
  `number` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `deleted` int(11) NOT NULL,
  `lastUpdated` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=389 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Interview_DisplayAnswer`
--

LOCK TABLES `Interview_DisplayAnswer` WRITE;
/*!40000 ALTER TABLE `Interview_DisplayAnswer` DISABLE KEYS */;
/*!40000 ALTER TABLE `Interview_DisplayAnswer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Participant`
--

DROP TABLE IF EXISTS `Participant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Participant` (
  `idParticipant` int(11) NOT NULL AUTO_INCREMENT,
  `reference` varchar(20) NOT NULL,
  `status` int(11) NOT NULL,
  `lastUpdated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`idParticipant`)
) ENGINE=InnoDB AUTO_INCREMENT=1437 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Participant`
--

LOCK TABLES `Participant` WRITE;
/*!40000 ALTER TABLE `Participant` DISABLE KEYS */;
/*!40000 ALTER TABLE `Participant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Rule_AdditionalField`
--

DROP TABLE IF EXISTS `Rule_AdditionalField`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Rule_AdditionalField` (
  `idRuleAdditionalField` bigint(20) NOT NULL AUTO_INCREMENT,
  `value` varchar(255) DEFAULT NULL,
  `idAdditionalField` bigint(20) DEFAULT NULL,
  `idRule` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`idRuleAdditionalField`),
  KEY `FK_q3hwcs12of05uekcp2d25dx5d` (`idAdditionalField`),
  KEY `FK_d57n41rgqvuttgn8swte8ciu6` (`idRule`),
  CONSTRAINT `FK_d57n41rgqvuttgn8swte8ciu6` FOREIGN KEY (`idRule`) REFERENCES `Rule` (`idRule`),
  CONSTRAINT `FK_q3hwcs12of05uekcp2d25dx5d` FOREIGN KEY (`idAdditionalField`) REFERENCES `AdditionalField` (`idadditionalfield`)
) ENGINE=InnoDB AUTO_INCREMENT=5683 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Rule_AdditionalField`
--

LOCK TABLES `Rule_AdditionalField` WRITE;
/*!40000 ALTER TABLE `Rule_AdditionalField` DISABLE KEYS */;
/*!40000 ALTER TABLE `Rule_AdditionalField` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Interview_Question`
--

DROP TABLE IF EXISTS `Interview_Question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Interview_Question` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `idinterview` bigint(20) NOT NULL,
  `topNodeId` bigint(20) NOT NULL,
  `question_id` bigint(20) DEFAULT NULL,
  `parentModuleId` bigint(20) NOT NULL,
  `parentAnswerId` bigint(20) DEFAULT NULL,
  `modCount` int(11) NOT NULL DEFAULT '0',
  `link` bigint(20) DEFAULT NULL,
  `name` varchar(2048) DEFAULT NULL,
  `description` varchar(2048) DEFAULT NULL,
  `nodeClass` varchar(255) DEFAULT NULL,
  `number` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `intQuestionSequence` int(11) NOT NULL,
  `deleted` int(11) NOT NULL,
  `isProcessed` tinyint(4) NOT NULL DEFAULT '0',
  `lastUpdated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=73372 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Interview_Question`
--

LOCK TABLES `Interview_Question` WRITE;
/*!40000 ALTER TABLE `Interview_Question` DISABLE KEYS */;
/*!40000 ALTER TABLE `Interview_Question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Rule`
--

DROP TABLE IF EXISTS `Rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Rule` (
  `idRule` bigint(20) NOT NULL AUTO_INCREMENT,
  `agentId` bigint(20) NOT NULL,
  `lastUpdated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `legacyRuleId` bigint(20) DEFAULT NULL,
  `level` int(11) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `deleted` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`idRule`)
) ENGINE=InnoDB AUTO_INCREMENT=13847 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Rule`
--

LOCK TABLES `Rule` WRITE;
/*!40000 ALTER TABLE `Rule` DISABLE KEYS */;
INSERT INTO `Rule` VALUES (13839,72,'2016-08-30 03:26:51',NULL,5,NULL,1),(13840,87,'2016-08-30 03:27:27',NULL,3,'',0),(13841,107,'2016-08-30 03:27:27',NULL,3,'',0),(13842,91,'2016-08-30 03:27:27',NULL,3,'',0),(13843,91,'2016-08-30 03:27:27',NULL,3,'',0),(13844,91,'2016-08-30 03:27:27',NULL,3,'',0),(13845,91,'2016-08-30 03:27:27',NULL,3,'',0),(13846,91,'2016-08-30 03:27:27',NULL,3,'',0);
/*!40000 ALTER TABLE `Rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Note`
--

DROP TABLE IF EXISTS `Note`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Note` (
  `idNote` bigint(20) NOT NULL AUTO_INCREMENT,
  `deleted` int(11) DEFAULT NULL,
  `lastUpdated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `text` varchar(2048) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `node_idNode` bigint(20) DEFAULT NULL,
  `interviewId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`idNote`),
  KEY `FK_kr3wlnbmt6jho41rbiv7hy693` (`node_idNode`),
  CONSTRAINT `FK_kr3wlnbmt6jho41rbiv7hy693` FOREIGN KEY (`node_idNode`) REFERENCES `Node` (`idNode`)
) ENGINE=InnoDB AUTO_INCREMENT=2918 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Note`
--

LOCK TABLES `Note` WRITE;
/*!40000 ALTER TABLE `Note` DISABLE KEYS */;
/*!40000 ALTER TABLE `Note` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Interview`
--

DROP TABLE IF EXISTS `Interview`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Interview` (
  `idinterview` bigint(20) NOT NULL AUTO_INCREMENT,
  `module_idNode` bigint(20) DEFAULT NULL,
  `fragment_idNode` bigint(20) DEFAULT NULL,
  `referenceNumber` varchar(255) NOT NULL,
  `idParticipant` bigint(20) NOT NULL,
  `parentId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`idinterview`),
  KEY `FK_srh0vgdnt8f7vvdmj88uxafsi` (`module_idNode`),
  CONSTRAINT `FK_srh0vgdnt8f7vvdmj88uxafsi` FOREIGN KEY (`module_idNode`) REFERENCES `Node` (`idNode`)
) ENGINE=InnoDB AUTO_INCREMENT=2053 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Interview`
--

LOCK TABLES `Interview` WRITE;
/*!40000 ALTER TABLE `Interview` DISABLE KEYS */;
/*!40000 ALTER TABLE `Interview` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Interview_Module`
--

DROP TABLE IF EXISTS `Interview_Module`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Interview_Module` (
  `idinterview` bigint(20) NOT NULL,
  `idNode` bigint(20) NOT NULL,
  `topNodeId` bigint(20) NOT NULL,
  `answerNode` bigint(20) NOT NULL,
  `parentNode` bigint(20) NOT NULL,
  `parentAnswerId` bigint(20) DEFAULT NULL,
  `name` varchar(2048) DEFAULT NULL,
  `number` varchar(255) DEFAULT NULL,
  `deleted` int(11) NOT NULL,
  `count` int(11) NOT NULL,
  `sequence` int(11) NOT NULL,
  `lastUpdated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`idinterview`,`idNode`,`count`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Interview_Module`
--

LOCK TABLES `Interview_Module` WRITE;
/*!40000 ALTER TABLE `Interview_Module` DISABLE KEYS */;
/*!40000 ALTER TABLE `Interview_Module` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AdditionalField`
--

DROP TABLE IF EXISTS `AdditionalField`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AdditionalField` (
  `idadditionalfield` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idadditionalfield`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AdditionalField`
--

LOCK TABLES `AdditionalField` WRITE;
/*!40000 ALTER TABLE `AdditionalField` DISABLE KEYS */;
/*!40000 ALTER TABLE `AdditionalField` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Node_Rule`
--

DROP TABLE IF EXISTS `Node_Rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Node_Rule` (
  `idRule` bigint(20) NOT NULL,
  `idNode` bigint(20) NOT NULL,
  KEY `FK_2jovkmckdt8xpts3eh0sjfuma` (`idNode`),
  KEY `FK_jj080ddim7h6s3vsdsfy42np7` (`idRule`),
  CONSTRAINT `FK_2jovkmckdt8xpts3eh0sjfuma` FOREIGN KEY (`idNode`) REFERENCES `Node` (`idNode`),
  CONSTRAINT `FK_jj080ddim7h6s3vsdsfy42np7` FOREIGN KEY (`idRule`) REFERENCES `Rule` (`idRule`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Node_Rule`
--

LOCK TABLES `Node_Rule` WRITE;
/*!40000 ALTER TABLE `Node_Rule` DISABLE KEYS */;
INSERT INTO `Node_Rule` VALUES (13840,44897),(13841,44897),(13842,44899),(13843,44901),(13844,44904),(13845,44908),(13846,44913);
/*!40000 ALTER TABLE `Node_Rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Interview_FiredRules`
--

DROP TABLE IF EXISTS `Interview_FiredRules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Interview_FiredRules` (
  `idInterview_FiredRules` int(11) NOT NULL AUTO_INCREMENT,
  `idinterview` int(11) DEFAULT NULL,
  `idRule` int(11) DEFAULT NULL,
  PRIMARY KEY (`idInterview_FiredRules`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Interview_FiredRules`
--

LOCK TABLES `Interview_FiredRules` WRITE;
/*!40000 ALTER TABLE `Interview_FiredRules` DISABLE KEYS */;
/*!40000 ALTER TABLE `Interview_FiredRules` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SYS_CONFIG`
--

DROP TABLE IF EXISTS `SYS_CONFIG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SYS_CONFIG` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(128) NOT NULL,
  `name` varchar(128) NOT NULL,
  `value` varchar(128) DEFAULT NULL,
  `updatedDt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updatedBy` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SYS_CONFIG`
--

LOCK TABLES `SYS_CONFIG` WRITE;
/*!40000 ALTER TABLE `SYS_CONFIG` DISABLE KEYS */;
INSERT INTO `SYS_CONFIG` VALUES (5,'config','activeintro','44944','2016-08-30 03:32:49',NULL);
/*!40000 ALTER TABLE `SYS_CONFIG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Node`
--

DROP TABLE IF EXISTS `Node`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Node` (
  `node_discriminator` varchar(31) NOT NULL,
  `idNode` bigint(20) NOT NULL AUTO_INCREMENT,
  `deleted` int(11) DEFAULT NULL,
  `description` varchar(2048) DEFAULT NULL,
  `lastUpdated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `link` bigint(20) NOT NULL,
  `name` varchar(2048) DEFAULT NULL,
  `nodeclass` varchar(255) DEFAULT NULL,
  `number` varchar(255) DEFAULT NULL,
  `originalId` bigint(20) NOT NULL,
  `sequence` int(11) NOT NULL,
  `topNodeId` bigint(20) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `parent_idNode` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`idNode`),
  KEY `FK_1ftn9ltvvjtvvdnswwoyxkj2b` (`parent_idNode`),
  CONSTRAINT `FK_1ftn9ltvvjtvvdnswwoyxkj2b` FOREIGN KEY (`parent_idNode`) REFERENCES `Node` (`idNode`)
) ENGINE=InnoDB AUTO_INCREMENT=44954 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Node`
--

LOCK TABLES `Node` WRITE;
/*!40000 ALTER TABLE `Node` DISABLE KEYS */;
INSERT INTO `Node` VALUES ('M',44894,0,'','2016-08-30 03:26:51',0,'default','M','',0,1,0,'M_Module',NULL),('M',44895,1,'Those who come into contact with animals. In following JSMs: Caretaker/Janitor, Health workers, Lab Worker/Chemist, Retail Workers and Teacher','2016-08-30 03:28:05',0,'(Copy from Import)aANI Animal workers aJSM','F',NULL,0,0,0,'F_ajsm',NULL),('Q',44896,0,'','2015-12-01 16:09:39',0,'Do you come into contact with animals at work?','Q','1',0,0,44895,'Q_simple',44895),('P',44897,0,'','2015-12-01 16:09:39',0,'Yes','P','1A',0,0,44895,'P_simple',44896),('Q',44898,0,'[Animal] Animal Handler Animal Type','2015-12-01 16:09:39',0,'Do you come into contact with any of the following animals at work?','Q','1A1',0,0,44895,'Q_multiple',44897),('P',44899,0,'mice','2015-12-01 16:09:39',0,'mice, rats, rabbits or guinea pigs','P','1A1A',0,0,44895,'P_simple',44898),('P',44901,0,'cats','2015-12-01 16:09:39',0,'cats','P','1A1B',0,1,44895,'P_simple',44898),('P',44904,0,'chickens','2015-12-01 16:09:39',0,'chickens, cows or pigs','P','1A1C',0,2,44895,'P_simple',44898),('P',44908,0,'frogs','2015-12-01 16:09:40',0,'frogs','P','1A1D',0,3,44895,'P_simple',44898),('P',44913,0,'bats','2015-12-01 16:09:40',0,'bats','P','1A1E',0,4,44895,'P_simple',44898),('P',44919,0,'','2015-12-01 16:09:40',0,'none of the above','P','1A1F',0,5,44895,'P_simple',44898),('P',44921,0,'','2015-12-01 16:09:40',0,'No','P','1B',0,1,44895,'P_simple',44896),('P',44924,0,'','2015-12-01 16:09:40',0,'Don\'t Know','P','1C',0,2,44895,'P_simple',44896),('F',44925,0,NULL,'2016-08-30 03:27:50',0,'aANI Animal','F',NULL,0,1,0,'F_ajsm',NULL),('Q',44926,0,'','2016-08-30 03:27:50',0,'Do you come into contact with animals at work?','Q','1',0,0,44925,'Q_simple',44925),('P',44927,0,'','2015-12-01 16:09:39',0,'Yes','P','1A',44897,0,44925,'P_simple',44926),('Q',44928,0,'[Animal] Animal Handler Animal Type','2015-12-01 16:09:39',0,'Do you come into contact with any of the following animals at work?','Q','1A1',44898,0,44925,'Q_multiple',44927),('P',44929,0,'mice','2015-12-01 16:09:39',0,'mice, rats, rabbits or guinea pigs','P','1A1A',44899,0,44925,'P_simple',44928),('P',44930,0,'cats','2015-12-01 16:09:39',0,'cats','P','1A1B',44901,1,44925,'P_simple',44928),('P',44931,0,'chickens','2015-12-01 16:09:39',0,'chickens, cows or pigs','P','1A1C',44904,2,44925,'P_simple',44928),('P',44932,0,'frogs','2015-12-01 16:09:40',0,'frogs','P','1A1D',44908,3,44925,'P_simple',44928),('P',44933,0,'bats','2015-12-01 16:09:40',0,'bats','P','1A1E',44913,4,44925,'P_simple',44928),('P',44934,0,'','2015-12-01 16:09:40',0,'none of the above','P','1A1F',44919,5,44925,'P_simple',44928),('P',44935,0,'','2015-12-01 16:09:40',0,'No','P','1B',44921,1,44925,'P_simple',44926),('P',44936,0,'','2015-12-01 16:09:40',0,'Don\'t Know','P','1C',44924,2,44925,'P_simple',44926),('Q',44937,0,NULL,'2016-08-30 03:28:51',0,'Standard Yes No?','Q','1',0,0,44894,'Q_simple',44894),('P',44938,0,NULL,'2016-08-30 03:28:51',0,'Yes','P','1A',0,0,44894,'P_simple',44937),('P',44939,0,NULL,'2016-08-30 03:28:51',0,'No','P','1B',0,1,44894,'P_simple',44937),('F',44940,0,NULL,'2016-08-30 03:29:03',0,'StandardYesNo','F',NULL,0,1,0,'F_template',NULL),('Q',44941,0,NULL,'2016-08-30 03:29:03',0,'Standard Yes No?','Q','1',0,0,44940,'Q_simple',44940),('P',44942,0,NULL,'2016-08-30 03:29:03',0,'Yes','P','1A',44938,0,44940,'P_simple',44941),('P',44943,0,NULL,'2016-08-30 03:29:03',0,'No','P','1B',44939,1,44940,'P_simple',44941),('M',44944,0,'New Description','2016-08-30 03:31:54',0,'AAAA Intro','M',NULL,0,0,0,'M_IntroModule',NULL),('Q',44945,0,NULL,'2016-08-30 03:29:52',0,'What was your job?','Q','1',0,0,44944,'Q_simple',44944),('P',44946,0,NULL,'2016-08-30 03:29:52',0,'Famer','P','1A',0,0,44944,'P_simple',44945),('P',44947,0,NULL,'2016-08-30 03:29:52',0,'Driver','P','1B',0,1,44944,'P_simple',44945),('M',44948,0,'About farming','2016-08-30 03:30:32',0,'FARM Farmer Module','M',NULL,0,0,0,'M_Module',NULL),('Q',44949,0,NULL,'2016-08-30 03:30:43',0,'Did you work with animals?','Q','1',0,0,44948,'Q_simple',44948),('P',44950,0,NULL,'2016-08-30 03:29:03',0,'Yes','P','1A',44942,0,44948,'P_simple',44949),('P',44951,0,NULL,'2016-08-30 03:29:03',0,'No','P','1B',44943,1,44948,'P_simple',44949),('Q',44952,0,NULL,'2016-08-30 03:31:15',44925,'aANI Animal','Q','1A1',0,0,44948,'Q_linkedajsm',44950),('Q',44953,0,'About farming','2016-08-30 03:31:54',44948,'FARM Farmer Module','Q','1A1',0,0,44944,'Q_linkedmodule',44946);
/*!40000 ALTER TABLE `Node` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_PROFILE`
--

DROP TABLE IF EXISTS `USER_PROFILE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USER_PROFILE` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `type` (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_PROFILE`
--

LOCK TABLES `USER_PROFILE` WRITE;
/*!40000 ALTER TABLE `USER_PROFILE` DISABLE KEYS */;
INSERT INTO `USER_PROFILE` VALUES (5,'ADMIN'),(2,'ASSESSOR'),(3,'CONTDEV'),(1,'INTERVIEWER'),(4,'READONLY');
/*!40000 ALTER TABLE `USER_PROFILE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AgentInfo`
--

DROP TABLE IF EXISTS `AgentInfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AgentInfo` (
  `agent_discriminator` varchar(31) NOT NULL,
  `idAgent` bigint(20) NOT NULL AUTO_INCREMENT,
  `deleted` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `lastUpdated` date DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `agentGroup_idAgent` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`idAgent`),
  KEY `FK_ht6dquacdf8c1xcah9fyja94u` (`agentGroup_idAgent`),
  CONSTRAINT `FK_ht6dquacdf8c1xcah9fyja94u` FOREIGN KEY (`agentGroup_idAgent`) REFERENCES `AgentInfo` (`idAgent`)
) ENGINE=InnoDB AUTO_INCREMENT=186 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AgentInfo`
--

LOCK TABLES `AgentInfo` WRITE;
/*!40000 ALTER TABLE `AgentInfo` DISABLE KEYS */;
INSERT INTO `AgentInfo` VALUES ('G',1,1,'','2014-10-30','PCBs Group',NULL),('A',2,0,'','2014-10-30','PCBs',1),('G',3,0,'','2014-10-30','SOLVENTS.',NULL),('A',4,0,'','2014-10-30','Benzene',3),('A',5,1,'','2014-10-30','Other Aromatic Solvents',3),('A',6,0,'','2014-10-30','Aliphatic Solvents',3),('A',7,0,'','2014-10-30','Chlorinated Solvents',3),('A',8,0,'','2014-10-30','Alcohol',3),('A',9,0,'','2014-10-30','Trichloroethylene',3),('A',10,0,'','2014-10-30','Tetrachloroethylene (PERC)',3),('G',11,0,'','2014-10-30','ORGANIC DUST',NULL),('A',12,0,'','2014-10-30','Wood Dust',11),('A',13,0,'','2014-10-30','Grain Dust',11),('A',14,0,'','2014-10-30','Cotton Dust',11),('A',15,1,'','2014-10-30','Other Organic Dust',11),('A',16,0,'','2014-10-30','Leather dust',11),('G',17,0,'','2014-10-30','PESTICIDES',NULL),('A',18,0,'','2014-10-30','Organochlorines',17),('A',19,0,'','2014-10-30','Organophosphates',17),('A',20,0,'','2014-10-30','Phenoxy Herbicides',17),('A',21,0,'','2014-10-30','Other Herbicides (glyphosate)',17),('A',22,0,'','2014-10-30','Other Pesticides',17),('G',23,0,'','2014-10-30','OILS',NULL),('A',24,0,'','2014-10-30','Mineral Oils',23),('A',25,1,'','2014-10-30','Synthetic Oils',23),('A',26,1,'','2014-10-30','Natural Oils',23),('G',27,0,'','2014-10-30','FERTILIZERS',NULL),('A',28,0,'','2014-10-30','Mineral Fertilizers',27),('A',29,0,'','2014-10-30','Natural Fertilizers',27),('G',30,0,'','2014-10-30','PRODUCTSOFCOMBUSTION',NULL),('A',31,0,'','2014-10-30','Diesel Exhaust',30),('A',32,0,'','2014-10-30','Petrol Exhaust',30),('A',33,0,'','2014-10-30','Other Exhausts',30),('A',34,0,'','2014-10-30','Other PAHs',30),('A',35,0,'','2014-10-30','Environmental Tobacco Smoke',30),('G',36,0,'','2014-10-30','INORGANIC DUSTS',NULL),('A',37,0,'','2014-10-30','Asbestos',36),('A',38,0,'','2014-10-30','Fibreglass',36),('A',39,0,'','2014-10-30','Silica',36),('A',40,0,'','2014-10-30','Other Inorganic Fibres',36),('A',41,1,'','2014-10-30','Other inorganic dusts',36),('G',42,1,'','2014-10-30','PHYSICAL ACTIVITY',NULL),('G',43,1,'','2014-10-30','PRESERVATIVES',NULL),('A',44,1,'','2014-10-30','Formaldehyde',43),('G',45,0,'','2014-10-30','RADIATION',NULL),('A',46,1,'','2014-10-30','UV',45),('A',47,0,'','2014-10-30','Ionizing radiation',45),('A',48,0,'','2014-10-30','ELF',45),('A',49,0,'','2014-10-30','RF',45),('G',50,0,'','2014-10-30','METALS',NULL),('A',51,0,'','2014-10-30','Lead',50),('A',52,1,'','2014-10-30','Other toxic metals',50),('A',53,1,'','2014-10-30','Other metals',50),('A',54,0,'','2014-10-30','Iron',50),('A',55,0,'','2014-10-30','Arsenic',50),('A',56,0,'','2014-10-30','Beryllium',50),('A',57,0,'','2014-10-30','Cadmium',50),('A',58,0,'','2014-10-30','Chromium VI',50),('A',59,0,'','2014-10-30','Cobalt',50),('A',60,0,'','2014-10-30','Nickel',50),('G',61,0,'','2014-10-30','NITROSAMINES GROUP',NULL),('A',62,0,'','2014-10-30','Nitrosamines',61),('G',63,0,'','2014-10-30','CHRONODISRUPTION',NULL),('A',64,0,'','2014-10-30','Light at night',63),('A',65,0,'','2014-10-30','Phase shift',63),('A',66,0,'','2014-10-30','Sleep disturbances',63),('A',67,0,'','2014-10-30','Diet and CD',63),('A',68,0,'','2014-10-30','Alcohol and CD',63),('A',69,0,'','2014-10-30','Physical activity and CD',63),('A',70,0,'','2014-10-30','Vit D and CD',63),('G',71,0,'','2014-10-30','INDUSTRIAL CHEMICALS',NULL),('A',72,0,'','2014-10-30','1,3 butadiene',71),('A',73,0,'','2014-10-30','Vinyl chloride',71),('A',74,0,'','2014-10-30','Epichlorhydrin',71),('A',75,0,'','2014-10-30','diethyl/dimethyl sulphate',71),('A',76,0,'','2014-10-30','Ortho-toluidine',71),('A',77,0,'','2014-10-30','Styrene',71),('A',78,0,'','2014-10-30','Acrylamide',71),('A',79,0,'','2014-10-30','MOCA',71),('A',80,0,'','2014-10-30','Acid mists',71),('A',81,0,'','2014-10-30','Ethylene oxide',71),('G',82,0,'','2014-10-30','PHARMACEUTICALS',NULL),('A',83,0,'','2014-10-30','Chemotherapeutics',82),('G',84,1,'','2014-10-30','CONSUMER PRODUCTS',NULL),('A',85,0,'','2014-10-30','Hairdyes',84),('G',86,0,'','2014-10-30','ASTHMAGENS',NULL),('A',87,0,'','2014-10-30','Arthropods Mites',86),('A',88,0,'','2014-10-30','Biological enzymes',86),('A',89,0,'','2014-10-30','Bioaerosols',86),('A',90,0,'','2014-10-30','Derived from Fish/Shellfish',86),('A',91,0,'','2014-10-30','Derived From Animals',86),('A',92,0,'','2014-10-30','Flour',86),('A',93,0,'','2014-10-30','Foods',86),('A',94,0,'','2014-10-30','Flowers',86),('A',95,0,'','2014-10-30','Latex',86),('A',96,0,'','2014-10-30','Asthma Wood Dusts',86),('A',97,0,'','2014-10-30','Derived from Plants - Other',86),('A',98,0,'','2014-10-30','Soldering',86),('A',99,0,'','2014-10-30','Reactive Dyes',86),('A',100,0,'','2014-10-30','Anhydrides',86),('A',101,0,'','2014-10-30','Acrylates',86),('A',102,0,'','2014-10-30','Epoxy',86),('A',103,0,'','2014-10-30','Asthma Ethylene Oxide',86),('A',104,0,'','2014-10-30','Asthma Aldehydes',86),('A',105,0,'','2014-10-30','Pesticides',86),('A',106,0,'','2014-10-30','Amines',86),('A',107,0,'','2014-10-30','Ammoniacal compounds',86),('A',108,0,'','2014-10-30','Industrial Cleaning and Sterlising Agents',86),('A',109,0,'','2014-10-30','Acids',86),('A',110,0,'','2014-10-30','Isocyanates',86),('A',111,0,'','2014-10-30','Other Reactive Chemicals',86),('A',112,0,'','2014-10-30','Asthma Metals',86),('A',113,0,'','2014-10-30','Drugs',86),('G',114,0,'','2014-11-25','NOISE AND VIBRATION',NULL),('G',115,1,'','2014-11-25','noise01',NULL),('A',116,0,'','2014-11-25','noise2',114),('G',121,1,'','2014-12-11','AGENTS WITH SPACES!#',NULL),('A',122,1,'','2014-12-11','Special agents #$%^&*()?/',121),('A',124,1,'','2014-12-11','CIA Agent',121),('A',125,1,'','2015-01-15','',114),('A',126,0,'','2015-01-15','IMPACT NOISE',114),('G',127,1,'','2015-06-25','TESTGROUP',NULL),('A',128,0,'','2015-06-25','test one',127),('A',129,1,'','2015-07-24','',45),('A',130,1,'','2015-07-24','',45),('A',131,0,'','2015-07-24','Solar UV',45),('A',132,0,'','2015-07-24','Artificial UV',45),('A',133,0,'','2015-07-24','Mercury',50),('A',134,0,'','2015-07-29','Toluene',3),('A',135,0,'','2015-07-29','p-xylene',3),('A',136,0,'','2015-07-29','Ethyl Benzene',3),('A',137,0,'','2015-07-29','n-hexane',3),('A',138,0,'','2015-07-29','Graveyard',63),('A',139,0,'','2015-07-29','Carbon Monoxide',30),('A',140,0,'','2015-07-29','Carbon Disulphide',71),('G',141,1,'','2015-08-18','TEST',NULL),('A',142,1,'','2015-08-18','test 1',141),('G',143,1,'','2015-08-18','AATEST',NULL),('A',144,0,'','2015-08-18','test1',143),('G',145,0,'','2015-08-19','TROY GROUP',NULL),('A',146,0,'','2015-08-19','Troy One',145),('A',147,0,'','2015-08-19','Troy Two',145),('A',148,0,'','2015-08-19','Troy Three',145),('G',149,0,'','2015-09-09','Delete group',NULL),('A',150,0,'','2015-10-21','Occular Solar UV',45),('A',151,0,'','2015-10-21','PCBs',71),('A',152,0,'','2015-10-21','Formaldehyde',71),('A',153,0,'','2015-10-21','Nitrosamines',71),('G',154,1,'','2015-10-22','test',NULL),('A',155,0,'','2015-10-22','test',154),('G',156,0,'','2015-11-09','',NULL),('A',157,0,'','2015-11-27','Vibration',114),('G',160,0,NULL,NULL,'ag test',NULL),('A',161,0,NULL,NULL,'a test',160),('G',164,0,NULL,NULL,'ag test',NULL),('A',165,0,NULL,NULL,'a test',164),('G',168,0,NULL,NULL,'ag test',NULL),('A',169,0,NULL,NULL,'a test',168),('G',172,0,NULL,NULL,'ag test',NULL),('A',173,0,NULL,NULL,'a test',172),('G',176,0,NULL,NULL,'ag test',NULL),('A',177,0,NULL,NULL,'a test',176),('G',180,0,NULL,NULL,'ag test',NULL),('A',181,0,NULL,NULL,'a test',180),('G',184,0,NULL,NULL,'ag test',NULL),('A',185,0,NULL,NULL,'a test',184);
/*!40000 ALTER TABLE `AgentInfo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `APP_USER`
--

DROP TABLE IF EXISTS `APP_USER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `APP_USER` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sso_id` varchar(30) NOT NULL,
  `password` varchar(100) NOT NULL,
  `first_name` varchar(30) NOT NULL,
  `last_name` varchar(30) NOT NULL,
  `email` varchar(30) NOT NULL,
  `state` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sso_id` (`sso_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `APP_USER`
--

LOCK TABLES `APP_USER` WRITE;
/*!40000 ALTER TABLE `APP_USER` DISABLE KEYS */;
INSERT INTO `APP_USER` VALUES (1,'interviewer','$2a$10$62LNfdU4F3HZgD7/dWX7r.i6XqledTrv/Rc9bi3gmNIL3JZxFpHRi','interviewer','interviewer','int@yahoo.com','Active'),(2,'assessor','$2a$10$NUhG9fZhLSsxaY6YJrAQcucM3XOJuh4vaw/uCSJ3lxC4meUwQCoMq','assessor','assessor','int@yahoo.com','Active'),(3,'contdev','$2a$10$r2yh1rcLaF9DQnY2OTtqsOMqfijen6XvnBB8yvz/nEm/fyy1Vc.E.','contdev','contdev','int@yahoo.com','Active'),(4,'readonly','$2a$10$0.2nTVeqCm0WAzy3g.cw0.W9qT/GiCJOKYaWciW3kBVd6WZ/2JHWC','readonly','readonly','int@yahoo.com','Active'),(5,'admin','$2a$10$lT0RYDaULC.WR3cJNT5JVOs0VQHr/Jdb.gFBydPfuCozGaxL1bL2a','admin','admin','int@yahoo.com','Active');
/*!40000 ALTER TABLE `APP_USER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Interview_Answer`
--

DROP TABLE IF EXISTS `Interview_Answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Interview_Answer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `idinterview` bigint(20) NOT NULL,
  `topNodeId` bigint(20) NOT NULL,
  `parentQuestionId` bigint(20) DEFAULT NULL,
  `answerId` bigint(20) DEFAULT NULL,
  `link` bigint(20) DEFAULT NULL,
  `name` varchar(2048) DEFAULT NULL,
  `description` varchar(2048) DEFAULT NULL,
  `answerFreetext` varchar(2048) DEFAULT NULL,
  `isProcessed` tinyint(4) NOT NULL,
  `modCount` int(11) DEFAULT '1',
  `nodeClass` varchar(235) DEFAULT NULL,
  `number` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `deleted` int(11) NOT NULL,
  `lastUpdated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `interviewQuestionId` bigint(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=56780 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Interview_Answer`
--

LOCK TABLES `Interview_Answer` WRITE;
/*!40000 ALTER TABLE `Interview_Answer` DISABLE KEYS */;
/*!40000 ALTER TABLE `Interview_Answer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `APP_USER_USER_PROFILE`
--

DROP TABLE IF EXISTS `APP_USER_USER_PROFILE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `APP_USER_USER_PROFILE` (
  `user_id` bigint(20) NOT NULL,
  `user_profile_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`user_profile_id`),
  KEY `FK_USER_PROFILE` (`user_profile_id`),
  CONSTRAINT `FK_APP_USER` FOREIGN KEY (`user_id`) REFERENCES `APP_USER` (`id`),
  CONSTRAINT `FK_USER_PROFILE` FOREIGN KEY (`user_profile_id`) REFERENCES `USER_PROFILE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `APP_USER_USER_PROFILE`
--

LOCK TABLES `APP_USER_USER_PROFILE` WRITE;
/*!40000 ALTER TABLE `APP_USER_USER_PROFILE` DISABLE KEYS */;
INSERT INTO `APP_USER_USER_PROFILE` VALUES (1,1),(2,2),(3,3),(4,4),(5,5);
/*!40000 ALTER TABLE `APP_USER_USER_PROFILE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Interview_AutoAssessedRules`
--

DROP TABLE IF EXISTS `Interview_AutoAssessedRules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Interview_AutoAssessedRules` (
  `idInterview_AutoAssessedRules` int(11) NOT NULL AUTO_INCREMENT,
  `idinterview` int(11) DEFAULT NULL,
  `idRule` int(11) DEFAULT NULL,
  PRIMARY KEY (`idInterview_AutoAssessedRules`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Interview_AutoAssessedRules`
--

LOCK TABLES `Interview_AutoAssessedRules` WRITE;
/*!40000 ALTER TABLE `Interview_AutoAssessedRules` DISABLE KEYS */;
/*!40000 ALTER TABLE `Interview_AutoAssessedRules` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AUDIT_LOG`
--

DROP TABLE IF EXISTS `AUDIT_LOG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AUDIT_LOG` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(30) NOT NULL,
  `user_type` varchar(30) DEFAULT NULL,
  `action` varchar(200) DEFAULT NULL,
  `method` varchar(200) DEFAULT NULL,
  `arguments` blob,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=111033 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AUDIT_LOG`
--

LOCK TABLES `AUDIT_LOG` WRITE;
/*!40000 ALTER TABLE `AUDIT_LOG` DISABLE KEYS */;
INSERT INTO `AUDIT_LOG` VALUES (111016,'','','Generic','DomainUsernamePasswordAuthenticationProvider-authenticate','arg[1]=[org.springframework.security.authentication.UsernamePasswordAuthenticationToken@1f: Principal: Optional.of(admin); Credentials: [PROTECTED]; Authenticated: false; Details: null; Not granted any authorities]','2016-08-30 13:26:41'),(111017,'','','Generic','DomainUsernamePasswordAuthenticationProvider-authenticate','arg[1]=[org.springframework.security.authentication.UsernamePasswordAuthenticationToken@1f: Principal: Optional.of(admin); Credentials: [PROTECTED]; Authenticated: false; Details: null; Not granted any authorities]','2016-08-30 13:27:11'),(111018,'admin','ROLE_ADMIN','Generic','ModuleServiceImpl-listAll',NULL,'2016-08-30 13:27:14'),(111019,'admin','ROLE_ADMIN','Generic','ModuleServiceImpl-listAll',NULL,'2016-08-30 13:27:27'),(111020,'admin','ROLE_ADMIN','Generic','ModuleServiceImpl-listAll',NULL,'2016-08-30 13:27:36'),(111021,'admin','ROLE_ADMIN','Generic','ModuleServiceImpl-listAll',NULL,'2016-08-30 13:27:55'),(111022,'admin','ROLE_ADMIN','Generic','ModuleServiceImpl-listAll',NULL,'2016-08-30 13:28:05'),(111023,'admin','ROLE_ADMIN','Generic','ModuleServiceImpl-listAll',NULL,'2016-08-30 13:29:15'),(111024,'admin','ROLE_ADMIN','Generic','ModuleServiceImpl-listAll',NULL,'2016-08-30 13:29:27'),(111025,'admin','ROLE_ADMIN','Generic','ModuleServiceImpl-listAll',NULL,'2016-08-30 13:29:55'),(111026,'admin','ROLE_ADMIN','Generic','ModuleServiceImpl-listAll',NULL,'2016-08-30 13:30:03'),(111027,'admin','ROLE_ADMIN','Generic','ModuleServiceImpl-listAll',NULL,'2016-08-30 13:30:32'),(111028,'admin','ROLE_ADMIN','Generic','ModuleServiceImpl-listAll',NULL,'2016-08-30 13:31:10'),(111029,'admin','ROLE_ADMIN','Generic','ModuleServiceImpl-listAll',NULL,'2016-08-30 13:31:50'),(111030,'admin','ROLE_ADMIN','Generic','ModuleServiceImpl-listAll',NULL,'2016-08-30 13:31:55'),(111031,'admin','ROLE_ADMIN','Generic','ModuleServiceImpl-listAll',NULL,'2016-08-30 13:32:00'),(111032,'admin','ROLE_ADMIN','Generic','ModuleServiceImpl-listAll',NULL,'2016-08-30 13:32:27');
/*!40000 ALTER TABLE `AUDIT_LOG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Interview_ManualAssessedRules`
--

DROP TABLE IF EXISTS `Interview_ManualAssessedRules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Interview_ManualAssessedRules` (
  `idInterview_ManualAssessedRules` int(11) NOT NULL AUTO_INCREMENT,
  `idinterview` int(11) DEFAULT NULL,
  `idRule` int(11) DEFAULT NULL,
  PRIMARY KEY (`idInterview_ManualAssessedRules`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Interview_ManualAssessedRules`
--

LOCK TABLES `Interview_ManualAssessedRules` WRITE;
/*!40000 ALTER TABLE `Interview_ManualAssessedRules` DISABLE KEYS */;
/*!40000 ALTER TABLE `Interview_ManualAssessedRules` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Interview_Display`
--

DROP TABLE IF EXISTS `Interview_Display`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Interview_Display` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `idinterview` bigint(20) NOT NULL,
  `name` varchar(2048) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `question_id` bigint(20) DEFAULT NULL,
  `number` varchar(255) DEFAULT NULL,
  `topNodeId` bigint(20) NOT NULL,
  `parentModuleId` bigint(20) NOT NULL,
  `parentAnswerId` bigint(20) DEFAULT NULL,
  `link` bigint(20) DEFAULT NULL,
  `description` varchar(2048) DEFAULT NULL,
  `nodeClass` varchar(255) DEFAULT NULL,
  `sequence` int(11) NOT NULL,
  `header` varchar(50) DEFAULT NULL,
  `deleted` int(11) NOT NULL,
  `lastUpdated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=387 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Interview_Display`
--

LOCK TABLES `Interview_Display` WRITE;
/*!40000 ALTER TABLE `Interview_Display` DISABLE KEYS */;
/*!40000 ALTER TABLE `Interview_Display` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-08-30 13:33:33
