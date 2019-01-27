package net.leo.message.client.animation.computer;

import java.awt.Rectangle;
import net.leo.message.client.player.CountView;
import net.leo.message.client.element.GameCard;

public interface Descendable {

	GameCard getCard();

	CountView getDestination();

	void setTopBounds(Rectangle top);
}
