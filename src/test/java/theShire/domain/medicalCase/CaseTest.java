package theShire.domain.medicalCase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.theShire.domain.media.Content;
import org.theShire.domain.media.ContentText;
import org.theShire.domain.media.Media;
import org.theShire.domain.medicalCase.Answer;
import org.theShire.domain.medicalCase.Case;
import org.theShire.domain.medicalCase.CaseVote;
import org.theShire.domain.medicalDoctor.User;
import org.theShire.domain.richType.*;
import org.theShire.foundation.Knowledges;
import org.theShire.service.CaseService;
import org.theShire.service.UserService;

import java.util.*;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.theShire.domain.exception.MedicalCaseException.exTypeCase;
import static org.theShire.service.CaseService.caseRepo;
import static org.theShire.service.UserService.userRepo;

public class CaseTest {
    Case medCase;
    User user1;
    User user2;
    User user3;
    Answer a1;
    Answer a2;
    List<Content> contents;
    Set<Knowledges> knowledges1;

    @BeforeEach
    public void setUp() {
        userRepo.deleteAll();
        caseRepo.deleteAll();
        // Check if User1 already exists
        UUID user1Id = UUID.fromString("bf3f660c-0c7f-48f2-bd5d-553d6eff5a91");
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



        // Initialize content
         contents = new ArrayList<>();
        // Add texts
        contents.add(new Content(new ContentText("My First Text")));
        contents.add(new Content(new ContentText("My Second Text")));
        // Add Media
        contents.add(new Content(new Media(200, 100, "My First Media", "200x100")));

        // Check if the case already exists
        UUID caseId = UUID.fromString("5a563273-bed3-4e8c-9c68-6a0229c11ce7");
            Set<Knowledges> knowledges4 = new HashSet<>();
            knowledges4.add(new Knowledges("pediatric emergency medicine"));
            knowledges4.add(new Knowledges("critical care or pain medicine"));
            LinkedHashSet<Answer> answers = new LinkedHashSet<>();
            a1 = new Answer("Cancer");
            a2 = new Answer("Ebola");
            answers.add(a1);
            answers.add(a2);
            medCase = CaseService.createCase(caseId, user1,
                    "my First Case",
                    knowledges4,
                    contents,
                    new CaseVote(answers),
                    user2,user3);

    }


    @Test
    public void testAddMember() {
        int size = medCase.getMembers().size();
        Set<Knowledges> knowledges3 = new HashSet<>();
        knowledges3.add(new Knowledges("pediatric emergency medicine"));
        knowledges3.add(new Knowledges("hand surgery"));
        List<EducationalTitle> educationalTitles3 = new ArrayList<>();
        educationalTitles3.add(new EducationalTitle("Title"));
        educationalTitles3.add(new EducationalTitle("Bringer"));
        educationalTitles3.add(new EducationalTitle("killer"));
        User user3 = UserService.createUser(UUID.randomUUID(), new Name("Frodo"), new Name("Beutlin"), new Email("Frodo@hobbit.orc"), new Password("FrodoProved1234"), new Language("Hobbitish"), new Location("Auenland"), "Frodo Profile", knowledges3, educationalTitles3);
        medCase.addMember(user3);
        assertEquals(size+1, medCase.getMembers().size());
    }

    @Test
    public void testRemoveMember() {
        int size = medCase.getMembers().size();
        medCase.removeMember(user2);
        assertEquals(size -1, medCase.getMembers().size());
    }

    @Test
    public void testAddContent() {
        Content newContent = new Content(new ContentText("Another Text"));
        medCase.addContent(newContent);
        assertEquals(4, medCase.getContent().size());
    }


    @Test
    void declareCorrectAnswer_ValidAnswer_CorrectlyUpdatesUserScores() {
        User owner = medCase.getOwner();
        int initialOwnerScore = owner.getScore();
        int initialUser2Score = user2.getScore();
        medCase.getCaseVote().voting(user2.getEntityId(), a1, 50);
        medCase.declareCorrectAnswer(a1);

        assertEquals(initialOwnerScore + 5, owner.getScore());
        assertEquals(initialUser2Score + 2 * 50 / 100 + 1, user2.getScore());
    }

    @Test
    void declareCorrectAnswer_InvalidAnswer_ThrowsException() {
        Answer incorrectAnswer = new Answer("Wrong Answer");

        assertThrows(exTypeCase, () -> medCase.declareCorrectAnswer(incorrectAnswer));
    }

    @Test
    public void testAddNullMember() {
        assertThrows(exTypeCase, () -> medCase.addMember(null));
    }

    @Test
    public void testRemoveNonMember() {
        List<EducationalTitle> educationalTitles3 = new ArrayList<>();
        educationalTitles3.add(new EducationalTitle("Smeargol"));
        User nonMember = UserService.createUser(UUID.randomUUID(), new Name("Gollum"), new Name("Smeagol"), new Email("gollum@middleearth.orc"), new Password("MyPrecious123"), new Language("Gollumish"), new Location("Mount Doom"), "Gollum Profile", new HashSet<>(), educationalTitles3);
        assertThrows(exTypeCase, () -> medCase.removeMember(nonMember));
    }

    @Test
    void testAddContentList_ShouldAddNewValue_WhenCalled() {
        contents.add(new Content(new ContentText("test")));
        medCase.addContentList(contents);
        assertNotEquals(medCase.getContent(), contents);
    }
}
