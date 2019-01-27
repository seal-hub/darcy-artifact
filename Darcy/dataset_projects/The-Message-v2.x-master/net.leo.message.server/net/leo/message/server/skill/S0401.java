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
 * 洞悉
 */
public class S0401 extends InsertedSkill {

	@Override
	public String description() {
		return "當情報被" + CardFunction.DECODE.getName() + "時，你可以蓋伏此角色牌。";
	}

	@Override
	public String getName() {
		return "洞悉";
	}

	@Override
	public Target getTarget(Game game, Player me, Decision<?> decision, GameEvent e) {
		return null;
	}

	@Override
	public int inform(Game game, Player me, GameEvent e) {
		if (e instanceof CardInvokingEvent) {
			if (((CardInvokingEvent) e).func == CardFunction.DECODE) {
				return 1;
			}
		}
		return 0;
	}

	@Override
	public void run(Game game, Player me, Decision<?> decision, GameEvent e) {
		game.operate().charCovered(me);
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
		return COVERING;
	}

	@Override
	public Action prepare(Game game, Player me, GameEvent e) {
		return null;
	}
}
