package net.leo.message.base.bridge.command.animation.sync;

import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.lang.Card;

public class IntelChangedCommand extends Command {

	public final Card next, origin;

	public IntelChangedCommand(Card origin, Card next) {
		super("同步動畫", "情報替換");
		if (origin == null || next == null) {
			throw new NullPointerException();
		}
		this.next = next;
		this.origin = origin;
	}
}
