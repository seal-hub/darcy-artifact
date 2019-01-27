package net.leo.message.base.bridge.command.data;

import java.io.Serializable;
import net.leo.message.base.lang.Condition;

public class CondAnimData implements Serializable {

	public final int seat;
	public final Condition cond;
	public final boolean renew;

	public CondAnimData(int seat, Condition cond, boolean renew) {
		if (seat < 0 || cond == null) {
			throw new IllegalArgumentException();
		}
		this.seat = seat;
		this.cond = cond;
		this.renew = renew;
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
		return getClass().getSimpleName() + "[Seat=" + seat + ", Code=" + cond.name() + ", Renew=" + renew + "]";
	}
}
