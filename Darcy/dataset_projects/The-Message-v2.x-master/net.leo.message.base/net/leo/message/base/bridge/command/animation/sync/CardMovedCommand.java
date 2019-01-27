package net.leo.message.base.bridge.command.animation.sync;

import java.util.ArrayList;
import java.util.List;
import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.lang.Card;

public class CardMovedCommand extends Command {

	public final int origin, dest;
	public final boolean orgHand, destHand;
	public final List<Card> cards;

	public CardMovedCommand(int origin, boolean orgHand, List<Card> cards, int dest, boolean destHand) {
		super("同步動畫", "卡牌移動");
		if (origin < 0 || dest < 0) {
			throw new IllegalArgumentException();
		}
		if (cards.isEmpty()) { //Implicitly check null
			throw new IllegalArgumentException();
		}
		this.cards = new ArrayList<>(cards);
		this.origin = origin;
		this.orgHand = orgHand;
		this.dest = dest;
		this.destHand = destHand;
	}

	public CardMovedCommand(int origin, boolean orgHand, Card card, int dest, boolean destHand) {
		this(origin, orgHand, List.of(card), dest, destHand);
	}
}
