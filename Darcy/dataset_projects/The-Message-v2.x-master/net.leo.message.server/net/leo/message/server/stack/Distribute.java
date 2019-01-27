package net.leo.message.server.stack;

import java.util.function.Predicate;
import net.leo.message.base.lang.Card;
import net.leo.message.base.lang.CardFunction;
import net.leo.message.server.game.Game;
import net.leo.message.server.game.Player;

public class Distribute extends ExecutableCard {

	public Distribute(Player user, Card card) {
		super(user, card, CardFunction.DISTRIBUTE);
	}

	@Override
	public int execute(Game game) {
		game.operate().triggerDistribute(card, user);
		return Stack.ONE_MORE_USAGE;
	}

	@Override
	public boolean invalidate(ExecutableCard rest) {
		return false;
	}

}
