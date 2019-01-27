package net.leo.message.server.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import net.leo.message.base.lang.Card;

@SuppressWarnings("serial")
public class Deck {

	private final Queue<Card> deadwood;
	private final LinkedList<Card> list = new LinkedList<>(Initializer.gamecards());


	public Deck(Queue<Card> deadwood) {
		this.deadwood = deadwood;
	}

	private Card draw() {
		if (isEmpty() && !wash()) {
			return null;
		}
		return list.removeFirst();
	}

	private boolean wash() {
		if (deadwood.isEmpty()) {
			return false;
		}
		list.addAll(deadwood);
		deadwood.clear();
		Collections.shuffle(list);
		return true;
	}


	public List<Card> draw(int count) {
		if (count <= 0) {
			throw new IllegalArgumentException();
		}

		List<Card> list = new ArrayList<>(count);
		Card card;
		for (int i = 0 ; i < count ; i++) {
			if ((card = draw()) == null) {
				return list;
			}
			list.add(card);
		}
		return list;
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public Card peek() {
		if (list.isEmpty() && !wash()) {
			return null;
		}
		return list.getFirst();
	}

	public void put(List<Card> cards) {
		cards = new LinkedList<>(cards);
		Collections.reverse(cards);
		list.addAll(0, cards);
	}

	public int size() {
		return list.size();
	}
}
