package org.theShire.domain.media;


import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.StreamResource;
import org.theShire.domain.exception.MediaException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

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
    private byte[] imageData;
    private Image image;

    public Media(int width, int height, String altText, String resolution) {
        setWidth(width);
        setHeight(height);
        setAltText(altText);
        setResolution(resolution);
    }
    public Media(String altText) {
        setWidth(500);
        setHeight(500);
        setAltText(altText);
        setResolution("500x500");
    }


    public Media(InputStream inputStream, String fileName) {
        try {
            BufferedImage image = ImageIO.read(inputStream);
            if (image == null) {
                throw new MediaException("Invalid image file: " + fileName);
            }
            setWidth(image.getWidth());
            setHeight(image.getHeight());
            setAltText(fileName);
            setResolution(width + "x" + height);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image,"png",byteArrayOutputStream);
            this.imageData = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new MediaException("Error reading image file: " + e.getMessage());
        }
    }


    //getter and setter-----------------------
    public Image getImage() {
        return image;
    }

    public byte[] getImageData() {
        return imageData;
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
