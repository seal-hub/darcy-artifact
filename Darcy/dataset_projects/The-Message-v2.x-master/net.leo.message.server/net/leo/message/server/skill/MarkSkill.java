package net.leo.message.server.skill;

import net.leo.message.base.bridge.command.action.Action;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.server.event.GameEvent;
import net.leo.message.server.game.Game;
import net.leo.message.server.game.Player;

/**
 * @author Parabola
 */
public abstract class MarkSkill implements Skill {

	@Override
	public final boolean isRed(){
		return true;
	}

	@Override
	public final int inform(Game game, Player me, GameEvent e) {
		return 0;
	}

	@Override
	public final void run(Game game, Player me, Decision<?> decision, GameEvent e) {

	}

	@Override
	public final boolean isMadantory() {
		return false;
	}

	@Override
	public final boolean isSurroundingIdeal(Game game, Player me, GameEvent e) {
		return false;
	}

	@Override
	public final int mode() {
		return ORDINARY;
	}

	@Override
	public final Action prepare(Game game, Player me, GameEvent e) {
		return null;
	}
}
