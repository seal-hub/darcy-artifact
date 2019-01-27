package net.leo.message.base.bridge.command.select;

import java.util.Collections;
import java.util.List;
import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.bridge.command.data.CharData;
import net.leo.message.base.bridge.reply.BasicReply;
import net.leo.message.base.bridge.reply.Reply;

public class CharSelectionCommand extends Command implements Selection {

	public final List<? extends CharData> charData;
	public final int time;
	public final int id;

	public CharSelectionCommand(int time, Reply defaultReply, List<? extends CharData> info, int id) {
		super("初始", "選擇角色");
		if (defaultReply == null) {
			throw new NullPointerException();
		}
		if (time < 0 || info.isEmpty()) {
			throw new IllegalArgumentException();
		}
		this.time = time;
		this.charData = Collections.unmodifiableList(info);
		this.id = id;
	}

	@Override
	public Reply getDefaultReply() {
		return new BasicReply<>(id, charData.get(0).character.getName());
	}

	@Override
	public int getMessageId() {
		return id;
	}

	@Override
	public boolean isMadantory() {
		return true;
	}

	@Override
	public boolean isReplyValid(Reply reply) {
		if (reply instanceof BasicReply) {
			Object value = ((BasicReply) reply).value;
			return charData.stream().anyMatch(datum -> datum.character.getName().equals(value));
		}
		return false;
	}
}
