package org.occideas.voxco.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TranslatedTexts {

    @JsonProperty(value = "TITLE")
    private TranslatedTextContent title;

    @JsonProperty(value = "TEXT")
    private TranslatedTextContent text;

    @JsonProperty(value = "SHORT_TEXT")
    private TranslatedTextContent shortText;

    public TranslatedTexts() {
    }

    public TranslatedTexts(TranslatedTextContent text) {
        this.text = text;
    }

    public TranslatedTexts(TranslatedTextContent title, TranslatedTextContent text, TranslatedTextContent shortText) {
        this.title = title;
        this.text = text;
        this.shortText = shortText;
    }

    public TranslatedTextContent getTitle() {
        return title;
    }

    public void setTitle(TranslatedTextContent title) {
        this.title = title;
    }

    public TranslatedTextContent getText() {
        return text;
    }

    public void setText(TranslatedTextContent text) {
        this.text = text;
    }

    public TranslatedTextContent getShortText() {
        return shortText;
    }

    public void setShortText(TranslatedTextContent shortText) {
        this.shortText = shortText;
    }
}
