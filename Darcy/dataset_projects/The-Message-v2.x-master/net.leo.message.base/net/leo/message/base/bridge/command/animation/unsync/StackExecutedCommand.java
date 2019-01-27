package net.leo.message.base.bridge.command.animation.unsync;

import net.leo.message.base.bridge.command.Command;

public class StackExecutedCommand extends Command {

	public static final StackExecutedCommand EXE = new StackExecutedCommand("堆疊生效");
	public static final StackExecutedCommand LOCK = new StackExecutedCommand("堆疊鎖定");
	public static final StackExecutedCommand UNLOCK = new StackExecutedCommand("堆疊解鎖");

	private StackExecutedCommand(String title) {
		super("非同步動畫", title);
	}

	@SuppressWarnings("unused")
	private StackExecutedCommand readResolve() {
		switch (title) {
		case "堆疊生效":
			return EXE;
		case "堆疊鎖定":
			return LOCK;
		case "堆疊解鎖":
			return UNLOCK;
		}
		return this;
	}
}
