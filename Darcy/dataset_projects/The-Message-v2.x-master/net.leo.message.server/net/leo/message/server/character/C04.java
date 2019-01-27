package net.leo.message.server.character;

import java.util.List;
import net.leo.message.base.lang.Character;
import net.leo.message.server.skill.S0401;
import net.leo.message.server.skill.Skill;

/**
 * 譯電員
 */
public class C04 extends CharacterCard {

	private Skill s1 = new S0401();

	protected C04() {
		super(Character.C04, false);
	}

	@Override
	public List<Skill> getSkills() {
		return List.of(s1);
	}

	@Override
	public String missionDescription() {
		return "獲得三張或以上的藍色情報";
	}
}
