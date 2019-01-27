package net.leo.message.server.game;

import java.util.ArrayList;
import java.util.Collection;
import net.leo.message.base.lang.Card;

public class CardList extends ArrayList<Card> {

	@Override
	public boolean removeAll(Collection<?> cards) {
		if (!containsAll(cards)) {
			throw new IllegalArgumentException();
		}
		cards.forEach(c -> {
			remove(c);
		});
		return true;
	}
}
