package net.leo.message.server.skill;

import java.util.List;
import net.leo.message.base.bridge.command.action.Action;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.base.lang.Card;
import net.leo.message.base.lang.CardFunction;
import net.leo.message.server.game.Game;
import net.leo.message.server.game.Player;
import net.leo.message.server.event.CardInvokingEvent;
import net.leo.message.server.event.GameEvent;
import net.leo.message.server.game.Target;

/**
 * 隔牆有耳
 */
public class S1101 extends InsertedSkill {

	@Override
	public String description() {
		return "當你使用試探時，你可以抽兩張牌，然後將一張手牌放回牌庫頂。";
	}

	@Override
	public String getName() {
		return "隔牆有耳";
	}

	@Override
	public Target getTarget(Game game, Player me, Decision<?> decision, GameEvent e) {
		return null;
	}

	@Override
	public void run(Game game, Player me, Decision<?> decision, GameEvent e) {
		game.operate().handDrawn(me, 2);
		if (me.getHands().isEmpty()) {
			return;
		}

		Card card = game.operate().selectHand(me, true, "將一張手牌放回牌庫頂。", "等待玩家 " + me.getSeat() + " 操作。", me.getHands());
		game.operate().handRecycled(me, List.of(card));
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
	public int inform(Game game, Player me, GameEvent e) {
		if (e instanceof CardInvokingEvent) {
			CardInvokingEvent event = (CardInvokingEvent) e;
			if (event.func == CardFunction.PROVE && event.user == me) {
				return 1;
			}
		}
		return 0;
	}


	@Override
	public Action prepare(Game game, Player me, GameEvent e) {
		return null;
	}
}
