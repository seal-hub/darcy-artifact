package net.leo.message.server.skill;

import net.leo.message.base.bridge.command.action.Action;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.base.lang.CardColor;
import net.leo.message.server.event.GameEvent;
import net.leo.message.server.event.IntelObtainedEvent;
import net.leo.message.server.game.Game;
import net.leo.message.server.game.Player;
import net.leo.message.server.game.Target;

/**
 * 運籌帷幄
 */
public class S1702 extends InsertedSkill {

	@Override
	public String description() {
		return "當你獲得一張真情報時，你抽一張牌。";
	}

	@Override
	public String getName() {
		return "運籌帷幄";
	}

	@Override
	public Target getTarget(Game game, Player me, Decision<?> decision, GameEvent e) {
		return null;
	}

	@Override
	public int inform(Game game, Player me, GameEvent e) {
		if (e instanceof IntelObtainedEvent) {
			IntelObtainedEvent event = (IntelObtainedEvent) e;
			if (event.getObtainer() == me) {
				return (int) event.getIntelligences()
				                  .stream()
				                  .filter(c -> c.getColor() != CardColor.BLACK)
				                  .count();
			}
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
		return ORDINARY;
	}

	@Override
	public Action prepare(Game game, Player me, GameEvent e) {
		return null;
	}
}
