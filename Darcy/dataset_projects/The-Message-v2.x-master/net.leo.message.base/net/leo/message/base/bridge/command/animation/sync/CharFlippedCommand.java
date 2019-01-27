package net.leo.message.base.bridge.command.animation.sync;

import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.bridge.command.data.CharData;

public class CharFlippedCommand extends Command {

	public final int seat;
	public final CharData animInfo;

	public CharFlippedCommand(int seat, CharData animInfo) {
		super("同步動畫", "玩家翻面");
		if (seat < 0) {
			throw new IllegalArgumentException();
		}
		this.seat = seat;
		this.animInfo = animInfo;
	}

	public CharFlippedCommand(int seat) {
		this(seat, null);
	}
}
