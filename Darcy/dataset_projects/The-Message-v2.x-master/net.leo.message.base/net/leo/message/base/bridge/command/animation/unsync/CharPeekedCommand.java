package net.leo.message.base.bridge.command.animation.unsync;

import java.util.Collections;
import java.util.Set;
import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.bridge.command.data.CharAnimData;
import net.leo.message.base.bridge.command.data.CharData;

public class CharPeekedCommand extends Command {

	public final Set<CharAnimData> info;

	public CharPeekedCommand(int seat, CharData info) {
		super("非同步動畫", "窺視");
		this.info = Set.of(new CharAnimData(seat, info));
	}

	public CharPeekedCommand(Set<CharAnimData> info) {
		super("非同步動畫", "窺視");
		if (info.isEmpty()) {
			throw new IllegalArgumentException();
		}
		this.info = Collections.unmodifiableSet(info);
	}
}
