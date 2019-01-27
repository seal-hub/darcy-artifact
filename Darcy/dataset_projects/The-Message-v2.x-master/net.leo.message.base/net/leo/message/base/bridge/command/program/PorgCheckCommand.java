package net.leo.message.base.bridge.command.program;

import net.leo.message.base.bridge.command.Command;

public class PorgCheckCommand extends Command {

	public final int id;

	public PorgCheckCommand(int id) {
		super("基本", "進度確認");
		this.id = id;
	}
}
