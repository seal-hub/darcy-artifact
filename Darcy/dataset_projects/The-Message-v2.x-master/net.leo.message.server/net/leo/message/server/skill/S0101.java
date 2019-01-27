package net.leo.message.server.skill;

import net.leo.message.base.bridge.command.action.Action;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.base.lang.CardFunction;
import net.leo.message.server.event.CardInvokingEvent;
import net.leo.message.server.event.GameEvent;
import net.leo.message.server.game.Game;
import net.leo.message.server.game.Player;
import net.leo.message.server.game.Target;

/**
 * 就計
 */
public class S0101 extends InsertedSkill {

	@Override
	public String description() {
		return "當你被試探或鎖定時，你可以翻開此角色牌，並抽兩張牌。";
	}

	@Override
	public String getName() {
		return "就計";
	}

	@Override
	public Target getTarget(Game game, Player me, Decision<?> decision, GameEvent e) {
		return null;
	}

	@Override
	public int inform(Game game, Player me, GameEvent e) {
		if (e instanceof CardInvokingEvent) {
			CardInvokingEvent event = (CardInvokingEvent) e;
			if (event.target == me &&
					(event.func == CardFunction.LOCK_ON || event.func == CardFunction.PROVE)) {
				return 1;
			}
		}
		return 0;
	}

	@Override
	public void run(Game game, Player me, Decision<?> decision, GameEvent e) {
		game.operate().handDrawn(me, 2);
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
