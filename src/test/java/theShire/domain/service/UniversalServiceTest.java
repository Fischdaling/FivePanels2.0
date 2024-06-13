package theShire.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import org.theShire.foundation.Knowledges;
import org.theShire.service.CaseService;
import org.theShire.service.ChatService;
import org.theShire.service.UniversalService;
import org.theShire.service.UserService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.theShire.service.CaseService.caseRepo;
import static org.theShire.service.ChatService.messengerRepo;
import static org.theShire.service.UniversalService.initData;
import static org.theShire.service.UserService.userRepo;

public class UniversalServiceTest {
    @BeforeEach
    void setUp(){
        userRepo.deleteAll();
        caseRepo.deleteAll();
        messengerRepo.deleteAll();
        initData();
    }



    @Test
    void saveAndLoadCase_ShouldWriteDownAllCasesInReposInCSVAndThenLoadThemIn_WhenCalled(){


        //init Content
        List<Content> contents = new ArrayList<>();
        //add texts
        contents.add(new Content(new ContentText("My First Text")));
        contents.add(new Content(new ContentText("My Second Text")));
        //add Media
        contents.add(new Content(new Media("MY FILE")));

        //Create a Case with user2&user3 as member and user1 as owner
        Set<Knowledges> knowledges4 = new HashSet<>();
        knowledges4.add(new Knowledges("pediatric emergency medicine"));
        knowledges4.add(new Knowledges("critical care or pain medicine"));
        LinkedHashSet<Answer> answers = new LinkedHashSet<>();
        Answer a1 =new Answer("Cancer");
        Answer a2 = new Answer("Ebola");
        answers.add(a1);
        answers.add(a2);
        Case case1 = CaseService.createCase(null,userRepo.findByID(UUID.fromString("bf3f660c-0c7f-48f2-bd5d-553d6eff5a91")),
                "my First Case",
                knowledges4,
                contents,
                new CaseVote(answers),
                userRepo.findByID(UUID.fromString("ba0a64e5-5fc9-4768-96d2-ad21df6e94c2")),
                userRepo.findByID(UUID.fromString("c3fc1109-be28-4bdc-8ca0-841e1fa4aee2")));
        case1.getCaseVote().voting(UUID.fromString("ba0a64e5-5fc9-4768-96d2-ad21df6e94c2"),a1,70);
        case1.getCaseVote().voting(UUID.fromString("ba0a64e5-5fc9-4768-96d2-ad21df6e94c2"),a2,30);


        case1.like(UUID.fromString("ba0a64e5-5fc9-4768-96d2-ad21df6e94c2"));
        case1.getGroupchat().sendMessage(new Message(UUID.fromString("c3fc1109-be28-4bdc-8ca0-841e1fa4aee2"),new Content(new ContentText("I can't"))));

        UniversalService.saveEntry();
        caseRepo.deleteAll();
        UniversalService.loadCase();

        assertTrue(caseRepo.existsById(case1.getEntityId()));
    }

    @Test
    void saveAndLoadChat_ShouldWriteDownAllChatInReposInCSVAndThenLoadThemIn_WhenCalled(){
        UUID uuid = UUID.randomUUID();
        //CREATE USER4-----------------------------------------------------------------
        Set<Knowledges> knowledges4 = new HashSet<>();
        knowledges4.add(new Knowledges("pediatric emergency medicine"));
        knowledges4.add(new Knowledges("hand surgery"));
        List<EducationalTitle> educationalTitles = new ArrayList<>();
        educationalTitles.add(new EducationalTitle("Ring Smith"));
        educationalTitles.add(new EducationalTitle("The eye"));

        User user4 = UserService.createUser(uuid,
                new Name("Sauron"),
                new Name("Morgoth"),
                new Email("Sauron@Mordor.orc"),
                new Password("WorldDomination!"),
                new Language("Elbish"),
                new Location("Mordor"),
                "Sauron Profile pic",
                knowledges4,
                educationalTitles);

        ChatService.sendRequest(user4, userRepo.findByID(UUID.fromString("c3fc1109-be28-4bdc-8ca0-841e1fa4aee2")));
        Chat chat = ChatService.acceptRequest(user4, userRepo.findByID(UUID.fromString("c3fc1109-be28-4bdc-8ca0-841e1fa4aee2")));

        chat.sendMessage(new Message(UUID.fromString("c3fc1109-be28-4bdc-8ca0-841e1fa4aee2"),new Content(new ContentText("I can't"))));

        UniversalService.saveEntry();
        messengerRepo.deleteAll();
        UniversalService.loadChat();

        assertTrue(messengerRepo.existsById(chat.getEntityId()));
    }
    @Test
    void saveAndLoadUser_ShouldWriteDownAllUsersInReposInCSVAndThenLoadThemIn_WhenCalled(){
        UUID uuid = UUID.randomUUID();
        //CREATE USER4-----------------------------------------------------------------
        Set<Knowledges> knowledges4 = new HashSet<>();
        knowledges4.add(new Knowledges("pediatric emergency medicine"));
        knowledges4.add(new Knowledges("hand surgery"));
        List<EducationalTitle> educationalTitles = new ArrayList<>();
        educationalTitles.add(new EducationalTitle("Ring Smith"));
        educationalTitles.add(new EducationalTitle("The eye"));

        User user = UserService.createUser(uuid,
                new Name("Sauron"),
                new Name("Morgoth"),
                new Email("Sauron@Mordor.orc"),
                new Password("WorldDomination!"),
                new Language("Elbish"),
                new Location("Mordor"),
                "Sauron Profile pic",
                knowledges4,
                educationalTitles);
        ChatService.sendRequest(user, userRepo.findByID(UUID.fromString("c3fc1109-be28-4bdc-8ca0-841e1fa4aee2")));
        Chat chat = ChatService.acceptRequest(user, userRepo.findByID(UUID.fromString("c3fc1109-be28-4bdc-8ca0-841e1fa4aee2")));
        chat.sendMessage(new Message(UUID.fromString("c3fc1109-be28-4bdc-8ca0-841e1fa4aee2"),new Content(new ContentText("I can't"))));

        UniversalService.saveEntry();
        userRepo.deleteAll();
        UniversalService.loadUser();

        assertTrue(userRepo.existsById(uuid));
    }
}
