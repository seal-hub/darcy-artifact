package net.leo.message.server.character;

import java.util.List;
import net.leo.message.base.lang.Character;
import net.leo.message.server.skill.S2501;
import net.leo.message.server.skill.Skill;

public class C25 extends CharacterCard{

	private Skill s1 = new S2501();

	public C25(){
		super(Character.C25, true);
	}

	@Override
	public List<Skill> getSkills() {
		return List.of(s1);
	}

	@Override
	public String missionDescription() {
		return null;
	}
}
