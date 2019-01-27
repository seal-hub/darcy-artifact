package net.leo.message.server.stack;

import net.leo.message.base.lang.Card;
import net.leo.message.base.lang.CardFunction;
import net.leo.message.server.game.Game;
import net.leo.message.server.game.Player;

public class Burn extends ExecutableCard {

	protected final Player target;

	public Burn(Player user, Card card, Player target) {
		super(user, card, CardFunction.BURN);
		if (target == null) {
			throw new NullPointerException();
		}
		this.target = target;
	}

	@Override
	public int execute(Game game) {
		game.operate().triggerBurn(card, user, target);
		return Stack.ONE_MORE_USAGE;
	}

	@Override
	public boolean invalidate(ExecutableCard exe) {
		return false;
	}
}

