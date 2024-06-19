package org.theShire.repository;

import org.theShire.domain.exception.MedicalCaseException;
import org.theShire.domain.media.Content;
import org.theShire.domain.media.ContentText;
import org.theShire.domain.medicalCase.Answer;
import org.theShire.domain.medicalCase.Case;
import org.theShire.domain.medicalCase.CaseVote;
import org.theShire.domain.medicalCase.Vote;
import org.theShire.domain.medicalDoctor.User;
import org.theShire.domain.richType.Knowledges;

import java.io.*;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.theShire.domain.exception.MedicalCaseException.exTypeCase;
import static org.theShire.foundation.DomainAssertion.isNotNull;
import static org.theShire.service.UserService.userRepo;

public class CaseRepository extends AbstractRepository<Case> {
    public Set<Case> getCaseByOwner(UUID ownerId) {
        return entryMap.values().stream()
                .filter(aCase -> aCase.getOwner().getEntityId().equals(ownerId))
                .collect(Collectors.toSet());
    }

    @Override
    public void saveEntryMap(String filepath) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filepath))) {
            entryMap.values().stream()
                    .map(Case::toCSVString)
                    .forEach(csvFile -> {
                        try {
                            bufferedWriter.write(csvFile);
                        } catch (IOException e) {
                            throw new MedicalCaseException(e.getMessage());
                        }
                    });
        } catch (IOException e) {
            throw new MedicalCaseException(String.format("File %s has a problem", filepath)+ e.getMessage());
        }
    }

    @Override
    public void loadEntryMap(String filepath) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath))) {
            bufferedReader.lines()
                    .map(this::parseCase)
                    .forEach(medCase -> entryMap.put(medCase.getEntityId(), medCase));
        } catch (IOException e) {
            throw new MedicalCaseException(e.getMessage());
        }
    }

    private Case parseCase(String line) {
        String[] parts = line.split(";");
        UUID entityId = UUID.fromString(parts[0]);
        Instant createdAt = Instant.parse(parts[1]);
        Instant updatedAt = Instant.parse(parts[2]);
        User owner = getOwnerID(parts[3]);
        String title = parts[4];
        List<Content> content = parseContent(parts[5]);
        Set<Knowledges> knowledges = parseKnowledges(parts[6]);
        int viewCount = Integer.parseInt(parts[7]);
        Set<User> members = parseMembers(parts[8]);
        int likeCount = Integer.parseInt(parts[9]);
        Set<UUID> usersLiked = parseUsersLiked(parts[10]);
        CaseVote caseVote = parseCaseVote(parts[11]);

        return new Case(entityId, createdAt, updatedAt, title, content, viewCount, knowledges, owner, members, likeCount, usersLiked, caseVote);
    }

    private static User getOwnerID(String part) {
        UUID ownerId = UUID.fromString(part);
        User owner = userRepo.findByID(ownerId);
        return isNotNull(owner,"owner",exTypeCase);
    }

    private List<Content> parseContent(String part) {
        return Arrays.stream(part.split(","))
                .map(text -> new Content(new ContentText(text.trim())))
                .collect(Collectors.toList());
    }

    private Set<Knowledges> parseKnowledges(String part) {
        return Arrays.stream(part.replaceAll("[\\[\\]]", "").split(","))
                .filter(str -> !str.isEmpty())
                .map(Knowledges::new)
                .collect(Collectors.toSet());
    }

    private HashMap<UUID, Set<Vote>> parseVotes(String part) {
        HashMap<UUID, Set<Vote>> votes = new HashMap<>();
        List<UUID> userKey = new ArrayList<>();
        String entry = part.replaceAll("\\{}", "");

            String[] voteParts = entry.split("=");

            String[] mapKeys = voteParts[0].replaceAll("[\\[\\]]","").split(", ");
            //[ba0a64e5-5fc9-4768-96d2-ad21df6e94c2, c3fc1109-be28-4bdc-8ca0-841e1fa4aee2]
            for (String key : mapKeys){
                userKey.add(UUID.fromString(key));
            }
            String[] valueParts =  voteParts[1].split(",");
            //[[Cancer|70.0, Ebola|30.0], [Ebola|80.0, Cancer|20.0]]
        for (String pair : valueParts){
            String[] pairs = pair.split(",");

            for (int i = 0; i < pairs.length;i++){
                String[] votePair = pairs[i].replaceAll("[\\[\\]]","").split("\\|");
                Answer answer = new Answer(votePair[0]);
                double percent = Double.parseDouble(votePair[1]);
                Vote vote = new Vote(answer, percent);
                votes.computeIfAbsent(userKey.get(i),r->new HashSet<>()).add(vote);
//                votes.put(userKey.get(i),new HashSet<>()).add(vote);
            }
        }


        return votes;
    }

    private LinkedHashSet<Answer> parseAnswers(String part) {
        return Arrays.stream(part.replaceAll("[\\[\\]]", "").split(","))
                .filter(str -> !str.isEmpty())
                .map(Answer::new)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Set<UUID> parseUsersLiked(String part) {
        return Arrays.stream(part.replaceAll("[\\[\\]]", "").split(","))
                .filter(str -> !str.isEmpty())
                .map(UUID::fromString)
                .collect(Collectors.toSet());
    }

    private Set<User> parseMembers(String part) {
        return Arrays.stream(part.replaceAll("[\\[\\]]", "").split(","))
                .filter(str -> !str.isEmpty())
                .map(UUID::fromString)
                .map(userRepo::findByID)
                .collect(Collectors.toSet());
    }

    private CaseVote parseCaseVote(String part) {
        String[] parts = part.split("\\$");
        LinkedHashSet<Answer> answers = parseAnswers(parts[0]);
        HashMap<UUID, Set<Vote>> votes = parseVotes(parts[1]);

        return new CaseVote(answers, votes);
    }
}
