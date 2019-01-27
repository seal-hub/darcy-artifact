package no.dervis.numbertotext.language.impl;

import no.dervis.numbertotext.api.language.English;
import no.dervis.numbertotext.api.language.Language;
import no.dervis.numbertotext.api.language.Norwegian;
import no.dervis.numbertotext.api.spi.NumberResourcesProvider;

import java.util.*;

public class NumberResourcesProviderImpl implements NumberResourcesProvider {
    @Override
    public Language getLanguage(String locale) {
        ResourceBundle numberBundle = getBundle("NumberBundle", Locale.forLanguageTag(locale));
        return toLanguage(numberBundle);
    }

    @Override
    public Language getLanguage() {
        ResourceBundle numberBundle = getBundle("NumberBundle", Locale.getDefault());
        return toLanguage(numberBundle);
    }

    public Map<Integer, String> toMap(ResourceBundle numberBundle) {
        Map<Integer, String> map = new HashMap<>();
        numberBundle.keySet()
                .forEach(s -> map.put(Integer.parseInt(s), numberBundle.getString(s)));
        return map;
    }

    private Language toLanguage(ResourceBundle numberBundle) {
        switch (numberBundle.getLocale().getLanguage()) {
            case "no":
            case "nb":
                return new Norwegian(toMap(numberBundle));
            default:
                return new English(toMap(numberBundle));
        }
    }

    @Override
    public List<Locale> getSupportedLanguages() {
        return List.of(Locale.ENGLISH, Locale.forLanguageTag("nb"));
    }

    @Override
    public ResourceBundle getBundle(String baseName, Locale locale) {
        return ResourceBundle.getBundle(baseName, locale);
    }
}
