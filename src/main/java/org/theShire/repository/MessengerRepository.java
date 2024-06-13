package org.theShire.repository;

import org.theShire.domain.exception.MessengerException;
import org.theShire.domain.media.Content;
import org.theShire.domain.media.ContentText;
import org.theShire.domain.media.Media;
import org.theShire.domain.medicalDoctor.User;
import org.theShire.domain.messenger.Chat;
import org.theShire.domain.messenger.Message;

import java.io.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.theShire.service.UserService.userRepo;


public class MessengerRepository extends AbstractRepository<Chat>{

    public Chat findByMembers(Set<User> members){
       return entryMap.values().stream().filter(chat -> chat.getPeople().equals(members)).findFirst().orElse(null);
    }

    @Override
    public void saveEntryMap(String filepath) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filepath))) {
            entryMap.values().stream()
                    .map(Chat::toCSVString)
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
                    .map(this::parseChat)
                    .forEach(chat -> entryMap.put(chat.getEntityId(), chat));
        } catch (IOException e) {
            throw new MessengerException(e.getMessage());
        }
    }

    private Chat parseChat(String line) {
        System.out.println(line);
        String[] parts = line.split(";");
        if (parts.length != 5) {
            throw new MessengerException("Invalid CSV format ParseChat");
        }

        UUID entityId = UUID.fromString(parts[0]);
        Instant createdAt = Instant.parse(parts[1]);
        Instant updatedAt = Instant.parse(parts[2]);
        Set<User> users = parseUsers(parts[3]);
        List<Message> chatHistory = parseHistory(parts[4]);

        Chat chat = new Chat(entityId, createdAt, updatedAt, users);
        chat.addChatHistory(new Message(UUID.randomUUID(),new Content(new ContentText("Chat Restored"))));
        chatHistory.forEach(chat::addChatHistory);
        return chat;
    }

    private Set<User> parseUsers(String part) {
        return Arrays.stream(part.split(","))
                .filter(str -> !str.isEmpty())
                .map(UUID::fromString)
                .map(userRepo::findByID)
                .collect(Collectors.toSet());
    }

    private List<Message> parseHistory(String part) {
        return Arrays.stream(part.split("\\$"))
                .map(this::parseMessage)
                .collect(Collectors.toList());
    }

    private Message parseMessage(String messageStr) {
        String[] parts = messageStr.split("\\|");

        UUID entityId = UUID.fromString(parts[0]);
        Instant createdAt = Instant.parse(parts[1]);
        Instant updatedAt = Instant.parse(parts[2]);
        UUID senderId = UUID.fromString(parts[3]);
        Message.Stage stage = Message.Stage.valueOf(parts[4]);
        List<Content> contents = Arrays.stream(parts[5].split(","))
                .map(this::parseContent)
                .collect(Collectors.toList());

        return new Message(entityId, createdAt, updatedAt, senderId, contents, stage);
    }

    private Content parseContent(String contentStr) {
        contentStr = contentStr.replaceAll("[\\[\\]]","");
        if (contentStr.startsWith("text:")) {
            String text = contentStr.substring(5);
            return new Content(new ContentText(text));
        } else if (contentStr.startsWith("media:")) {
            String media = contentStr.substring(6);
            return new Content(new Media(media));
        } else {
            throw new MessengerException("Unknown content format: " + contentStr);
        }
    }


}
