package net.leo.message.base.lang;

public enum IntelligenceType {

	EXPRESS("直達"),
	SECRET("密電"),
	DOCUMENT("文本");

	private final String name;

	IntelligenceType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + name + "]";
	}

}
