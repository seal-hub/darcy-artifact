package net.leo.message.server.character;

import java.util.List;
import net.leo.message.base.lang.Character;
import net.leo.message.server.skill.Skill;

public class CTest extends CharacterCard {

	public CTest() {
		super(Character.C01, false);
	}

	@Override
	public List<Skill> getSkills() {
		return List.of();
	}

	@Override
	public String missionDescription() {
		return "測試用。";
	}
}
