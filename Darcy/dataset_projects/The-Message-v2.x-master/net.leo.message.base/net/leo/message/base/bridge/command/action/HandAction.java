package net.leo.message.base.bridge.command.action;

import java.util.List;
import net.leo.message.base.lang.Card;

public class HandAction extends BasicAction<Card> {

	public HandAction(int min, int max, List<Card> candidates, boolean enforcement, String inst, Action next) {
		super(min, max, candidates, enforcement, inst, next);
	}

}
