package theShire.domain.media;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.theShire.domain.media.Media;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class MediaTest {
    int width;
    int height;
    String altText;
    String resolution;

    @BeforeEach
    void init() {
        width = 200;
        height = 180;
        altText = "Ein schoener Vogel";
        resolution = "1920x1080";
    }

    @Test
    void testSetWidth_ShouldHaveDifferentValue_WhenNewValueIsSet() {
        Media media = new Media(width, height, altText, resolution);
        int testSave = media.getWidth();
        media.setWidth(500);
        assertNotEquals(media.getWidth(), testSave);
    }

    @Test
    void testSetWidth_ShouldChangeWidthToParamValue_WhenCalled() {
        Media media = new Media(width, height, altText, resolution);
        media.setWidth(500);
        assertEquals(media.getWidth(), 500);
    }

    @Test
    void testSetHeight_ShouldHaveDifferentValue_WhenNewValueIsSet() {
        Media media = new Media(width, height, altText, resolution);
        int testSave = media.getHeight();
        media.setHeight(500);
        assertNotEquals(media.getHeight(), testSave);
    }

    @Test
    void testSetHeight_ShouldChangeWidthToParamValue_WhenCalled() {
        Media media = new Media(width, height, altText, resolution);
        media.setHeight(500);
        assertEquals(media.getHeight(), 500);
    }

    @Test
    void testSetAltText_ShouldHoldDifferentString_WhenNewStringIsSet() {
        Media media = new Media(width, height, altText, resolution);
        String oldAltText = media.getAltText();
        media.setAltText("Ein schircher Vogel");
        assertNotEquals(media.getAltText(), oldAltText);
    }

    @Test
    void testSetAltText_ShouldChangeAltTextToParamValue_WhenCalled() {
        Media media = new Media(width, height, altText, resolution);
        media.setAltText("Ein schircher Vogel");
        assertEquals(media.getAltText(), "Ein schircher Vogel");
    }

    @Test
    void testSetResolution_ShouldHoldDifferentString_WhenNewStringIsSet(){
        Media media = new Media(width, height, altText, resolution);
        String oldRes = media.getResolution();
        media.setResolution("0x0");
        assertNotEquals(media.getResolution(), oldRes);
    }
    @Test
    void testSetResolution_ShouldChangeResolutionToParamValue_WhenCalled(){
        Media media = new Media(width, height, altText, resolution);
        media.setResolution("0x0");
        assertEquals(media.getResolution(), "0x0");
    }

}
