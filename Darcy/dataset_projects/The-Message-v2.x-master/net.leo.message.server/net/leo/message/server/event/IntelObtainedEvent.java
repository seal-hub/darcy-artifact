package net.leo.message.server.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.leo.message.base.lang.Card;
import net.leo.message.server.game.Player;

public class IntelObtainedEvent implements GameEvent {

	protected Player acquierer;
	protected List<Card> intels;

	public IntelObtainedEvent(Player acquierer, List<Card> intels) {
		this.acquierer = Objects.requireNonNull(acquierer);
		this.intels = new ArrayList<>(intels);
	}

	public Player getObtainer() {
		return acquierer;
	}

	public List<Card> getIntelligences() {
		return new ArrayList<>(intels);
	}
}
