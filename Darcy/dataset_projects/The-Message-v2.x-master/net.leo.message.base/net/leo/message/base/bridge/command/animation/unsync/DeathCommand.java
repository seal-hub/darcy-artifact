package net.leo.message.base.bridge.command.animation.unsync;

import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.bridge.command.data.CharData;
import net.leo.message.base.lang.Identity;

public class DeathCommand extends Command {

	public final CharData chardata;
	public final boolean dead;
	public final Identity identity;
	public final int seat;

	public DeathCommand(CharData charData, boolean dead, Identity identity, int seat) {
		super("非同步動畫", "死亡");
		if (charData == null) {
			throw new NullPointerException();
		}
		if (seat < 0) {
			throw new IllegalArgumentException();
		}
		this.chardata = charData;
		this.dead = dead;
		this.identity = identity;
		this.seat = seat;
	}
}
