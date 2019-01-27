package no.dervis.numbertotext.spi;


import no.dervis.numbertotext.api.language.Language;
import no.dervis.numbertotext.language.impl.NumberResourcesProviderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class NumberResourcesProviderImplTest {

    private NumberResourcesProviderImpl impl;

    @BeforeEach
    void setUp() {
        impl = new NumberResourcesProviderImpl();
    }

    @Test
    void getLanguage() {
        Language no = impl.getLanguage("no");
        assertNotNull(no);
        assertTrue(no.getLanguageMap().size() == 36);

        Language nb = impl.getLanguage("nb");
        assertNotNull(nb);
        assertTrue(nb.getLanguageMap().size() == 36);

        // defaults to the NumberBundle.properties
        Language defualt = impl.getLanguage();
        assertTrue(defualt.getLanguageMap().containsKey(0));
        assertTrue(defualt.getLanguageMap().get(0).equals("zero"));
    }

    @Test
    void getSupportedLanguages() {
        List<Locale> supportedLanguages = impl.getSupportedLanguages();
        assertTrue(supportedLanguages.size() == 2);
        assertTrue(supportedLanguages.stream()
                .filter(locale -> locale.getLanguage().equals("nb"))
                .count() == 1);
    }

    @Test
    void getBundle() {
        assertThrows(MissingResourceException.class, () -> impl.getBundle("MissingBundle", Locale.ENGLISH));
        assertNotNull(impl.getBundle("NumberBundle", Locale.ENGLISH));
        assertNotNull(impl.getBundle("NumberBundle", Locale.forLanguageTag("no")));

        // should default to english if a language does not exist
        ResourceBundle bundle = impl.getBundle("NumberBundle", Locale.forLanguageTag("tr"));
        assertNotNull(bundle);

        Map<Integer, String> defualt = impl.toMap(bundle);
        assertTrue(defualt.containsKey(0));
        assertTrue(defualt.get(0).equals("zero"));
    }

}