package net.leo.message.base.bridge.command.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import net.leo.message.base.lang.Character;

public class CharData implements Serializable {

	public final Character character;
	public final List<SkillData> skillData = new ArrayList<>(4);
	public String mission = null;

	public CharData(Character character) {
		if (character == null) {
			throw new NullPointerException();
		}
		this.character = character;
	}

	public void addSkillInfo(SkillData info) {
		skillData.add(info);
	}

	public void setMission(String mission) {
		this.mission = mission;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[Name=" + character + ", Skills=" + skillData + (mission == null ? "" : ", Mission=" + mission) + "]";
	}
}
