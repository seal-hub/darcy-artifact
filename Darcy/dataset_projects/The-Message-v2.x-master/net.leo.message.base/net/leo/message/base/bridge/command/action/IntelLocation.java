package net.leo.message.base.bridge.command.action;

import net.leo.message.base.lang.Card;

public class IntelLocation {

	public final int location;
	public final Card card;

	public IntelLocation(int location, Card card) {
		if (card == null) {
			throw new NullPointerException();
		}
		if (location < 0) {
			throw new IllegalArgumentException();
		}
		this.location = location;
		this.card = card;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof IntelLocation) {
			IntelLocation ol = (IntelLocation) o;
			return location == ol.location && card == ol.card;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return card.hashCode() * location;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + location + ", " + card.name() + ")";
	}
}
