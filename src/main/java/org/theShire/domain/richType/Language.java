package org.theShire.domain.richType;

import org.theShire.domain.exception.MedicalDoctorException;

import java.util.Objects;

import static org.theShire.foundation.DomainAssertion.hasMaxLength;

public record Language(String value) {
    public Language(String value) {
        this.value = hasMaxLength(value, 50, "Language", MedicalDoctorException.class);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Language language = (Language) o;
        return Objects.equals(value, language.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
