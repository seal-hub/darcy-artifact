package net.leo.message.client.utility;

/**
 * A box that shows cards which are selctable
 * @author Leo Zeng
 */
public class OptionBox extends DisplayBox<OptionCardView> {

	private OptionBoxListener listener = null;

	public OptionBox() {
		super();
	}

	@Override
	protected boolean activateListener() {
		return count() != 0;
	}

	@Override
	protected void onMousePressed(int index) {
		super.onMousePressed(index);
		if (index == -1) {
			return;
		}

		OptionCardView card = get(index);
		card.setSelected(!card.isSelected());

		if (listener != null) {
			listener.onCardClicked(card, card.isSelected());
		}
	}


	/**
	 * Sets a listener of this box
	 * @param listener listener
	 */
	public void setListener(OptionBoxListener listener) {
		this.listener = listener;
	}
}
