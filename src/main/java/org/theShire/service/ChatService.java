package org.theShire.service;

import org.theShire.domain.media.Content;
import org.theShire.domain.media.ContentText;
import org.theShire.domain.medicalDoctor.Relation;
import org.theShire.domain.medicalDoctor.User;
import org.theShire.domain.medicalDoctor.UserRelationShip;
import org.theShire.domain.messenger.Chat;
import org.theShire.domain.messenger.Message;
import org.theShire.foundation.DomainAssertion;
import org.theShire.repository.MessengerRepository;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.theShire.domain.exception.MedicalDoctorException.exTypeUser;
import static org.theShire.domain.medicalDoctor.Relation.RelationType.*;
import static org.theShire.service.UserService.userRepo;

public class ChatService {
    public static final MessengerRepository messengerRepo = new MessengerRepository();


    public static List<Chat> findAllChat() {
        return messengerRepo.findAll();
    }

    public static Chat findById(UUID id) {
       return messengerRepo.findByID(id);
    }

    public static Chat createChat(User... users) {
        Chat chat = new Chat(users);
        chat.addChatHistory(new Message(UUID.fromString("bf3f660c-0c7f-48f2-bd5d-553d6eff5a91"),new Content(new ContentText("Chat Created"))));
        return messengerRepo.save(chat);
    }

    public static void sendMessage(Chat chat, Message message) {
        chat.sendMessage(message);
        chat.setUpdatedAt(Instant.now());
    }

    public static void sendRequest(User sender, User receiver) {
        DomainAssertion.isNotEqual(sender, receiver, "sender and receiver", exTypeUser);
        DomainAssertion.isInCollection(sender, userRepo.findAll(), "sender", exTypeUser);
        DomainAssertion.isInCollection(receiver, userRepo.findAll(), "receiver", exTypeUser);

        String keyOutgoing = UserRelationShip.createMapKey(sender, receiver);
        String keyIncoming = UserRelationShip.createMapKey(receiver, sender);

        DomainAssertion.isTrue(!UserRelationShip.relationShip.containsKey(keyIncoming) && !UserRelationShip.relationShip.containsKey(keyOutgoing), () -> "Relation already Existing", exTypeUser);

        Relation relationOutgoing = new Relation(sender, receiver, OUTGOING);
        Relation relationIncoming = new Relation(receiver, sender, INCOMING);

        UserRelationShip.relationShip.put(keyOutgoing, relationOutgoing);
        UserRelationShip.relationShip.put(keyIncoming, relationIncoming);
    }

    public static Chat acceptRequest(User sender, User receiver) {
        DomainAssertion.isNotNull(sender, "sender", exTypeUser);
        DomainAssertion.isNotNull(receiver, "receiver", exTypeUser);

        String keyIncoming = UserRelationShip.createMapKey(sender, receiver);
        String keyOutgoing = UserRelationShip.createMapKey(receiver, sender);

        Relation relationIncoming = UserRelationShip.relationShip.get(keyIncoming);
        Relation relationOutgoing = UserRelationShip.relationShip.get(keyOutgoing);

        if (relationIncoming != null && relationOutgoing != null) {
            relationIncoming.setType(ESTABLISHED);
            relationOutgoing.setType(ESTABLISHED);
            UserRelationShip.relationShip.put(keyIncoming, relationIncoming);
            UserRelationShip.relationShip.put(keyOutgoing, relationOutgoing);

            if (UserRelationShip.messageable(sender, receiver)) {
                return createChat(sender, receiver);
            }
            sender.addContacts(UserRelationShip.getRelation(sender, receiver));
            receiver.addContacts(UserRelationShip.getRelation(sender, receiver));
        }
        return null;
    }

    public static void declineRequest(User sender, User receiver) {
        DomainAssertion.isNotNull(sender, "sender", exTypeUser);
        DomainAssertion.isNotNull(receiver, "receiver", exTypeUser);

        String keyIncoming = UserRelationShip.createMapKey(sender, receiver);
        String keyOutgoing = UserRelationShip.createMapKey(receiver, sender);

        Set<User> tmpSet = new HashSet<>();
        tmpSet.add(sender);
        tmpSet.add(receiver);
        if (UserRelationShip.getRelation(sender, receiver) != null) {
            if (UserRelationShip.getRelation(sender, receiver).getType().equals(ESTABLISHED)) {
                messengerRepo.deleteById(messengerRepo.findByMembers(tmpSet).getEntityId());
            }
        }
        UserRelationShip.relationShip.remove(keyIncoming);
        UserRelationShip.relationShip.remove(keyOutgoing);

    }
}
