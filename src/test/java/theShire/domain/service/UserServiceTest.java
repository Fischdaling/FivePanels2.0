    package theShire.domain.service;

    import org.junit.jupiter.api.AfterEach;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;
    import org.theShire.domain.exception.MedicalDoctorException;
    import org.theShire.domain.medicalDoctor.User;
    import org.theShire.domain.richType.*;
    import org.theShire.domain.richType.Knowledges;
    import org.theShire.service.UserService;

    import java.util.*;

    import static org.junit.jupiter.api.Assertions.*;
    import static org.theShire.domain.exception.MedicalDoctorException.exTypeUser;
    import static org.theShire.domain.medicalDoctor.UserRelationShip.relationShip;
    import static org.theShire.service.CaseService.caseRepo;
    import static org.theShire.service.UserService.*;

    public class UserServiceTest {
        User user1;
        User user2;

        @BeforeEach
        public void setUp(){
            //CREATE USER1 -----------------------------------------------------------------------
            userRepo.deleteAll();
            caseRepo.deleteAll();
            // Check if User1 already exists
            UUID user1Id = UUID.fromString("bf3f660c-0c7f-48f2-bd5d-553d6eff5a91");
            if (userRepo.findByID(user1Id) == null) {
                Set<Knowledges> knowledges1 = new HashSet<>();
                knowledges1.add(new Knowledges("Test"));
                knowledges1.add(new Knowledges("adult cardiothoracic anesthesiology"));
                List<EducationalTitle> educationalTitles = new ArrayList<>();
                educationalTitles.add(new EducationalTitle("Fassreiter"));
                educationalTitles.add(new EducationalTitle("Meister Dieb"));
                user1 = UserService.createUser(user1Id,
                        new Name("Bilbo"),
                        new Name("Beutlin"),
                        new Email("Bilbo@hobbit.orc"),
                        new Password("VerySafe123"),
                        new Language("Hobbitisch"),
                        new Location("Auenland"),
                        "Bilbo Profile",
                        knowledges1,
                        educationalTitles);
            } else {
                user1 = userRepo.findByID(user1Id);
            }

            //CREATE USER2-----------------------------------------------------------------

            // Check if User2 already exists
            UUID user2Id = UUID.fromString("ba0a64e5-5fc9-4768-96d2-ad21df6e94c2");
            if (userRepo.findByID(user2Id) == null) {
                Set<Knowledges> knowledges2 = new HashSet<>();
                knowledges2.add(new Knowledges("critical care or pain medicine"));
                knowledges2.add(new Knowledges("pediatric anesthesiology"));
                List<EducationalTitle> educationalTitles2 = new ArrayList<>();
                educationalTitles2.add(new EducationalTitle("Arathorns Sohn"));
                educationalTitles2.add(new EducationalTitle("König von Gondor"));
                user2 = UserService.createUser(user2Id,
                        new Name("Aragorn"),
                        new Name("Arathorn"),
                        new Email("Aragorn@gondor.orc"),
                        new Password("EvenSaver1234"),
                        new Language("Gondorisch"),
                        new Location("Gondor"),
                        "Aragorn Profile",
                        knowledges2,
                        educationalTitles2);
            } else {
                user2 = userRepo.findByID(user2Id);
            }

        }

        @AfterEach
        public void tearDown() {
            relationShip.clear();
            user1 = null;
            user2 = null;
            caseRepo.deleteAll();
            userRepo.deleteAll();
        }

        @Test
        public void testCreateUser_ShouldAddUserToRepo_WhenCorrectlyFilled(){
            UUID uuid = UUID.randomUUID();
            Set<Knowledges> knowledges3 = new HashSet<>();
            knowledges3.add(new Knowledges("pediatric emergency medicine"));
            knowledges3.add(new Knowledges("hand surgery"));
            List<EducationalTitle> educationalTitles3 = new ArrayList<>();
            educationalTitles3.add(new EducationalTitle("The Gray"));
            educationalTitles3.add(new EducationalTitle("The White"));
            educationalTitles3.add(new EducationalTitle("Ainur"));
            User user3 = UserService.createUser(uuid, new Name("Gandalf"), new Name("Wizardo"), new Email("Gandalf@Wizardo.beard"), new Password("ICastFireBall!"), new Language("all"), new Location("world"), "Gandalf Profile", knowledges3, educationalTitles3);

            assertEquals(userRepo.findByID(uuid),user3);
        }
        @Test
        public void testCreateUser_ShouldThrowMedicalDoctorException_WhenWrongParamter(){
            UUID uuid = UUID.randomUUID();
            Set<Knowledges> knowledges3 = new HashSet<>();
            knowledges3.add(new Knowledges("pediatric emergency medicine"));
            knowledges3.add(new Knowledges("hand surgery"));
            List<EducationalTitle> educationalTitles3 = new ArrayList<>();
            educationalTitles3.add(new EducationalTitle("The Gray"));
            educationalTitles3.add(new EducationalTitle("The White"));
            educationalTitles3.add(new EducationalTitle("Ainur"));

            assertThrows(MedicalDoctorException.class,()->{
                UserService.createUser(uuid, new Name(null), new Name("Wizardo"), new Email("Gandalf@Wizardo.beard"), new Password("ICastFireBall!"), new Language("all"), new Location("world"), "Gandalf Profile", knowledges3, educationalTitles3);
            });
        }

        @Test
        public void testDeleteUser_ShouldRemoveUserFromRepo_WhenCalled(){
            userLoggedIn = user2;
            UserService.deleteUserById(user1.getEntityId());

            assertFalse(userRepo.existsById(user1.getEntityId()));
        }

        @Test
        public void testDeleteUser_ShouldThrowMedicalDoctorException_WhenWrongParameter(){
            assertThrows(MedicalDoctorException.class, ()->UserService.deleteUserById(UUID.randomUUID()));
        }



        @Test
        public void login_ShouldReturnLoggedInUser_WhenUserExistsAndPasswordEquals(){
            User userReturned = login(user1.getEmail(),"VerySafe123");
            assertEquals(user1, userReturned);
        }

        @Test
        public void login_ShouldThrow_WhenUserExistsAndPasswordNotEquals(){
            assertThrows(exTypeUser,()->login(user1.getEmail(),"VerySafe"));
        }


        @Test
        public void findByName_ShouldReturnUsers_WhenUsersExist() {
            String name = "Bilbo";

            Set<User> result = UserService.findByName(name);
            assertTrue(result.contains(user1));
        }

        @Test
        public void findByName_ShouldThrow_WhenUsersDoesntExist() {
            String name = "Grimar";

            Set<User> result = UserService.findByName(name);
            assertFalse(result.contains(user1));
            assertTrue(result.isEmpty());
        }

        @Test
        public void cancelFriendship_ShouldCancelFriendship_WhenIncoming() {
            sendRequest(user1, user2);
            cancelFriendship(user1, user2);
            Set<User> incoming = seeIncoming(user2);

            assertNull(incoming);
        }

    }
