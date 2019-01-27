package net.leo.message.client.animation.computer;

import java.awt.Rectangle;
import net.leo.message.client.player.CountView;
import net.leo.message.client.element.GameCard;

public interface Risable {

	GameCard getCard();

	CountView getOrigin();

	void setTopBounds(Rectangle top);
}
