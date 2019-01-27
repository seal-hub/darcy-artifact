package net.leo.message.base.bridge.command.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.leo.message.base.bridge.reply.Decision;

public class ButtonAction implements Action {

	public final List<String> candidates;
	public final Action nextAction;
	public final String inst;

	public ButtonAction(List<String> candidates, String inst, Action nextAction) {
		if (candidates.stream().allMatch(c -> c.endsWith("X"))) {
			throw new IllegalArgumentException();
		}
		if (nextAction != null && !nextAction.isMadantory()) {
			throw new IllegalArgumentException();
		}

		this.candidates = new ArrayList<>(candidates);
		this.inst = Objects.requireNonNull(inst);
		this.nextAction = nextAction;
	}

	@Override
	public Decision<?> getDefaultDecision() {
		int i = 0;
		for (String opt : candidates) {
			if (!opt.endsWith("X")) {
				return new Decision<>(i, nextAction == null ? null : nextAction.getDefaultDecision());
			}
			i++;
		}
		return null; // Never happens
	}

	@Override
	public boolean isDecisionLegal(Decision<?> decision) {
		if (decision == null) {
			return false;
		}

		List<?> list = decision.contributes;
		if (list == null || list.size() != 1 || !Integer.class.isInstance(list.get(0))) {
			return false;
		}
		int i = (Integer) list.get(0);
		return !candidates.get(i).endsWith("X")
				&& (nextAction == null || nextAction.isDecisionLegal(decision.nextDecision));
	}

	@Override
	public boolean isMadantory() {
		return true;
	}

	@Override
	public Action nextAction() {
		return null;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder(getClass().getSimpleName()).append("[");
		boolean first = true;

		for (String text : candidates) {
			if (first) {
				first = false;
			}
			else {
				str.append(", ");
			}

			if (text.endsWith("X")) {
				str.append(text.substring(0, text.length() - 1)).append("(不可)");
			}
			else {
				str.append(text).append("(可)");
			}
		}

		str.append("]");
		return str.toString();
	}
}
