package org.theShire.domain.media;

import org.theShire.domain.BaseEntity;

import static org.theShire.domain.exception.MediaException.exTypeMedia;
import static org.theShire.foundation.DomainAssertion.isNotNull;


public class Content extends BaseEntity {
    private ContentText text;
    //wields metadata of the Media class
    private Media media;

    public Content(Media media) {
        setMedia(media);
    }

    public Content(ContentText text) {
        setContentText(text);
    }

    public ContentText getContentText() {
        return text;
    }

    public void setContentText(ContentText text) {
        this.text = isNotNull(text, "text", exTypeMedia);
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = isNotNull(media, "media", exTypeMedia);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        if (text != null) {
            sb.append(text);
        }
        if (media != null) {
            sb.append(media);
        }
        return sb.toString();
    }

}
