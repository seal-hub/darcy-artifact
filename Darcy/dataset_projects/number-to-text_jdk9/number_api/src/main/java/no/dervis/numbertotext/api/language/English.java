package no.dervis.numbertotext.api.language;

import java.util.Map;

public class English implements Language {

    private final Map<Integer, String> map;

    public English(Map<Integer, String> map) {
        this.map = map;
    }

    @Override
    public Map<Integer, String> getLanguageMap() {
        return map;
    }

    @Override
    public String prefixRule(int n, int div) {
        return map.get(n / div);
    }

    @Override
    public String AND() {
        return "and";
    }

}
