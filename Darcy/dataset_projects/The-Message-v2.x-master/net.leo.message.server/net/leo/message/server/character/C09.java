package net.leo.message.server.character;

import java.util.List;
import net.leo.message.base.lang.Character;
import net.leo.message.server.skill.S0901;
import net.leo.message.server.skill.Skill;

/**
 * 峨嵋
 */
public class C09 extends CharacterCard {

	private Skill s1 = new S0901();

	public C09() {
		super(Character.C09, false);
	}

	@Override
	public List<Skill> getSkills() {
		return List.of(s1);
	}

	@Override
	public String missionDescription() {
		return "獲得三張或以上的藍色情報。";
	}
}
