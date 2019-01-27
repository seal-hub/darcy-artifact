package net.leo.message.base.bridge.command.animation.sync;

import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.lang.Card;

public class IntelFlippedAndReceivedCommand extends Command {

	public final int dest;
	public final Card next, origin;

	public IntelFlippedAndReceivedCommand(Card origin, Card next, int dest) {
		super("同步動畫", "情報翻面接收");
		if (origin == null || next == null) {
			throw new NullPointerException();
		}
		if (dest < 0) {
			throw new IllegalArgumentException();
		}
		this.next = next;
		this.dest = dest;
		this.origin = origin;
	}
}
