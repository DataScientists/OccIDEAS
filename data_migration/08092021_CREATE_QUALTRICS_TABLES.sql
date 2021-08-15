DROP TABLE IF EXISTS QUALTRICS_SURVEY_SUBSCRIPTION;

CREATE TABLE QUALTRICS_SURVEY_SUBSCRIPTION
(
    subscriptionId   varchar(255) NOT NULL,
    surveyId         varchar(255) DEFAULT NULL,
    status           varchar(255) DEFAULT NULL,
    subscriptionDate timestamp    NOT NULL,
    PRIMARY KEY (subscriptionId)
);

DROP TABLE IF EXISTS QUALTRICS_SURVEY;

CREATE TABLE QUALTRICS_SURVEY
(
    responseId      varchar(255) NOT NULL,
    topic           varchar(255) NOT NULL,
    brandId         varchar(255) NOT NULL,
    surveyId        varchar(255) NOT NULL,
    qualtricsStatus varchar(255) NOT NULL,
    completedDate   timestamp    NOT NULL,
    response        TEXT DEFAULT NULL,
--     isProcessed     BIT DEFAULT NULL,
    PRIMARY KEY (responseId)
);