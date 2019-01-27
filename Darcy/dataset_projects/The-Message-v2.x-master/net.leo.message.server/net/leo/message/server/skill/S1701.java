package net.leo.message.server.skill;

import java.util.List;
import static java.util.stream.Collectors.toList;
import net.leo.message.base.bridge.command.action.Action;
import net.leo.message.base.bridge.command.action.HandAction;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.base.lang.Card;
import net.leo.message.base.lang.CardColor;
import net.leo.message.server.event.GameEvent;
import net.leo.message.server.event.IntelReceviedEvent;
import net.leo.message.server.game.Game;
import net.leo.message.server.game.Player;
import net.leo.message.server.game.Target;

/**
 * 笑裡藏刀
 */
public class S1701 extends InsertedSkill {

	@Override
	public String description() {
		return "當一位玩家獲得你的真情報時，你可以在他面前放置一張假情報。";
	}

	@Override
	public String getName() {
		return "笑裡藏刀";
	}

	@Override
	public Target getTarget(Game game, Player me, Decision<?> decision, GameEvent e) {
		Player target = ((IntelReceviedEvent) e).getObtainer();
		return target;
	}

	@Override
	public int inform(Game game, Player me, GameEvent e) {
		if (e instanceof IntelReceviedEvent) {
			IntelReceviedEvent event = (IntelReceviedEvent) e;
			if (event.getOrigin() == me && event.getObtainer() != me && event.getIntelligence().getColor() != CardColor.BLACK) {
				return 1;
			}
		}
		return 0;
	}

	@Override
	public void run(Game game, Player me, Decision<?> decision, GameEvent e) {
		Card hand = (Card) decision.contributes.get(0);
		Player target = ((IntelReceviedEvent) e).getObtainer();
		game.operate().cardMoved(hand, me, true, target, false);
	}

	@Override
	public boolean isMadantory() {
		return false;
	}

	@Override
	public boolean isSurroundingIdeal(Game game, Player me, GameEvent e) {
		return !me.getHands().isEmpty();
	}

	@Override
	public int mode() {
		return ORDINARY;
	}

	@Override
	public Action prepare(Game game, Player me, GameEvent e) {
		List<Card> hCands = me.getHands()
		                      .stream()
		                      .filter(h -> h.getColor() == CardColor.BLACK)
		                      .collect(toList());
		return new HandAction(1, 1, hCands, false, "請選擇一放置的假情報。", null);
	}
}
