package net.leo.message.server.stack;

import java.util.function.Predicate;
import net.leo.message.base.lang.Card;
import net.leo.message.base.lang.CardFunction;
import net.leo.message.server.game.Game;
import net.leo.message.server.game.Player;

public class Prove extends ExecutableCard {

	protected final Player target;

	public Prove(Player user, Card card, Player target) {
		super(user, card, CardFunction.PROVE);
		if (target == null) {
			throw new NullPointerException();
		}
		this.target = target;
	}

	@Override
	public int execute(Game game) {
		game.operate().triggerProve(card, user, target);
		return Stack.ONE_MORE_USAGE;
	}

	@Override
	public boolean invalidate(ExecutableCard rest) {
		return false;
	}

}
