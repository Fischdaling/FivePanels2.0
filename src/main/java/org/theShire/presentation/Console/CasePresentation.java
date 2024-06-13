package org.theShire.presentation.Console;

import org.theShire.domain.exception.MedicalCaseException;
import org.theShire.domain.media.Content;
import org.theShire.domain.medicalCase.Answer;
import org.theShire.domain.medicalCase.Case;
import org.theShire.domain.medicalCase.CaseVote;
import org.theShire.domain.medicalDoctor.User;
import org.theShire.foundation.Knowledges;
import org.theShire.service.CaseService;
import org.theShire.service.UserService;

import java.util.*;

import static org.theShire.domain.exception.MedicalCaseException.exTypeCase;
import static org.theShire.foundation.DomainAssertion.*;
import static org.theShire.presentation.Console.Main.scanner;
import static org.theShire.presentation.Console.ScannerUtil.enterUUID;
import static org.theShire.presentation.Console.ScannerUtil.scan;
import static org.theShire.service.UserService.userLoggedIn;


public class CasePresentation {

    public static void findAllCase() {
        CaseService.findAllCase().forEach(System.out::println);
    }

    public static void deleteCaseById() {
        UUID tmpCase = enterUUID("Enter Case Id", Case.class);
        CaseService.deleteCaseById(tmpCase);

    }

    public static void likeCase() {
        UUID medCase = enterUUID("Enter Case Id", Case.class);
        CaseService.likeCase(medCase);
    }

    public static void findCaseById() {
        UUID medicCase = enterUUID("Enter Case Id", Case.class);
        System.out.println(CaseService.findCaseById(medicCase));
    }

    public static void vote() {
        List<Answer> answers = new ArrayList<>();
        List<Double> percentages = new ArrayList<>();
        UUID caseId = enterUUID("Enter Case ID to Vote for", Case.class);
        Case medCase = CaseService.findCaseById(caseId);
        for (Answer answer : medCase.getCaseVote().getAnswers()) {
            System.out.println(answer.getName());
        }
        double percentTrack = 0.0;
        while (percentTrack < 100.0) {
            Answer inAnswer = scan("Enter Answer to vote for", Answer::new);
            Optional<Answer> answer = medCase.getCaseVote().getAnswers().stream().filter(answer1 -> answer1.equals(inAnswer)).findFirst();
            isTrue(answer.isPresent(), () -> "Answer not found", exTypeCase);
            answers.add(answer.get());

            double percentage = scan("Enter the percent you want to vote this answer with",Double::parseDouble);
            lesserThan(percentage, 101.0, "percentage", exTypeCase);
            percentages.add(percentage);
            percentTrack += percentage;
            if (percentTrack > 100.0)
                throw new MedicalCaseException("percentage above 100");
        }
        CaseService.vote(caseId, answers, percentages);
    }

    public static void addCase() {
        int ansCount = scan("How many possible Answers does the Case have", Integer::parseInt);
        LinkedHashSet<Answer> answer = new LinkedHashSet<>();
        for (int i = 0; i < ansCount; i++) {
            answer.add(scan("Enter Answer to the Case", Answer::new));
        }

        String title = scan("Enter Case Title", String::new);
        UUID ownerId = userLoggedIn.getEntityId();
        List<Content> caseContents = new ArrayList<>();

        ScannerUtil.contentUtil(caseContents);

        int doctors = scan("How many Doctors do you want to add?", Integer::parseInt);
        User[] members = new User[doctors];
        scanner.nextLine();
        for (int i = 0; i < doctors; i++) {
            UUID uuid = enterUUID("Enter Member id", User.class);
            isNotEqual(uuid, ownerId, "Id", exTypeCase);
            members[i] = UserService.findById(uuid);

        }

        CaseVote caseVote = new CaseVote(answer);
        int knowledges = scan("How many Knowledges do you want to add?", Integer::parseInt);

        Set<Knowledges> knowledgesSet = new HashSet<>();
        Knowledges.getLegalKnowledges().forEach(System.out::println);
        System.out.println();
        for (int i = 0; i < knowledges; i++) {
            knowledgesSet.add(scan("Enter Knowledge",Knowledges::new));
        }
        CaseService.createCase(null,UserService.findById(ownerId), title, knowledgesSet, caseContents, caseVote, members);

    }

    public static void correctAnswer() {
        UUID caseId = enterUUID("Enter Case ID", Case.class);
        if (!CaseService.findCaseById(caseId).isCaseDone()) {
            isTrue(CaseService.findCaseById(caseId).getOwner().equals(userLoggedIn), () -> "You must be the owner of the case", exTypeCase);
            System.out.println(CaseService.findCaseById(caseId).getCaseVote().getAnswers() + "\n");
            System.out.println("Enter Correct Answer");
            Answer answer = scan("Enter Correct Answer", Answer::new);
            CaseService.correctAnswer(caseId, answer);
            System.out.println(answer + " Was declared as the right Answer. Doctors that made this assumption will earn points.");
            System.out.println("Answer Voted the most was " + CaseService.findCaseById(caseId).getCaseVote().getTop3Answer());
        }
    }

    public static User removeMember() {
        UUID medCaseId = enterUUID("Enter Case Id", Case.class);
        UUID memberId = enterUUID("Enter Member Id", User.class);
        User member = UserService.findById(memberId);
        CaseService.removeMember(medCaseId, member);
        System.out.println("Member was successfully removed");
        return member;
    }

    public static User addMember() {
        UUID medCaseId = enterUUID("Enter Case Id", Case.class);
        UUID memberId = enterUUID("Enter Member Id", User.class);
        User member = UserService.findById(memberId);
        CaseService.addMember(medCaseId, member);
        return member;
    }

}
