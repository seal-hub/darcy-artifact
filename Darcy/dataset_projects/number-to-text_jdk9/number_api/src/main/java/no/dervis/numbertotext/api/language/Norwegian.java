package no.dervis.numbertotext.api.language;

import java.util.Map;

public class Norwegian implements Language {

    private final Map<Integer, String> map;
    private static final String EN = "en";
    private static final String ETT = "ett";
    private static final String OG = "og";

    public Norwegian(Map<Integer, String> map) {
        this.map = map;
    }

    @Override
    public Map<Integer, String> getLanguageMap() {
        return map;
    }

    @Override
    public String prefixRule(int n, int div) {
        String en = map.get(n / div);
        return en.equals(EN) ? Norwegian.ETT : en;
    }

    @Override
    public String AND() {
        return OG;
    }
}
