package org.occideas.exceptions;


public class BookNotExistException extends RuntimeException {

    public BookNotExistException(String message) {
        super(message);
    }
}
