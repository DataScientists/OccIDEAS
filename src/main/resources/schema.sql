CREATE TABLE APP_USER
(
    id         bigint       NOT NULL AUTO_INCREMENT,
    sso_id     varchar(30)  NOT NULL,
    password   varchar(100) NOT NULL,
    first_name varchar(30)  NOT NULL,
    last_name  varchar(30)  NOT NULL,
    email      varchar(30)  NOT NULL,
    state      varchar(30)  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY sso_id (sso_id)
);

CREATE TABLE APP_USER_USER_PROFILE
(
    user_id         bigint NOT NULL,
    user_profile_id bigint NOT NULL,
    PRIMARY KEY (user_id, user_profile_id)
);

CREATE TABLE USER_PROFILE
(
    id   bigint      NOT NULL AUTO_INCREMENT,
    type varchar(30) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY type (type)
);

CREATE TABLE AdditionalField
(
    idadditionalfield bigint NOT NULL AUTO_INCREMENT,
    type              varchar(255) DEFAULT NULL,
    value             varchar(255) DEFAULT NULL,
    PRIMARY KEY (idadditionalfield)
);

CREATE TABLE AgentInfo
(
    agent_discriminator varchar(31) NOT NULL,
    idAgent             bigint      NOT NULL AUTO_INCREMENT,
    deleted             int         NOT NULL,
    description         varchar(255)         DEFAULT NULL,
    lastUpdated         timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    name                varchar(255)         DEFAULT NULL,
    agentGroup_idAgent  bigint               DEFAULT NULL,
    PRIMARY KEY (idAgent)
);

CREATE TABLE AUDIT_LOG
(
    id        bigint      NOT NULL AUTO_INCREMENT,
    username  varchar(30) NOT NULL,
    user_type varchar(1024) DEFAULT NULL,
    action    varchar(200)  DEFAULT NULL,
    method    varchar(200)  DEFAULT NULL,
    arguments blob,
    date      datetime      DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE Interview
(
    idinterview     bigint       NOT NULL AUTO_INCREMENT,
    module_idNode   bigint       DEFAULT NULL,
    fragment_idNode bigint       DEFAULT NULL,
    referenceNumber varchar(255) NOT NULL,
    idParticipant   bigint       NOT NULL,
    parentId        bigint       DEFAULT NULL,
    assessedStatus  varchar(255) DEFAULT '',
    PRIMARY KEY (idinterview)
);

CREATE TABLE Interview_Answer
(
    id                  bigint    NOT NULL AUTO_INCREMENT,
    idinterview         bigint    NOT NULL,
    topNodeId           bigint    NOT NULL,
    parentQuestionId    bigint             DEFAULT NULL,
    answerId            bigint             DEFAULT NULL,
    link                bigint             DEFAULT NULL,
    name                varchar(2048)      DEFAULT NULL,
    description         varchar(2048)      DEFAULT NULL,
    answerFreetext      mediumblob,
    isProcessed         tinyint   NOT NULL,
    modCount            int                DEFAULT '1',
    nodeClass           varchar(235)       DEFAULT NULL,
    number              varchar(255)       DEFAULT NULL,
    type                varchar(255)       DEFAULT NULL,
    deleted             int       NOT NULL,
    lastUpdated         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    interviewQuestionId bigint             DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE Interview_AutoAssessedRules
(
    idInterview_AutoAssessedRules int NOT NULL AUTO_INCREMENT,
    idinterview                   int DEFAULT NULL,
    idRule                        int DEFAULT NULL,
    PRIMARY KEY (idInterview_AutoAssessedRules)
);

CREATE TABLE Interview_Display
(
    id             bigint    NOT NULL AUTO_INCREMENT,
    idinterview    bigint    NOT NULL,
    name           varchar(2048)      DEFAULT NULL,
    type           varchar(255)       DEFAULT NULL,
    question_id    bigint             DEFAULT NULL,
    topNodeId      bigint    NOT NULL,
    parentModuleId bigint    NOT NULL,
    parentAnswerId bigint             DEFAULT NULL,
    link           bigint             DEFAULT NULL,
    description    varchar(2048)      DEFAULT NULL,
    nodeClass      varchar(255)       DEFAULT NULL,
    sequence       int       NOT NULL,
    header         varchar(50)        DEFAULT NULL,
    deleted        int       NOT NULL,
    lastUpdated    timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    number         varchar(2048)      DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE Interview_DisplayAnswer
(
    id                 bigint NOT NULL AUTO_INCREMENT,
    interviewDisplayId bigint NOT NULL,
    answerId           bigint NOT NULL,
    name               varchar(2048) DEFAULT NULL,
    answerFreetext     varchar(2048) DEFAULT NULL,
    nodeClass          varchar(235)  DEFAULT NULL,
    number             varchar(255)  DEFAULT NULL,
    type               varchar(255)  DEFAULT NULL,
    deleted            int    NOT NULL,
    lastUpdated        datetime      DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE Interview_FiredRules
(
    idInterview_FiredRules int NOT NULL AUTO_INCREMENT,
    idinterview            int DEFAULT NULL,
    idRule                 int DEFAULT NULL,
    PRIMARY KEY (idInterview_FiredRules)
);

CREATE TABLE Interview_ManualAssessedRules
(
    idInterview_ManualAssessedRules int NOT NULL AUTO_INCREMENT,
    idinterview                     int DEFAULT NULL,
    idRule                          int DEFAULT NULL,
    PRIMARY KEY (idInterview_ManualAssessedRules)
);

CREATE TABLE Interview_Module
(
    idinterview    bigint    NOT NULL,
    idNode         bigint    NOT NULL,
    topNodeId      bigint    NOT NULL,
    answerNode     bigint    NOT NULL,
    parentNode     bigint    NOT NULL,
    parentAnswerId bigint             DEFAULT NULL,
    name           varchar(2048)      DEFAULT NULL,
    number         varchar(255)       DEFAULT NULL,
    deleted        int       NOT NULL,
    linkNum        varchar(255)       DEFAULT NULL,
    count          int       NOT NULL,
    sequence       int       NOT NULL,
    lastUpdated    timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (idinterview, idNode, count)
);

CREATE TABLE Interview_Question
(
    id                  bigint    NOT NULL AUTO_INCREMENT,
    idinterview         bigint    NOT NULL,
    topNodeId           bigint    NOT NULL,
    question_id         bigint             DEFAULT NULL,
    parentModuleId      bigint    NOT NULL,
    parentAnswerId      bigint             DEFAULT NULL,
    modCount            int       NOT NULL DEFAULT '0',
    link                bigint             DEFAULT NULL,
    name                varchar(2048)      DEFAULT NULL,
    description         varchar(2048)      DEFAULT NULL,
    nodeClass           varchar(255)       DEFAULT NULL,
    number              varchar(255)       DEFAULT NULL,
    type                varchar(255)       DEFAULT NULL,
    intQuestionSequence int       NOT NULL,
    deleted             int       NOT NULL,
    isProcessed         tinyint   NOT NULL DEFAULT '0',
    lastUpdated         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE JMXLog
(
    id             bigint        NOT NULL AUTO_INCREMENT,
    sessionId      varchar(1024) NOT NULL,
    userId         varchar(1024) NOT NULL,
    function       varchar(1024) NOT NULL,
    header         varchar(1024) NOT NULL,
    getParameters  varchar(1024)          DEFAULT NULL,
    postParameters varchar(1024)          DEFAULT NULL,
    deleted        int                    DEFAULT NULL,
    createdDate    timestamp     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE Language
(
    id          bigint        NOT NULL AUTO_INCREMENT,
    language    varchar(20)   NOT NULL,
    description varchar(2048) NOT NULL,
    lastUpdated datetime      DEFAULT NULL,
    flag        varchar(2048) DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE Node
(
    node_discriminator varchar(31) NOT NULL,
    idNode             bigint      NOT NULL AUTO_INCREMENT,
    deleted            int                  DEFAULT NULL,
    description        varchar(2048)        DEFAULT NULL,
    lastUpdated        timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    link               bigint      NOT NULL,
    name               varchar(2048)        DEFAULT NULL,
    nodeclass          varchar(255)         DEFAULT NULL,
    number             varchar(255)         DEFAULT NULL,
    originalId         bigint      NOT NULL,
    sequence           int         NOT NULL,
    topNodeId          bigint      NOT NULL,
    type               varchar(255)         DEFAULT NULL,
    parent_idNode      bigint               DEFAULT NULL,
    PRIMARY KEY (idNode)
);

CREATE TABLE Node_Language
(
    id          bigint    NOT NULL AUTO_INCREMENT,
    languageId  bigint    NOT NULL,
    word        varchar(2048)      DEFAULT NULL,
    translation varchar(2048)      DEFAULT NULL,
    lastUpdated timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE node_qsf
(
    surveyId    varchar(255) NOT NULL,
    idNode      bigint       NOT NULL,
    results     varchar(255)          DEFAULT NULL,
    lastUpdated timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (surveyId)
);

CREATE TABLE Node_Rule
(
    idRule bigint NOT NULL,
    idNode bigint NOT NULL
);

CREATE TABLE node_voxco
(
    surveyId            bigint    NOT NULL,
    idNode              bigint    NOT NULL,
    surveyName          varchar(255)       DEFAULT NULL,
    deleted             int                DEFAULT NULL,
    lastUpdated         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    extractionId        bigint             DEFAULT NULL,
    extractionStatus    varchar(50)        DEFAULT NULL,
    fileId              bigint             DEFAULT NULL,
    extractionStart     timestamp NULL DEFAULT NULL,
    extractionEnd       timestamp NULL DEFAULT NULL,
    resultPath          varchar(255)       DEFAULT NULL,
    importFilterCount   bigint             DEFAULT NULL,
    importQuestionCount bigint             DEFAULT NULL,
    voxcoQuestionCount  bigint             DEFAULT NULL,
    lastValidated       timestamp NULL DEFAULT NULL,
    PRIMARY KEY (surveyId)
);

CREATE TABLE Note
(
    idNote      bigint    NOT NULL AUTO_INCREMENT,
    deleted     int                DEFAULT NULL,
    lastUpdated timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    text        varchar(2048)      DEFAULT NULL,
    type        varchar(255)       DEFAULT NULL,
    node_idNode bigint             DEFAULT NULL,
    interviewId bigint             DEFAULT NULL,
    PRIMARY KEY (idNote)
);

CREATE TABLE Participant
(
    idParticipant int         NOT NULL AUTO_INCREMENT,
    reference     varchar(20) NOT NULL,
    status        int         NOT NULL,
    lastUpdated   timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted       tinyint(1) NOT NULL DEFAULT '0',
    PRIMARY KEY (idParticipant)
);

CREATE TABLE REPORT_HISTORY
(
    id          int          NOT NULL AUTO_INCREMENT,
    type        varchar(128) NOT NULL,
    name        varchar(128) NOT NULL,
    path        varchar(128)          DEFAULT NULL,
    status      varchar(128) NOT NULL,
    progress    varchar(128) NOT NULL,
    requestor   varchar(128) NOT NULL,
    jsonData    longtext,
    updatedDt   timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updatedBy   varchar(128)          DEFAULT NULL,
    startDt     timestamp NULL DEFAULT NULL,
    endDt       timestamp NULL DEFAULT NULL,
    duration    float                 DEFAULT NULL,
    recordCount int                   DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE Rule
(
    idRule       bigint    NOT NULL AUTO_INCREMENT,
    agentId      bigint    NOT NULL,
    lastUpdated  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    legacyRuleId bigint             DEFAULT NULL,
    level        int       NOT NULL,
    type         varchar(255)       DEFAULT NULL,
    deleted      int       NOT NULL DEFAULT '0',
    PRIMARY KEY (idRule)
);

CREATE TABLE Rule_AdditionalField
(
    idRuleAdditionalField bigint NOT NULL AUTO_INCREMENT,
    value                 varchar(255) DEFAULT NULL,
    idAdditionalField     bigint       DEFAULT NULL,
    idRule                bigint       DEFAULT NULL,
    PRIMARY KEY (idRuleAdditionalField)
);

CREATE TABLE SYS_CONFIG
(
    id        int          NOT NULL AUTO_INCREMENT,
    type      varchar(128) NOT NULL,
    name      varchar(128) NOT NULL,
    value     varchar(128)          DEFAULT NULL,
    updatedDt timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updatedBy varchar(128)          DEFAULT NULL,
    PRIMARY KEY (id)
);

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
    isProcessed     BIT  DEFAULT NULL,
    PRIMARY KEY (responseId)
);

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
    results           LONGTEXT DEFAULT NULL,
    PRIMARY KEY (id)
);


DROP VIEW IF EXISTS ModuleRule;
CREATE VIEW ModuleRule AS
SELECT concat(m.idNode, ':', nr.idRule, ':', r.agentId, ':', n.idNode) as primaryKey,
       m.idNode                                                        as idModule,
       m.name                                                          as moduleName,
       nr.idRule,
       r.level                                                         as ruleLevel,
       r.agentId                                                       as idAgent,
       a.name as agentName,
       n.idNode,
       n.number as nodeNumber
FROM Node n
         INNER JOIN Node m ON n.topNodeId = m.idNode
         INNER JOIN Node_Rule nr ON n.idNode = nr.idNode
         INNER JOIN Rule r ON nr.idRule = r.idRule
         INNER JOIN AgentInfo a ON r.agentId = a.idAgent
WHERE m.deleted=0 and n.deleted=0 and r.deleted=0;

DROP VIEW IF EXISTS InterviewIntroModule_Module;
CREATE VIEW InterviewIntroModule_Module AS
SELECT
    concat(m.idNode,':',iq.id) AS
                      primaryKey,
    m.idNode as idModule,
    m.type as moduleType,
    m.name as introModuleNodeName,
    iq.id as interviewPrimaryKey,
    iq.idInterview as interviewId,
    iq.name as interviewModuleName
from Node m
         INNER JOIN Interview_Question iq
where m.idNode = iq.link
  and (iq.type='Q_linkedmodule' OR iq.type='M_IntroModule')
  AND iq.deleted=0;

DROP VIEW IF EXISTS InterviewModule_Fragment;
CREATE VIEW InterviewModule_Fragment AS
SELECT
    concat(m.idNode,':',iq.id) AS
                      primaryKey,
    m.idNode as idFragment,
    m.name as fragmentNodeName,
    iq.id as interviewPrimaryKey,
    iq.idInterview as interviewId,
    iq.name as interviewFragmentName
from Node m
         INNER JOIN Interview_Question iq
where m.idNode = iq.link
  and iq.type='Q_linkedajsm' AND iq.deleted=0;

DROP VIEW IF EXISTS Module_Fragment;
CREATE VIEW Module_Fragment AS
SELECT concat(m.idNode, ':',n.idNode, ':',n.number, ':',n.link)  as primaryKey,
       m.idNode as moduleId,
       m.name as moduleName,
       n.idNode as idNode,
       n.name as fragmentName,
       n.number as nodeNumber,
       n.link as fragmentId
FROM Node m
         INNER JOIN Node n ON n.topNodeId = m.idNode
WHERE n.type = 'Q_linkedajsm'
  AND n.deleted=0;

DROP VIEW IF EXISTS Module_IntroModule;
CREATE VIEW Module_IntroModule AS
SELECT concat(m.idNode, ':',n.idNode, ':',n.number, ':',n.link)  as primaryKey,
       m.idNode as moduleId,
       m.name as moduleName,
       n.idNode as idNode,
       n.name as moduleLinkName,
       n.number as nodeNumber,
       n.link as moduleLinkId
FROM Node m
         INNER JOIN Node n ON n.topNodeId = m.idNode
WHERE n.type = 'Q_linkedmodule'
  AND n.deleted=0;

DROP VIEW IF EXISTS AssessmentAnswerSummary;
CREATE VIEW AssessmentAnswerSummary AS
SELECT concat(p.idParticipant, ':',p.reference, ':',i.idinterview, ':',ia.answerId)
           as primaryKey,
       p.idParticipant,p.reference,i.idinterview,ia.answerId,ia.name
        ,ia.answerFreetext,ia.type,p.status,i.assessedStatus,im.interviewModuleName
from Interview i,Participant p,
     Interview_Answer ia,InterviewIntroModule_Module im
where i.idinterview = ia.idinterview
  and i.idinterview = im.interviewId
  and i.idParticipant = p.idParticipant
  and p.deleted = 0
  and ia.deleted = 0
  and im.moduleType != 'M_IntroModule';

