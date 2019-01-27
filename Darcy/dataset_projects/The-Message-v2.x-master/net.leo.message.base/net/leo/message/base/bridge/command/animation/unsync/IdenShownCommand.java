package net.leo.message.base.bridge.command.animation.unsync;

import java.util.Collections;
import java.util.Set;
import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.bridge.command.data.IdenAnimData;
import net.leo.message.base.lang.Identity;

public class IdenShownCommand extends Command {

	public Set<IdenAnimData> animInfo;

	public IdenShownCommand(Identity identity, int seat, boolean hidden) {
		super("非同步動畫", "身分");
		animInfo = Set.of(new IdenAnimData(identity, hidden, seat));
	}

	public IdenShownCommand(Set<IdenAnimData> animInfo) {
		super("非同步動畫", "身分");
		if (animInfo.isEmpty()) {
			throw new IllegalArgumentException();
		}
		this.animInfo = Collections.unmodifiableSet(animInfo);
	}
}
