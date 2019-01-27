package net.leo.message.base.bridge.command.action;

import java.util.List;
import net.leo.message.base.lang.Card;

public class CardAction extends BasicAction<Card> {

	public CardAction(int min, int max, List<? extends Card> candidates, boolean enforcement, String inst, Action nextAction) {
		super(min, max, candidates, enforcement, inst, nextAction);
	}
}
