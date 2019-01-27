package net.leo.message.server.character;

import java.util.ArrayList;
import java.util.List;
import net.leo.message.base.lang.Character;
import net.leo.message.server.skill.S2301;
import net.leo.message.server.skill.S2302;
import net.leo.message.server.skill.Skill;

/**
 * 99
 */
public class C23 extends CharacterCard {

	private Skill s1 = new S2301();
	private Skill s2 = new S2302();

	C23() {
		super(Character.C23, true);
	}

	@Override
	public List<Skill> getSkills() {
		return new ArrayList<>(List.of(s1, s2));
	}

	@Override
	public String missionDescription() {
		return "集齊九張或以上的手牌。";
	}
}
