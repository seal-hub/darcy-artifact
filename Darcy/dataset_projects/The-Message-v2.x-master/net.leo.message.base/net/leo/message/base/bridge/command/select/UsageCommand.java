package net.leo.message.base.bridge.command.select;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.bridge.command.action.Action;
import net.leo.message.base.bridge.reply.BasicReply;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.base.bridge.reply.NullReply;
import net.leo.message.base.bridge.reply.Reply;

public class UsageCommand extends Command implements Selection {

	public static final UsageCommand STOP = new UsageCommand();

	public final Map<?, ? extends Action> candidates;
	public final Set<Integer> participants;
	public final int time;
	public final int id;

	public UsageCommand(Map<?, ? extends Action> candidates, Set<Integer> participants, int time, int id) {
		super("用牌階段", "出牌");
		if (participants.isEmpty() || time < 0) {
			throw new IllegalArgumentException();
		}
		this.candidates = new HashMap<>(candidates);
		this.participants = participants;
		this.time = time;
		this.id = id;
	}

	public UsageCommand(Set<Integer> participants, int time) {
		super("用牌階段", "旁觀");
		if (participants.isEmpty() || time < 0) {
			throw new IllegalArgumentException();
		}
		this.candidates = null;
		this.participants = participants;
		this.time = time;
		this.id = -1;
	}

	private UsageCommand() {
		super("用牌階段", "終止");
		this.candidates = null;
		this.participants = null;
		this.time = -1;
		this.id = -1;
	}

	@Override
	public Reply getDefaultReply() {
		return new NullReply(id);
	}

	@Override
	public int getMessageId() {
		return id;
	}

	@Override
	public boolean isMadantory() {
		return false;
	}

	@Override
	public boolean isReplyValid(Reply reply) {
		if (reply instanceof NullReply) {
			return id == reply.id;
		}

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
