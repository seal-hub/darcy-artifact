package net.leo.message.server.character;

import java.util.List;
import net.leo.message.base.lang.Character;
import net.leo.message.server.skill.S1601;
import net.leo.message.server.skill.Skill;

/**
 * 小馬哥
 */
public class C16 extends CharacterCard {

	private Skill s1 = new S1601();

	public C16() {
		super(Character.C16, true);
	}

	@Override
	public List<Skill> getSkills() {
		return List.of(s1);
	}

	@Override
	public String missionDescription() {
		return "一位潛伏戰線玩家和一位軍情處玩家死亡。";
	}
}
