package org.theShire.domain.media;

import static org.theShire.domain.exception.MediaException.exTypeMedia;
import static org.theShire.foundation.DomainAssertion.isNotBlank;

public class ContentText {
    //a String that holds text
    private String text;


    public ContentText(String text) {
        setText(text);
    }

    //setter and getter---------------------
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = isNotBlank(text, "text", exTypeMedia);
    }

    @Override
    public String toString() {
        return "text:" + text;
    }
}
