package org.theShire.domain.richType;

import java.util.Objects;

import static org.theShire.domain.exception.MedicalDoctorException.exTypeUser;
import static org.theShire.foundation.DomainAssertion.hasMaxLength;


public record EducationalTitle(String value) {
    public EducationalTitle(String value) {
        this.value = hasMaxLength(value, 30, "educationalTitle", exTypeUser);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EducationalTitle that = (EducationalTitle) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}