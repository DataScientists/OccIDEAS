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
  `lastUpdated` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=292 DEFAULT CHARSET=latin1;