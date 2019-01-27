package net.leo.message.server.skill;

import java.util.List;
import static java.util.stream.Collectors.toList;
import net.leo.message.base.bridge.command.action.Action;
import net.leo.message.base.bridge.command.action.CardAction;
import net.leo.message.base.bridge.command.action.PlayerAction;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.base.lang.Card;
import net.leo.message.base.lang.CardColor;
import net.leo.message.server.event.GameEvent;
import net.leo.message.server.event.IntelReceviedEvent;
import net.leo.message.server.game.Game;
import net.leo.message.server.game.Player;
import net.leo.message.server.game.Target;

/**
 * 偷天
 */
public class S2001 extends InsertedSkill {

	@Override
	public String description() {
		return "當一位玩家獲得你傳出的假情報時，你可以將他面前一張真情報收入一位玩家的手牌中。";
	}

	@Override
	public String getName() {
		return "偷天";
	}

	@Override
	public Target getTarget(Game game, Player me, Decision<?> decision, GameEvent e) {
		Player recevier = ((IntelReceviedEvent) e).getObtainer();
		return recevier;
	}

	@Override
	public int inform(Game game, Player me, GameEvent e) {
		if (e instanceof IntelReceviedEvent) {
			IntelReceviedEvent event = (IntelReceviedEvent) e;
			if (event.getOrigin() == me && event.getIntelligence().getColor() == CardColor.BLACK) {
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
		Player recevier = ((IntelReceviedEvent) e).getObtainer();
		return recevier.getIntels().stream().anyMatch(i -> i.getColor() != CardColor.BLACK);
	}

	@Override
	public int mode() {
		return ORDINARY;
	}

	@Override
	public Action prepare(Game game, Player me, GameEvent e) {
		List<Integer> pCands = game.getAlives().stream().map(p -> p.getSeat()).collect(toList());
		Action pAction = new PlayerAction(1, 1, pCands, false, "請選擇要拿走的情報。", null);

		Player recevier = ((IntelReceviedEvent) e).getObtainer();
		List<Card> cCands = recevier.getIntels().stream().filter(i -> i.getColor() != CardColor.BLACK).collect(toList());
		Action first = new CardAction(1, 1, cCands, false, "請選擇要拿走的情報。", pAction);

		return first;
	}

	@Override
	public void run(Game game, Player me, Decision<?> decision, GameEvent e) {
		Player recevier = ((IntelReceviedEvent) e).getObtainer();
		Card card = (Card) decision.contributes.get(0);
		Player robber = game.getPlayer((Integer) decision.nextDecision.contributes.get(0));
		game.operate().cardMoved(card, recevier, false, robber, true);
	}
}
