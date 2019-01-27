package net.leo.message.server.game;

import net.leo.message.base.lang.Card;
import net.leo.message.base.lang.IntelligenceType;
import static net.leo.message.base.lang.IntelligenceType.EXPRESS;

public class Intelligence {

	private Card intel = null;
	private IntelligenceType type = null;
	private Player sender = null;
	private Player location = null;
	private boolean covered = false;
	private boolean dir = false;

	public Intelligence() {
	}

	private void requiresIntel() {
		if (intel == null) {
			throw new IllegalStateException();
		}
	}

	public boolean getDirection() {
		requiresIntel();
		return dir;
	}

	public Card getFace() {
		if (covered) {
			switch (intel.getType()) {
			case DOCUMENT:
				return Card.COVERED_DOCUMENT;
			case EXPRESS:
				return Card.COVERED_EXPRESS;
			case SECRET:
				return Card.COVERED_SECRET;
			default:
				throw new IllegalArgumentException();
			}
		}
		else {
			return intel;
		}
	}

	public Card getIntelligence() {
		return intel;
	}

	public Player getLocation() {
		return location;
	}

	public Player getNext(PlayerList players) {
		requiresIntel();
		if (type == EXPRESS) {
			return sender;
		}
		else {
			return dir ? players.nextAlive(location) : players.previousAlive(location);
		}
	}

	public Player getSender() {
		requiresIntel();
		return sender;
	}

	public IntelligenceType getType() {
		requiresIntel();
		return type;
	}

	public boolean isCovered() {
		requiresIntel();
		return covered;
	}

	public void moveTo(Player dest) {
		requiresIntel();
		location = dest;
	}

	public void reset() {
		this.intel = null;
		this.type = null;
		this.sender = null;
		this.location = null;
		this.covered = false;
		this.dir = false;
	}

	public void sendTo(Card intel, IntelligenceType type, boolean covered, Player sender, Player dest) {
		if (this.intel != null) {
			throw new IllegalStateException();
		}
		this.intel = intel;
		this.type = type;
		this.sender = sender;
		this.location = dest;
		this.covered = type != IntelligenceType.DOCUMENT;

		if (type == IntelligenceType.EXPRESS) {
			dir = false;
		}
		else {
			dir = true;
		}
	}

	public void setCovered(boolean covered) {
		requiresIntel();
		this.covered = covered;
	}

	public void setDirection(boolean dir) {
		requiresIntel();
		if (type == IntelligenceType.EXPRESS) {
			throw new IllegalStateException();
		}
		this.dir = dir;
	}

	public void setIntelligence(Card intel) {
		requiresIntel();
		this.intel = intel;
	}

	public void setLocation(Player location) {
		requiresIntel();
		this.location = location;
	}

	public void setSender(Player sender) {
		requiresIntel();
		this.sender = sender;
	}

	public void setType(IntelligenceType type) {
		requiresIntel();
		this.type = type;
	}
}
