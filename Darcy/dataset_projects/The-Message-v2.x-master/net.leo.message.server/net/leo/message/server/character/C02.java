package net.leo.message.server.character;

import java.util.List;
import net.leo.message.base.lang.Character;
import net.leo.message.server.skill.S0101;
import net.leo.message.server.skill.Skill;

/**
 * 老槍
 */
public class C02 extends CharacterCard {

	private Skill s1 = new S0101();

	protected C02() {
		super(Character.C02, false);
	}

	@Override
	public List<Skill> getSkills() {
		return List.of(s1);
	}

	@Override
	public String missionDescription() {
		return "獲得三張或以上的紅色情報";
	}
}
