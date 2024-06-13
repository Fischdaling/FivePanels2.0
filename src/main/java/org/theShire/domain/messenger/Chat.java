package org.theShire.domain.messenger;

import org.theShire.domain.BaseEntity;
import org.theShire.domain.medicalDoctor.User;
import org.theShire.domain.medicalDoctor.UserProfile;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.theShire.domain.exception.MessengerException.exTypeMes;
import static org.theShire.foundation.DomainAssertion.isNotInCollection;
import static org.theShire.foundation.DomainAssertion.isNotNull;
import static org.theShire.service.UserService.userRepo;

public class Chat extends BaseEntity {
    // The Users in the chat
    private final Set<User> people;
    // All past sent messanges
    private List<Message> chatHistory;

    public Chat(User... chatters) {
        super();
        people = new HashSet<>();
        chatHistory = new ArrayList<>();

        addChatters(chatters);
    }

    public Chat(UUID uuid, Instant createdAt, Instant updatedAt, Set<User> people) {
        super(uuid, createdAt, updatedAt);
        this.people = people;
        this.chatHistory = new ArrayList<>();
    }

    private void addChatters(User... chatters) {
        for (User chatter : chatters) {
            addChatter(chatter);
        }
    }

    public void addChatter(User chatter) {
        people.add(isNotInCollection(chatter, this.people, "chatter", exTypeMes));
        chatter.addChat(this);
    }

    public Set<User> getPeople() {
        return people;
    }

    public List<Message> getChatHistory() {
        return chatHistory;
    }

    public void addChatHistory(Message message) {
        chatHistory.add(isNotNull(message, "message", exTypeMes));
    }

    public void sendMessage(Message message) {
        addChatHistory(message);
        message.setStage(Message.Stage.SENT);
    }

    public void removeChatter(UUID chatter) {
        isNotNull(chatter, "chatter", exTypeMes);
        people.remove(userRepo.findByID(chatter));
    }

    public void removeChatters(UUID... chatters) {
        isNotNull(chatters, "chatter", exTypeMes);
        for (UUID chatter : chatters) {
            people.remove(userRepo.findByID(chatter));
        }
    }

    public void addPerson(User chatter) {
        people.add(isNotNull(chatter, "chatter", exTypeMes));
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Chat:").append(getEntityId()).append(System.lineSeparator());
        sb.append(getPeople().stream().map(User::getProfile).map(UserProfile::getFirstName).collect(Collectors.toSet()));
        sb.append(chatHistory);
        return sb.toString();
    }

    @Override
    public String toCSVString() {
        final StringBuilder sb = new StringBuilder(super.toCSVString());
        sb.append(people.stream().map(User::getEntityId).map(UUID::toString).collect(Collectors.joining(","))).append(";");
        sb.append(chatHistory.stream().map(Message::toCSVString).collect(Collectors.joining("|"))).append(System.lineSeparator());
        return sb.toString();
    }


}
