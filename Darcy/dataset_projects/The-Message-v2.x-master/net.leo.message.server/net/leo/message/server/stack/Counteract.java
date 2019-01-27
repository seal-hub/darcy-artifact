package net.leo.message.server.stack;

import java.util.Objects;
import net.leo.message.base.lang.Card;
import net.leo.message.base.lang.CardFunction;
import net.leo.message.server.game.Game;
import net.leo.message.server.game.Player;

public class Counteract extends ExecutableCard {

	protected final ExecutableCard target;

	public Counteract(Player user, Card card, ExecutableCard target) {
		super(user, card, CardFunction.COUNTERACT);
		this.target = Objects.requireNonNull(target);
	}

	@Override
	public int execute(Game game) {
		game.operate().triggerCounteract(card, user, target);
		return Stack.ONE_MORE_USAGE;
	}

	@Override
	public boolean invalidate(ExecutableCard exe) {
		return exe == target;
	}
}
