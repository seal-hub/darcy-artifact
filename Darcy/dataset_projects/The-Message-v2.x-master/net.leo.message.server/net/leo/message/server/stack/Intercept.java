package net.leo.message.server.stack;

import net.leo.message.base.lang.Card;
import net.leo.message.base.lang.CardFunction;
import net.leo.message.server.game.Game;
import net.leo.message.server.game.Player;

public class Intercept extends ExecutableCard {


	public Intercept(Player user, Card card) {
		super(user, card, CardFunction.INTERCEPT);
	}

	@Override
	public int execute(Game game) {
		game.operate().triggerIntercept(card, user);
		return Stack.ONE_MORE_USAGE;
	}

	@Override
	public boolean invalidate(ExecutableCard exe) {
		return exe instanceof Intercept;
	}
}
