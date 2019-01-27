package net.leo.message.server.skill;

import net.leo.message.server.event.GameEvent;
import net.leo.message.server.event.UsageStartEvent;
import net.leo.message.server.game.Game;
import net.leo.message.server.game.Player;
import net.leo.message.server.stack.Executable;

public abstract class ExecutableSkill implements Skill, Executable {

	@Override
	public final int inform(Game game, Player me, GameEvent e) {
		if (e instanceof UsageStartEvent) {
			return 1;
		}
		return 0;
	}

	@Override
	public final boolean isEffective() {
		//Always effective
		return true;
	}

	@Override
	public final boolean isMadantory() {
		return false;
	}

	@Override
	public final boolean isRed() {
		//Always "Blue skill"
		return false;
	}

	@Override
	public final boolean isSurroundingIdeal(Game game, Player me, GameEvent e) {
		return true;
	}

	@Override
	public final void setEffective(boolean effective) {
		//Do nothing
	}
}
