package net.leo.message.base.bridge.command.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import net.leo.message.base.bridge.reply.Decision;

public abstract class BasicAction<T> implements Action {

	public static void requiresLegalRange(int min, int max, Collection<?> candidates, boolean enforcement) {
		if (min < 0 || min > max || min == 0 && max == 0 || enforcement && candidates.size() < min) {
			throw new IllegalArgumentException();
		}
	}

	public final List<? extends T> candidates;
	public final Action nextAction;
	public final String inst;
	public final int min, max;
	public final boolean enforcement;

	public BasicAction(int min, int max, List<? extends T> candidates, boolean enforcement, String inst, Action nextAction) {
		requiresLegalRange(min, max, candidates, enforcement);
		if (nextAction != null && enforcement != nextAction.isMadantory()) {
			throw new IllegalArgumentException();
		}

		this.candidates = new ArrayList<>(candidates);
		this.nextAction = nextAction;
		this.inst = Objects.requireNonNull(inst);
		this.min = min;
		this.max = max;
		this.enforcement = enforcement;
	}

	@Override
	public final Decision<?> getDefaultDecision() {
		List<T> list = new ArrayList<>(min);
		int n = 0;
		for (T t : candidates) {
			list.add(t);
			if (++n >= min) {
				break;
			}
		}
		return new Decision<>(list, nextAction == null ? null : nextAction.getDefaultDecision());
	}

	@Override
	public final boolean isDecisionLegal(Decision<?> decision) {
		if(decision==null){
			return false;
		}

		List<?> list = decision.contributes;
		if (list == null) {
			return false;
		}

		int size = list.size();
		if (size < min || size > max) {
			return false;
		}

		return candidates.containsAll(list)
				&& (nextAction == null || nextAction.isDecisionLegal(decision.nextDecision));
	}

	@Override
	public final boolean isMadantory() {
		return enforcement;
	}

	@Override
	public final Action nextAction() {
		return nextAction;
	}

	@Override
	public String toString() {
		return getClass().getName() + "[Range=" + min + "~" + max + ", " + (enforcement ? "強制" : "非強制") + ", Candidates=" + candidates + ", next=" + nextAction + "]";
	}
}
