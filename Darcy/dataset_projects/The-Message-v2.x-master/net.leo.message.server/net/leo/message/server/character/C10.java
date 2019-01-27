package net.leo.message.server.character;

import java.util.List;
import net.leo.message.base.lang.Character;
import net.leo.message.server.skill.S1001;
import net.leo.message.server.skill.Skill;

/**
 * 黃雀
 */
public class C10 extends CharacterCard {

	private Skill s1 = new S1001();

	public C10() {
		super(Character.C10, false);
	}

	@Override
	public List<Skill> getSkills() {
		return List.of(s1);
	}

	@Override
	public String missionDescription() {
		return null;
	}
}
