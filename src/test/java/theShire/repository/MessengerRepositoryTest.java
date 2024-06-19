package theShire.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.theShire.domain.medicalDoctor.User;
import org.theShire.domain.messenger.Chat;
import org.theShire.domain.richType.*;
import org.theShire.domain.richType.Knowledges;
import org.theShire.repository.MessengerRepository;
import org.theShire.service.UserService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessengerRepositoryTest {
    MessengerRepository messengerRepository;
    User user1;
    User user2;
    Chat chat;

    @BeforeEach
    void setUp() {
        //CREATE USER1 -----------------------------------------------------------------------

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
        chat = new Chat(user1, user2);
        messengerRepository = new MessengerRepository();
        messengerRepository.save(chat);

    }

    @Test
    void findByMembers_ShouldReturnChat_WhenFound() {
        Set<User> members = new HashSet<>();
        members.add(user1);
        members.add(user2);

        Chat foundChat = messengerRepository.findByMembers(members);
        assertEquals(chat, foundChat);

        members = new HashSet<>();
        members.add(user2);
        members.add(user1);

        foundChat = messengerRepository.findByMembers(members);
        assertEquals(chat, foundChat);
    }

}
