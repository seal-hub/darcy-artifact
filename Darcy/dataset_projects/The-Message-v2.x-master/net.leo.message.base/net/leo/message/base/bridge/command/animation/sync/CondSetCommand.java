package net.leo.message.base.bridge.command.animation.sync;

import java.util.Collections;
import java.util.Set;
import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.bridge.command.data.CondAnimData;
import net.leo.message.base.lang.Condition;

public class CondSetCommand extends Command {

	public final Set<CondAnimData> animInfo;

	public void addInfo(int seat, Condition cond, boolean renew) {
		animInfo.add(new CondAnimData(seat, cond, renew));
	}

	public CondSetCommand(Set<CondAnimData> animInfo) {
		super("同步動畫", "設定狀態");
		this.animInfo = Collections.unmodifiableSet(animInfo);
	}

	public CondSetCommand(int seat, Condition cond, boolean renew) {
		super("同步動畫", "設定狀態");
		animInfo = Set.of(new CondAnimData(seat, cond, renew));
	}
}
