package org.theShire.presentation.Console;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.theShire.domain.exception.MedicalDoctorException;
import org.theShire.domain.media.Media;
import org.theShire.domain.medicalDoctor.User;
import org.theShire.domain.medicalDoctor.UserProfile;
import org.theShire.domain.richType.*;
import org.theShire.foundation.Knowledges;
import org.theShire.service.UserService;

import java.time.Instant;
import java.util.*;

import static org.theShire.domain.exception.MedicalDoctorException.exTypeUser;
import static org.theShire.foundation.DomainAssertion.isTrue;
import static org.theShire.presentation.Console.Main.scanner;
import static org.theShire.presentation.Console.ScannerUtil.enterUUID;
import static org.theShire.presentation.Console.ScannerUtil.scan;
import static org.theShire.service.UserService.userLoggedIn;


public class UserPresentation {
    public static void findAllUser() {
        UserService.findAllUser().forEach(System.out::println);
    }

    public static void deleteUserById() {
        UUID userId = enterUUID("Enter User Id", User.class);
        UserService.deleteUserById(userId);
    }

    public static void findByName() {
        System.out.println("Enter name");
        String name = scanner.nextLine();
        System.out.println(UserService.findByName(name));
    }

    public static void relationCommands() {
        User sender = userLoggedIn;
        User receiver;

        System.out.println("1. See Incoming");
        System.out.println("2. send request");
        System.out.println("3. accept request");
        System.out.println("4. decline request or remove friend");
        int answer = scanner.nextInt();
        scanner.nextLine();
        switch (answer) {
            case 1 ->
                    UserService.seeIncoming(sender).forEach((aSender) -> System.out.println("Request from: " + aSender.getProfile().getFirstName()));
            case 2 -> {
                receiver = UserService.findById(enterUUID("Enter target's id", User.class));
                UserService.sendRequest(sender, receiver);
                System.out.println("Request sent from " + sender.getProfile().getFirstName() + " to " + receiver.getProfile().getFirstName());
            }
            case 3 -> {
                receiver = UserService.findById(enterUUID("Enter target's id", User.class));
                UserService.acceptRequest(sender, receiver);
                System.out.println("Request from " + sender.getProfile().getFirstName() + " " + sender.getEntityId() + " to " + receiver.getProfile().getFirstName() + " accepted.");
            }
            case 4 -> {
                receiver = UserService.findById(enterUUID("Enter target's id", User.class));
                UserService.cancelFriendship(sender, receiver);
            }
            default -> System.out.println("Invalid option.");
        }
    }


    public static User addUser() {

        Email email = scan("Enter Email", Email::new);
        Password password = scan("Enter Password", Password::new);
        String inConfirmPassword = scan("Enter Password", String::new);
        BCrypt.Result verify = BCrypt.verifyer().verify(inConfirmPassword.toCharArray(), password.value());
        isTrue(verify.verified, () -> "passwords", exTypeUser);
        UserProfile profile = enterUserProfile();
        int i = scan("How many specialties do you want to add?", Integer::parseInt);
        Knowledges.getLegalKnowledges().forEach(System.out::println);
        Set<Knowledges> specialty = new HashSet<>();
        for (int j = 0; j < i; j++) {
            specialty.add(scan("Enter Specialty:", Knowledges::new));
        }
        return UserService.createUser(null, profile.getFirstName(), profile.getFirstName(), email, password, profile.getLanguage(), profile.getLocation(), profile.getProfilePicture().getAltText(), specialty, profile.getEducationalTitles());
    }

    public static void changeProfile() {
        User user = userLoggedIn;
        System.out.println(user.getProfile().toString());
        UserProfile profile = enterUserProfile();
        user.setProfile(profile);
        user.setUpdatedAt(Instant.now());
    }

    private static UserProfile enterUserProfile() {
        Name firstname = scan("Enter Firstname:", Name::new);
        Name lastname = scan("Enter Lastname:", Name::new);
        Language language = scan("Enter Language:", Language::new);
        Location location = scan("Enter Location:", Location::new);
        Media profilePic = scan("Enter Picture path:", Media::new);
        System.out.println("How many educational titles do you want to add?");
        int i = scanner.nextInt();
        scanner.nextLine();
        List<EducationalTitle> title = new ArrayList<>();
        for (int j = 0; j < i; j++) {
            title.add(scan("Enter Title:", EducationalTitle::new));
        }
        return UserService.updateProfile(language, location, profilePic, firstname, lastname, title);
    }


    public static User init() {
        System.out.println("1. Login");
        System.out.println("2. Create new User");
        System.out.println("0. Exit");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1 -> {
                return login();
            }
            case 2 -> {
                return addUser();
            }
            case 0 -> System.exit(0);
            default -> System.out.println("Invalid option.");
        }
        throw new MedicalDoctorException("Unexpected Error");
    }

    public static User login() {
        Email email = scan("Enter Email", Email::new);
        scanner.nextLine();
        System.out.println("Enter Password: ");
        String password = scanner.nextLine();
        return UserService.login(email, password);
    }

}
