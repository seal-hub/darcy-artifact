package net.leo.message.base.bridge.reply;

import net.leo.message.base.bridge.Message;

public abstract class Reply extends Message {

	public final int id;

	public Reply(int id) {
		super();
		this.id = id;
	}
}
