package net.leo.message.server.stack;

import net.leo.message.base.lang.Card;
import net.leo.message.base.lang.CardFunction;
import net.leo.message.base.lang.Condition;
import net.leo.message.server.game.Game;
import net.leo.message.server.game.Player;

public class Trap extends ExecutableCard {

	protected final Player target;

	public Trap(Player user, Card card, Player target) {
		super(user, card, CardFunction.TRAP);
		if (target == null) {
			throw new NullPointerException();
		}
		this.target = target;
	}

	@Override
	public int execute(Game game) {
		game.operate().triggerTrap(card, user, target);
		return Stack.ONE_MORE_USAGE;
	}

	@Override
	public boolean invalidate(ExecutableCard rest) {
		return false;
	}

	@Override
	public boolean isEffective() {
		if (super.isEffective()) {
			Condition cond = target.getCondition();
			return cond != Condition.LOCKED;
		}
		return false;
	}
}
