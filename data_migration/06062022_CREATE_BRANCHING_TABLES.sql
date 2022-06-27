DROP TABLE IF EXISTS BOOK;

CREATE TABLE BOOK
(
    id          bigint(20) NOT NULL AUTO_INCREMENT,
    name        varchar(255) NOT NULL,
    description varchar(255) NULL,
    lastUpdated timestamp    NOT NULL,
    createdBy   varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS BOOK_MODULE;

CREATE TABLE BOOK_MODULE
(
    bookId   varchar(255) NOT NULL,
    idNode   bigint(20) NOT NULL,
    fileName varchar(255) NOT NULL,
    json     blob         NOT NULL,
    hashCode bigint(20) NOT NULL,
    type     varchar(255) NOT NULL,
    author   varchar(255) NOT NULL,
    PRIMARY KEY (bookId, idNode)
);