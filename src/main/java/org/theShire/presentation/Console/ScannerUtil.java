package org.theShire.presentation.Console;

import org.theShire.domain.BaseEntity;
import org.theShire.domain.media.Content;
import org.theShire.domain.media.ContentText;
import org.theShire.domain.media.Media;
import org.theShire.domain.medicalCase.Case;
import org.theShire.domain.medicalDoctor.User;
import org.theShire.domain.messenger.Chat;
import org.theShire.foundation.DomainAssertion;
import org.theShire.service.CaseService;
import org.theShire.service.ChatService;
import org.theShire.service.UserService;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ScannerUtil {

    public static final Scanner scanner = new Scanner(System.in);

    public static <T> T scan(String message, Function<String, T> convert) {
        T value = null;
        boolean valid = false;
        while (!valid) {
            try {
                System.out.println(message);
                String input = scanner.nextLine();
                value = convert.apply(input);
                valid = true;
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                System.err.println("Please enter a valid value.");
            }
        }
        return value;
    }

    public static List<Content> contentUtil(List<Content> content) {

        while (true) {
            System.out.println("Do you want to add Content true/false");
            boolean addContent = Main.scanner.nextBoolean();
            if (!addContent) {
                break;
            }
            System.out.println("1. Media Content");
            System.out.println("2. Text Content");
            int choice = Main.scanner.nextInt();
            switch (choice) {
                case 1 -> {
                    System.out.println("Enter Filepath");
                    Main.scanner.nextLine();
                    content.add(new Content(new Media(Main.scanner.nextLine())));
                }
                case 2 -> {
                    System.out.println("Enter Text");
                    Main.scanner.nextLine();
                    content.add(new Content(new ContentText(Main.scanner.nextLine())));
                }
                default -> System.out.println("Invalid choice");
            }

        }
        return content;
    }

    public static void findAll() {
        System.out.println("Enter Entity");
        System.out.println("1. Doctor");
        System.out.println("2. Case");
        System.out.println("3. Chat");
        int entityId = Main.scanner.nextInt();
        switch (entityId) {
            case 1 -> UserPresentation.findAllUser();
            case 2 -> CasePresentation.findAllCase();
            case 3 -> ChatPresentation.findAllChat();
            default -> System.out.println("invalid command");
        }

    }

    public static <T extends BaseEntity> UUID enterUUID(String enterMessage, Class<T> clazz) {
        DomainAssertion.isNotNull(clazz, "entity", RuntimeException.class);
        StringBuilder str = new StringBuilder();
        printEntities(clazz, str);
        System.out.println(str);

        System.out.println(enterMessage);
        return UUID.fromString(Main.scanner.nextLine());

    }

    private static <T extends BaseEntity> void printEntities(Class<T> clazz, StringBuilder str) {
        if (clazz == User.class) {
            str.append("User").repeat('-', 20).append(System.lineSeparator());
            for (User user : UserService.findAllUser()) {
                str.append(user.getProfile().getFirstName()).append(" ").append(user.getProfile().getLastName()).append(System.lineSeparator());
                str.append(user.getEntityId()).append(System.lineSeparator());
            }
        } else if (clazz == Case.class) {
            str.append("Cases").repeat('-', 20).append(System.lineSeparator());
            for (Case medCase : CaseService.findAllCase()) {
                str.append(medCase.getTitle()).append(System.lineSeparator());
                str.append(medCase.getEntityId()).append(System.lineSeparator());
            }
        } else if (clazz == Chat.class) {
            str.append("Chat").repeat('-', 20).append(System.lineSeparator());
            for (Chat chat : ChatService.findAllChat()) {
                str.append(chat.getPeople().stream().map(aChat -> aChat.getProfile().getFirstName()).collect(Collectors.toSet()));
                str.append(chat.getEntityId()).append(System.lineSeparator());
            }
        }
    }
}

