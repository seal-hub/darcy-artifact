package net.leo.message.base.bridge.reply;

public class NullReply extends Reply {

	public NullReply(int id) {
		super(id);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof NullReply) {
			return id == ((NullReply) o).id;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
