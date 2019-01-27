package net.leo.message.base.bridge.command.animation.sync;

import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.lang.Card;

public class IntelReceivedCommand extends Command {

	public final Card intel;
	public final int dest;

	public IntelReceivedCommand(int dest, Card intel) {
		super("同步動畫", "情報接收");
		if (intel == null) {
			throw new NullPointerException();
		}
		if (dest < 0) {
			throw new IllegalArgumentException();
		}
		this.dest = dest;
		this.intel = intel;
	}
}
