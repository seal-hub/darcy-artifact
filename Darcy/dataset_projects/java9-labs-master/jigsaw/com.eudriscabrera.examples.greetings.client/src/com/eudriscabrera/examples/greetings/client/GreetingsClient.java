package com.eudriscabrera.examples.greetings.client;

import com.eudriscabrera.examples.greetings.World;

/**
 * @author ecabrerar
 *
 */
public class GreetingsClient {

	public static void main(String[] args) {
		System.out.format("Greetings %s!%n", World.name());
    }
}
