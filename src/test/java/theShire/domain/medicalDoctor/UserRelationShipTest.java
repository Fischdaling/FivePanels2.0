package theShire.domain.medicalDoctor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.theShire.domain.medicalDoctor.Relation;
import org.theShire.domain.medicalDoctor.User;
import org.theShire.domain.medicalDoctor.UserRelationShip;
import org.theShire.domain.messenger.Chat;
import org.theShire.domain.richType.*;
import org.theShire.foundation.Knowledges;
import org.theShire.service.ChatService;
import org.theShire.service.UserService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.theShire.domain.medicalDoctor.Relation.RelationType.*;
import static org.theShire.domain.medicalDoctor.UserRelationShip.relationShip;
import static org.theShire.service.ChatService.messengerRepo;
import static org.theShire.service.UserService.userRepo;

public class UserRelationShipTest {
    User user1;
    User user2;

    @BeforeEach
    public void setUp(){
        relationShip= new HashMap<>();
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
    }

    @Test
    public void createKey_ShouldConcatTwoUUIDs_WhenCalled() {
        UUID uuid1 = user1.getEntityId();
        UUID uuid2 = user2.getEntityId();

        assertEquals(uuid1.toString()+uuid2, UserRelationShip.createMapKey(user1, user2));

        assertEquals(uuid2.toString()+uuid1, UserRelationShip.createMapKey(user2, user1));
    }

    @Test
    public void sendRequest_ShouldSendIncomingRequestToUser2andOutGoingRequestToUser1_WhenCalled() {
        ChatService.sendRequest(user1,user2);

        Set<User> request = UserRelationShip.getRequest(user2);

        assertEquals(1, request.size());
        assertTrue(request.contains(user1));
        assertFalse(request.contains(user2));

        Relation relationFromUser1ToUser2 = UserRelationShip.getRelation(user1,user2);
        Relation relationFromUser2ToUser1 = UserRelationShip.getRelation(user2,user1);


        assertEquals(OUTGOING,relationFromUser1ToUser2.getType());
        assertEquals(INCOMING,relationFromUser2ToUser1.getType());
    }

    @Test
    public void acceptRequest_ShouldAcceptIncomingRequestFromUser1ToUser2_WhenCalled() {
        ChatService.sendRequest(user1,user2);
        ChatService.acceptRequest(user1,user2);
        Set<User> request = UserRelationShip.getRequest(user2);

        assertEquals(0, request.size());

        Relation relationFromUser1ToUser2 = UserRelationShip.getRelation(user1,user2);
        Relation relationFromUser2ToUser1 = UserRelationShip.getRelation(user2,user1);

        assertEquals(ESTABLISHED,relationFromUser1ToUser2.getType());
        assertEquals(ESTABLISHED,relationFromUser2ToUser1.getType());
    }

    @Test
    public void acceptRequest_ShouldOpenChat_WhenAccepted() {
        ChatService.sendRequest(user1,user2);
        ChatService.acceptRequest(user1,user2);

        Chat medChat = messengerRepo.findAll().stream().
                filter(chat -> chat.getPeople().contains(user1) && chat.getPeople().contains(user2)).
                findFirst().orElse(null);

        assertNotNull(medChat);
    }

    @Test
    public void getRelationType_ShouldReturnRelationType_WhenCalled() {
        ChatService.sendRequest(user1,user2);

        assertEquals(OUTGOING,UserRelationShip.getRelationType(user1,user2));
        assertEquals(INCOMING,UserRelationShip.getRelationType(user2,user1));
    }

    @Test
    public void getRequests_ShouldReturnAllIncomingRequestsFromUser_WhenCalled(){
        ChatService.sendRequest(user1,user2);

        Set<User> users = new HashSet<>();
        users.add(user1);

        assertEquals(users,UserRelationShip.getRequest(user2));

        ChatService.acceptRequest(user1,user2);
        users.remove(user1);
        assertEquals(users,UserRelationShip.getRequest(user2));
    }

    @Test
    public void getSend_ShouldReturnAllOutGoingRequestsFromUser_WhenCalled(){
        ChatService.sendRequest(user1,user2);

        Set<User> users = new HashSet<>();
        users.add(user2);

        assertEquals(users,UserRelationShip.getSent(user1));

        ChatService.acceptRequest(user1,user2);
        users.remove(user2);
        assertEquals(users,UserRelationShip.getSent(user1));
    }

    @Test
    public void declineRequest_ShouldRemoveRequest_WhenDeclined() {
        ChatService.sendRequest(user1, user2);
        ChatService.declineRequest(user1, user2);

        assertTrue(UserRelationShip.getRequest(user2).isEmpty());
        assertNull(UserRelationShip.getRelation(user1, user2));
        assertNull(UserRelationShip.getRelation(user2, user1));
    }

    @Test
    public void declineRequest_ShouldRemoveRequestAndCloseChat_WhenDeclinedWithEstablishedRelation() {
        ChatService.sendRequest(user1, user2);
        ChatService.acceptRequest(user1, user2);

        ChatService.declineRequest(user1, user2);

        assertTrue(UserRelationShip.getRequest(user2).isEmpty());
        assertNull(UserRelationShip.getRelation(user1, user2));
        assertNull(UserRelationShip.getRelation(user2, user1));
    }


    @Test
    public void messageable_ShouldReturnFalse_WhenNoRelationEstablished() {
        assertFalse(UserRelationShip.messageable(user1, user2));
    }

    @Test
    public void messageable_ShouldReturnTrue_WhenRelationEstablished(){
        ChatService.sendRequest(user1,user2);
        ChatService.acceptRequest(user1,user2);

        assertTrue(UserRelationShip.messageable(user1,user2));
    }
    @Test
    public void messageable_ShouldReturnFalse_WhenRelationNotEstablished() {
        ChatService.sendRequest(user1, user2);
        assertFalse(UserRelationShip.messageable(user1, user2));
    }

    @Test
    public void messageable_ShouldReturnFalse_WhenRelationOutgoing() {
        ChatService.sendRequest(user1, user2);
        assertFalse(UserRelationShip.messageable(user1, user2));
    }

    @Test
    public void messageable_ShouldReturnFalse_WhenRelationIncoming() {
        ChatService.sendRequest(user1, user2);
        assertFalse(UserRelationShip.messageable(user2, user1));
    }

}
