DROP TABLE IF EXISTS `APP_USER_USER_PROFILE`;

DROP TABLE IF EXISTS `USER_PROFILE`;

DROP TABLE IF EXISTS `APP_USER`;

/*All User's gets stored in APP_USER table*/
create table APP_USER (
   `id` BIGINT NOT NULL AUTO_INCREMENT,
   `sso_id` VARCHAR(30) NOT NULL,
   `password` VARCHAR(100) NOT NULL,
   `first_name` VARCHAR(30) NOT NULL,
   `last_name`  VARCHAR(30) NOT NULL,
   `email` VARCHAR(30) NOT NULL,
   `state` VARCHAR(30) NOT NULL,  
   PRIMARY KEY (`id`),
   UNIQUE (`sso_id`)
) ENGINE=InnoDB AUTO_INCREMENT=292 DEFAULT CHARSET=latin1;

/* USER_PROFILE table contains all possible roles */ 
create table USER_PROFILE(
   `id` BIGINT NOT NULL AUTO_INCREMENT,
   `type` VARCHAR(30) NOT NULL,
   PRIMARY KEY (`id`),
   UNIQUE (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=292 DEFAULT CHARSET=latin1;

/* JOIN TABLE for MANY-TO-MANY relationship*/  
CREATE TABLE APP_USER_USER_PROFILE (
    `user_id` BIGINT NOT NULL,
    `user_profile_id` BIGINT NOT NULL,
    PRIMARY KEY (`user_id`, `user_profile_id`),
    CONSTRAINT FK_APP_USER FOREIGN KEY (`user_id`) REFERENCES APP_USER (`id`),
    CONSTRAINT FK_USER_PROFILE FOREIGN KEY (`user_profile_id`) REFERENCES USER_PROFILE (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=292 DEFAULT CHARSET=latin1;

/* Populate USER_PROFILE Table */
INSERT INTO USER_PROFILE(type)
VALUES ('READONLY');
 
INSERT INTO USER_PROFILE(type)
VALUES ('ASSESSOR');
 
INSERT INTO USER_PROFILE(type)
VALUES ('CONTDEV');

INSERT INTO USER_PROFILE(type)
VALUES ('INTERVIEWER');

/* Interviewer */
INSERT INTO APP_USER(sso_id, password, first_name, last_name, email, state)
VALUES ('interviewer','$2a$10$62LNfdU4F3HZgD7/dWX7r.i6XqledTrv/Rc9bi3gmNIL3JZxFpHRi', 'interviewer','interviewer','int@yahoo.com', 'Active');

/* Assessor */
INSERT INTO APP_USER(sso_id, password, first_name, last_name, email, state)
VALUES ('assessor','$2a$10$NUhG9fZhLSsxaY6YJrAQcucM3XOJuh4vaw/uCSJ3lxC4meUwQCoMq', 'assessor','assessor','int@yahoo.com', 'Active');

/* Content Developer */
INSERT INTO APP_USER(sso_id, password, first_name, last_name, email, state)
VALUES ('contdev','$2a$10$r2yh1rcLaF9DQnY2OTtqsOMqfijen6XvnBB8yvz/nEm/fyy1Vc.E.', 'contdev','contdev','int@yahoo.com', 'Active');
