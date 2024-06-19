package org.theShire.service;

import org.theShire.domain.media.Content;
import org.theShire.domain.media.ContentText;
import org.theShire.domain.media.Media;
import org.theShire.domain.medicalCase.Answer;
import org.theShire.domain.medicalCase.Case;
import org.theShire.domain.medicalCase.CaseVote;
import org.theShire.domain.medicalDoctor.User;
import org.theShire.domain.messenger.Chat;
import org.theShire.domain.messenger.Message;
import org.theShire.domain.richType.*;
import org.theShire.domain.richType.Knowledges;
import org.theShire.presentation.Console.UserPresentation;

import java.util.*;

import static org.theShire.domain.medicalDoctor.UserRelationShip.createMapKey;
import static org.theShire.domain.medicalDoctor.UserRelationShip.relationShip;
import static org.theShire.service.CaseService.caseRepo;
import static org.theShire.service.ChatService.messengerRepo;
import static org.theShire.service.UserService.userLoggedIn;
import static org.theShire.service.UserService.userRepo;

public class UniversalService {

    public static void loadEntry(){
//        loadChat();
//        loadUser();
//        loadCase();
        System.out.println("Buy Premium to unlock this feature $$$");
    }

    public static void loadUser() {
        String userFilePath = "src/main/java/org/theShire/persistence/userRepoCSV.csv";

        userRepo.loadEntryMap(userFilePath);
    }
    public static void loadCase() {
        String caseFilePath = "src/main/java/org/theShire/persistence/caseRepoCSV.csv";

        caseRepo.loadEntryMap(caseFilePath);
    }
    public static void loadChat() {
        String chatFilePath = "src/main/java/org/theShire/persistence/chatRepoCSV.csv";

        messengerRepo.loadEntryMap(chatFilePath);
    }

    public static void saveEntry() {
        try {
            userRepo.saveEntryMap("src/main/java/org/theShire/persistence/userRepoCSV.csv");
            caseRepo.saveEntryMap("src/main/java/org/theShire/persistence/caseRepoCSV.csv");
            messengerRepo.saveEntryMap("src/main/java/org/theShire/persistence/chatRepoCSV.csv");
        } catch (Exception e) {
            throw new RuntimeException("error while saving entries: " + e.getMessage());
        }
    }

    public static void initData() {

        User user1;
        User user2;
        User user3;
        Case case1;

        //CREATE USER1 -----------------------------------------------------------------------

        // Check if User1 already exists
        UUID user1Id = UUID.fromString("bf3f660c-0c7f-48f2-bd5d-553d6eff5a91");
        if (userRepo.findByID(user1Id) == null) {
            Set<Knowledges> knowledges1 = new HashSet<>();
            knowledges1.add(new Knowledges("Test"));
            knowledges1.add(new Knowledges("adult cardiothoracic anesthesiology"));
            List<EducationalTitle> educationalTitles = new ArrayList<>();
            educationalTitles.add(new EducationalTitle("Fassreiter"));
            educationalTitles.add(new EducationalTitle("Meister Dieb"));
            user1 = UserService.createUser(user1Id,
                    new Name("Bilbo"),
                    new Name("Beutlin"),
                    new Email("Bilbo@hobbit.orc"),
                    new Password("VerySafe123"),
                    new Language("Hobbitisch"),
                    new Location("Auenland"),
                    "Bilbo Profile",
                    knowledges1,
                    educationalTitles);
        } else {
            user1 = userRepo.findByID(user1Id);
        }

        //CREATE USER2-----------------------------------------------------------------

        // Check if User2 already exists
        UUID user2Id = UUID.fromString("ba0a64e5-5fc9-4768-96d2-ad21df6e94c2");
        if (userRepo.findByID(user2Id) == null) {
            Set<Knowledges> knowledges2 = new HashSet<>();
            knowledges2.add(new Knowledges("critical care or pain medicine"));
            knowledges2.add(new Knowledges("pediatric anesthesiology"));
            List<EducationalTitle> educationalTitles2 = new ArrayList<>();
            educationalTitles2.add(new EducationalTitle("Arathorns Sohn"));
            educationalTitles2.add(new EducationalTitle("König von Gondor"));
            user2 = UserService.createUser(user2Id,
                    new Name("Aragorn"),
                    new Name("Arathorn"),
                    new Email("Aragorn@gondor.orc"),
                    new Password("EvenSaver1234"),
                    new Language("Gondorisch"),
                    new Location("Gondor"),
                    "Aragorn Profile",
                    knowledges2,
                    educationalTitles2);
        } else {
            user2 = userRepo.findByID(user2Id);
        }

        //CREATE USER3-----------------------------------------------------------------

        // Check if User3 already exists
        UUID user3Id = UUID.fromString("c3fc1109-be28-4bdc-8ca0-841e1fa4aee2");
        if (userRepo.findByID(user3Id) == null) {
            Set<Knowledges> knowledges3 = new HashSet<>();
            knowledges3.add(new Knowledges("pediatric emergency medicine"));
            knowledges3.add(new Knowledges("hand surgery"));
            List<EducationalTitle> educationalTitles3 = new ArrayList<>();
            educationalTitles3.add(new EducationalTitle("The Gray"));
            educationalTitles3.add(new EducationalTitle("The White"));
            educationalTitles3.add(new EducationalTitle("Ainur"));
            user3 = UserService.createUser(user3Id,
                    new Name("Gandalf"),
                    new Name("Wizardo"),
                    new Email("Gandalf@Wizardo.beard"),
                    new Password("ICastFireBall!"),
                    new Language("all"),
                    new Location("world"),
                    "Gandalf Profile",
                    knowledges3,
                    educationalTitles3);
        } else {
            user3 = userRepo.findByID(user3Id);
        }

        // Check if users are friends and create friendship if not
        if (!relationShip.containsKey(createMapKey(user1, user2))) {
            // Send a friend request
            ChatService.sendRequest(user1, user2);
            Chat chat = ChatService.acceptRequest(user1, user2);
            chat.sendMessage(new Message(user1.getEntityId(), new Content(new ContentText("When can we eat something?"))));
            chat.sendMessage(new Message(user2.getEntityId(), new Content(new ContentText("We already had breakfast"))));
            chat.sendMessage(new Message(user1.getEntityId(), new Content(new ContentText("But whats with the second breakfast? :("))));
        }

        // Initialize content
        List<Content> contents = new ArrayList<>();
        // Add texts
        contents.add(new Content(new ContentText("My First Text")));
        contents.add(new Content(new ContentText("My Second Text")));
        // Add Media
        contents.add(new Content(new Media(200, 100, "My First Media", "200x100")));

        // Check if the case already exists
        UUID caseId = UUID.fromString("5a563273-bed3-4e8c-9c68-6a0229c11ce7");
        if (caseRepo.findByID(caseId) == null) {
            Set<Knowledges> knowledges4 = new HashSet<>();
            knowledges4.add(new Knowledges("pediatric emergency medicine"));
            knowledges4.add(new Knowledges("critical care or pain medicine"));
            LinkedHashSet<Answer> answers = new LinkedHashSet<>();
            Answer a1 = new Answer("Cancer");
            Answer a2 = new Answer("Ebola");
            answers.add(a1);
            answers.add(a2);
            case1 = CaseService.createCase(caseId,user1,
                    "my First Case",
                    knowledges4,
                    contents,
                    new CaseVote(answers),
                    user2,
                    user3);

            case1.like(user2.getEntityId());
            case1.getCaseVote().voting(user2.getEntityId(), a1, 70);
            case1.getCaseVote().voting(user2.getEntityId(), a2, 30);
            case1.getCaseVote().voting(user3.getEntityId(), a1, 20);
            case1.getCaseVote().voting(user3.getEntityId(), a2, 80);
        }else {
            caseRepo.findByID(caseId);
        }
    }


    public static void initUser() {
        userLoggedIn = UserPresentation.init();
    }

}
