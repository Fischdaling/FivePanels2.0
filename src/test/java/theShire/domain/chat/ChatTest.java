package theShire.domain.chat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.theShire.domain.media.Content;
import org.theShire.domain.media.ContentText;
import org.theShire.domain.medicalDoctor.User;
import org.theShire.domain.messenger.Chat;
import org.theShire.domain.messenger.Message;
import org.theShire.domain.richType.*;
import org.theShire.domain.richType.Knowledges;
import org.theShire.service.UserService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.theShire.domain.medicalDoctor.UserRelationShip.relationShip;

public class ChatTest {

    private User user1;
    private User user2;
    private Chat chat;

    @BeforeEach
    public void setUp() {
        relationShip = new HashMap<>();
        Set<Knowledges> knowledges1 = new HashSet<>();
        knowledges1.add(new Knowledges("Test"));
        knowledges1.add(new Knowledges("adult cardiothoracic anesthesiology"));
        List<EducationalTitle> educationalTitles = new ArrayList<>();
        educationalTitles.add(new EducationalTitle("Fassreiter"));
        educationalTitles.add(new EducationalTitle("Meister Dieb"));
        user1 = UserService.createUser(UUID.fromString("bf3f660c-0c7f-48f2-bd5d-553d6eff5a91"), new Name("Bilbo"), new Name("Beutlin"), new Email("Bilbo@hobbit.orc"), new Password("VerySafe123"), new Language("Hobbitish"), new Location("Auenland"), "Bilbo Profile", knowledges1, educationalTitles);
        Set<Knowledges> knowledges2 = new HashSet<>();
        knowledges2.add(new Knowledges("critical care or pain medicine"));
        knowledges2.add(new Knowledges("pediatric anesthesiology"));
        List<EducationalTitle> educationalTitles2 = new ArrayList<>();
        educationalTitles2.add(new EducationalTitle("Arathorns Sohn"));
        educationalTitles2.add(new EducationalTitle("König von Gondor"));
        user2 = UserService.createUser(UUID.fromString("ba0a64e5-5fc9-4768-96d2-ad21df6e94c2"),  new Name("Aragorn"), new Name("Arathorn"), new Email("Aragorn@gondor.orc"), new Password("EvenSaver1234"), new Language("Gondorisch"), new Location("Gondor"), "Aragorn Profile", knowledges2, educationalTitles2);
        chat = new Chat(user1, user2);
    }

    @Test
    public void testConstructor_ShouldBuildCorrectly_WhenCalled() {
        assertNotNull(chat);
        assertEquals(2, chat.getPeople().size());
    }

    @Test
    public void testAddChatter_ShouldAddChatter_WhenCalled() {
        UUID uuid = UUID.randomUUID();
        Set<Knowledges> knowledges3 = new HashSet<>();
        knowledges3.add(new Knowledges("pediatric emergency medicine"));
        knowledges3.add(new Knowledges("hand surgery"));
        List<EducationalTitle> educationalTitles = new ArrayList<>();
        educationalTitles.add(new EducationalTitle("The Gray"));
        educationalTitles.add(new EducationalTitle("The White"));
        educationalTitles.add(new EducationalTitle("Ainur"));
        User user3 = UserService.createUser(uuid, new Name("Gandalf"), new Name("Wizardo"), new Email("Gandalf@Wizardo.beard"), new Password("ICastFireBall!"), new Language("all"), new Location("world"), "Gandalf Profile", knowledges3, educationalTitles);
        chat.addChatter(user3);
        assertEquals(3, chat.getPeople().size());
    }

    @Test
    public void testRemoveChatter_ShouldRemoveChatter_WhenCalled() {
        chat.removeChatter(user2.getEntityId());
        assertEquals(1, chat.getPeople().size());
        assertFalse(chat.getPeople().contains(user2));
    }

    @Test
    public void testAddChatHistory_ShouldAddChatHistory_WhenCalled() {
        Message message = new Message(UUID.randomUUID(), new Content(new ContentText("Test message")));
        chat.addChatHistory(message);
        assertTrue(chat.getChatHistory().contains(message));
    }


    @Test
    public void testAddPerson_ShouldAddPerson_WhenCalled() {
        UUID uuid = UUID.randomUUID();
        Set<Knowledges> knowledges3 = new HashSet<>();
        knowledges3.add(new Knowledges("pediatric emergency medicine"));
        knowledges3.add(new Knowledges("hand surgery"));
        List<EducationalTitle> educationalTitles3 = new ArrayList<>();
        educationalTitles3.add(new EducationalTitle("The Gray"));
        educationalTitles3.add(new EducationalTitle("The White"));
        educationalTitles3.add(new EducationalTitle("Ainur"));
        User user3 = UserService.createUser(uuid, new Name("Gandalf"), new Name("Wizardo"), new Email("Gandalf@Wizardo.beard"), new Password("ICastFireBall!"), new Language("all"), new Location("world"), "Gandalf Profile", knowledges3, educationalTitles3);
        chat.addPerson(user3);
        assertTrue(chat.getPeople().contains(user3));
    }
}
