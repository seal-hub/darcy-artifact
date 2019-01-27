package net.leo.message.server.skill;

import net.leo.message.base.bridge.command.action.Action;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.server.event.GameEvent;
import net.leo.message.server.event.ProveInvokingEvent;
import net.leo.message.server.game.Game;
import net.leo.message.server.game.Player;
import net.leo.message.server.game.Target;

public class S0102 extends InsertedSkill {

	@Override
	public String description() {
		return "你被試探時始終隱瞞身分。";
	}

	@Override
	public String getName() {
		return "城府";
	}

	@Override
	public Target getTarget(Game game, Player me, Decision<?> decision, GameEvent e) {
		return null;
	}

	@Override
	public int inform(Game game, Player me, GameEvent e) {
		if (e instanceof ProveInvokingEvent) {
			ProveInvokingEvent event = (ProveInvokingEvent) e;
			if (event.target == me) {
				return 1;
			}
		}
		return 0;
	}

	@Override
	public boolean isMadantory() {
		return true;
	}

	@Override
	public boolean isSurroundingIdeal(Game game, Player me, GameEvent e) {
		return true;
	}

	@Override
	public int mode() {
		return ORDINARY;
	}

	@Override
	public Action prepare(Game game, Player me, GameEvent e) {
		return null;
	}

	@Override
	public void run(Game game, Player me, Decision<?> decision, GameEvent e) {
		ProveInvokingEvent event = (ProveInvokingEvent) e;
		event.denial = true;
		event.admission = true;
	}
}
