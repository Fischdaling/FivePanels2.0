package org.theShire.domain.medicalDoctor;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.theShire.domain.medicalDoctor.Relation.RelationType.ESTABLISHED;
import static org.theShire.domain.medicalDoctor.Relation.RelationType.OUTGOING;

public class UserRelationShip {
    //      KEY             VALUE
    /*      UUID(User)    RELATION
     *       User12       User1,User2,TYPE:OUTGOING
     *       User21       User1,User2,TYPE:INCOMING
     *       User34       User3, User4, TYPE:ESTABLISHED
     *
     */
    public static HashMap<String, Relation> relationShip;

    public UserRelationShip() {
        relationShip = new HashMap<>();
    }

    public static String createMapKey(User user1, User user2) {
        return user1.getEntityId().toString() + user2.getEntityId().toString();
    }

    public static Relation getRelation(User user1, User user2) {
        String key = createMapKey(user1, user2);
        return relationShip.get(key);
    }

    //important to know! The relation type always differs
    //from the direction of the relation. User1 and User2 have a different POVs


    public static Relation.RelationType getRelationType(User user1, User user2) {
        return Optional.of(getRelation(user1, user2)).map(Relation::getType).orElse(null);
        /*
        creates a collection of Relations and returns the type of Relation
        between 2 Users if a Relation exists (otherwise returns null)
         */
    }

    public static boolean messageable(User user1, User user2) {
        Relation relation = getRelation(user1, user2);
        return relation != null && relation.getType() == ESTABLISHED;
        /*
        Basically delivers the relationType of a Relation between 2 users
        if the relation is established.
         With that we can ensure that chatting between those users is possible
        */
    }


    public static Set<User> getRequest(User user1) {
        return relationShip.values().stream()
                .filter(relation -> relation.getUser1().equals(user1) && relation.getType() == Relation.RelationType.INCOMING)
                .map(Relation::getUser2)
                .collect(Collectors.toSet());
    /*
    Filters the relationships in the hashmap to find those that are incoming requests for the user.
    Collects the users who have sent the requests and returns them as a set.
    */
    }

    public static Set<User> getSent(User user1) {
        return relationShip.values().stream()
                .filter(relation -> relation.getUser1().equals(user1) && relation.getType() == OUTGOING)
                .map(Relation::getUser2)
                .collect(Collectors.toSet());

        /*
        Takes the values of each User in the Hashmap (the enums), filters out
        the OUTGOING enums and returns a Map that wields the user2 and the Relation to User2
        */
    }


}


