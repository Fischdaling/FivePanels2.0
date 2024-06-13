package org.theShire.domain.exception;

public class MessengerException extends RuntimeException {
    //saves the class for exceptionhandling
    public static final Class<MessengerException> exTypeMes = MessengerException.class;

    public MessengerException(String message) {
        super(message);
    }
}
