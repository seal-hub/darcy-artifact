package net.leo.message.base.bridge.command.action;

import java.util.Objects;
import net.leo.message.base.bridge.reply.Decision;

public class SkillAction implements Action {

	public final String skillName;
	public final Action nextAction;

	public SkillAction(String skillName, Action nextAction) {
		this.skillName = Objects.requireNonNull(skillName);
		this.nextAction = nextAction;
	}

	@Override
	public Decision<?> getDefaultDecision() {
		return new Decision<>(skillName, nextAction == null ? null : nextAction.getDefaultDecision());
	}

	@Override
	public boolean isDecisionLegal(Decision<?> decision) {
		if (decision == null) {
			return false;
		}

		if (decision.contributes.size() == 1) {
			if (skillName.equals(decision.contributes.get(0))) {
				return nextAction == null ? decision.nextDecision == null : nextAction.isDecisionLegal(decision.nextDecision);
			}
		}
		return false;
	}

	@Override
	public boolean isMadantory() {
		return false;
	}

	@Override
	public Action nextAction() {
		return nextAction;
	}
}
