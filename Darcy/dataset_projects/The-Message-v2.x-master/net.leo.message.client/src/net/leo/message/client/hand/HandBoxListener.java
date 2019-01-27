package net.leo.message.client.hand;

@FunctionalInterface
public interface HandBoxListener {

	void onHandToggled(HandView hand, boolean status);
}
