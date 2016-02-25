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
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-02-24  8:05:17
