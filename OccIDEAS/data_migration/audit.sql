DROP TABLE IF EXISTS `AUDIT_LOG`;

/*All User's gets stored in APP_USER table*/
create table AUDIT_LOG (
   `id` BIGINT NOT NULL AUTO_INCREMENT,
   `username` VARCHAR(30) NOT NULL,
   `user_type` VARCHAR(30) NOT NULL,
   `action` VARCHAR(200) NOT NULL,
   `date`  datetime DEFAULT NULL,
   `user_ip` VARCHAR(50) NOT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=292 DEFAULT CHARSET=latin1;