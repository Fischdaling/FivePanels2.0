package theShire.domain.medicalCase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.theShire.domain.medicalCase.Answer;
import org.theShire.domain.medicalCase.CaseVote;
import org.theShire.domain.medicalCase.Vote;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.theShire.domain.exception.MedicalCaseException.exTypeCase;
import static org.theShire.domain.medicalDoctor.UserRelationShip.relationShip;

class CaseVoteTest {
    LinkedHashSet<Answer> answerLul;
    UUID userID;
    Answer answer;
    Answer answer1;
    CaseVote caseVote;
    HashMap<UUID, Set<Vote>> votesSafe;

    @BeforeEach
    public void init() {
        relationShip = new HashMap<>();
        //initialization of the LinkedHashSet filled with Answers
        answerLul = new LinkedHashSet<>();
        userID = UUID.randomUUID();
        answer = new Answer("Krebs");
        answer1 = new Answer("Ebola");
        answerLul.add(answer);
        answerLul.add(answer1);
        caseVote = new CaseVote(answerLul);
        votesSafe = new HashMap<>();
    }


    @Test
    void TestVoting_ShouldReturnTrue_WhenComparedToAnEqualObject() {
        //testing if the UUID stays the same after voting throughout saving the old values of the votes Hashmap
        votesSafe = caseVote.getVotes();
        caseVote.voting(userID, answer1, 10.0);
        assertEquals(votesSafe.values(), caseVote.getVotes().values());

        Vote vote1 = new Vote(answer1, 10.0);


        assertEquals(caseVote.getVotes().get(userID).stream().findFirst().orElse(null).getAnswer(), vote1.getAnswer());
        assertEquals(caseVote.getVotes().get(userID).stream().findFirst().orElse(null).getPercent(), vote1.getPercent());

    }

    @Test
    void TestVoting_ShouldReturnTrue_WhenComparedToASemiEqualObject() {
        caseVote.voting(userID, answer1, 10.0);
        Vote vote2 = new Vote(answer1, 5.0);


        assertEquals(caseVote.getVotes().get(userID).stream().findFirst().orElse(null).getAnswer(), vote2.getAnswer());
        assertNotEquals(caseVote.getVotes().get(userID).stream().findFirst().orElse(null).getPercent(), vote2.getPercent());

    }

    @Test
    void TestVoting_ShouldReturnTrue_WhenComparedToASemiEqualObjectv2() {
        caseVote.voting(userID, answer1, 10.0);


        Vote vote3 = new Vote(answer, 10.0);


        assertNotEquals(caseVote.getVotes().get(userID).stream().findFirst().orElse(null).getAnswer(), vote3.getAnswer());
        assertEquals(caseVote.getVotes().get(userID).stream().findFirst().orElse(null).getPercent(), vote3.getPercent());
    }

    @Test
    void TestVoting_ShouldReturnTrue_WhenComparedToANonEqualObject() {
        caseVote.voting(userID, answer1, 10.0);

        Vote vote4 = new Vote(answer, 5.0);


        assertNotEquals(caseVote.getVotes().get(userID).stream().findFirst().orElse(null).getAnswer(), vote4.getAnswer());
        assertNotEquals(caseVote.getVotes().get(userID).stream().findFirst().orElse(null).getPercent(), vote4.getPercent());

    }

    @Test
    void testVoting_ShouldAddVote_When0Percent() {
        caseVote.voting(userID, answer1, 0.0);

        assertTrue(caseVote.getVotes().containsKey(userID));
    }

    @Test
    void testVoting_ShouldAddVote_When100Percent() {
        caseVote.voting(userID, answer1, 100.0);

        assertTrue(caseVote.getVotes().containsKey(userID));
    }

    @Test
    void testVoting_ShouldNotAddVote_WhenOver100Percent() {
        assertThrows(exTypeCase,()->caseVote.voting(userID, answer1, 101.0));

        assertFalse(caseVote.getVotes().containsKey(userID));
    }

    //TODO MAYBE DEAD
//    @Test
//    void testMultipleVotes_ShouldUpdateVote_WhenUserVotes2TimesTheSameAnswer() {
//        caseVote.voting(userID, answer1, 10.0);
//        caseVote.voting(userID, answer1, 20.0);
//
//        assertEquals(20.0, caseVote.getVotes().get(userID).stream().findFirst().orElse(null).getPercent());
//    }


    @Test
    void testVoting_ShouldNotAddVote_WhenPercentIsNegative() {
        assertThrows(exTypeCase, () -> caseVote.voting(userID, answer, -10.0));

        assertFalse(caseVote.getVotes().containsKey(userID));
    }

    @Test
    void testVoting_ShouldNotAddVote_WhenPercentIsMoreThan100() {

        assertThrows(exTypeCase, () -> caseVote.voting(userID, answer, 101.0));

        assertFalse(caseVote.getVotes().containsKey(userID));
    }

    @Test
    void testMultipleVotes_ShouldAddVotes_WhenSameUserForDifferentAnswers() {
        caseVote.voting(userID, answer, 30.0);

        caseVote.voting(userID, answer1, 40.0);

        assertTrue(caseVote.getVotes().containsKey(userID));
        assertEquals(2, caseVote.getVotes().get(userID).size());
    }

    @Test
    void testVoting_ShouldNotAddVote_WhenVoterNull() {
       assertThrows(exTypeCase,()->caseVote.voting(null, answer1, 10.0));
    }

    @Test
    void testVoting_ShouldNotAddVote_WhenVotingForNonexxistingAnswer() {
        Answer nonexistentAnswer = new Answer("Test Answer");

        assertThrows(exTypeCase,()-> caseVote.voting(userID, nonexistentAnswer, 10.0));

    }

    @Test
    void testGetTop3Answer_ShouldReturnTop3AnswersWithPercentages_WhenVoted() {
        caseVote = new CaseVote(answerLul);
        caseVote.voting(userID, answer, 40.0);
        caseVote.voting(userID, answer1, 60.0);


        Map<Answer, Double> top3Answers = caseVote.getTop3Answer();

        assertEquals(2, top3Answers.size());
        assertEquals(40,top3Answers.get(answer));
        assertEquals(60, top3Answers.get(answer1));
    }

    @Test
    void testGetTop3Answer_ShouldReturnTop3AnswersWithPercentagesDividedBy2_When2Voted() {
        UUID uuid = UUID.randomUUID();
        caseVote = new CaseVote(answerLul);
        caseVote.voting(userID, answer, 40.0);
        caseVote.voting(userID, answer1, 60.0);

        caseVote.voting(uuid, answer, 80.0);
        caseVote.voting(uuid, answer1, 20.0);


        Map<Answer, Double> top3Answers = caseVote.getTop3Answer();

        assertEquals(2, top3Answers.size());
        assertEquals(120/2,top3Answers.get(answer));
        assertEquals(80/2, top3Answers.get(answer1));
    }

    @Test
    void testVoting_ShouldThrowException_WhenNegativePercent() {
        assertThrows(exTypeCase, () -> caseVote.voting(userID, answer1, -10.0));

        assertFalse(caseVote.getVotes().containsKey(userID));
    }

}