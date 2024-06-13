package org.theShire.domain.exception;

public class MediaException extends RuntimeException {
    //saves the class for exceptionhandling
    public static final Class<MediaException> exTypeMedia = MediaException.class;

    public MediaException(String message) {
        super(message);
    }
}
