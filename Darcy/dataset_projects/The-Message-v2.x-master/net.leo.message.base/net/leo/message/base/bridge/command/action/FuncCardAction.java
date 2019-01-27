package net.leo.message.base.bridge.command.action;

import java.util.List;

public class FuncCardAction extends BasicAction<Integer> {

	public FuncCardAction(int min, int max, List<Integer> candidates, boolean enforcement, String inst, Action next) {
		super(min, max, candidates, enforcement, inst, next);
	}
}
