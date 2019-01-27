package net.leo.message.base.bridge.command.animation.sync;

import java.util.ArrayList;
import java.util.List;
import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.lang.Card;

public class CardDroppedCommand extends Command {

	public final boolean handcard;
	public final int seat;
	public final List<Card> cards;

	public CardDroppedCommand(List<Card> cards, boolean handCard, int seat) {
		super("同步動畫", "卡牌棄置");
		if (cards.isEmpty()) { //Implicitly check null
			throw new IllegalArgumentException();
		}
		if (seat < 0) {
			throw new IllegalArgumentException();
		}
		this.handcard = handCard;
		this.seat = seat;
		this.cards = new ArrayList<>(cards);
	}

	public CardDroppedCommand(Card card, boolean handCard, int seat) {
		this(List.of(card), handCard, seat);
	}
}
