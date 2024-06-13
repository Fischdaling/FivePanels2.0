package org.theShire.domain.messenger;

import org.theShire.domain.BaseEntity;
import org.theShire.domain.exception.MessengerException;
import org.theShire.domain.media.Content;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.theShire.domain.messenger.Message.Stage.SENT;
import static org.theShire.foundation.DomainAssertion.isNotNull;
import static org.theShire.service.UserService.userRepo;

public class Message extends BaseEntity {

    //The ID from the Sender
    private UUID senderId;
    //The stage the Message is in (SENT,ARRIVED,READ)
    private Stage stage;
    //The Content of the message (Text or Media)
    private List<Content> contents;

    public Message(UUID senderId, Content... contents) {
        super();
        this.contents = new ArrayList<>();
        this.senderId = senderId;
        this.stage = SENT;
        addContents(contents);
    }

    public Message(UUID uuid, Instant createdAt, Instant updatedAt, UUID senderId, List<Content> contents, Stage stage) {
        super(uuid, createdAt, updatedAt);
        this.senderId = senderId;
        this.contents = contents;
        this.stage = stage;
    }

    private void addContents(Content... contents) {
        for (Content content : contents) {
            addContent(content);
        }
    }

    public UUID getSenderId() {
        return senderId;
    }

    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }

    public void addContent(Content content) {
        this.contents.add(isNotNull(content, "content", MessengerException.exTypeMes));
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("From: ").append(userRepo.findByID(senderId).getProfile().getFirstName());
        sb.append(System.lineSeparator()).append(contents);
        sb.append(System.lineSeparator()).append(stage);
        return sb.toString();
    }

    public String toCSVString() {
        final StringBuilder sb = new StringBuilder(super.toCSVString().replaceAll(";", "|"));
        sb.append(senderId).append("|");
        sb.append(stage).append("|");
        sb.append(contents);
        return sb.toString();
    }


    public enum Stage {
        SENT,
        ARRIVED,
        READ
    }
}
