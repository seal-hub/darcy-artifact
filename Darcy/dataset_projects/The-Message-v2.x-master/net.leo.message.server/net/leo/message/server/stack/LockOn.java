package net.leo.message.server.stack;

import java.util.function.Predicate;
import net.leo.message.base.lang.Card;
import net.leo.message.base.lang.CardFunction;
import net.leo.message.server.game.Game;
import net.leo.message.server.game.Player;

public class LockOn extends ExecutableCard {

	protected final Player target;

	public LockOn(Player user, Card card, Player target) {
		super(user, card, CardFunction.LOCK_ON);
		if (target == null) {
			throw new NullPointerException();
		}
		this.target = target;
	}

	@Override
	public int execute(Game game) {
		game.operate().triggerLockOn(card, user, target);
		return Stack.ONE_MORE_USAGE;
	}

	@Override
	public boolean invalidate(ExecutableCard exe) {
		return exe instanceof Trap && ((Trap) exe).target == target;
	}

}
