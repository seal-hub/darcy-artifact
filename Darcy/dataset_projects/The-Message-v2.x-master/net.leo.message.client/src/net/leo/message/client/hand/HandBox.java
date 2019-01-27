package net.leo.message.client.hand;

import java.util.Set;
import net.leo.message.base.lang.Card;
import net.leo.message.client.selection.SelectionSet;
import net.leo.message.client.utility.DisplayBox;

public class HandBox extends DisplayBox<HandView> implements SelectionSet<Card> {

	private HandBoxListener listener = null;
	private Set<Card> candidates;

	public HandBox() {
		super();
	}

	@Override
	protected boolean activateListener() {
		return !isEmpty();
	}

	@Override
	protected void onMousePressed(int index) {
		super.onMousePressed(index);
		if (index == -1) {
			return;
		}
		if (candidates != null) {
			HandView hv = get(index);
			Card card = hv.getGameCard().getElement();
			if (candidates.contains(card)) {
				hv.setSelected(!hv.isSelected());
				if (listener != null) {
					listener.onHandToggled(hv, hv.isSelected());
				}
			}
		}
	}

	@Override
	public void runSelection(Set<Card> candidates) {
		getAll().forEach(view -> {
			view.setDarken(!candidates.contains(view.getGameCard().getElement()));
		});
		this.candidates = candidates;
	}

	public void setListener(HandBoxListener listener) {
		this.listener = listener;
	}

	@Override
	public void stopSelection() {
		getAll().forEach(view -> {
			view.setDarken(false);
			view.setSelected(false);
		});
		candidates = null;
	}
}
