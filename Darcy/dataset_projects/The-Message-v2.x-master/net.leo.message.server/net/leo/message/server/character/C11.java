package net.leo.message.server.character;

import java.util.ArrayList;
import java.util.List;
import net.leo.message.base.lang.Character;
import net.leo.message.server.skill.S1101;
import net.leo.message.server.skill.Skill;

/**
 * 處長
 */
public class C11 extends CharacterCard {

	private Skill s1 = new S1101();

	C11() {
		super(Character.C11, true);
	}

	@Override
	public List<Skill> getSkills() {
		return new ArrayList<>(List.of(s1));
	}

	@Override
	public String missionDescription() {
		return "獲得三張或以上的紅色情報。";
	}
}
