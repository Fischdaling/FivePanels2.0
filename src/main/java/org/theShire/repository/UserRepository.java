package org.theShire.repository;

import org.theShire.domain.exception.MedicalDoctorException;
import org.theShire.domain.media.Media;
import org.theShire.domain.medicalCase.Case;
import org.theShire.domain.medicalDoctor.Relation;
import org.theShire.domain.medicalDoctor.User;
import org.theShire.domain.medicalDoctor.UserProfile;
import org.theShire.domain.messenger.Chat;
import org.theShire.domain.richType.*;
import org.theShire.domain.richType.Knowledges;

import java.io.*;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.theShire.service.CaseService.caseRepo;
import static org.theShire.service.ChatService.messengerRepo;
import static org.theShire.service.UserService.userRepo;

public class UserRepository extends AbstractRepository<User> {

    public Set<User> findByName(Name name) {
        return entryMap.values().stream()
                .filter(user -> user.getProfile().getFirstName().equals(name))
                .collect(Collectors.toSet());
    }

    public Optional<User> findByEmail(Email email) {
        return entryMap.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public void saveEntryMap(String filepath) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filepath))) {
            entryMap.values().stream()
                    .map(User::toCSVString)
                    .forEach(csvFile -> {
                        try {
                            bufferedWriter.write(csvFile);
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(String.format("File %s has a problem", filepath), e);
        }
    }

    @Override
    public void loadEntryMap(String filepath) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath))) {
            bufferedReader.lines()
                    .map(this::parseUser)
                    .forEach(user -> entryMap.put(user.getEntityId(), user));
        } catch (IOException e) {
            throw new MedicalDoctorException(e.getMessage());
        }
    }

    private User parseUser(String line) {
        String[] parts = line.split(";");
        if (parts.length != 17) {
            throw new MedicalDoctorException("Invalid CSV format");
        }

        UUID entityId = UUID.fromString(parts[0]);
        Instant createdAt = Instant.parse(parts[1]);
        Instant updatedAt = Instant.parse(parts[2]);
        Email email = new Email(parts[3]);
        Password password = new Password(parts[4]);
        int score = Integer.parseInt(parts[5]);
        Set<Relation> contacts = parseContacts(parts[6]);
        Set<Chat> chats = parseChats(parts[7]);
        Set<Knowledges> specializations = parseSpecializations(parts[8]);
        Set<Case> ownedCases = parseCases(parts[9]);
        Set<Case> memberOfCase = parseCases(parts[10]);
        Name firstName = new Name(parts[11]);
        Name lastName = new Name(parts[12]);
        List<EducationalTitle> educationalTitles = parseEducationalTitles(parts[13]);
        Media profilePicture = parseMedia(parts[14]);
        Location location = new Location(parts[15]);
        Language language = new Language(parts[16]);

        UserProfile profile = new UserProfile(language, location, profilePicture, firstName, lastName, educationalTitles);
        User user = new User(entityId, createdAt, updatedAt, email, password, profile, score, contacts, chats, ownedCases, memberOfCase, specializations);
        user.setScore(score);
        return user;
    }

    private Set<Relation> parseContacts(String value) {
        return Arrays.stream(value.replaceAll("[\\[\\]]", "").split(","))
                .filter(str -> !str.isEmpty())
                .map(relStr -> {
                    String[] relParts = relStr.split(":");
                    UUID userId1 = UUID.fromString(relParts[0]);
                    UUID userId2 = UUID.fromString(relParts[1]);
                    Relation.RelationType type = Relation.RelationType.valueOf(relParts[2]);

                    User user1 = userRepo.findByID(userId1);
                    User user2 = userRepo.findByID(userId2);

                    if (user1 == null || user2 == null) {
                        throw new MedicalDoctorException("sender and receiver cannot be null");
                    }

                    return new Relation(user1, user2, type);
                })
                .collect(Collectors.toSet());
    }

    private Set<Chat> parseChats(String value) {
        return Arrays.stream(value.replaceAll("[\\[\\]]", "").split(","))
                .filter(str -> !str.isEmpty())
                .map(uuid -> messengerRepo.findByID(UUID.fromString(uuid.trim())))
                .collect(Collectors.toSet());
    }

    private Set<Knowledges> parseSpecializations(String value) {
        return Arrays.stream(value.replaceAll("[\\[\\]]", "").trim().split(", "))
                .filter(str -> !str.trim().isEmpty())
                .map(Knowledges::new)
                .collect(Collectors.toSet());
    }

    private Set<Case> parseCases(String value) {
        System.out.println(value);
        return Arrays.stream(value.replaceAll("[\\[\\]]", "").split(","))
                .filter(str -> !str.isEmpty() && !str.equals("null"))
                .map(uuid -> caseRepo.findByID(UUID.fromString(uuid)))
                .collect(Collectors.toSet());
    }

    private List<EducationalTitle> parseEducationalTitles(String value) {
        return Arrays.stream(value.replaceAll("[\\[\\]]", "").split(","))
                .filter(str -> !str.isEmpty())
                .map(String::trim)
                .map(EducationalTitle::new)
                .collect(Collectors.toList());
    }

    private Media parseMedia(String value) {
        String altText = value.substring(6);
        return new Media(altText);
    }
}
