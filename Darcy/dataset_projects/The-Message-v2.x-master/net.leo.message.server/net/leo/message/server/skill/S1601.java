package net.leo.message.server.skill;

import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toSet;
import net.leo.message.base.bridge.command.action.Action;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.base.lang.Card;
import net.leo.message.base.lang.CardColor;
import net.leo.message.server.event.GameEvent;
import net.leo.message.server.event.IntelObtainedEvent;
import net.leo.message.server.game.Game;
import net.leo.message.server.game.Player;
import net.leo.message.server.game.Target;

/**
 * 英雄本色
 */
public class S1601 extends InsertedSkill {

	@Override
	public String description() {
		return "當你獲得一張假情報時，你可以抽一張牌，然後可以在一位玩家面前放置一張假情報。";
	}

	@Override
	public String getName() {
		return "英雄本色";
	}

	@Override
	public Target getTarget(Game game, Player me, Decision<?> decision, GameEvent e) {
		return null;
	}

	@Override
	public int inform(Game game, Player me, GameEvent e) {
		if (e instanceof IntelObtainedEvent) {
			IntelObtainedEvent event = (IntelObtainedEvent) e;
			if (event.getObtainer() != me) {
				return 0;
			}
			return (int) event.getIntelligences()
			                  .stream()
			                  .filter(c -> c.getColor() == CardColor.BLACK)
			                  .count();
		}
		return 0;
	}

	@Override
	public void run(Game game, Player me, Decision<?> decision, GameEvent e) {
		game.operate().handDrawn(me, 1);
		if (me.getHands().isEmpty()) {
			return;
		}

		List<Card> hCands = new ArrayList<>(me.getHands()
		                                      .stream()
		                                      .filter(h -> h.getColor() == CardColor.BLACK)
		                                      .collect(toSet()));
		Card hand = game.operate().selectHand(me, false, "選擇要放置的情報。", "請等待玩家 " + me.getSeat() + " 操作。", hCands);
		if (hand == null) {
			return;
		}

		List<Player> pCands = game.getAlives();
		Player shot = game.operate().selectPlayer(me, false, "請選擇要放置的玩家。", "請等待玩家 " + me.getSeat() + " 操作。", pCands);
		if (shot == null) {
			return;
		}

		game.operate().cardMoved(hand, me, true, shot, false);
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
