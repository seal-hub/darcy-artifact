package net.leo.message.server.skill;

import java.util.List;
import static java.util.stream.Collectors.toList;
import net.leo.message.base.bridge.command.action.Action;
import net.leo.message.base.bridge.command.action.CardAction;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.base.lang.Card;
import net.leo.message.base.lang.CardColor;
import net.leo.message.base.lang.CardFunction;
import net.leo.message.server.event.GameEvent;
import net.leo.message.server.event.IntelObtainedEvent;
import net.leo.message.server.game.Game;
import net.leo.message.server.game.Player;
import net.leo.message.server.game.Target;

/**
 * 計中計
 */
public class S0901 extends InsertedSkill {

	@Override
	public String description() {
		return "當一位玩家獲得你的一張真情報時，你可以" + CardFunction.BURN.getName() + "他面前一張假情報。";
	}

	@Override
	public String getName() {
		return "計中計";
	}

	@Override
	public Target getTarget(Game game, Player me, Decision<?> decision, GameEvent e) {
		return null;
	}

	@Override
	public int inform(Game game, Player me, GameEvent e) {
		if (e instanceof IntelObtainedEvent) {
			IntelObtainedEvent event = (IntelObtainedEvent) e;
			if (event.getObtainer() == me) {
				return (int) event.getIntelligences()
				                  .stream()
				                  .filter(i -> i.getColor() != CardColor.BLACK)
				                  .count();
			}
		}
		return 0;
	}

	@Override
	public void run(Game game, Player me, Decision<?> decision, GameEvent e) {
		Player target = ((IntelObtainedEvent) e).getObtainer();
		game.operate().cardBurnt(target, (List<Card>) decision.contributes);
	}

	@Override
	public boolean isMadantory() {
		return false;
	}

	@Override
	public boolean isSurroundingIdeal(Game game, Player me, GameEvent e) {
		Player target = ((IntelObtainedEvent) e).getObtainer();
		return target.getIntels()
		             .stream()
		             .anyMatch(i -> i.getColor() == CardColor.BLACK);
	}

	@Override
	public int mode() {
		return ORDINARY;
	}

	@Override
	public Action prepare(Game game, Player me, GameEvent e) {
		Player target = ((IntelObtainedEvent) e).getObtainer();
		List<Card> cands = target.getIntels()
		                         .stream()
		                         .filter(i -> i.getColor() == CardColor.BLACK)
		                         .collect(toList());
		Action action = new CardAction(1, 1, cands, false, "選擇一張一燒毀的假情報。", null);
		return action;
	}
}
