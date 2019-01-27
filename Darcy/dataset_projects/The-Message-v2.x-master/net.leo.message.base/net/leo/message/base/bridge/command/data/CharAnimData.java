package net.leo.message.base.bridge.command.data;

import java.io.Serializable;

public class CharAnimData implements Serializable{

	public final int seat;
	public final CharData charData;

	public CharAnimData(int seat, CharData charData) {
		if (seat < 0) {
			throw new IllegalArgumentException();
		}
		this.seat = seat;
		this.charData = charData;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof CondAnimData) {
			return seat == ((CondAnimData) o).seat;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return seat;
	}

	@Override
	public String toString() {
		return getClass().getName() + "[Seat=" + seat + ", " + charData + "]";
	}
}
