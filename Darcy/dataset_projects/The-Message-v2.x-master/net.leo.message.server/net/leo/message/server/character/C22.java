package net.leo.message.server.character;

import java.util.List;
import net.leo.message.base.lang.Character;
import net.leo.message.server.skill.S2201;
import net.leo.message.server.skill.Skill;

/**
 * 禮服
 */
public class C22 extends CharacterCard {

	private Skill s1 = new S2201();

	public C22() {
		super(Character.C22, true);
	}

	@Override
	public List<Skill> getSkills() {
		return List.of(s1);
	}

	@Override
	public String missionDescription() {
		return "一位女性宣告勝利。";
	}
}
