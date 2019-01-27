package net.leo.message.server.character;

import java.util.List;
import net.leo.message.base.lang.Character;
import net.leo.message.server.skill.S1901;
import net.leo.message.server.skill.S1902;
import net.leo.message.server.skill.Skill;

public class C19 extends CharacterCard {

	private Skill s1 = new S1901(), s2 = new S1902();

	public C19() {
		super(Character.C19, true);
	}

	@Override
	public List<Skill> getSkills() {
		return List.of(s1, s2);
	}

	@Override
	public String missionDescription() {
		return "當一位玩家宣告勝利時，沒有玩家死亡。你的勝利會導致他的宣告失敗。";
	}
}
