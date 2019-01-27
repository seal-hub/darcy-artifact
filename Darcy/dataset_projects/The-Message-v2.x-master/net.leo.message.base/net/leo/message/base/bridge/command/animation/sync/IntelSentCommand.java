package net.leo.message.base.bridge.command.animation.sync;

import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.lang.Card;

public class IntelSentCommand extends Command {

	public int origin, dest;
	public Card intel;

	public IntelSentCommand(int origin, int dest, Card intel) {
		super("同步動畫", "情報發送");
		if (intel == null) {
			throw new NullPointerException();
		}
		if (dest < 0 || origin < 0) {
			throw new IllegalArgumentException();
		}
		this.origin = origin;
		this.dest = dest;
		this.intel = intel;
	}
}
