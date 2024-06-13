package org.theShire.domain.medicalCase;

import org.theShire.domain.BaseEntity;
import org.theShire.domain.media.Content;
import org.theShire.domain.medicalDoctor.User;
import org.theShire.domain.messenger.Chat;
import org.theShire.foundation.Knowledges;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.theShire.domain.exception.MedicalCaseException.exTypeCase;
import static org.theShire.foundation.DomainAssertion.*;
import static org.theShire.service.UserService.userRepo;


public class Case extends BaseEntity {

    //the title provides information about the topic of the case, cannot be left blank and has a max length
    private String title;
    //A list of Content (contains text and metadata)
    private List<Content> content;
    //A list of Knowledges (portraits different medical knowledges in form of hashtags)
    private Set<Knowledges> knowledges;
    //portraits how often a given case was viewed
    private int viewcount;
    //wields the UUID of the user that is the owner of a given case
    private User owner;
    //wields all UUIDs of every member that is part of the case
    private Set<User> members;
    //portraits how many users liked a given case
    private int likeCount;
    //remembers which user has already liked a case by their id (prevents a user to like the same case more often)
    private Set<UUID> userLiked;
    private Chat groupchat;
    //portraits the total votes of all members combined
    private CaseVote caseVote;
    private boolean caseDone;

    public Case(User owner, String title, Set<Knowledges> knowledges, List<Content> content, CaseVote caseVote, User... members) {
        super();
        caseDone = false;
        this.userLiked = new HashSet<>();
        setOwner(owner);
        setTitle(title);
        this.content = new ArrayList<>();
        this.knowledges = knowledges;
        addContentList(content);
        this.members = new HashSet<>();
        addMembers(members);
        Chat caseChat = new Chat(members);
        this.caseVote = caseVote;
        caseChat.addPerson(owner);
    }
    public Case(UUID uuid, User owner, String title, Set<Knowledges> knowledges, List<Content> content, CaseVote caseVote, User... members) {
            super(uuid);
            caseDone = false;
            this.userLiked = new HashSet<>();
            setOwner(owner);
            setTitle(title);
            this.content = new ArrayList<>();
            this.knowledges = knowledges;
            addContentList(content);
            this.members = new HashSet<>();
            addMembers(members);
            Chat caseChat = new Chat(members);
            this.caseVote = caseVote;
            caseChat.addPerson(owner);
        }

    public Case(UUID uuid, Instant createdAt, Instant updatedAt, String title, List<Content> content, int viewcount, Set<Knowledges> knowledges, User owner, Set<User> members, int likeCount, Set<UUID> userLiked, CaseVote caseVote) {
        super(uuid, createdAt, updatedAt);
        this.title = title;
        this.content = content;
        this.viewcount = viewcount;
        this.knowledges = knowledges;
        this.members = members != null ? members : new HashSet<>();
        this.likeCount = likeCount;
        this.userLiked = userLiked != null ? userLiked : new HashSet<>();
        this.caseVote = caseVote;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = hasMaxLength(title, 30, "title", exTypeCase);
    }

    public List<Content> getContent() {
        return content;
    }


    public Set<Knowledges> getKnowledges() {
        return knowledges;
    }


    public int getViewcount() {
        return viewcount;
    }


    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = isNotNull(owner, "owner", exTypeCase);
    }

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }

    public int getLikeCount() {
        return likeCount;
    }


    public Set<UUID> getUserLiked() {
        return userLiked;
    }


    public CaseVote getCaseVote() {
        return caseVote;
    }

    public void setCaseVote(CaseVote caseVote) {
        this.caseVote = isNotNull(caseVote, "caseVote", exTypeCase);
    }

    public void setViewcount(int viewcount) {
        this.viewcount = viewcount;
    }

    public void declareCorrectAnswer(Answer correctAnswer) {
        isInCollection(correctAnswer, caseVote.getAnswers(), "correctAnswer", exTypeCase);
        Map<UUID, Set<Vote>> votes = caseVote.getVotes();
        Set<UUID> userIdsWithCorrectVotes = votes.entrySet().stream()
                .filter(entry -> entry.getValue().stream().anyMatch(vote -> vote.getAnswer().equals(correctAnswer)))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        for (UUID userId : userIdsWithCorrectVotes) {
            User user = userRepo.findByID(userId);
            isNotNull(user, "user", exTypeCase);
            double percentVoted = votes.get(userId).stream()
                    .filter(vote -> vote.getAnswer().equals(correctAnswer))
                    .mapToDouble(Vote::getPercent)
                    .sum();
            int newScore = user.getScore() + (int) (2 * percentVoted / 100 + 1);
            user.setScore(newScore);
        }
        owner.setScore(getOwner().getScore() + 5);
        setUpdatedAt(Instant.now());
        caseDone = true;
    }


    public Chat getGroupchat() {
        return groupchat;
    }

    public void setCaseDone(boolean caseDone) {
        this.caseDone = caseDone;
    }

    public boolean isCaseDone() {
        return caseDone;
    }

    public void setGroupchat(Chat groupchat) {
        this.groupchat = groupchat;
    }

    //------------------


    public void addContent(Content content) {
        this.content.add(isNotNull(content, "content", exTypeCase));
    }

    public void addContentList(List<Content> contentList) {
        this.content.addAll(isNotNull(contentList, "contentList", exTypeCase));
    }

    public void addKnowledge(Knowledges knowledges) {
        this.knowledges.add(isNotNull(knowledges, "knowledges", exTypeCase));
    }

    // service?
    public void like(UUID userLiked) {
        this.userLiked.add(isNotInCollection(userLiked, this.userLiked, "userLiked", exTypeCase));
        likeCount++;
    }

    public void addMember(User member) {
        this.members.add(
                isNotInCollection(member, this.members, "members", exTypeCase)
        );
    }

    public void addMembers(User... members) {
        for (User member : members) {
            addMember(member);
        }
    }

    public void removeMember(User member) {
        this.members.remove(isInCollection(member, this.members, "member", exTypeCase));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Case: ").append(getEntityId()).append(System.lineSeparator());
        sb.append(title).append("-".repeat(60)).append(System.lineSeparator());
        sb.append(content).append(System.lineSeparator());
        sb.append("knowledges: ").append(knowledges).append(System.lineSeparator());
        sb.append("viewcount: ").append(viewcount).append(System.lineSeparator());
        sb.append("owner: ").append(owner.getProfile().getFirstName()).append(System.lineSeparator());
        sb.append("members: ").append(members.stream().map(user -> user.getProfile().getFirstName()).collect(Collectors.toList())).append(System.lineSeparator());
        sb.append("likeCount: ").append(likeCount).append(System.lineSeparator());
        sb.append("userLiked: ").append(userLiked).append(System.lineSeparator());
        sb.append("caseVote: ").append(caseVote).append(System.lineSeparator());
        sb.append("Top 3 Answer: ").append(caseVote.getTop3Answer()).append(System.lineSeparator());
        return sb.toString();
    }

    @Override
    public String toCSVString() {
        final StringBuilder sb = new StringBuilder(super.toCSVString());
        sb.append(getOwner().getEntityId()).append(";");
        sb.append(title).append(";");
        sb.append(content.stream().map(Content::toString).collect(Collectors.joining(","))).append(";");
        sb.append(knowledges.stream().map(Knowledges::toString).collect(Collectors.joining(","))).append(";");
        sb.append(viewcount).append(";");
        sb.append(members.stream().map(User::getEntityId).map(UUID::toString).collect(Collectors.joining(","))).append(";");
        sb.append(likeCount).append(";");
        sb.append(userLiked.stream().map(UUID::toString).collect(Collectors.joining(","))).append(";");
        sb.append(caseVote.toCSVString()).append(System.lineSeparator());
        return sb.toString();
    }


}
