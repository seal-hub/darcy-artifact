package net.leo.message.server.event;

import java.util.List;
import java.util.Objects;
import net.leo.message.base.lang.Card;
import net.leo.message.server.game.Player;

public class IntelReceviedEvent extends IntelObtainedEvent {

	protected Player origin;

	public IntelReceviedEvent(Player origin, Player receiver, Card intel) {
		super(receiver, List.of(intel));
		this.origin = Objects.requireNonNull(origin);
	}

	public Card getIntelligence() {
		return super.intels.get(0);
	}

	public Player getOrigin() {
		return origin;
	}
}
