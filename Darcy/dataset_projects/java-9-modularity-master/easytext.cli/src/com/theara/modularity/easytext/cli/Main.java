package com.theara.modularity.easytext.cli;

import com.theara.easytext.service.Analysis;

import java.util.ServiceLoader;

public class Main {

    public static void main(String[] args) {
        if (args.length < 1){
            System.out.println("Welcome to EasyText. Please provide some text for us. Thank you!");
            return;
        }

        Iterable<Analysis> analysisServices = ServiceLoader.load(Analysis.class);

        analysisServices.forEach(analysis -> {
            System.out.println("Flesch-Kincaid : " + analysis.analyze(args[0]).getStatementLength());
        });
    }

}
