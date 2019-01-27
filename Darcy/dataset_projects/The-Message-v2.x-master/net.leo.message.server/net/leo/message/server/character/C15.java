package net.leo.message.server.character;

import java.util.List;
import net.leo.message.base.lang.Character;
import net.leo.message.server.skill.S1501;
import net.leo.message.server.skill.S1502;
import net.leo.message.server.skill.Skill;

/**
 * 殺手
 */
public class C15 extends CharacterCard {

	private Skill s1 = new S1501(), s2 = new S1502();

	public C15() {
		super(Character.C15, true);
	}

	@Override
	public List<Skill> getSkills() {
		return List.of(s1, s2);
	}

	@Override
	public String missionDescription() {
		return "親手殺死另一位玩家，使他成為第二位或以後的死者。";
	}
}
