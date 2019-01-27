package net.leo.message.base.bridge.command.action;

import java.util.List;

public class IntelAction extends BasicAction<IntelLocation> {

	public IntelAction(int min, int max, List<? extends IntelLocation> candidates, boolean enforcement, String inst, Action next) {
		super(min, max, candidates, enforcement, inst, next);
	}

}
