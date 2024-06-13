package org.theShire.presentation.Console;

import org.theShire.service.UniversalService;

import java.io.IOException;
import java.util.Scanner;

import static org.theShire.service.UniversalService.initData;
import static org.theShire.service.UniversalService.initUser;
import static org.theShire.service.UserService.userLoggedIn;

public class Main {
    public static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Press Enter to start...");
        String[] arguments = scanner.nextLine().split(" ");

        try {
            initData();
            initUser();
        } catch (Throwable e) {
            System.err.println(e.getMessage());
            main(arguments);
        }

        // THE SHOW MUST GO ON
        while (true) {
            try {
                inputHandler();
            } catch (Throwable e) {
                System.err.println(e.getMessage());//swallow catch them all
                main(arguments);
            }
        }
    }

    private static void inputHandler() throws IOException {
        System.out.println("Commands");
        System.out.println("1. add Doctor");
        System.out.println("2. add Case");
        System.out.println("3. Open Chat by id");
        System.out.println("4. View entities");
        System.out.println("5. Find Doctor by name");
        System.out.println("6. Find Case by id");
        System.out.println("7. Delete Doctor by id");
        System.out.println("8. Delete Case by id");
        System.out.println("9. manage Relations");
        System.out.println("10. Vote for Case Answer");
        System.out.println("11. Leave a like for a Case");
        System.out.println("12. Save Data");
        System.out.println("13. Load Data");
        System.out.println("14. Change Profile");
        System.out.println("15. Set correct Answer");
        System.out.println("16. remove member from Case");
        System.out.println("17. add member to Case");
        System.out.println("0. Logout");

        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1 -> UserPresentation.addUser();
            case 2 -> CasePresentation.addCase();
            case 3 -> ChatPresentation.openChat();
            case 4 -> ScannerUtil.findAll();
            case 5 -> UserPresentation.findByName();
            case 6 -> CasePresentation.findCaseById();
            case 7 -> UserPresentation.deleteUserById();
            case 8 -> CasePresentation.deleteCaseById();
            case 9 -> UserPresentation.relationCommands();
            case 10 -> CasePresentation.vote();
            case 11 -> CasePresentation.likeCase();
            case 12 -> UniversalService.saveEntry();
            case 13 -> UniversalService.loadEntry();
            case 14 -> UserPresentation.changeProfile();
            case 15 -> CasePresentation.correctAnswer();
            case 16 -> CasePresentation.removeMember();
            case 17 -> CasePresentation.addMember();
            case 0 -> {
                System.out.println("Goodbye");
                userLoggedIn = UserPresentation.init();
            }
            default -> System.out.println("Invalid choice");
        }
    }
}
