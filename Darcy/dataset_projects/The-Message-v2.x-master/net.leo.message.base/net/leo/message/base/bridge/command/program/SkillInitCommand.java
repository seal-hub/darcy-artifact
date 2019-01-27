package net.leo.message.base.bridge.command.program;

import java.util.ArrayList;
import java.util.List;
import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.bridge.command.data.SkillData;

public class SkillInitCommand extends Command {

	public final List<SkillData> skillData;


	public SkillInitCommand(List<SkillData> skillData) {
		super("初始", "註冊技能");
		this.skillData = new ArrayList<>(skillData);
	}
}
