package org.theShire.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.theShire.domain.media.Media;
import org.theShire.domain.medicalCase.Case;
import org.theShire.domain.medicalDoctor.User;
import org.theShire.domain.medicalDoctor.UserProfile;
import org.theShire.domain.medicalDoctor.UserRelationShip;
import org.theShire.domain.richType.*;
import org.theShire.domain.richType.Knowledges;
import org.theShire.presentation.Console.Main;
import org.theShire.repository.UserRepository;

import java.time.Instant;
import java.util.*;

import static org.theShire.domain.exception.MedicalDoctorException.exTypeUser;
import static org.theShire.foundation.DomainAssertion.isNotNull;
import static org.theShire.foundation.DomainAssertion.isTrue;

public class UserService {
    public static final UserRepository userRepo = new UserRepository();
    public static UserRelationShip relations = new UserRelationShip(); // Don't touch
    public static User userLoggedIn = null;

    public static List<User> findAllUser() {
        return userRepo.findAll();
    }

    public static void deleteUserById(UUID userId) {
        User user = userRepo.findByID(userId);
        isTrue(userRepo.existsById(userId), () -> "User not found", exTypeUser);
        Set<Case> medCase = user.isMemberOfCases();
        medCase.forEach(mCase -> mCase.removeMember(user));
        if (userLoggedIn.getEntityId().equals(userId)) Main.main(new String[]{});
        //throw user out
        user.isMemberOfCases().forEach(aCase -> aCase.setUpdatedAt(Instant.now()));

        userRepo.deleteById(userId);
    }

    public static Set<User> findByName(String name) {
        Set<User> user = userRepo.findByName(new Name(name));
        return isNotNull(user, "user", exTypeUser);
    }

    public static User findById(UUID uuid) {
        User user = userRepo.findByID(uuid);
        return isNotNull(user, "user", exTypeUser);
    }


    public static void cancelFriendship(User sender, User receiver) {
        isTrue(userRepo.getEntryMap().containsKey(sender.getEntityId()), () -> "Sender not found.", exTypeUser);
        isTrue(userRepo.getEntryMap().containsKey(receiver.getEntityId()), () -> "receiver not found.", exTypeUser);

        Set<User> users = new HashSet<>();
        users.add(sender);
        users.add(receiver);
        ChatService.declineRequest(sender, receiver);
//        if (UserRelationShip.getRelation(sender,receiver)!= null) {
//            if (UserRelationShip.getRelation(sender, receiver).getType().equals(ESTABLISHED)) {
//                messengerRepo.deleteById(messengerRepo.findByMembers(users).getEntityId());
//            }
//        }
    }

    public static void acceptRequest(User sender, User receiver) {
        isTrue(UserRelationShip.getRequest(sender).contains(receiver), () -> "Receiver not found.", exTypeUser);
        ChatService.acceptRequest(sender, receiver);
        receiver.setUpdatedAt(Instant.now());
        sender.setUpdatedAt(Instant.now());
    }

    public static void sendRequest(User sender, User receiver) {
        isTrue(userRepo.getEntryMap().containsKey(sender.getEntityId()), () -> "Sender not found.", exTypeUser);
        isTrue(userRepo.getEntryMap().containsKey(receiver.getEntityId()), () -> "Receiver not found.", exTypeUser);
        ChatService.sendRequest(sender, receiver);
    }

    public static Set<User> seeIncoming(User sender) {
        Set<User> incomingRequests = UserRelationShip.getRequest(sender);
        if (incomingRequests.isEmpty()) {
            return null;
        } else
            return incomingRequests;
    }


    public static UserProfile updateProfile(UserProfile profile, Language language, Location location, Media profilePic, Name firstname, Name lastname, List<EducationalTitle> title) {
        profile.setLanguage(language);
        profile.setLocation(location);
        profile.setProfilePicture(profilePic);
        profile.setFirstName(firstname);
        profile.setLastName(lastname);
        profile.setEducationalTitles(title);
        return profile;
    }


    public static User createUser(UUID uuid, Name firstname, Name lastname, Email email, Password
            password, Language language, Location location, String picture, Set<Knowledges> specialization, List<EducationalTitle> educationalTitle) {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }

        List<EducationalTitle> titles = educationalTitle;
        Media media = new Media(500, 400, picture, "500x400");
        Set<Knowledges> knowledges = specialization;
        UserProfile profile = updateProfile(new UserProfile(), language, location, media, firstname, lastname, titles);
        User user = new User(uuid, password, email, profile, knowledges);
        userRepo.save(user);
        return user;
    }


    public static User login(Email email, String password) {
        Optional<User> userOpt = userRepo.findByEmail(email);

        isTrue(userOpt.isPresent(), () -> "User not found.", exTypeUser);
        User user = userOpt.get();
        //The Password entered //The Password from the User
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword().value());
        isTrue(result.verified, () -> "Invalid password.", exTypeUser);
        return user;

    }

}
