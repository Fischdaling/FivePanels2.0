package theShire.domain.medicalDoctor;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.theShire.domain.medicalDoctor.UserRelationShip.relationShip;
import static org.theShire.service.CaseService.caseRepo;
import static org.theShire.service.UserService.userRepo;

public class UserTest {
    User user1;
    User user2;
    Case case1;
    Set<Case> set;

    @BeforeEach
    void setUp(){
        userRepo.deleteAll();
        caseRepo.deleteAll();
        relationShip = new HashMap<>();
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
        List<Content> contents = new ArrayList<>();
        //add texts
        contents.add(new Content(new ContentText("My First Text")));
        contents.add(new Content(new ContentText("My Second Text")));
        //add Media
        contents.add(new Content(new Media(200, 100, "My First Media", "200x100")));

        //Create a Case with user2&user3 as member and user1 as owner
        Set<Knowledges> knowledges4 = new HashSet<>();
        knowledges4.add(new Knowledges("pediatric emergency medicine"));
        knowledges4.add(new Knowledges("critical care or pain medicine"));
        LinkedHashSet<Answer> answers = new LinkedHashSet<>();
        answers.add(new Answer("Answer 1"));
        answers.add(new Answer("Answer 2"));
        case1 = CaseService.createCase(UUID.fromString("5a563273-bed3-4e8c-9c68-6a0229c11ce7"),user1, "my First Case", knowledges4, contents, new CaseVote(answers), user2);

         set = new HashSet<>();
    }

    @Test
    public void removeCase_ShouldDeleteRefrences_WhenCaseDeleted() {
        user1.removeCase(case1);
        user2.removeCase(case1);

        assertEquals(set, user1.getOwnedCases());
        assertEquals(set, user2.isMemberOfCases());
    }

    @Test
    public void addOwnedCase_ShouldAddCase_WhenCalled() {
        user1.addOwnedCase(case1);

        assertTrue(user1.getOwnedCases().contains(case1));
    }

    @Test
    public void removeOwnedCase_ShouldRemoveCase_WhenCalled() {
        user1.addOwnedCase(case1);

        user1.removeCase(case1);

        assertFalse(user1.getOwnedCases().contains(case1));
    }

    @Test
    public void addMemberOfCase_ShouldAddCase_WhenCalled() {
        user1.addMemberOfCase(case1);

        assertTrue(user1.isMemberOfCases().contains(case1));
    }

    @Test
    public void removeMemberOfCase_ShouldRemoveCase_WhenCalled() {
        user1.addMemberOfCase(case1);

        user1.removeCase(case1);

        assertFalse(user1.isMemberOfCases().contains(case1));
    }

}
