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
    isProcessed     BIT  DEFAULT NULL,
    PRIMARY KEY (responseId)
);

DROP TABLE IF EXISTS QUALTRICS_QUESTION_MAPPER;

CREATE TABLE QUALTRICS_QUESTION_MAPPER
(
    surveyId        varchar(255)  NOT NULL,
    questionId      varchar(255)  NOT NULL,
    idNode          bigint(20) NOT NULL,
    questionText    varchar(2048) NOT NULL,
    type            varchar(255)  NOT NULL,
    frequencyIdNode bigint(20) DEFAULT NULL,
    PRIMARY KEY (surveyId, questionId)
);

DROP TABLE IF EXISTS QUALTRICS_SURVEY_RESPONSE;

CREATE TABLE QUALTRICS_SURVEY_RESPONSE
(
    id                bigint(20) NOT NULL AUTO_INCREMENT,
    surveyId          varchar(255) NOT NULL,
    responseId        varchar(255) NOT NULL,
    ipAddress         varchar(255) NOT NULL,
    progress          varchar(255) NOT NULL,
    duration          varchar(255) NOT NULL,
    finished          varchar(255) NOT NULL,
    recordedDate      varchar(255) NOT NULL,
    locationLatitude  varchar(255) NOT NULL,
    locationLongitude varchar(255) NOT NULL,
    questionAnswers   LONGTEXT DEFAULT NULL,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS INTERVIEW_RESULTS;

CREATE TABLE INTERVIEW_RESULTS
(
    interviewId     bigint(20) NOT NULL,
    referenceNumber varchar(255) NOT NULL,
    results         LONGTEXT DEFAULT NULL,
    PRIMARY KEY (interviewId)
);