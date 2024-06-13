package org.theShire.domain.richType;

import org.theShire.domain.exception.MedicalDoctorException;

import java.util.Objects;

import static org.theShire.foundation.DomainAssertion.isValidEmail;

public record Email(String value) {
    public Email(String value) {

        this.value = isValidEmail(value, "Email", MedicalDoctorException.class);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(value, email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
