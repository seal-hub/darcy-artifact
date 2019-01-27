package no.dervis.numbertotext.api.generator;

public interface Generator {

    String convert(int n);

    String millions(int n);

    String hundreds_thousands(int n);

    String tenthousands(int n);

    String thousands(int n);

    String hundreds(int n);

    String tens(int n);
}
