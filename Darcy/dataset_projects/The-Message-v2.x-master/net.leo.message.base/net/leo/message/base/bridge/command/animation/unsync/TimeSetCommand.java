package net.leo.message.base.bridge.command.animation.unsync;

import java.util.HashSet;
import java.util.Set;
import net.leo.message.base.bridge.command.Command;

public class TimeSetCommand extends Command {

	public final int time;
	public final String inst;
	public final Set<Integer> seats;

	public TimeSetCommand(int time, String inst, Set<Integer> seats) {
		super("非同步動畫", "時間軸");
		if (time < -1 || seats.isEmpty() || seats.stream().anyMatch(s -> s < 0)) {
			throw new IllegalArgumentException();
		}
		this.time = time;
		this.inst = inst;
		this.seats = new HashSet<>(seats);
	}

	public TimeSetCommand(int time, String inst, int seat) {
		super("非同步動畫", "時間軸");
		if (time < -1 || seat < 0) {
			throw new IllegalArgumentException();
		}
		this.time = time;
		this.inst = inst;
		this.seats = Set.of(seat);
	}
}
