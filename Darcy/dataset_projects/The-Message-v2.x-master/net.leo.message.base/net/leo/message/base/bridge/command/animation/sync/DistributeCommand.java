package net.leo.message.base.bridge.command.animation.sync;

import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.lang.Card;
import net.leo.message.base.lang.CardColor;

public class DistributeCommand extends Command {

	public final Card card;
	public final int seat;
	public final CardColor color;

	public DistributeCommand(Card card, int seat, CardColor color) {
		super("同步動畫", "真偽發牌");
		if (card == null) {
			throw new NullPointerException();
		}
		if (seat < 0 || !(color == CardColor.RED || color == CardColor.BLUE || color == CardColor.BLACK)) {
			throw new IllegalArgumentException();
		}
		this.card = card;
		this.seat = seat;
		this.color = color;
	}
}
