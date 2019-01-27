package net.leo.message.server.skill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import static java.util.stream.Collectors.toList;
import net.leo.message.base.bridge.command.action.Action;
import net.leo.message.base.bridge.command.action.PlayerAction;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.base.lang.Card;
import net.leo.message.server.event.GameEvent;
import net.leo.message.server.event.UsageStartEvent;
import net.leo.message.server.game.Game;
import net.leo.message.server.game.Player;
import net.leo.message.server.game.Target;

/**
 * 先機
 */
public class S0801 extends InsertedSkill {

	@Override
	public String description() {
		return "你可以翻開此角色牌，抽取另一位玩家三張手牌，然後他抽一張牌。";
	}

	@Override
	public String getName() {
		return "先機";
	}

	@Override
	public Target getTarget(Game game, Player me, Decision<?> decision, GameEvent e) {
		Player target = game.getPlayer((Integer) decision.contributes.get(0));
		return target;
	}

	@Override
	public int inform(Game game, Player me, GameEvent e) {
		if (e instanceof UsageStartEvent) {
			return 1;
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
		return OPENING;
	}

	@Override
	public Action prepare(Game game, Player me, GameEvent e) {
		List<Integer> candidates = game.getAlives()
		                               .stream()
		                               .filter(p -> p != me)
		                               .map(p -> p.getSeat())
		                               .collect(toList());
		return new PlayerAction(1, 1, candidates, false, "選擇目標玩家。", null);
	}

	@Override
	public void run(Game game, Player me, Decision<?> decision, GameEvent e) {
		Player target = game.getPlayer((Integer) decision.contributes.get(0));
		List<Card> handList = new ArrayList<>(target.getHands());
		if (!handList.isEmpty()) {
			Collections.shuffle(handList, new Random());
			List<Card> conts = handList.subList(0, Math.min(handList.size(), 3));
			game.operate().handTransfer(conts, target, me);
		}
		game.operate().handDrawn(target, 1);
	}
}
