package net.leo.message.base.bridge.command.select;

import java.util.Objects;
import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.bridge.command.action.Action;
import net.leo.message.base.bridge.reply.BasicReply;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.base.bridge.reply.NullReply;
import net.leo.message.base.bridge.reply.Reply;

public class BasicSelectionCommand extends Command implements Selection {

	public final int time;
	public final Action action;
	public final int id;

	public BasicSelectionCommand(int time, Action action, int id) {
		super("選擇", "一般選擇");
		if (time < 0) {
			throw new IllegalArgumentException();
		}
		this.time = time;
		this.action = Objects.requireNonNull(action);
		this.id = id;
	}

	@Override
	public Reply getDefaultReply() {
		if (!action.isMadantory()) {
			return new NullReply(id);
		}
		return new BasicReply<>(id, action.getDefaultDecision());
	}

	@Override
	public int getMessageId() {
		return id;
	}

	@Override
	public boolean isMadantory() {
		return action.isMadantory();
	}

	@Override
	public boolean isReplyValid(Reply reply) {
		if (reply instanceof NullReply) {
			return !action.isMadantory() && id == reply.id;
		}

		if (reply instanceof BasicReply && ((BasicReply) reply).value instanceof Decision) {
			return action.isDecisionLegal(((BasicReply<Decision<?>>) reply).value);
		}

		return false;
	}
}
