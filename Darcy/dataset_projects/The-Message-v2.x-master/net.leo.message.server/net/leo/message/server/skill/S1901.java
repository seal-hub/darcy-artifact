package net.leo.message.server.skill;

import net.leo.message.base.bridge.command.action.Action;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.server.event.GameEvent;
import net.leo.message.server.event.UsageStartEvent;
import net.leo.message.server.game.Game;
import net.leo.message.server.game.Player;
import net.leo.message.server.game.Target;

/**
 * 彈藥無限
 */
public class S1901 extends InsertedSkill {

	@Override
	public String description() {
		return "若你沒有手牌，你可以翻開此角色牌，並抽一張牌。";
	}

	@Override
	public String getName() {
		return "彈藥無限";
	}

	@Override
	public Target getTarget(Game game, Player me, Decision<?> decision, GameEvent e) {
		return null;
	}

	@Override
	public int inform(Game game, Player me, GameEvent e) {
		if (e instanceof UsageStartEvent) {
			return me.getHands().isEmpty() ? 1 : 0;
		}
		return 0;
	}

	@Override
	public void run(Game game, Player me, Decision<?> decision, GameEvent e) {
		game.operate().handDrawn(me, 1);
	}

	@Override
	public boolean isMadantory() {
		return false;
	}

	@Override
	public boolean isSurroundingIdeal(Game game, Player me, GameEvent e) {
		return true;
	}

	@Override
	public int mode() {
		return OPENING;
	}

	@Override
	public Action prepare(Game game, Player me, GameEvent e) {
		return null;
	}
}
