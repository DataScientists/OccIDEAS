DROP TABLE IF EXISTS QUALTRICS_SURVEY;

CREATE TABLE QUALTRICS_SURVEY
(
    id              bigint(20) NOT NULL AUTO_INCREMENT,
    responseId      varchar(255) NOT NULL,
    topic           varchar(255) NOT NULL,
    brandId         varchar(255) NOT NULL,
    surveyId        varchar(255) NOT NULL,
    qualtricsStatus varchar(255) NOT NULL,
    completedDate   timestamp    NOT NULL,
    response        TEXT DEFAULT NULL,
    isProcessed     BIT  DEFAULT NULL,
    PRIMARY KEY (id)
);