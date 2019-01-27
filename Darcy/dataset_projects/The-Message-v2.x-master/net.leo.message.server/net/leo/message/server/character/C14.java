package net.leo.message.server.character;

import java.util.List;
import net.leo.message.base.lang.Character;
import net.leo.message.server.skill.S1401;
import net.leo.message.server.skill.Skill;

/**
 * 66
 */
public class C14 extends CharacterCard {

	private Skill s1 = new S1401();

	C14() {
		super(Character.C14, true);
	}

	@Override
	public List<Skill> getSkills() {
		return List.of(s1);
	}

	@Override
	public String missionDescription() {
		return "獲得六張或以上的情報。";
	}
}
