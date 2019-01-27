package net.leo.message.server.character;

import java.util.List;
import net.leo.message.base.lang.Character;
import net.leo.message.server.skill.S0801;
import net.leo.message.server.skill.Skill;

public class C08 extends CharacterCard {

	private Skill s1 = new S0801();

	public C08() {
		super(Character.C08, false);
	}

	@Override
	public List<Skill> getSkills() {
		return List.of(s1);
	}

	@Override
	public String missionDescription() {
		return "你成為第二位或以後死亡的玩家。";
	}
}
