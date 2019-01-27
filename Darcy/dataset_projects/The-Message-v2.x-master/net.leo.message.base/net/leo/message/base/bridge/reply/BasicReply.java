package net.leo.message.base.bridge.reply;

import java.util.Objects;

public class BasicReply<T> extends Reply {

	public final T value;

	public BasicReply(int id, T value) {
		super(id);
		if (value == null) {
			throw new NullPointerException();
		}
		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof BasicReply) {
			BasicReply oReply = (BasicReply) o;
			return id == oReply.id && value.equals(oReply.value);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(value, id);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + value;
	}
}
