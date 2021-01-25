DROP TABLE IF EXISTS `node_voxco`;
CREATE TABLE `node_voxco` (
	`surveyId` bigint(20) NOT NULL,
      `idNode` bigint(20) NOT NULL,
      `surveyName` varchar (255) NULL,
      `deleted` int(11) DEFAULT NULL,
      `lastUpdated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
      `extractionId` bigint(20) NULL,
      `extractionStatus` varchar (50) NULL,
      `fileId` bigint(10) NULL,
      `extractionStart` timestamp NULL,
      `extractionEnd` timestamp NULL,
      `resultPath` varchar (255) NULL,
	PRIMARY KEY (surveyId),
	CONSTRAINT `FK_NVXCO_NODE` FOREIGN KEY (`idNode`) REFERENCES `Node` (`idNode`)
);
