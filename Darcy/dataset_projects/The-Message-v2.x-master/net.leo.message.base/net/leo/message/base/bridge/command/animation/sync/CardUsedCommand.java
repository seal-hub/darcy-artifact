package net.leo.message.base.bridge.command.animation.sync;

import java.util.Objects;
import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.lang.Card;

public class CardUsedCommand extends Command {

	public final Card card;
	public final int user;

	/**
	 * Target; -1 indicates null
	 */
	public final int target;

	public CardUsedCommand(Card card, int user, int target) {
		super("同步動畫", "出牌");
		if (user < 0) {
			throw new IllegalArgumentException();
		}
		this.card = Objects.requireNonNull(card);
		this.target = target;
		this.user = user;
	}

	public CardUsedCommand(Card card, int user) {
		this(card, user, -1);
	}
}
