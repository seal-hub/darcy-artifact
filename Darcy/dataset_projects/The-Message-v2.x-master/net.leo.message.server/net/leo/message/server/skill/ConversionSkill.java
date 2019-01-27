package net.leo.message.server.skill;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import static java.util.stream.Collectors.toList;
import net.leo.message.base.bridge.command.action.Action;
import net.leo.message.base.bridge.command.action.HandAction;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.base.lang.Card;
import net.leo.message.base.lang.CardFunction;
import net.leo.message.server.event.GameEvent;
import net.leo.message.server.event.UsageStartEvent;
import net.leo.message.server.game.Game;
import net.leo.message.server.game.Player;
import net.leo.message.server.game.Target;
import net.leo.message.server.game.Usage;

public abstract class ConversionSkill implements Skill {

	private Predicate<? super Card> filter;
	private CardFunction func;

	public ConversionSkill(Predicate<? super Card> filter, CardFunction func) {
		this.filter = Objects.requireNonNull(filter);
		this.func = Objects.requireNonNull(func);
	}

	public ConversionSkill(Set<CardFunction> conversed, CardFunction func) {
		this(c -> conversed.contains(c.getFunction()), func);
	}

	@Override
	public final Target getTarget(Game game, Player me, Decision<?> decision, GameEvent e) {
		return null;
	}

	public final int inform(Game game, Player me, GameEvent e) {
		if (e instanceof UsageStartEvent) {
			return Usage.isCardUsable(func, me, game) ? 1 : 0;
		}
		return 0;
	}

	@Override
	public final boolean isInvokable(Game game, Player me, GameEvent e) {
		//Always invokable
		return true;
	}

	@Override
	public final boolean isMadantory() {
		//Never madantory
		return false;
	}

	@Override
	public final boolean isRed() {
		//Always "Red Skill".
		return true;
	}

	@Override
	public final boolean isSurroundingIdeal(Game game, Player me, GameEvent e) {
		//Always ideal
		return true;
	}

	@Override
	public final int mode() {
		//Always ordinary
		return ORDINARY;
	}

	public final Action prepare(Game game, Player me, GameEvent e) {
		Action next = Usage.getActionOfFunction(me, func, game);
		List<Card> candidates = me.getHands()
		                          .stream()
		                          .filter(filter)
		                          .collect(toList());
		Action first = new HandAction(1, 1, candidates, false, "選擇要當作 " + func.getName() + " 的牌。", next);
		return first;
	}

	@Override
	public final void run(Game game, Player me, Decision<?> decision, GameEvent e) {
		Usage.sendUsageCommand(me, (Decision<Card>) decision, func, game);
	}
}
