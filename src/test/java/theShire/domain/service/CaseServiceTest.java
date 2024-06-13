package theShire.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.theShire.domain.media.Content;
import org.theShire.domain.media.ContentText;
import org.theShire.domain.media.Media;
import org.theShire.domain.medicalCase.Answer;
import org.theShire.domain.medicalCase.Case;
import org.theShire.domain.medicalCase.CaseVote;
import org.theShire.domain.medicalCase.Vote;
import org.theShire.domain.medicalDoctor.User;
import org.theShire.domain.richType.*;
import org.theShire.foundation.Knowledges;
import org.theShire.service.CaseService;
import org.theShire.service.UserService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.theShire.domain.exception.MedicalCaseException.exTypeCase;
import static org.theShire.service.CaseService.caseRepo;
import static org.theShire.service.UserService.userLoggedIn;
import static org.theShire.service.UserService.userRepo;

public class CaseServiceTest {
    Case medCase;
    User user1;
    User user2;
    Answer a1;
    Answer a2;

    @BeforeEach
    public void setUp() {
        //CREATE USER1 -----------------------------------------------------------------------
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

// Initialize content
        List<Content> contents = new ArrayList<>();
        // Add texts
        contents.add(new Content(new ContentText("My First Text")));
        contents.add(new Content(new ContentText("My Second Text")));
        // Add Media
        contents.add(new Content(new Media(200, 100, "My First Media", "200x100")));
        //Create a Case with user2&user3 as member and user1 as owner
        Set<Knowledges> knowledges4 = new HashSet<>();
        knowledges4.add(new Knowledges("pediatric emergency medicine"));
        knowledges4.add(new Knowledges("critical care or pain medicine"));
        LinkedHashSet<Answer> answers = new LinkedHashSet<>();
        a1 = new Answer("Answer 1");
        answers.add(a1);
        a2 = new Answer("Answer 2");
        answers.add(a2);
        medCase = CaseService.createCase(UUID.fromString("5a563273-bed3-4e8c-9c68-6a0229c11ce7"),user1, "my First Case", knowledges4, contents, new CaseVote(answers),user2);

        userLoggedIn = user1;


    }


    @Test
    public void testCreateCase_ShouldAddCaseToRepo_WhenCreated() {
        caseRepo.deleteAll();
        // Initialize content
        List<Content> contents = new ArrayList<>();
        // Add texts
        contents.add(new Content(new ContentText("My First Text")));
        contents.add(new Content(new ContentText("My Second Text")));
        // Add Media
        contents.add(new Content(new Media(200, 100, "My First Media", "200x100")));

        // Check if the case already exists
        UUID caseId = UUID.fromString("5a563987-bed3-4e8c-9c68-6a0229c11ce7");
            Set<Knowledges> knowledges4 = new HashSet<>();
            knowledges4.add(new Knowledges("pediatric emergency medicine"));
            knowledges4.add(new Knowledges("critical care or pain medicine"));
            LinkedHashSet<Answer> answers = new LinkedHashSet<>();
            Answer a1 = new Answer("Cancer");
            Answer a2 = new Answer("Ebola");
            answers.add(a1);
            answers.add(a2);
            medCase = CaseService.createCase(caseId, user1,
                    "my First Case",
                    knowledges4,
                    contents,
                    new CaseVote(answers),
                    user2);

            assertEquals(caseRepo.findByID(medCase.getEntityId()), medCase);
    }

    @Test
    public void testDeleteCaseById_ShouldRemoveCaseFromCaseRepo_WhenCalled() {
        CaseService.deleteCaseById(medCase.getEntityId());
        assertNotEquals(caseRepo.findByID(medCase.getEntityId()), medCase);
    }

    @Test
    public void testCorrectAnswer_ShouldSetCorrectAnswer_WhenCalled() {
        userLoggedIn = user1;
        CaseService.correctAnswer(medCase.getEntityId(), a1);
        assertTrue(medCase.isCaseDone());
    }

    @Test
    public void testDeleteCaseById_ShouldRemoveCaseFromCaseRepo_WhenCalledv2() {
        CaseService.deleteCaseById(medCase.getEntityId());
        assertNull(caseRepo.findByID(medCase.getEntityId()));
    }

    @Test
    public void like_ShouldIncreaseLikeCounterByOne_WhenLiked() {
        userLoggedIn = user2;
        int oldLike = medCase.getLikeCount();
        CaseService.likeCase(medCase.getEntityId());

        assertEquals(oldLike + 1, medCase.getLikeCount());
    }

    @Test
    public void like_ShouldAddUserToUserLiked_WhenLiked() {
        userLoggedIn = user2;
        CaseService.likeCase(medCase.getEntityId());

        assertTrue(medCase.getUserLiked().contains(UserService.userLoggedIn.getEntityId()));
    }

    @Test
    public void findCaseById_ShouldIncreaseViewCountByOne_WhenFound() {
        int oldView = medCase.getViewcount();
        CaseService.findCaseById(medCase.getEntityId());

        assertEquals(oldView + 1, medCase.getViewcount());
    }

    @Test
    public void findCaseById_ShouldThrow_WhenNotFound() {
        int oldView = medCase.getViewcount();


        assertThrows(exTypeCase, () -> CaseService.findCaseById(UUID.randomUUID()));
        assertEquals(oldView, medCase.getViewcount());
    }

    @Test
    public void correctAnswer_ShouldIncreaseScoreOfUser_WhenUserVotedAnswer() {
        int oldScore = user2.getScore();
        System.out.println(medCase.getCaseVote().getAnswers());

        medCase.getCaseVote().voting(user2.getEntityId(), a1, 25);
        medCase.getCaseVote().voting(user2.getEntityId(), a1, 25);
        medCase.getCaseVote().voting(user2.getEntityId(), a2, 50);
        medCase.declareCorrectAnswer(a1);
        assertEquals(oldScore + (2 * 50 / 100 + 1), user2.getScore());
    }
@Test
    public void testCorrectAnswer_ShouldChangeScoreOfUser_WhenUserVotedAnswer() {
        int oldScore = user2.getScore();
        System.out.println(medCase.getCaseVote().getAnswers());

        medCase.getCaseVote().voting(user2.getEntityId(), a1, 25);
        medCase.getCaseVote().voting(user2.getEntityId(), a1, 25);
        medCase.getCaseVote().voting(user2.getEntityId(), a2, 50);
        medCase.declareCorrectAnswer(a1);
        assertNotEquals(oldScore, user2.getScore());
    }

    @Test
    public void correctAnswer_ShouldThrow_WhenUserVotedMoreThan100percent() {
        System.out.println(medCase.getCaseVote().getAnswers());

        assertThrows(exTypeCase, () -> medCase.getCaseVote().voting(user1.getEntityId(), a1, 105));
    }

    @Test
    public void removeMember_ShouldRemoveMemberFromCase_WhenOwner() {
        CaseService.removeMember(medCase.getEntityId(), user2);

        assertFalse(medCase.getUserLiked().contains(user2.getEntityId()));
        assertFalse(user2.isMemberOfCases().contains(medCase));
    }

    @Test
    public void removeMember_ShouldThrowException_WhenNotOwner() {
        userLoggedIn = user2;
        assertThrows(exTypeCase, () -> {
            CaseService.removeMember(medCase.getEntityId(), user2);
        });
    }

    @Test
    public void addMember_ShouldAddMemberToCase_WhenOwner() {
        CaseService.removeMember(medCase.getEntityId(), user2);
        CaseService.addMember(medCase.getEntityId(), user2);

        assertTrue(user2.isMemberOfCases().contains(medCase));
        assertTrue(medCase.getMembers().contains(user2));
        assertTrue(medCase.getGroupchat().getPeople().contains(user2));
    }

    @Test
    public void addMember_ShouldThrowException_WhenNotOwner() {
        CaseService.removeMember(medCase.getEntityId(), user2);
        userLoggedIn = user2;
        assertThrows(RuntimeException.class, () -> {
            CaseService.addMember(medCase.getEntityId(), user2);
        });
    }

    @Test
    public void vote_ShouldVoteForAnswers_WhenValid() {
        userLoggedIn = user2;

        List<Answer> answers = Arrays.asList(a1, a2);
        List<Double> percentages = Arrays.asList(50.0, 50.0);
        CaseService.vote(medCase.getEntityId(), answers, percentages);

        Map<UUID, Set<Vote>> votes = medCase.getCaseVote().getVotes();
        assertTrue(votes.containsKey(userLoggedIn.getEntityId()));
        assertEquals(2, votes.get(userLoggedIn.getEntityId()).size());
        assertTrue(votes.get(userLoggedIn.getEntityId()).stream().allMatch(vote ->
                (vote.getAnswer().equals(a1) && vote.getPercent() == 50.0) ||
                        (vote.getAnswer().equals(a2) && vote.getPercent() == 50.0)
        ));
    }

    @Test
    public void vote_ShouldThrowException_WhenCaseNotFound() {
        userLoggedIn = user2;
        UUID uuid = UUID.randomUUID();
        List<Answer> answers = Arrays.asList(a1, a2);
        List<Double> percentages = Arrays.asList(50.0, 50.0);

        assertThrows(exTypeCase, () -> {
            CaseService.vote(uuid, answers, percentages);
        });
    }

    @Test
    public void vote_ShouldThrowException_WhenUserNotMember() {
        userLoggedIn = user2;
        //CREATE USER3-----------------------------------------------------------------

        // Check if User3 already exists
        UUID user3Id = UUID.fromString("c3fc1109-be28-4bdc-8ca0-841e1fa4aee2");
            Set<Knowledges> knowledges3 = new HashSet<>();
            knowledges3.add(new Knowledges("pediatric emergency medicine"));
            knowledges3.add(new Knowledges("hand surgery"));
            List<EducationalTitle> educationalTitles3 = new ArrayList<>();
            educationalTitles3.add(new EducationalTitle("The Gray"));
            educationalTitles3.add(new EducationalTitle("The White"));
            educationalTitles3.add(new EducationalTitle("Ainur"));
            User user3 = UserService.createUser(user3Id,
                    new Name("Gandalf"),
                    new Name("Wizardo"),
                    new Email("Gandalf@Wizardo.beard"),
                    new Password("ICastFireBall!"),
                    new Language("all"),
                    new Location("world"),
                    "Gandalf Profile",
                    knowledges3,
                    educationalTitles3);

        List<Answer> answers = Arrays.asList(a1, a2);
        List<Double> percentages = Arrays.asList(50.0, 50.0);

        assertThrows(exTypeCase, () -> {
            CaseService.vote(user3.getEntityId(), answers, percentages);
        });
    }

    @Test
    public void vote_ShouldThrowException_WhenPercentagesNot100() {
        List<Answer> answers = Arrays.asList(a1, a2);
        List<Double> percentages = Arrays.asList(50.0, 40.0);

        assertThrows(exTypeCase, () -> {
            CaseService.vote(medCase.getEntityId(), answers, percentages);
        });
    }
    @Test
    public void testCreateCase_ShouldThrowException_WhenNoContentProvided() {
        Set<Knowledges> knowledges = new HashSet<>();
        knowledges.add(new Knowledges("pediatric emergency medicine"));
        knowledges.add(new Knowledges("critical care or pain medicine"));
        LinkedHashSet<Answer> answers = new LinkedHashSet<>();
        answers.add(new Answer("Answer 1"));
        answers.add(new Answer("Answer 2"));

        assertThrows(exTypeCase, () -> {
            CaseService.createCase(UUID.fromString("5a563273-bed3-4e8c-9c68-6a0229c11ce7"),user1, "Case Title", knowledges, null, new CaseVote(answers), user2);
        });
    }

    @Test
    public void testCreateCase_ShouldThrowException_WhenNoOwnerProvided() {
        Set<Knowledges> knowledges = new HashSet<>();
        knowledges.add(new Knowledges("pediatric emergency medicine"));
        knowledges.add(new Knowledges("critical care or pain medicine"));
        LinkedHashSet<Answer> answers = new LinkedHashSet<>();
        answers.add(new Answer("Answer 1"));
        answers.add(new Answer("Answer 2"));

        assertThrows(RuntimeException.class, () -> {
            CaseService.createCase(UUID.fromString("5a563273-bed3-4e8c-9c68-6a0229c11ce7"),null, "Case Title", knowledges, Collections.emptyList(), new CaseVote(answers), user2);
        });
    }
    @Test
    public void testVote_ShouldHandleLargeNumberOfAnswersAndPercentages() {
        userLoggedIn = user2;
        List<Answer> answers = new ArrayList<>();
        List<Double> percentages = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            medCase.getCaseVote().addAnswer(new Answer("Answer "+i));
            answers.add(new Answer("Answer " + i));
            percentages.add(100.0/1000);
        }

        CaseService.vote(medCase.getEntityId(), answers, percentages);

    }

    @Test
    public void testVote_ShouldHandleCaseWithNoAnswers() {
        List<Answer> answers = Collections.emptyList();
        List<Double> percentages = Collections.emptyList();

        assertThrows(exTypeCase,()->CaseService.vote(medCase.getEntityId(), answers, percentages)) ;

    }
    @Test
    public void testVote_ShouldThrowException_WhenCaseNotFound() {
        List<Answer> answers = Arrays.asList(a1, a2);
        List<Double> percentages = Arrays.asList(50.0, 50.0);

        assertThrows(RuntimeException.class, () -> {
            CaseService.vote(UUID.randomUUID(), answers, percentages);
        });
    }

    @Test
    public void testVote_ShouldThrowException_WhenUserNotMember() {
        UUID userId = UUID.randomUUID();
        List<Answer> answers = Arrays.asList(a1, a2);
        List<Double> percentages = Arrays.asList(50.0, 50.0);

        assertThrows(RuntimeException.class, () -> {
            CaseService.vote(userId, answers, percentages);
        });
    }

}
