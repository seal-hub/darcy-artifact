package net.leo.message.base.bridge.command.action;

import java.util.List;
import java.util.Objects;
import net.leo.message.base.lang.Card;

public class OrderedCardrAction extends BasicAction<Card> {

	public final String inst2;

	public OrderedCardrAction(int min, int max, List<? extends Card> candidates, boolean enforcement, String inst, String inst2, Action nextAction) {
		super(min, max, candidates, enforcement, inst, nextAction);
		this.inst2 = Objects.requireNonNull(inst2);
	}
}
