CREATE TABLE QUALTRICS_SURVEY_SUBSCRIPTION
(
    subscriptionId   varchar(2048) NOT NULL,
    surveyId         varchar(255) DEFAULT NULL,
    status           varchar(255) DEFAULT NULL,
    subscriptionDate timestamp     NOT NULL,
    PRIMARY KEY (subscriptionId)
);