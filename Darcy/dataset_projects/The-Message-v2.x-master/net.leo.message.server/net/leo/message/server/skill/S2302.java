package net.leo.message.server.skill;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import net.leo.message.base.bridge.command.action.Action;
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
 * 陽謀
 */
public class S2302 extends InsertedSkill {

	public S2302() {
	}

	@Override
	public String description() {
		return "當你獲得另一位玩家傳出的假情報時，你可以抽取另一位玩家一張手牌。";
	}

	@Override
	public String getName() {
		return "陽謀";
	}

	@Override
	public Target getTarget(Game game, Player me, Decision<?> decision, GameEvent e) {
		Player robbed = game.getPlayers().get((Integer) decision.contributes.get(0));
		return robbed;
	}

	@Override
	public int inform(Game game, Player me, GameEvent e) {
		if (e instanceof IntelReceviedEvent) {
			IntelReceviedEvent event = (IntelReceviedEvent) e;
			if (event.getObtainer() == me
					&& event.getOrigin() != me
					&& event.getIntelligence().getColor() == CardColor.BLACK) {
				return 1;
			}
		}
		return 0;
	}

	@Override
	public void run(Game game, Player me, Decision<?> decision, GameEvent e) {
		//Gets hand card
		Player robbed = game.getPlayers().get((Integer) decision.contributes.get(0));
		List<Card> cards = robbed.getHands();
		Card card = cards.get(new Random().nextInt(cards.size()));
		game.operate().handTransfer(card, robbed, me);
	}

	@Override
	public boolean isMadantory() {
		return false;
	}

	@Override
	public boolean isSurroundingIdeal(Game game, Player me, GameEvent e) {
		return game.getAlives().stream().anyMatch(p -> p != me && !p.getHands().isEmpty());
	}

	@Override
	public int mode() {
		return ORDINARY;
	}

	@Override
	public Action prepare(Game game, Player me, GameEvent e) {

		//Selects players
		List<Integer> candidates = game.getAlives()
		                               .stream()
		                               .filter(p -> p != me && !p.getHands().isEmpty())
		                               .map(p -> p.getSeat())
		                               .collect(Collectors.toList());
		return new PlayerAction(1, 1, candidates, false, "請選擇要搶手牌的玩家。", null);
	}
}
