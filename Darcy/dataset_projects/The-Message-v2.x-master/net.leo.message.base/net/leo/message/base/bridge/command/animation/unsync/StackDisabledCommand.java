package net.leo.message.base.bridge.command.animation.unsync;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import net.leo.message.base.bridge.command.Command;

public class StackDisabledCommand extends Command {

	public final Set<Integer> diss;

	public StackDisabledCommand(Collection<Integer> diss) {
		super("非同步動畫", "堆疊失效");
		this.diss = new HashSet<>(diss);
	}
}
