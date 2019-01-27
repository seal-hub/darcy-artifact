package net.leo.message.client.dialog;

import java.util.List;
import net.leo.message.base.lang.Card;
import net.leo.message.client.element.GameCard;
import net.leo.message.client.utility.OptionCardView;

/**
 * A card net.leo.message.client.select dialog that asks client to net.leo.message.client.select one card.
 * @author Leo Zeng
 */
public class RadioDialog extends AbstractOptionDialog {

	/**
	 * The card that is selected.
	 */
	protected OptionCardView contribute = null;

	/**
	 * Constructs a card net.leo.message.client.select dialog.
	 * @param candidates  candidates
	 * @param title       title of this dialog
	 * @param enforcement true if this selection is mandatory
	 * @throws IllegalArgumentException if no candidates
	 * @throws NullPointerException     if title or timer is null
	 */
	public RadioDialog(String title, List<? extends Card> candidates, boolean enforcement) {
		super(title, candidates, enforcement);
	}


	@Override
	protected void onCancelCliked() {
		if (listener != null) {
			listener.onCardSubmit(this, null);
		}
	}

	@Override
	protected void onCardClicked(OptionCardView source, boolean status) {
		if (status) {
			if (contribute != null) {
				contribute.setSelected(false);
				contribute = source;
			}
			else {
				contribute = source;
				submit.setEnabled(true);
			}
		}
		else {
			contribute = null;
			if (enforcement) {
				submit.setEnabled(false);
			}
		}
	}

	@Override
	protected void onSubmitClicked() {
		if (listener != null) {
			List<GameCard> list = contribute == null ? List.of() : List.of(contribute.getGameCard());
			listener.onCardSubmit(this, list);
		}
	}
}
