package net.leo.message.server.skill;

import java.util.List;
import java.util.Random;
import net.leo.message.base.bridge.command.action.Action;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.base.lang.Card;
import net.leo.message.base.lang.CardFunction;
import net.leo.message.server.event.CardInvokingEvent;
import net.leo.message.server.event.GameEvent;
import net.leo.message.server.game.Game;
import net.leo.message.server.game.Player;
import net.leo.message.server.game.Target;

/**
 * 拷問
 */
public class S1401 extends InsertedSkill {

	public S1401() {
	}

	@Override
	public String description() {
		return "當你試探一位玩家時，你可以抽取他一張手牌。";
	}

	@Override
	public String getName() {
		return "拷問";
	}

	@Override
	public Target getTarget(Game game, Player me, Decision<?> decision, GameEvent e) {
		Player target = (Player) ((CardInvokingEvent) e).target;
		return target;
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
	public boolean isMadantory() {
		return false;
	}

	@Override
	public boolean isSurroundingIdeal(Game game, Player me, GameEvent e) {
		return !((Player) ((CardInvokingEvent) e).target).getHands().isEmpty();
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
		Player target = (Player) ((CardInvokingEvent) e).target;
		List<Card> handList = target.getHands();
		Card rand = handList.get(new Random().nextInt(handList.size()));

		game.operate().handTransfer(rand, target, me);
	}
}
