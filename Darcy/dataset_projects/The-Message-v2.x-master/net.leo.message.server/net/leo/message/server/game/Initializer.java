package net.leo.message.server.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import static java.util.stream.Collectors.toList;
import net.leo.message.base.lang.Card;
import net.leo.message.base.lang.Character;
import net.leo.message.base.lang.Identity;
import net.leo.message.server.character.CharacterCard;

public final class Initializer {

	public static List<Identity> identities(int count) {
		List<Identity> list = new ArrayList<>();

		//TODO
		switch (count) {
		case 3:
			list.add(Identity.RED_TEAM);
			list.add(Identity.BLUE_TEAM);
			list.add(Identity.PASSER_BY);
			break;
		default:
			throw new IllegalArgumentException();
		}

		Collections.shuffle(list, new Random());
		return list;
	}

	public static List<Card> gamecards() {
		List<Card> list = new ArrayList<>(81);
		//TODO add cards
		for (int i = 0 ; i < 20 ; i++) {
			list.add(Card.BLACK_PROVE_BLUE_PLUS);
			list.add(Card.BLACK_PROVE_BLUE_MINUS);
		}
		Collections.shuffle(list);
		return list;
	}

	public static List<CharacterCard> characters(int count) {

		List<Character> list = new ArrayList<>(Arrays.asList(Character.values()).subList(0, count));
		Collections.shuffle(list, new Random());
		list.add(2, Character.C01);
		list.add(1, Character.C01);
		list.add(0, Character.C01);
		return list.stream().map(c -> CharacterCard.getInstance(c)).collect(toList());
	}

	private Initializer() {
	}
}
