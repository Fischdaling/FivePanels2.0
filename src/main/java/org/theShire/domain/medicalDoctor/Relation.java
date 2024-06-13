package org.theShire.domain.medicalDoctor;

import org.theShire.domain.BaseEntity;

import static org.theShire.domain.exception.MedicalDoctorException.exTypeUser;
import static org.theShire.foundation.DomainAssertion.isNotNull;

public class Relation extends BaseEntity {
    //following 2 attributes are 2 participants of the relation
    private User user1;
    private User user2;
    // The type of Relation (OUTGOING,INCOMING,ESTABLISHED)
    private RelationType type;

    public Relation(User user1, User user2, RelationType relationType) {
        setUser1(user1);
        setUser2(user2);
        setType(relationType);
    }

    private void setUser1(User user) {
        this.user1 = isNotNull(user, "user", exTypeUser);
    }

    private void setUser2(User user) {
        this.user2 = isNotNull(user, "user", exTypeUser);
    }

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }

    public RelationType getType() {
        return type;
    }

    public void setType(RelationType type) {
//        this.type = isNotNull(type, "Relation type can't be null", exTypeUser);
        //should be nullable because if no relation exists, it is null
        this.type = type;

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("From ").append(user1.getProfile().getFirstName());
        sb.append(" to ").append(user2.getProfile().getFirstName());
        sb.append(" is ").append(type);
        return sb.toString();
    }

    public enum RelationType {
        OUTGOING,
        INCOMING,
        ESTABLISHED,
    }
}
