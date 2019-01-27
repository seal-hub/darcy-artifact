package net.leo.message.client.utility;

import java.awt.Component;

@FunctionalInterface
public interface ButtonListener {

	void onButtonClicked(Component source, int buttonIndex);
}
