package org.theShire.domain.medicalDoctor;

import org.theShire.domain.media.Media;
import org.theShire.domain.richType.EducationalTitle;
import org.theShire.domain.richType.Language;
import org.theShire.domain.richType.Location;
import org.theShire.domain.richType.Name;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.theShire.domain.exception.MedicalDoctorException.exTypeUser;
import static org.theShire.foundation.DomainAssertion.isInCollection;

public class UserProfile {

    private Name firstName;
    private Name lastName;
    private List<EducationalTitle> educationalTitles;
    private Media profilePicture;
    private Location location;
    private Language language;


    public UserProfile(Language language, Location location, Media profilePicture, Name firstName, Name lastName, EducationalTitle... educationalTitle) {
        this.language = language;
        this.location = location;
        this.profilePicture = profilePicture;
        this.firstName = firstName;
        this.lastName = lastName;
        this.educationalTitles = Arrays.stream(educationalTitle).collect(Collectors.toList());
    }

    public UserProfile(Language language, Location location, Media profilePicture, Name firstName, Name lastName, List<EducationalTitle> titles) {
        this.language = language;
        this.location = location;
        this.profilePicture = profilePicture;
        this.firstName = firstName;
        this.lastName = lastName;
        this.educationalTitles = titles;
    }


    public Name getFirstName() {
        return firstName;
    }


    public Name getLastName() {
        return lastName;
    }


    public Media getProfilePicture() {
        return profilePicture;
    }


    public Location getLocation() {
        return location;
    }


    public Language getLanguage() {
        return language;
    }


    public void addEducationalTitle(EducationalTitle educationalTitle) {
        this.educationalTitles.add(isInCollection(educationalTitle, educationalTitles, "educationalTitle", exTypeUser));
    }

    public void addEducationalTitles(EducationalTitle... educationalTitle) {
        for (EducationalTitle title : educationalTitle) {
            addEducationalTitle(title);
        }
    }

    public List<EducationalTitle> getEducationalTitles() {
        return educationalTitles;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(firstName).append(" ").append(lastName).append(System.lineSeparator());
        sb.append("educationalTitles: ").append(educationalTitles).append(System.lineSeparator());
        sb.append("profilePicture: ").append(profilePicture).append(System.lineSeparator());
        sb.append("location: ").append(location).append(System.lineSeparator());
        sb.append("language: ").append(language).append(System.lineSeparator());
        return sb.toString();
    }

    public String toCSVString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(firstName).append(";");
        sb.append(lastName).append(";");
        sb.append(educationalTitles).append(";");
        sb.append(profilePicture).append(";");
        sb.append(location).append(";");
        sb.append(language);
        return sb.toString();
    }
}
