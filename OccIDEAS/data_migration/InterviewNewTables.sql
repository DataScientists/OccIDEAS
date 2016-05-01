DROP TABLE IF EXISTS `Interview_Question_Answer`;

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
  `lastUpdated` datetime DEFAULT NULL,
  PRIMARY KEY (`idinterview`,`idNode`,`count`)
) ENGINE=InnoDB AUTO_INCREMENT=292 DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `Interview_Question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;

CREATE TABLE `Interview_Question` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `idinterview` bigint(20) NOT NULL,
  `topNodeId` bigint(20) NOT NULL,
  `question_id` bigint(20) DEFAULT NULL,
  `parentId` bigint(20) NOT NULL,
  `parentAnswerId` bigint(20) DEFAULT NULL,
  `modCount` int(11) NOT NULL,
  `link` bigint(20) DEFAULT NULL,
  `name` varchar(2048) DEFAULT NULL,
  `description` varchar(2048) DEFAULT NULL,
  `nodeClass` varchar(255) DEFAULT NULL,
  `number` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `intQuestionSequence` int(11) NOT NULL, 
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
  `link` bigint(20) DEFAULT NULL,
  `name` varchar(2048) DEFAULT NULL,
  `description` varchar(2048) DEFAULT NULL,
  `answerFreetext` varchar(2048) DEFAULT NULL,
  `isProcessed` tinyint NOT NULL,
  `modCount` int(11) NOT NULL,
  `nodeClass` varchar(235) DEFAULT NULL,
  `number` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `deleted` int(11) NOT NULL,
  `lastUpdated` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=292 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `Interview_Linked`;