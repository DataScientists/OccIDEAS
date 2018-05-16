DROP TABLE JMXLog;

CREATE TABLE JMXLog
(
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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;