package no.dervis.numbertotext.generator;

import no.dervis.numbertotext.api.generator.Generator;

public class Main {

    public static void main(String[] args) {

        String[] defaultArgs = {"100", "en"};
        if (args.length != 2) args = defaultArgs;

        Generator base10Generator = new Base10Generator(args[1]);

        System.out.println(base10Generator.convert(Integer.parseInt(args[0])));

    }

}
