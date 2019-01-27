package net.leo.message.server.character;

import java.util.List;
import net.leo.message.base.lang.Character;
import net.leo.message.server.skill.S2401;
import net.leo.message.server.skill.Skill;

/**
 * 刀鋒
 */
public class C24 extends CharacterCard {

	private Skill s1 = new S2401();

	protected C24() {
		super(Character.C24, true);
	}

	@Override
	public List<Skill> getSkills() {
		return List.of(s1);
	}

	@Override
	public String missionDescription() {
		return "一位軍情處玩家和一位潛伏戰線玩家死亡。";
	}
}
