package org.theShire.domain.medicalDoctor;

import org.theShire.domain.BaseEntity;
import org.theShire.domain.exception.MedicalDoctorException;
import org.theShire.domain.medicalCase.Case;
import org.theShire.domain.messenger.Chat;
import org.theShire.domain.richType.Email;
import org.theShire.domain.richType.Password;
import org.theShire.foundation.Knowledges;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.theShire.domain.exception.MedicalDoctorException.exTypeUser;
import static org.theShire.foundation.DomainAssertion.isNotInCollection;
import static org.theShire.foundation.DomainAssertion.isNotNull;

public class User extends BaseEntity {
    //The Email must contain @ and . cannot be empty and has a max length
    private Email email;
    //The Password must be up to certain standards (not null, not Empty, min length,...)
    private Password password;
    // The UserProfile contains ertain information about the user (Easy to display)
    private UserProfile profile;
    // The Score of the User gets increased when sucessfully publishing cases and/or voting on cases and/or leaving comments
    private int score;
    //The Contact list of a user (What Friendship he has)
    private Set<Relation> contacts;
    //The Chats from a User (single and Group chats)
    private Set<Chat> chats;
    // all the cases owned and lead by the User
    private Set<Case> ownedCases;
    // all the cases the user is a member of
    private Set<Case> memberOfCase;
    private Set<Knowledges> specialization;


    public User(Password password, Email email, UserProfile profile, Set<Knowledges> specialization) {
        super();
        contacts = new HashSet<>();
        chats = new HashSet<>();
        ownedCases = new HashSet<>();
        memberOfCase = new HashSet<>();
        this.password = password;
        this.email = email;
        this.profile = profile;
        this.specialization = specialization;
    }

    //Test
    public User(UUID uuid, Password password, Email email, UserProfile profile, Set<Knowledges> specialization) {
        super(uuid);
        contacts = new HashSet<>();
        chats = new HashSet<>();
        ownedCases = new HashSet<>();
        memberOfCase = new HashSet<>();
        this.password = password;
        this.email = email;
        this.profile = profile;
        this.specialization = specialization;
    }

    public User(UUID uuid, Instant createdAt, Instant updatedAt, Email email, Password password, UserProfile profile, int score, Set<Relation> contacts, Set<Chat> chats, Set<Case> ownedCases, Set<Case> memberOfCase, Set<Knowledges> specialization) {
        super(uuid, createdAt, updatedAt);
        this.email = email;
        this.password = password;
        this.profile = profile;
        this.score = score;
        this.contacts = contacts;
        this.chats = chats;
        if (ownedCases == null) {
            ownedCases = new HashSet<>();
        }
        this.ownedCases = ownedCases;
        if (memberOfCase == null) {
            memberOfCase = new HashSet<>();
        }
        this.memberOfCase = memberOfCase;
        this.specialization = specialization;
    }

    // SETTER & GETTER -----------------------------------------------------------


    public UserProfile getProfile() {
        return profile;
    }


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


    public Set<Case> getOwnedCases() {
        return ownedCases;
    }


    public Set<Case> isMemberOfCases() {
        return memberOfCase;
    }

    public Email getEmail() {
        return email;
    }

    public Set<Chat> getChats() {
        return chats;
    }

    // Methods ------------------------------------------------------------


    public void addChat(Chat chat) {
        this.chats.add(isNotInCollection(chat, chats, "Chat already in Set", exTypeUser));
    }

    public void addOwnedCase(Case medCase) {
        this.ownedCases.add(isNotNull(medCase, "medCase", exTypeUser));
    }

    public void addMemberOfCase(Case medCase) {
        this.memberOfCase.add(isNotInCollection(medCase, memberOfCase, "memberOfCase", exTypeUser));
    }


    public void removeCase(Case medCase) {
        if (memberOfCase.contains(medCase)) {
            this.memberOfCase.remove(medCase);
        } else if (ownedCases.contains(medCase)) {
            this.ownedCases.remove(medCase);
        } else {
            throw new MedicalDoctorException("Unknown medical case");
        }
    }


    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    public Set<Knowledges> getSpecialization() {
        return specialization;
    }

    public Password getPassword() {
        return password;
    }

    public void addContacts(Relation relation) {
        contacts.add(isNotInCollection(relation, contacts, "user", exTypeUser));
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder().append(System.lineSeparator());
        isNotNull(profile, "profile", exTypeUser);
        sb.append("ID: ").append(getEntityId()).append(System.lineSeparator());
        sb.append(profile).append(System.lineSeparator());
        sb.append("email: ").append(email).append(System.lineSeparator());
        sb.append("password: ").append(password).append(System.lineSeparator());
        sb.append("score: ").append(getScore()).append(System.lineSeparator());
        sb.append("chats: ").append(System.lineSeparator()).append(chats).append(System.lineSeparator());
        sb.append("specializations: ").append(specialization).append(System.lineSeparator());
        sb.append("ownedCases: ").append(ownedCases.stream().map(Case::getTitle).findAny().orElse(null)).append(System.lineSeparator());
        sb.append("memberOfCase: ").append(memberOfCase.stream().map(Case::getTitle).findAny().orElse(null)).append(System.lineSeparator());
        return sb.toString();
    }

    @Override
    public String toCSVString() {
        final StringBuilder sb = new StringBuilder(super.toCSVString());
        sb.append(email).append(";");
        sb.append(password).append(";");
        sb.append(score).append(";");
        sb.append(contacts.stream().map(Relation::toString).collect(Collectors.toSet())).append(";");
        sb.append(chats.stream().map(Chat::getEntityId).collect(Collectors.toSet())).append(";");
        sb.append(specialization).append(";");
        sb.append(ownedCases.stream().map(Case::getEntityId).findAny().orElse(null)).append(";");
        sb.append(memberOfCase.stream().map(Case::getEntityId).findAny().orElse(null)).append(";");
        sb.append(profile.toCSVString());
        sb.append(System.lineSeparator());
        return sb.toString();
    }


}
