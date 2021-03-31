package org.occideas.qsf.payload;

import java.util.List;

public class AvailableLanguage {

    private List<String> languages;

    public AvailableLanguage() {
    }

    public AvailableLanguage(List<String> languages) {
        this.languages = languages;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }
}
