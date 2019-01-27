package net.leo.message.server.character;

import java.util.List;
import net.leo.message.base.lang.Character;
import net.leo.message.server.skill.S2001;
import net.leo.message.server.skill.S2002;
import net.leo.message.server.skill.Skill;

/**
 * 帽子
 */
public class C20 extends CharacterCard {

	private Skill s1 = new S2001(), s2 = new S2002();

	protected C20() {
		super(Character.C20, true);
	}

	@Override
	public List<Skill> getSkills() {
		return List.of(s1, s2);
	}

	@Override
	public String missionDescription() {
		return "手牌集齊三張紅色卡牌和藍色卡牌。";
	}
}
