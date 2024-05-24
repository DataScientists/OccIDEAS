DROP TABLE IF EXISTS Participant_Details;

CREATE TABLE Participant_Details
(
    id   			bigint(20) NOT NULL AUTO_INCREMENT,
    participantId   bigint(20) NOT NULL,
    detailName      varchar(255) NOT NULL,
    detailValue 	varchar(2048) NOT NULL,
    PRIMARY KEY (id)
);