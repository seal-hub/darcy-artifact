package net.leo.message.server.skill;

import java.util.List;
import static java.util.stream.Collectors.toList;
import net.leo.message.base.bridge.command.action.Action;
import net.leo.message.base.bridge.command.action.PlayerAction;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.base.lang.Card;
import net.leo.message.server.event.GameEvent;
import net.leo.message.server.game.Game;
import net.leo.message.server.game.Player;
import net.leo.message.server.game.Target;
import net.leo.message.server.stack.ExecutableCard;
import net.leo.message.server.stack.Stack;

public class S1001 extends ExecutableSkill {

	private Player target = null;
	private Player me = null;

	@Override
	public String description() {
		return "你可以翻開此角色牌，然後將另一位玩家的一張情報放回牌庫頂。";
	}

	@Override
	public void dispose(Game game) {
		target = null;
		me = null;
	}

	@Override
	public int execute(Game game) {
		List<Card> cands = target.getIntels();
		if (!cands.isEmpty()) {
			Card cont = game.operate().selectCards(me, 1, 1, true, "選擇一張情報放回牌庫頂。", "等待玩家 " + target.getSeat() + " 操作。", cands).get(0);
			game.operate().cardRecycled(target, cont);
		}
		target = null;
		me = null;
		return Stack.ONE_MORE_USAGE;
	}

	@Override
	public String getName() {
		return "潛藏伏擊";
	}

	@Override
	public Target getTarget(Game game, Player me, Decision<?> decision, GameEvent e) {
		this.target = game.getPlayer((Integer) decision.contributes.get(0));
		this.me = me;
		return target;
	}

	@Override
	public boolean invalidate(ExecutableCard rest) {
		return false;
	}

	@Override
	public int mode() {
		return OPENING;
	}

	@Override
	public Action prepare(Game game, Player me, GameEvent e) {
		List<Integer> cands = game.getAlives()
		                          .stream()
		                          .filter(p -> p != me && !p.getIntels().isEmpty())
		                          .map(p -> p.getSeat())
		                          .collect(toList());
		Action action = new PlayerAction(1, 1, cands, false, "選擇目標玩家。", null);
		return action;
	}

	@Override
	public void run(Game game, Player me, Decision<?> decision, GameEvent e) {

	}
}
