package net.leo.message.server.stack;

import java.util.function.Predicate;
import net.leo.message.base.lang.Card;
import net.leo.message.base.lang.CardFunction;
import net.leo.message.server.game.Game;
import net.leo.message.server.game.Player;

public class Decode extends ExecutableCard {

	public Decode(Player user, Card card) {
		super(user, card, CardFunction.DECODE);
	}

	@Override
	public int execute(Game game) {
		game.operate().triggerDecode(card, user);
		return Stack.ONE_MORE_USAGE;
	}

	@Override
	public boolean invalidate(ExecutableCard rest) {
		return false;
	}

}
