package net.leo.message.server.skill;

import java.util.List;
import static java.util.stream.Collectors.toList;
import net.leo.message.base.bridge.command.action.Action;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.base.lang.Card;
import net.leo.message.base.lang.CardColor;
import net.leo.message.server.event.GameEvent;
import net.leo.message.server.event.IntelReceviedEvent;
import net.leo.message.server.game.Game;
import net.leo.message.server.game.Player;
import net.leo.message.server.game.Target;

/**
 * 連擊
 */
public class S1501 extends InsertedSkill {

	@Override
	public String description() {
		return "當另一位玩家獲得妳傳出的假情報時，你抽一張牌，然後可以在他面前再放置一張假情報。";
	}

	@Override
	public String getName() {
		return "連擊";
	}

	@Override
	public Target getTarget(Game game, Player me, Decision<?> decision, GameEvent e) {
		Player target = ((IntelReceviedEvent) e).getObtainer();
		return target;
	}

	@Override
	public int inform(Game game, Player me, GameEvent e) {
		if (e instanceof IntelReceviedEvent) {
			return ((IntelReceviedEvent) e).getOrigin() == me ? 1 : 0;
		}
		return 0;
	}

	@Override
	public void run(Game game, Player me, Decision<?> decision, GameEvent e) {
		game.operate().handDrawn(me, 1);
		if (me.getHands().isEmpty()) {
			return;
		}
		List<Card> hCands = me.getHands()
		                      .stream()
		                      .filter(h -> h.getColor() == CardColor.BLACK)
		                      .collect(toList());
		Card hand = game.operate().selectHand(me, false, "請選擇要放置的假情報。", "請等待玩家 " + me.getSeat() + " 操作。", hCands);
		if (hand == null) {
			return;
		}

		Player target = ((IntelReceviedEvent) e).getObtainer();
		game.operate().cardMoved(hand, me, true, target, false);
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
