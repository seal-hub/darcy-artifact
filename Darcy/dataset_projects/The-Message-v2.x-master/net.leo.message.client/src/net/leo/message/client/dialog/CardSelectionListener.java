package net.leo.message.client.dialog;

import java.awt.Component;
import java.util.List;
import net.leo.message.client.element.GameCard;

/**
 * A listener of a card(s) selection
 */
@FunctionalInterface
public interface CardSelectionListener {

	/**
	 * Notifys this listener that user has decided to select cards.
	 * @param source      a selection dialog
	 * @param contributes cards selected
	 */
	void onCardSubmit(Component source, List<GameCard> contributes);
}
