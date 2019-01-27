package net.leo.message.base.bridge.command.animation.sync;

import net.leo.message.base.bridge.command.Command;

public class InitHandDrawnCommand extends Command {

	public final int nCard;

	public InitHandDrawnCommand(int nCard) {
		super("同步動畫", "初始抽牌");
		if (nCard <= 0) {
			throw new IllegalArgumentException();
		}
		this.nCard = nCard;
	}
}
