package org.theShire.presentation.Console;

import org.theShire.domain.media.Content;
import org.theShire.domain.messenger.Chat;
import org.theShire.domain.messenger.Message;
import org.theShire.service.ChatService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.theShire.presentation.Console.ScannerUtil.enterUUID;
import static org.theShire.service.UserService.userLoggedIn;

public class ChatPresentation {
    public static void findAllChat() {
        ChatService.findAllChat().forEach(System.out::println);
    }

    public static void openChat() {
        UUID uuid = enterUUID("Enter chat uuid", Chat.class);
        Chat chat = ChatService.findById(uuid);
        System.out.print("chat with ");
        chat.getPeople().forEach(person -> System.out.print(person.getProfile().getFirstName()));
        System.out.print(" opened");
        chat.getChatHistory().stream().filter(message -> !message.getSenderId().equals(userLoggedIn.getEntityId())).forEach(message -> message.setStage(Message.Stage.READ));
        System.out.println(chat.getChatHistory());
        List<Content> contents = new ArrayList<>();

        ScannerUtil.contentUtil(contents);
        ChatService.sendMessage(chat, new Message(userLoggedIn.getEntityId(), contents.toArray(new Content[0])));
    }
}
