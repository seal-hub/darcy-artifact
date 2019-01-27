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
import net.leo.message.server.stack.ExecutableCard;

/**
 * 棄卒保帥
 */
public class S0301 extends InsertedSkill {

	public S0301() {
	}

	@Override
	public String description() {
		return "當你的卡牌被識破時，你可以翻開此角色牌，抽五張牌，然後將兩張手牌放回牌庫頂。";
	}

	@Override
	public int inform(Game game, Player me, GameEvent e) {
		if (e instanceof CardInvokingEvent) {
			CardInvokingEvent event = (CardInvokingEvent) e;
			if (event.func == CardFunction.COUNTERACT && ((ExecutableCard) event.target).user == me) {
				return 1;
			}
		}
		return 0;
	}

	@Override
	public String getName() {
		return "棄卒保帥";
	}

	@Override
	public Target getTarget(Game game, Player me, Decision<?> decision, GameEvent e) {
		return null;
	}

	@Override
	public void run(Game game, Player me, Decision<?> decision, GameEvent e) {
		game.operate().handDrawn(me, 5);
		int nBack = Math.min(me.getHands().size(), 2);
		if (nBack == 0) {
			return;
		}
		List<Card> removed = game.operate().selectCardsByOrder(me,
		                                                       nBack,
		                                                       nBack,
		                                                       true,
		                                                       "請將 " + nBack + " 張手牌放回牌庫頂。",
		                                                       "左側的牌會放在牌庫頂端。",
		                                                       "請等候玩家 " + me.getSeat() + " 操作。",
		                                                       me.getHands());
		game.operate().handRecycled(me, removed);
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
