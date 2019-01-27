package net.leo.message.base.bridge.reply;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MultiReply<T> extends Reply {

	public final List<T> values;

	public MultiReply(int id, List<T> values) {
		super(id);
		this.values = new ArrayList<>(values);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof MultiReply) {
			MultiReply oReply = (MultiReply) o;
			return oReply.id == id && oReply.values.equals(values);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(values, id);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + values;
	}
}
