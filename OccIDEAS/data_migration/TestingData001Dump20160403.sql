CREATE DATABASE  IF NOT EXISTS `occideas` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `occideas`;
-- MySQL dump 10.13  Distrib 5.5.47, for debian-linux-gnu (x86_64)
--
-- Host: 127.0.0.1    Database: occideas
-- ------------------------------------------------------
-- Server version	5.5.47-0ubuntu0.14.04.1

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
-- Table structure for table `Participant`
--

DROP TABLE IF EXISTS `Participant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Participant` (
  `idParticipant` int(11) NOT NULL AUTO_INCREMENT,
  `reference` varchar(20) NOT NULL,
  `status` int(11) NOT NULL,
  `lastUpdated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`idParticipant`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=5119 DEFAULT CHARSET=latin1;
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
  `question_id` bigint(20) DEFAULT NULL,
  `parentId` bigint(20) NOT NULL,
  `name` varchar(2048) DEFAULT NULL,
  `description` varchar(2048) DEFAULT NULL,
  `nodeClass` varchar(5) DEFAULT NULL,
  `number` varchar(20) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  `deleted` int(11) NOT NULL,
  `lastUpdated` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=309 DEFAULT CHARSET=latin1;
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
  `lastUpdated` datetime DEFAULT NULL,
  `legacyRuleId` bigint(20) DEFAULT NULL,
  `level` int(11) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `deleted` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`idRule`)
) ENGINE=InnoDB AUTO_INCREMENT=9130 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Rule`
--

LOCK TABLES `Rule` WRITE;
/*!40000 ALTER TABLE `Rule` DISABLE KEYS */;
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
  `lastUpdated` datetime DEFAULT NULL,
  `text` varchar(2048) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `node_idNode` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`idNote`),
  KEY `FK_kr3wlnbmt6jho41rbiv7hy693` (`node_idNode`),
  CONSTRAINT `FK_kr3wlnbmt6jho41rbiv7hy693` FOREIGN KEY (`node_idNode`) REFERENCES `Node` (`idNode`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
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
  `idParticipant` bigint(20) NOT NULL,
  `parentId` bigint(20) DEFAULT NULL,
  `fragment_idNode` bigint(20) DEFAULT NULL,
  `referenceNumber` varchar(255) NOT NULL,
  PRIMARY KEY (`idinterview`),
  KEY `FK_srh0vgdnt8f7vvdmj88uxafsi` (`module_idNode`),
  CONSTRAINT `FK_srh0vgdnt8f7vvdmj88uxafsi` FOREIGN KEY (`module_idNode`) REFERENCES `Node` (`idNode`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Interview`
--

LOCK TABLES `Interview` WRITE;
/*!40000 ALTER TABLE `Interview` DISABLE KEYS */;
/*!40000 ALTER TABLE `Interview` ENABLE KEYS */;
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
  CONSTRAINT `FK_jj080ddim7h6s3vsdsfy42np7` FOREIGN KEY (`idRule`) REFERENCES `Rule` (`idRule`),
  CONSTRAINT `FK_2jovkmckdt8xpts3eh0sjfuma` FOREIGN KEY (`idNode`) REFERENCES `Node` (`idNode`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Node_Rule`
--

LOCK TABLES `Node_Rule` WRITE;
/*!40000 ALTER TABLE `Node_Rule` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Interview_FiredRules`
--

LOCK TABLES `Interview_FiredRules` WRITE;
/*!40000 ALTER TABLE `Interview_FiredRules` DISABLE KEYS */;
/*!40000 ALTER TABLE `Interview_FiredRules` ENABLE KEYS */;
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
  `lastUpdated` datetime DEFAULT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=31208 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Node`
--

LOCK TABLES `Node` WRITE;
/*!40000 ALTER TABLE `Node` DISABLE KEYS */;
INSERT INTO `Node` VALUES ('M',1,1,'',NULL,0,'default','Q','1A1',0,0,31176,'Q_linkedmodule',31178),('Q',2,0,NULL,NULL,0,'New Question','Q','1',0,0,1,'Q_simple',1),('P',3,0,NULL,NULL,0,'New Possible Answer','P','1A',0,0,1,'P_simple',2),('P',4,0,NULL,NULL,0,'New Possible Answer','P','1B',0,1,1,'P_simple',2),('M',31176,0,'New Description',NULL,0,'IntroM','M',NULL,0,0,0,'M_IntroModule',NULL),('Q',31177,1,NULL,NULL,0,'Intro New Question','Q','2',0,1,31176,'Q_simple',31176),('P',31178,1,NULL,NULL,0,'New Possible Answer1','P','2A',0,0,31176,'P_simple',31177),('P',31179,1,NULL,NULL,0,'New Possible Answer2','P','2B',0,1,31176,'P_simple',31177),('Q',31180,1,'',NULL,1,'default','Q','1A2',0,1,31176,'Q_linkedmodule',31178),('M',31181,0,'New Description',NULL,0,'AAA','M',NULL,0,0,0,'M_Module',NULL),('Q',31182,0,NULL,NULL,0,'New Question','Q','1',0,0,31181,'Q_simple',31181),('P',31183,0,NULL,NULL,0,'New Possible Answer','P','1A',0,0,31181,'P_simple',31182),('Q',31184,1,'New Description',NULL,31181,'AAA','Q','1',0,0,31176,'Q_linkedmodule',31176),('Q',31185,1,NULL,NULL,0,'New Question','Q','6',0,5,31181,'Q_simple',31181),('Q',31186,0,NULL,NULL,0,'New Question(Copy)','Q','4',0,3,31181,'Q_simple',31181),('P',31187,0,NULL,NULL,0,'New Possible Answer','P','4A',0,0,31181,'P_simple',31186),('Q',31188,0,NULL,NULL,0,'New Question(Copy)(Copy)','Q','5',0,4,31181,'Q_simple',31181),('P',31189,0,NULL,NULL,0,'New Possible Answer','P','5A',0,0,31181,'P_simple',31188),('Q',31190,0,NULL,NULL,0,'New Question(Copy)(Copy)(Copy)','Q','2',0,1,31181,'Q_simple',31181),('P',31191,0,NULL,NULL,0,'New Possible Answer','P','2A',0,0,31181,'P_simple',31190),('Q',31192,0,NULL,NULL,0,'New Question(Copy)(Copy)(Copy)(Copy)','Q','3',0,2,31181,'Q_simple',31181),('P',31193,0,NULL,NULL,0,'New Possible Answer','P','3A',0,0,31181,'P_simple',31192),('Q',31194,0,NULL,NULL,0,'New Question1','Q','1',0,0,31176,'Q_single',31176),('P',31195,0,NULL,NULL,0,'New Possible Answer1','P','1A',0,0,31176,'P_simple',31194),('P',31196,0,NULL,NULL,0,'New Possible Answer2','P','1B',0,1,31176,'P_simple',31194),('Q',31197,0,NULL,NULL,0,'New Question2','Q','2',0,1,31176,'Q_simple',31176),('P',31198,0,NULL,NULL,0,'New Possible Answer','P','2B',0,1,31176,'P_simple',31197),('P',31199,0,NULL,NULL,0,'New Possible Answer','P','2A',0,0,31176,'P_simple',31197),('Q',31200,1,NULL,NULL,0,'New Question(Copy)','Q','1B1',0,0,31176,'Q_simple',31196),('P',31201,0,NULL,NULL,0,'New Possible Answer','P','1B1A',0,0,31176,'P_simple',31200),('P',31202,0,NULL,NULL,0,'New Possible Answer','P','1B1B',0,1,31176,'P_simple',31200),('Q',31203,0,NULL,NULL,0,'New Question3','Q','3',0,2,31176,'Q_simple',31176),('P',31204,0,NULL,NULL,0,'New Possible Answer','P','3A',0,0,31176,'P_simple',31203),('P',31205,0,NULL,NULL,0,'New Possible Answer','P','3B',0,1,31176,'P_simple',31203),('Q',31206,1,'New Description',NULL,31181,'AAA','Q','1A1A1',0,0,31176,'Q_linkedmodule',31198),('Q',31207,0,'New Description',NULL,31181,'AAA','Q','1A1',0,0,31176,'Q_linkedmodule',31195);
/*!40000 ALTER TABLE `Node` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Interview_Linked`
--

DROP TABLE IF EXISTS `Interview_Linked`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Interview_Linked` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `idinterview` bigint(20) NOT NULL,
  `parentQuestionId` bigint(20) DEFAULT NULL,
  `linkedId` bigint(20) DEFAULT NULL,
  `name` varchar(2048) DEFAULT NULL,
  `description` varchar(2048) DEFAULT NULL,
  `deleted` int(11) NOT NULL,
  `lastUpdated` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=292 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Interview_Linked`
--

LOCK TABLES `Interview_Linked` WRITE;
/*!40000 ALTER TABLE `Interview_Linked` DISABLE KEYS */;
/*!40000 ALTER TABLE `Interview_Linked` ENABLE KEYS */;
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
/*!40000 ALTER TABLE `AgentInfo` ENABLE KEYS */;
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
  `topQuestionId` bigint(20) NOT NULL,
  `parentQuestionId` bigint(20) DEFAULT NULL,
  `answerId` bigint(20) DEFAULT NULL,
  `name` varchar(2048) DEFAULT NULL,
  `description` varchar(2048) DEFAULT NULL,
  `answerFreetext` varchar(2048) DEFAULT NULL,
  `nodeClass` varchar(5) DEFAULT NULL,
  `number` varchar(20) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  `deleted` int(11) NOT NULL,
  `lastUpdated` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=309 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Interview_Answer`
--

LOCK TABLES `Interview_Answer` WRITE;
/*!40000 ALTER TABLE `Interview_Answer` DISABLE KEYS */;
/*!40000 ALTER TABLE `Interview_Answer` ENABLE KEYS */;
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
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-04-03 11:23:56
