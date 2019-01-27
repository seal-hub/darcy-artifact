package net.leo.message.base.lang;

public enum CardColor {

	RED("紅"),
	BLUE("藍"),
	BLACK("黑");

	private final String name;

	CardColor(String name) {
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
