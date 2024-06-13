package org.theShire.domain.media;


import java.awt.image.BufferedImage;

import static org.theShire.domain.exception.MediaException.exTypeMedia;
import static org.theShire.foundation.DomainAssertion.greaterZero;
import static org.theShire.foundation.DomainAssertion.isNotBlank;

public class Media {
    //declares the width of a picture in pixels
    private int width;
    //declares the height of a picture in pixels
    private int height;
    //an alternative description of a picture in case the picture cannot be displayed
    private String altText;
    //defines teh resolution of a picture
    private String resolution;
    BufferedImage image = null;

    public Media(int width, int height, String altText, String resolution) {
        setWidth(width);
        setHeight(height);
        setAltText(altText);
        setResolution(resolution);
    }

    public Media(String filename) {

        setHeight(500);
        setWidth(500);
        setAltText(filename);
        setResolution("500x500");
//        try {
//            image = ImageIO.read(new File("src/main/resources/"+filename));
//        } catch (IOException e) {
//                throw new MediaException(e.getMessage());
//        }
    }

    //getter and setter-----------------------
    public BufferedImage getImage() {
        return image;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = greaterZero(width, "width", exTypeMedia);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = greaterZero(height, "height", exTypeMedia);
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = isNotBlank(altText, "altText", exTypeMedia);
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) { //TODO calc from height and width
        this.resolution = isNotBlank(resolution, "resolution", exTypeMedia);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("media:");
        sb.append(altText);
        return sb.toString();
    }
}
