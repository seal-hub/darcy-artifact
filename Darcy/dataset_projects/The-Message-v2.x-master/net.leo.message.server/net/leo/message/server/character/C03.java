package net.leo.message.server.character;

import java.util.List;
import net.leo.message.base.lang.Character;
import net.leo.message.server.skill.S0301;
import net.leo.message.server.skill.Skill;

/**
 * 老金
 */
public class C03 extends CharacterCard {

	private Skill s01 = new S0301();

	C03() {
		super(Character.C03, false);
	}

	@Override
	public List<Skill> getSkills() {
		return List.of(s01);
	}

	@Override
	public String missionDescription() {
		return "手牌集齊三張紅色卡牌和三張藍色卡牌。";
	}
}
