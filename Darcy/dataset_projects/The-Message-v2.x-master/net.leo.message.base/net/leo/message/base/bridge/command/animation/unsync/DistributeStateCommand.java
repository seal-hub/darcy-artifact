package net.leo.message.base.bridge.command.animation.unsync;

import java.io.Serializable;
import net.leo.message.base.bridge.command.Command;

public class DistributeStateCommand extends Command implements Serializable {

	public static final DistributeStateCommand END = new DistributeStateCommand();
	public final int count;

	public DistributeStateCommand(int count) {
		super("非同步動畫", "真偽莫辨");
		if (count <= 0) {
			throw new IllegalArgumentException();
		}
		this.count = count;
	}

	private DistributeStateCommand() {
		super("非同步動畫", "真偽莫辨");
		count = -1;
	}

	@SuppressWarnings("unused")
	private DistributeStateCommand readResolve() {
		if (count == -1) {
			return END;
		}
		return this;
	}
}
