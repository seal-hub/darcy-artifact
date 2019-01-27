package net.leo.message.base.bridge.command.data;

import java.io.Serializable;

public class SkillData implements Serializable {

	public String name;
	public boolean passive;
	public String description;

	public SkillData(String name, boolean passive, String description) {
		if (name == null || description == null) {
			throw new NullPointerException();
		}
		this.name = name;
		this.passive = passive;
		this.description = description;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof SkillData) {
			return name.equals(((SkillData) o).name);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + (passive ? "被動技" : "主動技") + "[" + name + "]";
	}
}
