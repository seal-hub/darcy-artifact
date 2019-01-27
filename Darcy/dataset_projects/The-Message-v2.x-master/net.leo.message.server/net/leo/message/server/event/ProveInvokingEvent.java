package net.leo.message.server.event;

import java.util.Objects;
import net.leo.message.base.lang.Card;
import net.leo.message.base.lang.CardFunction;
import net.leo.message.server.game.Player;

public class ProveInvokingEvent extends CardInvokingEvent {

	public boolean admission;
	public boolean denial;

	public ProveInvokingEvent(Player user, Card card, Player target) {
		super(user, card, CardFunction.PROVE, target);
		if (card.getFunction() != CardFunction.PROVE) {
			throw new IllegalArgumentException();
		}
		Objects.requireNonNull(target);
		admission = card.getProveTarget() == target.getIdentity();
		denial = !admission;
	}
}
