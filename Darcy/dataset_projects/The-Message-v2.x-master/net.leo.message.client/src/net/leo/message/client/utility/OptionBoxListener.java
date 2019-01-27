package net.leo.message.client.utility;

@FunctionalInterface
public interface OptionBoxListener {

	void onCardClicked(OptionCardView source, boolean status);
}
