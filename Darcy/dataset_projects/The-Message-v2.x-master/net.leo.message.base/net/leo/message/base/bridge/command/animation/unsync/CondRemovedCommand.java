package net.leo.message.base.bridge.command.animation.unsync;

import java.util.Collections;
import java.util.Set;
import net.leo.message.base.bridge.command.Command;

public class CondRemovedCommand extends Command {

	public final Set<Integer> seats;

	public CondRemovedCommand(Set<Integer> seats) {
		super("非同步動畫", "解除狀態");
		if (seats.stream().anyMatch(s -> s < 0)) {
			throw new IllegalArgumentException();
		}
		this.seats = Collections.unmodifiableSet(seats);
	}
}
