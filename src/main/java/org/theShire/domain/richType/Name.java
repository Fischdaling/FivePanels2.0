package org.theShire.domain.richType;

import org.theShire.domain.exception.MedicalDoctorException;

import java.util.Objects;

import static org.theShire.foundation.DomainAssertion.hasMaxLength;


public record Name(String value) {
    public Name(String value) {
        this.value = hasMaxLength(value, 30, "Name", MedicalDoctorException.class);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name = (Name) o;
        return Objects.equals(value, name.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
