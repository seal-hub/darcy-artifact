package net.leo.message.client.utility;

import java.util.Comparator;
import net.leo.message.client.element.GameCard;

/**
 * A view that shows a card.
 * @author Leo Zeng
 */
public interface CardView extends Comparable<CardView> {

	Comparator<CardView> COMPARATOR = Comparator.comparing(v -> v.getGameCard());

	@Override
	default int compareTo(CardView o) {
		return getGameCard().compareTo(o.getGameCard());
	}

	/**
	 * Gets the card in this view.
	 * @return card in this view
	 */
	GameCard getGameCard();
}
