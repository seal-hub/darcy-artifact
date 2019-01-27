package net.leo.message.base.bridge.command.select;

import java.util.Map;
import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.bridge.command.action.Action;
import net.leo.message.base.bridge.reply.BasicReply;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.base.bridge.reply.NullReply;
import net.leo.message.base.bridge.reply.Reply;

public class PsSkillSelctionCommand extends Command implements Selection {

	public final int time;
	public final Map<String, ? extends Action> candidates;
	public final int id;
	public final String enforcement;

	public PsSkillSelctionCommand(int time, Map<String, ? extends Action> candidates, String enforcement, int id) {
		super("選擇", "被動技能");
		if (time < 0 || candidates.isEmpty()) {
			throw new IllegalArgumentException();
		}
		this.time = time;
		this.candidates = candidates;
		this.id = id;
		this.enforcement = enforcement;
	}

	@Override
	public Reply getDefaultReply() {
		if (enforcement == null) {
			return new NullReply(id);
		}

		Action dfAction = candidates.get(enforcement);
		Decision dfDecs = new Decision(enforcement, dfAction == null ? null : dfAction.getDefaultDecision());
		return new BasicReply<>(id, dfDecs);
	}

	@Override
	public int getMessageId() {
		return id;
	}

	@Override
	public boolean isMadantory() {
		return enforcement != null;
	}

	@Override
	public boolean isReplyValid(Reply reply) {
		if (reply instanceof NullReply) {
			return !isMadantory();
		}

		if (reply instanceof BasicReply && ((BasicReply) reply).value instanceof Decision) {
			Decision decs = (Decision) ((BasicReply) reply).value;
			if (decs.contributes.size() != 1 || !(decs.contributes.get(0) instanceof String)) {
				return false;
			}
			String skillName = (String) decs.contributes.get(0);
			Action action = candidates.get(skillName);
			if (action == null) {
				return decs.nextDecision == null;
			}
			return action.isDecisionLegal(decs.nextDecision);
		}
		return false;
	}
}
