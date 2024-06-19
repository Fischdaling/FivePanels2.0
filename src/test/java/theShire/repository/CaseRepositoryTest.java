package theShire.repository;

import org.junit.jupiter.api.Test;
import org.theShire.domain.media.Content;
import org.theShire.domain.media.ContentText;
import org.theShire.domain.media.Media;
import org.theShire.domain.medicalCase.Answer;
import org.theShire.domain.medicalCase.Case;
import org.theShire.domain.medicalCase.CaseVote;
import org.theShire.domain.medicalDoctor.User;
import org.theShire.domain.richType.*;
import org.theShire.domain.richType.Knowledges;
import org.theShire.repository.CaseRepository;
import org.theShire.service.CaseService;
import org.theShire.service.UserService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CaseRepositoryTest {

    @Test
    void getCaseByOwner() {
        //CREATE USER1 -----------------------------------------------------------------------

        // Check if User1 already exists
        UUID user1Id = UUID.fromString("bf3f660c-0c7f-48f2-bd5d-553d6eff5a91");
            Set<Knowledges> knowledges1 = new HashSet<>();
            knowledges1.add(new Knowledges("Test"));
            knowledges1.add(new Knowledges("adult cardiothoracic anesthesiology"));
            List<EducationalTitle> educationalTitles = new ArrayList<>();
            educationalTitles.add(new EducationalTitle("Fassreiter"));
            educationalTitles.add(new EducationalTitle("Meister Dieb"));
            User user1 = UserService.createUser(user1Id,
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
            User user2 = UserService.createUser(user2Id,
                    new Name("Aragorn"),
                    new Name("Arathorn"),
                    new Email("Aragorn@gondor.orc"),
                    new Password("EvenSaver1234"),
                    new Language("Gondorisch"),
                    new Location("Gondor"),
                    "Aragorn Profile",
                    knowledges2,
                    educationalTitles2);
      //init Content
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
        Answer a1 = new Answer("Answer 1");
        answers.add(a1);
        Answer a2 = new Answer("Answer 2");
        answers.add(a2);
        Case medCase = CaseService.createCase(UUID.fromString("5a563273-bed3-4e8c-9c68-6a0229c11ce7"),user1, "my First Case", knowledges4, contents, new CaseVote(answers), user2);
        Case medCase1 = CaseService.createCase(null,user2, "my Second Case", knowledges4, contents, new CaseVote(answers), user1);

        CaseRepository caseRepository = new CaseRepository();
        caseRepository.save(medCase);
        caseRepository.save(medCase1);

        Set<Case> ownerCases = caseRepository.getCaseByOwner(user1.getEntityId());
        assertEquals(1, ownerCases.size());
        assertEquals(medCase, ownerCases.iterator().next());
    }
}
