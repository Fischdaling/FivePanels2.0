package org.theShire.domain.medicalCase;


import java.util.*;
import java.util.stream.Collectors;

import static org.theShire.domain.exception.MedicalCaseException.exTypeCase;
import static org.theShire.foundation.DomainAssertion.*;

public class CaseVote {
    //a LinkedHashSet (it's simply a sorted HashSet) of the answers related to a specific case
    private LinkedHashSet<Answer> answers = new LinkedHashSet<>();
    //a HashMap of a Set of Votes (so we can differentiate between the members)
    private HashMap<UUID, Set<Vote>> votes = new HashMap<>();


    public CaseVote(LinkedHashSet<Answer> answers) {
        addAnswers(answers);
    }

    public CaseVote(LinkedHashSet<Answer> answers, HashMap<UUID, Set<Vote>> votes) {
        addAnswers(answers);
        this.votes = votes;
    }


    public void voting(UUID voter, Answer answerChosen, double percent) {
        isNotNull(voter, "voter", exTypeCase);
        isNotNull(answerChosen, "answerChosen", exTypeCase);
        inRange(percent, 0.0, 100.0, "percent", exTypeCase);

        Vote vote = new Vote(answerChosen, percent);
        isInCollection(answerChosen, answers, "answer", exTypeCase);

        Set<Vote> voteSet = votes.computeIfAbsent(voter, k -> new HashSet<>());
        double totalPercent = getSumPercent(voter) + percent;
        isTrue(totalPercent <= 100.0, () -> "votes over 100%", exTypeCase);

        voteSet.add(vote);
    }


    // getter & setter-----------------------------------
    public LinkedHashSet<Answer> getAnswers() {
        return answers;
    }

    public void addAnswers(LinkedHashSet<Answer> answers) {
        for (Answer answer : answers) {
            addAnswer(answer);
        }
    }

    public void addAnswer(Answer answer) {
        this.answers.add(isNotNull(answer, "answers", exTypeCase));
    }

    public HashMap<UUID, Set<Vote>> getVotes() {
        return votes;
    }

    public double getSumPercent(UUID voter) {
        return votes.get(voter).stream().mapToDouble(Vote::getPercent).sum();
    }

    public Map<Answer, Double> getTop3Answer() {
        Map<Answer, Double> answerPercentageMap = new HashMap<>();
        int totalVoters = votes.entrySet().size();

        votes.values().stream()
                .flatMap(Set::stream)
                .forEach(vote -> {
                    Answer answer = vote.getAnswer();
                    double percent = vote.getPercent();
                    answerPercentageMap.merge(answer, percent, Double::sum);
                });

        return answerPercentageMap.entrySet().stream()
                .sorted(Map.Entry.<Answer, Double>comparingByValue().reversed())
                .limit(3)
                .collect(Collectors.toMap(Map.Entry::getKey, map -> map.getValue() / totalVoters));
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("answers: ").append(answers);
        sb.append("votes: ").append(votes);

        return sb.toString();
    }

    public String toCSVString() {
        final StringBuffer sb = new StringBuffer();
        sb.append(answers).append("$");
        sb.append(votes.keySet()).append("=");
        sb.append(votes.values().stream().map(votes1 -> votes1.stream().map(Vote::ToCSVString).collect(Collectors.toSet())).collect(Collectors.toSet()));
        return sb.toString();
    }
}
