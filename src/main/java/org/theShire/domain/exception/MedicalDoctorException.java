package org.theShire.domain.exception;

public class MedicalDoctorException extends RuntimeException {
    //saves the class for exceptionhandling
    public static final Class<MedicalDoctorException> exTypeUser = MedicalDoctorException.class;

    public MedicalDoctorException(String message) {
        super(message);
    }
}
