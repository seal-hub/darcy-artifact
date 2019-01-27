package net.leo.message.client.dialog;

import java.awt.Dimension;

/**
 * A dialog which contains card view(s).
 */
@FunctionalInterface
public interface CardDialog extends Dialog {

	/**
	 * Sets the preferred size of each card view.
	 * @param size preferred size of card view
	 */
	void setPreferredCardSize(Dimension size);
}
