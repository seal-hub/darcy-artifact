package net.leo.message.base.bridge.command.animation.sync;

import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.lang.Card;

public class IntelMovedCommand extends Command {

	public final Card intel;
	public final int dest;

	public IntelMovedCommand(Card intel, int dest) {
		super("同步動畫", "情報移動");
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
