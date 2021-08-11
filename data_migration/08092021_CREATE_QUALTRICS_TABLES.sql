CREATE TABLE QUALTRICS_SURVEY_SUBSCRIPTION
(
    subscriptionId   varchar(2048) NOT NULL,
    surveyId         varchar(255) DEFAULT NULL,
    status           varchar(255) DEFAULT NULL,
    subscriptionDate timestamp     NOT NULL,
    PRIMARY KEY (subscriptionId)
);

CREATE TABLE QUALTRICS_SURVEY
(
    responseId      varchar(2048) NOT NULL,
    topic           varchar(2048) NOT NULL,
    brandId         varchar(255)  NOT NULL,
    surveyId        varchar(255)  NOT NULL,
    qualtricsStatus varchar(255)  NOT NULL,
    completedDate   timestamp     NOT NULL,
    response        TEXT DEFAULT NULL,
    PRIMARY KEY (responseId)
);