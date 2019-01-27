package no.dervis.numbertotext.api.spi;

import no.dervis.numbertotext.api.language.Language;

import java.util.List;
import java.util.Locale;
import java.util.spi.ResourceBundleProvider;

public interface NumberResourcesProvider extends ResourceBundleProvider {
    Language getLanguage(String local);

    Language getLanguage();

    List<Locale> getSupportedLanguages();
}
