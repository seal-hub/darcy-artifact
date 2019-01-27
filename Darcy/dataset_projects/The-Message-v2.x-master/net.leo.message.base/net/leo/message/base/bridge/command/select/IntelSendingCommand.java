package net.leo.message.base.bridge.command.select;

import java.util.HashMap;
import java.util.Map;
import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.bridge.command.action.Action;
import net.leo.message.base.bridge.reply.BasicReply;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.base.bridge.reply.Reply;

public class IntelSendingCommand extends Command implements Selection {

	public final Map<Object, Action> candidates;
	public final int time;
	public final int id;

	public IntelSendingCommand(Map<?, ? extends Action> candidates, int time, int id) {
		super("選擇", "傳遞情報");
		if (candidates.isEmpty()) {
			throw new IllegalArgumentException();
		}
		if (time < 0) {
			throw new IllegalArgumentException();
		}
		this.candidates = new HashMap<>(candidates);
		this.time = time;
		this.id = id;
	}

	@Override
	public Reply getDefaultReply() {
		Object firstKey = null;
		for (Object key : candidates.keySet()) {
			firstKey = key;
			break;
		}
		Action action = candidates.get(firstKey);
		return new BasicReply<>(id, new Decision(firstKey, action == null ? null : action.getDefaultDecision()));
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
		if (reply instanceof BasicReply && ((BasicReply) reply).value instanceof Decision) {
			Decision decs = (Decision) ((BasicReply) reply).value;
			if (decs.contributes.size() != 1) {
				return false;
			}
			Object first = decs.contributes.get(0);
			if (!candidates.containsKey(first)) {
				return false;
			}
			Action action = candidates.get(first);
			if (action == null) {
				return decs.nextDecision == null;
			}
			return action.isDecisionLegal(decs.nextDecision);
		}
		return false;
	}
}
