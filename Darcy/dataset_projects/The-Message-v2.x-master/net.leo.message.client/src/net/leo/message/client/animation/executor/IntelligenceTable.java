package net.leo.message.client.animation.executor;

import net.leo.message.client.element.GameCard;
import net.leo.message.client.player.CountView;

public interface IntelligenceTable {

	void changeTo(GameCard next) ;

	void flipAndReceive(GameCard next, CountView dest) ;

	void flipTo(GameCard next) ;

	void moveTo(int dest) ;

	void receive(CountView dest) ;

	void sendTo(GameCard intel, int org, int dest) ;
}
