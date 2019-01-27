package net.leo.message.base.bridge.command.data;

import java.io.Serializable;
import net.leo.message.base.lang.Identity;

public class IdenAnimData implements Serializable {

	public final Identity identity;
	public final boolean hidden;
	public final int seat;

	public IdenAnimData(Identity identity, boolean hidden, int seat) {
		if (seat < 0) {
			throw new IllegalArgumentException();
		}
		this.identity = identity;
		this.hidden = hidden;
		this.seat = seat;
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
		String idenName = identity == null ? "無" : identity.name();
		return getClass().getSimpleName() + "[Saet=" + seat + ", Identity=" + idenName + ", Hidden=" + hidden + "]";
	}
}
