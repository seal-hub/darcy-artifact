package net.leo.message.client.dialog;

import java.awt.Component;
import net.leo.message.client.element.CharCard;

/**
 * A listener of a character selection.
 */
@FunctionalInterface
public interface CharDialogListener {

	/**
	 * Notifys this listener that user has decided which character to use.
	 * @param source   a selection dialog
	 * @param selected a character selected
	 */
	void onCompleted(Component source, CharCard selected);
}
