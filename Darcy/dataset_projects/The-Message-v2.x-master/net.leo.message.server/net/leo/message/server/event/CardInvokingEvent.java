package net.leo.message.server.event;

import java.util.Objects;
import net.leo.message.base.lang.Card;
import net.leo.message.base.lang.CardFunction;
import net.leo.message.server.game.Player;
import net.leo.message.server.game.Target;

public class CardInvokingEvent implements GameEvent {

	public final Player user;
	public final Card card;
	public final Target target;
	public final CardFunction func;

	public CardInvokingEvent(Player user, Card card, CardFunction func, Target target) {
		this.user = Objects.requireNonNull(user);
		this.card = card;
		this.target = target;
		this.func = Objects.requireNonNull(func);
	}

}
