package net.leo.message.base.bridge.command.program;

import java.util.Collections;
import java.util.List;
import net.leo.message.base.bridge.command.Command;

public class GameBeginningCommand extends Command {

	public final List<String> names;
	public final int seat;

	public GameBeginningCommand(List<String> names, int seat) {
		super("初始", "遊戲開始");
		if (names.isEmpty() || seat < 0) {
			throw new IllegalArgumentException();
		}
		this.names = Collections.unmodifiableList(names);
		this.seat = seat;
	}
}
