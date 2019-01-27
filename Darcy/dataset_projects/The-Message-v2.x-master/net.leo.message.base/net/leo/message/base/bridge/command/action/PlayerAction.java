package net.leo.message.base.bridge.command.action;

import java.util.List;

public class PlayerAction extends BasicAction<Integer> {

	public PlayerAction(int min, int max, List<Integer> candidates, boolean enforcement, String inst, Action next) {
		super(min, max, candidates, enforcement, inst, next);
	}
}
