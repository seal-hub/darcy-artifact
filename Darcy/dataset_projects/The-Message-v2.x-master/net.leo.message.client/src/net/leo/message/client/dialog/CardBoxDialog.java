package net.leo.message.client.dialog;

import java.awt.Dimension;

/**
 * A dialog which contains card box(es).
 */
@FunctionalInterface
public interface CardBoxDialog extends Dialog {

	/**
	 * Sets the preferred size of each card box.
	 * @param size preferred size of each card box
	 */
	void setPreferredBoxSize(Dimension size);
}
