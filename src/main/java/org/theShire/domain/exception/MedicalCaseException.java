package org.theShire.domain.exception;

public class MedicalCaseException extends RuntimeException {
    //saves the class for exceptionhandling
    public static final Class<MedicalCaseException> exTypeCase = MedicalCaseException.class;

    public MedicalCaseException(String message) {
        super(message);
    }
}
