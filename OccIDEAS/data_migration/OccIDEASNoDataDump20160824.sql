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
) ENGINE=InnoDB AUTO_INCREMENT=13839 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=44894 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=187 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `email` varchar(1024) DEFAULT NULL,
  `state` varchar(30) NOT NULL,
  `first_name` varchar(1024) DEFAULT NULL,
  `last_name` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sso_id` (`sso_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=111016 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-08-24 13:48:50
