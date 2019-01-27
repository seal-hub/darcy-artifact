package net.leo.message.server.skill;

import net.leo.message.base.bridge.command.action.Action;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.base.lang.CardFunction;
import net.leo.message.server.event.GameEvent;
import net.leo.message.server.event.UsageStartEvent;
import net.leo.message.server.game.Game;
import net.leo.message.server.game.Player;
import net.leo.message.server.game.Target;
import net.leo.message.server.game.Usage;

/**
 * 聲東擊西
 */
public class S1902 extends InsertedSkill {

	@Override
	public String description() {
		return "你可以蓋伏此角色牌，視為使用調虎離山。";
	}

	@Override
	public String getName() {
		return "聲東擊西";
	}

	@Override
	public Target getTarget(Game game, Player me, Decision<?> decision, GameEvent e) {
		Player target = game.getPlayer((Integer) decision.contributes.get(0));
		return target;
	}

	@Override
	public int inform(Game game, Player me, GameEvent e) {
		if (e instanceof UsageStartEvent) {
			return Usage.isCardUsable(CardFunction.TRAP, me, game) ? 1 : 0;
		}
		return 0;
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
		return Usage.getActionOfFunction(me, CardFunction.TRAP, game);
	}

	@Override
	public void run(Game game, Player me, Decision<?> decision, GameEvent e) {
		Player target = game.getPlayer((Integer) decision.contributes.get(0));
		game.operate().triggerTrap(null, me, target);
	}
}
