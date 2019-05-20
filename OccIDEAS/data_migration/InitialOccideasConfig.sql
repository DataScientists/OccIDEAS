-- MySQL dump 10.13  Distrib 5.7.26, for Linux (x86_64)
--
-- Host: localhost    Database: occideas
-- ------------------------------------------------------
-- Server version	5.7.26-0ubuntu0.16.04.1

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `APP_USER`
--

LOCK TABLES `APP_USER` WRITE;
/*!40000 ALTER TABLE `APP_USER` DISABLE KEYS */;
INSERT INTO `APP_USER` VALUES (1,'admin','$2a$10$DvrQhzYrmVIUa2c8dQtSdOsmiE861DKiJ6VMMy4jDHbwUJXard3L6','admin','admin','int@yahoo.com','Active');
/*!40000 ALTER TABLE `APP_USER` ENABLE KEYS */;
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
INSERT INTO `APP_USER_USER_PROFILE` VALUES (1,1);
/*!40000 ALTER TABLE `APP_USER_USER_PROFILE` ENABLE KEYS */;
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
  `user_type` varchar(1024) DEFAULT NULL,
  `action` varchar(200) DEFAULT NULL,
  `method` varchar(200) DEFAULT NULL,
  `arguments` blob,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AUDIT_LOG`
--

LOCK TABLES `AUDIT_LOG` WRITE;
/*!40000 ALTER TABLE `AUDIT_LOG` DISABLE KEYS */;
/*!40000 ALTER TABLE `AUDIT_LOG` ENABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AdditionalField`
--

LOCK TABLES `AdditionalField` WRITE;
/*!40000 ALTER TABLE `AdditionalField` DISABLE KEYS */;
/*!40000 ALTER TABLE `AdditionalField` ENABLE KEYS */;
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
  `lastUpdated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `name` varchar(255) DEFAULT NULL,
  `agentGroup_idAgent` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`idAgent`),
  KEY `FK_ht6dquacdf8c1xcah9fyja94u` (`agentGroup_idAgent`),
  CONSTRAINT `FK_ht6dquacdf8c1xcah9fyja94u` FOREIGN KEY (`agentGroup_idAgent`) REFERENCES `AgentInfo` (`idAgent`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AgentInfo`
--

LOCK TABLES `AgentInfo` WRITE;
/*!40000 ALTER TABLE `AgentInfo` DISABLE KEYS */;
/*!40000 ALTER TABLE `AgentInfo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `AssessmentAnswerSummary`
--

DROP TABLE IF EXISTS `AssessmentAnswerSummary`;
/*!50001 DROP VIEW IF EXISTS `AssessmentAnswerSummary`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `AssessmentAnswerSummary` AS SELECT 
 1 AS `primaryKey`,
 1 AS `idParticipant`,
 1 AS `reference`,
 1 AS `idinterview`,
 1 AS `answerId`,
 1 AS `name`,
 1 AS `answerFreetext`,
 1 AS `type`,
 1 AS `status`,
 1 AS `assessedStatus`,
 1 AS `interviewModuleName`*/;
SET character_set_client = @saved_cs_client;

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
  `assessedStatus` varchar(255) DEFAULT '',
  PRIMARY KEY (`idinterview`),
  KEY `FK_srh0vgdnt8f7vvdmj88uxafsi` (`module_idNode`),
  CONSTRAINT `FK_srh0vgdnt8f7vvdmj88uxafsi` FOREIGN KEY (`module_idNode`) REFERENCES `Node` (`idNode`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Interview`
--

LOCK TABLES `Interview` WRITE;
/*!40000 ALTER TABLE `Interview` DISABLE KEYS */;
/*!40000 ALTER TABLE `Interview` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `InterviewIntroModule_Module`
--

DROP TABLE IF EXISTS `InterviewIntroModule_Module`;
/*!50001 DROP VIEW IF EXISTS `InterviewIntroModule_Module`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `InterviewIntroModule_Module` AS SELECT 
 1 AS `primaryKey`,
 1 AS `idModule`,
 1 AS `introModuleNodeName`,
 1 AS `interviewPrimaryKey`,
 1 AS `interviewId`,
 1 AS `interviewModuleName`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `InterviewModule_Fragment`
--

DROP TABLE IF EXISTS `InterviewModule_Fragment`;
/*!50001 DROP VIEW IF EXISTS `InterviewModule_Fragment`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `InterviewModule_Fragment` AS SELECT 
 1 AS `primaryKey`,
 1 AS `idFragment`,
 1 AS `fragmentNodeName`,
 1 AS `interviewPrimaryKey`,
 1 AS `interviewId`,
 1 AS `interviewFragmentName`*/;
SET character_set_client = @saved_cs_client;

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
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
  `number` varchar(2048) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Interview_Display`
--

LOCK TABLES `Interview_Display` WRITE;
/*!40000 ALTER TABLE `Interview_Display` DISABLE KEYS */;
/*!40000 ALTER TABLE `Interview_Display` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Interview_DisplayAnswer`
--

LOCK TABLES `Interview_DisplayAnswer` WRITE;
/*!40000 ALTER TABLE `Interview_DisplayAnswer` DISABLE KEYS */;
/*!40000 ALTER TABLE `Interview_DisplayAnswer` ENABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Interview_FiredRules`
--

LOCK TABLES `Interview_FiredRules` WRITE;
/*!40000 ALTER TABLE `Interview_FiredRules` DISABLE KEYS */;
/*!40000 ALTER TABLE `Interview_FiredRules` ENABLE KEYS */;
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
  `linkNum` varchar(255) DEFAULT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Interview_Question`
--

LOCK TABLES `Interview_Question` WRITE;
/*!40000 ALTER TABLE `Interview_Question` DISABLE KEYS */;
/*!40000 ALTER TABLE `Interview_Question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `JMXLog`
--

DROP TABLE IF EXISTS `JMXLog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `JMXLog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sessionId` varchar(1024) NOT NULL,
  `userId` varchar(1024) NOT NULL,
  `function` varchar(1024) NOT NULL,
  `header` varchar(1024) NOT NULL,
  `getParameters` varchar(1024) DEFAULT NULL,
  `postParameters` varchar(1024) DEFAULT NULL,
  `deleted` int(11) DEFAULT NULL,
  `createdDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `JMXLog`
--

LOCK TABLES `JMXLog` WRITE;
/*!40000 ALTER TABLE `JMXLog` DISABLE KEYS */;
/*!40000 ALTER TABLE `JMXLog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Language`
--

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Language`
--

LOCK TABLES `Language` WRITE;
/*!40000 ALTER TABLE `Language` DISABLE KEYS */;
/*!40000 ALTER TABLE `Language` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `ModuleRule`
--

DROP TABLE IF EXISTS `ModuleRule`;
/*!50001 DROP VIEW IF EXISTS `ModuleRule`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `ModuleRule` AS SELECT 
 1 AS `primaryKey`,
 1 AS `idModule`,
 1 AS `moduleName`,
 1 AS `idRule`,
 1 AS `ruleLevel`,
 1 AS `idAgent`,
 1 AS `agentName`,
 1 AS `idNode`,
 1 AS `nodeNumber`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `Module_Fragment`
--

DROP TABLE IF EXISTS `Module_Fragment`;
/*!50001 DROP VIEW IF EXISTS `Module_Fragment`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `Module_Fragment` AS SELECT 
 1 AS `primaryKey`,
 1 AS `moduleId`,
 1 AS `moduleName`,
 1 AS `idNode`,
 1 AS `fragmentName`,
 1 AS `nodeNumber`,
 1 AS `fragmentId`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `Module_IntroModule`
--

DROP TABLE IF EXISTS `Module_IntroModule`;
/*!50001 DROP VIEW IF EXISTS `Module_IntroModule`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `Module_IntroModule` AS SELECT 
 1 AS `primaryKey`,
 1 AS `moduleId`,
 1 AS `moduleName`,
 1 AS `idNode`,
 1 AS `moduleLinkName`,
 1 AS `nodeNumber`,
 1 AS `moduleLinkId`*/;
SET character_set_client = @saved_cs_client;

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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Node`
--

LOCK TABLES `Node` WRITE;
/*!40000 ALTER TABLE `Node` DISABLE KEYS */;
INSERT INTO `Node` VALUES ('M',1,0,'New Description','2019-05-17 12:29:08',0,'default','M',NULL,0,1,0,'M_Module',NULL),('Q',2,0,NULL,'2019-05-17 12:29:28',0,'New Question','Q','1',0,0,1,'Q_simple',1),('F',3,0,NULL,'2019-05-17 12:29:43',0,'default','F',NULL,0,1,0,'F_ajsm',NULL),('Q',4,0,NULL,'2019-05-17 12:29:43',0,'New Question','Q','1',0,0,3,'Q_simple',3);
/*!40000 ALTER TABLE `Node` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Node_Language`
--

DROP TABLE IF EXISTS `Node_Language`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Node_Language`
--

LOCK TABLES `Node_Language` WRITE;
/*!40000 ALTER TABLE `Node_Language` DISABLE KEYS */;
/*!40000 ALTER TABLE `Node_Language` ENABLE KEYS */;
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
/*!40000 ALTER TABLE `Node_Rule` ENABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Participant`
--

LOCK TABLES `Participant` WRITE;
/*!40000 ALTER TABLE `Participant` DISABLE KEYS */;
/*!40000 ALTER TABLE `Participant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `REPORT_HISTORY`
--

DROP TABLE IF EXISTS `REPORT_HISTORY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `REPORT_HISTORY` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(128) NOT NULL,
  `name` varchar(128) NOT NULL,
  `path` varchar(128) DEFAULT NULL,
  `status` varchar(128) NOT NULL,
  `progress` varchar(128) NOT NULL,
  `requestor` varchar(128) NOT NULL,
  `jsonData` longtext,
  `updatedDt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updatedBy` varchar(128) DEFAULT NULL,
  `startDt` timestamp NULL DEFAULT NULL,
  `endDt` timestamp NULL DEFAULT NULL,
  `duration` float DEFAULT NULL,
  `recordCount` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REPORT_HISTORY`
--

LOCK TABLES `REPORT_HISTORY` WRITE;
/*!40000 ALTER TABLE `REPORT_HISTORY` DISABLE KEYS */;
/*!40000 ALTER TABLE `REPORT_HISTORY` ENABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Rule`
--

LOCK TABLES `Rule` WRITE;
/*!40000 ALTER TABLE `Rule` DISABLE KEYS */;
/*!40000 ALTER TABLE `Rule` ENABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Rule_AdditionalField`
--

LOCK TABLES `Rule_AdditionalField` WRITE;
/*!40000 ALTER TABLE `Rule_AdditionalField` DISABLE KEYS */;
/*!40000 ALTER TABLE `Rule_AdditionalField` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SYS_CONFIG`
--

LOCK TABLES `SYS_CONFIG` WRITE;
/*!40000 ALTER TABLE `SYS_CONFIG` DISABLE KEYS */;
INSERT INTO `SYS_CONFIG` VALUES (1,'config','activeIntro','1','2019-05-18 04:47:25','admin'),(2,'config','REPORT_EXPORT_CSV_DIR','/tmp/','2019-05-18 04:47:50',NULL);
/*!40000 ALTER TABLE `SYS_CONFIG` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_PROFILE`
--

LOCK TABLES `USER_PROFILE` WRITE;
/*!40000 ALTER TABLE `USER_PROFILE` DISABLE KEYS */;
INSERT INTO `USER_PROFILE` VALUES (1,'ADMIN');
/*!40000 ALTER TABLE `USER_PROFILE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Final view structure for view `AssessmentAnswerSummary`
--

/*!50001 DROP VIEW IF EXISTS `AssessmentAnswerSummary`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `AssessmentAnswerSummary` AS select concat(`p`.`idParticipant`,':',`p`.`reference`,':',`i`.`idinterview`,':',`ia`.`answerId`) AS `primaryKey`,`p`.`idParticipant` AS `idParticipant`,`p`.`reference` AS `reference`,`i`.`idinterview` AS `idinterview`,`ia`.`answerId` AS `answerId`,`ia`.`name` AS `name`,`ia`.`answerFreetext` AS `answerFreetext`,`ia`.`type` AS `type`,`p`.`status` AS `status`,`i`.`assessedStatus` AS `assessedStatus`,`im`.`interviewModuleName` AS `interviewModuleName` from (((`Interview` `i` join `Participant` `p`) join `Interview_Answer` `ia`) join `InterviewIntroModule_Module` `im`) where ((`i`.`idinterview` = `ia`.`idinterview`) and (`i`.`idinterview` = `im`.`interviewId`) and (`i`.`idParticipant` = `p`.`idParticipant`) and (`p`.`deleted` = 0) and (`ia`.`deleted` = 0) and (`im`.`idModule` <> (select `SYS_CONFIG`.`value` from `SYS_CONFIG` where (`SYS_CONFIG`.`name` = 'activeintro') limit 1))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `InterviewIntroModule_Module`
--

/*!50001 DROP VIEW IF EXISTS `InterviewIntroModule_Module`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `InterviewIntroModule_Module` AS select concat(`m`.`idNode`,':',`iq`.`id`) AS `primaryKey`,`m`.`idNode` AS `idModule`,`m`.`name` AS `introModuleNodeName`,`iq`.`id` AS `interviewPrimaryKey`,`iq`.`idinterview` AS `interviewId`,`iq`.`name` AS `interviewModuleName` from (`Node` `m` join `Interview_Question` `iq`) where ((`m`.`idNode` = `iq`.`link`) and ((`iq`.`type` = 'Q_linkedmodule') or (`iq`.`type` = 'M_IntroModule')) and (`iq`.`deleted` = 0)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `InterviewModule_Fragment`
--

/*!50001 DROP VIEW IF EXISTS `InterviewModule_Fragment`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `InterviewModule_Fragment` AS select concat(`m`.`idNode`,':',`iq`.`id`) AS `primaryKey`,`m`.`idNode` AS `idFragment`,`m`.`name` AS `fragmentNodeName`,`iq`.`id` AS `interviewPrimaryKey`,`iq`.`idinterview` AS `interviewId`,`iq`.`name` AS `interviewFragmentName` from (`Node` `m` join `Interview_Question` `iq`) where ((`m`.`idNode` = `iq`.`link`) and (`iq`.`type` = 'Q_linkedajsm') and (`iq`.`deleted` = 0)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `ModuleRule`
--

/*!50001 DROP VIEW IF EXISTS `ModuleRule`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `ModuleRule` AS select concat(`m`.`idNode`,':',`nr`.`idRule`,':',`r`.`agentId`,':',`n`.`idNode`) AS `primaryKey`,`m`.`idNode` AS `idModule`,`m`.`name` AS `moduleName`,`nr`.`idRule` AS `idRule`,`r`.`level` AS `ruleLevel`,`r`.`agentId` AS `idAgent`,`a`.`name` AS `agentName`,`n`.`idNode` AS `idNode`,`n`.`number` AS `nodeNumber` from ((((`Node` `n` join `Node` `m` on((`n`.`topNodeId` = `m`.`idNode`))) join `Node_Rule` `nr` on((`n`.`idNode` = `nr`.`idNode`))) join `Rule` `r` on((`nr`.`idRule` = `r`.`idRule`))) join `AgentInfo` `a` on((`r`.`agentId` = `a`.`idAgent`))) where ((`m`.`deleted` = 0) and (`n`.`deleted` = 0) and (`r`.`deleted` = 0)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `Module_Fragment`
--

/*!50001 DROP VIEW IF EXISTS `Module_Fragment`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `Module_Fragment` AS select concat(`m`.`idNode`,':',`n`.`idNode`,':',`n`.`number`,':',`n`.`link`) AS `primaryKey`,`m`.`idNode` AS `moduleId`,`m`.`name` AS `moduleName`,`n`.`idNode` AS `idNode`,`n`.`name` AS `fragmentName`,`n`.`number` AS `nodeNumber`,`n`.`link` AS `fragmentId` from (`Node` `m` join `Node` `n` on((`n`.`topNodeId` = `m`.`idNode`))) where ((`n`.`type` = 'Q_linkedajsm') and (`n`.`deleted` = 0)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `Module_IntroModule`
--

/*!50001 DROP VIEW IF EXISTS `Module_IntroModule`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `Module_IntroModule` AS select concat(`m`.`idNode`,':',`n`.`idNode`,':',`n`.`number`,':',`n`.`link`) AS `primaryKey`,`m`.`idNode` AS `moduleId`,`m`.`name` AS `moduleName`,`n`.`idNode` AS `idNode`,`n`.`name` AS `moduleLinkName`,`n`.`number` AS `nodeNumber`,`n`.`link` AS `moduleLinkId` from (`Node` `m` join `Node` `n` on((`n`.`topNodeId` = `m`.`idNode`))) where ((`n`.`type` = 'Q_linkedmodule') and (`n`.`deleted` = 0)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-05-18  4:51:37
