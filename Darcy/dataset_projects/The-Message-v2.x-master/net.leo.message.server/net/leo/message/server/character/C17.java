package net.leo.message.server.character;

import java.util.List;
import net.leo.message.base.lang.Character;
import net.leo.message.server.skill.S1701;
import net.leo.message.server.skill.S1702;
import net.leo.message.server.skill.Skill;

/**
 * 老闆
 */
public class C17 extends CharacterCard {

	private Skill s1 = new S1701(), s2 = new S1702();

	protected C17() {
		super(Character.C17, true);
	}

	@Override
	public List<Skill> getSkills() {
		return List.of(s1, s2);
	}

	@Override
	public String missionDescription() {
		return "獲得三張或以上的藍色情報。";
	}
}
