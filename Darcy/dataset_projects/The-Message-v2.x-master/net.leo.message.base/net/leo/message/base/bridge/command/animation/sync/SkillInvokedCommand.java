package net.leo.message.base.bridge.command.animation.sync;

import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.lang.Character;

public class SkillInvokedCommand extends Command {

	public final int target;
	public final int invoker;
	public final Character character;
	public final String name;

	public SkillInvokedCommand(Character character, String name, int invoker, int target) {
		super("同步動畫", "發動技能");
		if (character == null || name == null) {
			throw new NullPointerException();
		}
		this.character = character;
		this.name = name;
		this.invoker = invoker;
		this.target = target;
	}

}
