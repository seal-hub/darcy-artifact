package net.leo.message.base.lang;

public enum CardFunction {

	RETURN("退回"),
	DISTRIBUTE("真偽莫辨"),
	LOCK_ON("鎖定"),
	TRAP("調虎離山"),
	DECODE("破譯"),
	PROVE("試探"),
	BURN("燒燬"),
	INTERCEPT("截獲"),
	COUNTERACT("識破");


	private final String name;

	CardFunction(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getSimpleName() {
		return name.substring(0, 2);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + name + "]";
	}

}
