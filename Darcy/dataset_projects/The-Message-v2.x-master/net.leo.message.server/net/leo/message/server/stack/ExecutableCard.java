package net.leo.message.server.stack;

import java.util.Objects;
import net.leo.message.base.lang.Card;
import net.leo.message.base.lang.CardFunction;
import net.leo.message.server.game.Game;
import net.leo.message.server.game.Player;
import net.leo.message.server.game.Target;

public abstract class ExecutableCard implements Executable, Target {

	public final Card card;
	public final CardFunction func;
	public final Player user;

	public boolean effective = true;

	public ExecutableCard(Player user, Card card, CardFunction func) {
		this.card = card;
		this.func = Objects.requireNonNull(func);
		this.user = user;
	}

	@Override
	public void dispose(Game game) {
	}

	@Override
	public boolean isEffective() {
		return effective;
	}

	@Override
	public void setEffective(boolean effective) {
		this.effective = effective;
	}
}
