package br.com.phtcosta.media.movie.nfo.test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class TestRunner {

	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(NfoParserTest.class);
		
		System.out.println("Success: "+result.wasSuccessful());
		System.out.println("Run: "+result.getRunCount()+" tests");
		System.out.println("Run time: "+result.getRunTime()+" ms");	
		System.out.println("Failures: "+result.getFailureCount());
		result.getFailures().forEach(System.out::println);
	}
}
