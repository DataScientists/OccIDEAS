DROP TABLE IF EXISTS COMMIT_VERSION;

CREATE TABLE COMMIT_VERSION
(
    id          varchar(255) NOT NULL,
    author      varchar(255) NOT NULL,
    lastUpdated timestamp    NOT NULL,
    parent      varchar(255) default NULL,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS FOLDER;

CREATE TABLE FOLDER
(
    id          bigint(20) NOT NULL AUTO_INCREMENT,
    name        varchar(255) NOT NULL,
    version     bigint(20) NOT NULL,
    lastUpdated timestamp    NOT NULL,
    head        varchar(255) default NULL,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS COMMIT_FILE;

CREATE TABLE COMMIT_FILE
(
    id       bigint(20) NOT NULL AUTO_INCREMENT,
    commitId varchar(255) NOT NULL,
    filename varchar(255) NOT NULL,
    hash     blob         NOT NULL,
    PRIMARY KEY (id)
);