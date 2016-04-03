DROP TABLE IF EXISTS `Interview_Question_Answer`;

DROP TABLE IF EXISTS `Interview_Question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;

CREATE TABLE `Interview_Question` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `idinterview` bigint(20) NOT NULL,
  `topNodeId` bigint(20) NOT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=292 DEFAULT CHARSET=latin1;

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
) ENGINE=InnoDB AUTO_INCREMENT=292 DEFAULT CHARSET=latin1;

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