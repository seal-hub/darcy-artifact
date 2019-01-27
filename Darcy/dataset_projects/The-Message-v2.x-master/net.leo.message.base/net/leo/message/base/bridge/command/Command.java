package net.leo.message.base.bridge.command;

import net.leo.message.base.bridge.Message;

public abstract class Command extends Message {

	public final String title;
	public final String type;

	public Command(String type, String title) {
		super();
		if (type == null || title == null) {
			throw new NullPointerException();
		}
		this.title = title;
		this.type = type;
	}
}
