package net.leo.message.server.character;

import java.util.List;
import net.leo.message.base.lang.Character;
import net.leo.message.server.skill.S0101;
import net.leo.message.server.skill.S0102;
import net.leo.message.server.skill.Skill;

/**
 * 老鬼
 */
public class C01 extends CharacterCard {

	private Skill s01 = new S0101(), s02 = new S0102();

	protected C01() {
		super(Character.C01, false);
	}

	@Override
	public List<Skill> getSkills() {
		return List.of(s01, s02);
	}

	@Override
	public String missionDescription() {
		return "當妳死亡時，展示妳的手牌，其中有三張或以上的紅色卡牌。";
	}
}
