package theShire.domain.richTypes;

import org.junit.jupiter.api.Test;
import org.theShire.domain.richType.*;
import org.theShire.foundation.Knowledges;

import static org.junit.jupiter.api.Assertions.*;
import static org.theShire.domain.exception.MedicalDoctorException.exTypeUser;

public class richTypeTests {

    //TitleTests--------------------
    @Test
    void titleConstructor_ShouldInitializeCorrectly_WhenCalledWithString() {
        EducationalTitle title = new EducationalTitle("Doctor of Medicine");
        assertEquals("Doctor of Medicine", title.value());
    }

    @Test
    void constructor_ShouldThrowException_WhenBiggerThenMaxLength() {
        assertThrows(exTypeUser, () -> new EducationalTitle("A".repeat(31)));
    }

    @Test
    void toString_ShouldReturnTitleAsString_WhenToStringCalled() {
        EducationalTitle title = new EducationalTitle("Doctor of Medicine");
        assertEquals("Doctor of Medicine", title.toString());
    }

    //EmailTests
    @Test
    void emailConstructor_ShouldInitializeCorrectly_WhenCalledWithString() {
        Email email = new Email("test@example.com");
        assertEquals("test@example.com", email.value());
    }

    @Test
    void emailConstructor_ShouldThrowException_WhenToLarge() {
        assertThrows(exTypeUser, () -> new Email("a".repeat(51)));
    }

    @Test
    void emailConstructor_ShouldThrowException_WhenNoAinmail() {
        assertThrows(exTypeUser, () -> new Email("invalid_email.orc"));
    }

    @Test
    void emailConstructor_ShouldThrowException_WhenMoreTHan1Ainmail() {
        assertThrows(exTypeUser, () -> new Email("invalid@em@ail.orc"));
    }

    @Test
    void toString_ShouldReturnEmailAsString_WhenToStringCalled() {
        Email email = new Email("test@email.orc");
        assertEquals("test@email.orc", email.toString());
    }

    @Test
    void emailConstructor_ShouldThrowException_WhenNoPointAfterMail() {
        assertThrows(exTypeUser, () -> new Email("invalid@email"));
    }

    @Test
    void emailConstructor_ShouldThrowException_WhenRigthPartEmpty() {
        assertThrows(exTypeUser, () -> new Email("invalid@"));
    }
    @Test
    void emailConstructor_ShouldThrowException_WhenLeftPartEmpty() {
        assertThrows(exTypeUser, () -> new Email("@email.orc"));
    }



    //Language
    @Test
    void languageConstructor_ShouldInitializeCorrectly_WhenCalledWithString() {
        Language language = new Language("English");
        assertEquals("English", language.value());
    }

    @Test
    void languageConstructor_ShouldThrowException_WhenToBig() {
        assertThrows(exTypeUser, () -> new Language("a".repeat(51)));
    }

    @Test
    void toString_ShouldReturnLanguageAsString_WhenToStringCalled() {
        Language language = new Language("English");
        assertEquals("English", language.toString());
    }

    //Location
    @Test
    void locationConstructor_ShouldInitializeCorrectly_WhenCalledWithString() {
        Location location = new Location("New York");
        assertEquals("New York", location.value());
    }

    @Test
    void locationConstructor_ShouldThrowException_WhenToLarge() {
        assertThrows(exTypeUser, () -> new Location("a".repeat(51)));
    }

    @Test
    void toString_ShouldReturnLocationAsString_WhenToStringCalled() {
        Location location = new Location("New York");
        assertEquals("New York", location.toString());
    }

    //Name
    @Test
    void nameConstructor_ShouldInitializeCorrectly_WhenCalledWithString() {
        Name name = new Name("John Doe");
        assertEquals("John Doe", name.value());
    }

    @Test
    void nameConstructor_ShouldThrowException_WhenToLarge() {
        assertThrows(exTypeUser, () -> new Name("a".repeat(31)));
    }

    @Test
    void toString_ShouldReturnNameAsString_WhenToStringCalled() {
        Name name = new Name("John Doe");
        assertEquals("John Doe", name.toString());
    }

    //Password
    @Test
    void passwwordConstructor_ShouldInitializeCorrectly_WhenStringCalled() {
        Password password = new Password("StrongPassword123!");
        assertNotNull(password.value());
    }

    @Test
    void constructor_ShouldThrowException_WhenWeakPassword() {
        assertThrows(exTypeUser, () -> new Password("weak"));
    }

    @Test
    void toString_ShouldReturnHashedPasswordAsString_WhenToStringCalled() {
        Password password = new Password("StrongPassword123!");
        assertNotNull(password.toString());
    }

    //Knowledges
    @Test
    void knowledgesConstructor_ShouldInitializeCorrectly_WhenCalledWithString() {
        Knowledges knowledges = new Knowledges("Test");
        assertEquals("Test", knowledges.getKnowledge());
    }

    @Test
    void setKnowledge_ShouldSetCorrectKnowledge_WhenCorrectlyCalled() {
        Knowledges knowledges = new Knowledges("Test");
        knowledges.setKnowledge("toxicology");
        assertEquals("toxicology", knowledges.getKnowledge());
    }

    @Test
    void setKnowledge_ShouldThrowException_WhenInvalidKnowledge() {
        Knowledges knowledges = new Knowledges("Test");
        assertThrows(exTypeUser, () -> knowledges.setKnowledge("FailPls"));
    }
}
