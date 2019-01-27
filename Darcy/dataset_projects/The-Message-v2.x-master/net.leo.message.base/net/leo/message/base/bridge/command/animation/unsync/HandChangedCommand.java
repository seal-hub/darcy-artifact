package net.leo.message.base.bridge.command.animation.unsync;

import java.util.ArrayList;
import java.util.List;
import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.lang.Card;

public class HandChangedCommand extends Command {

	public final List<Card> cards;
	public final boolean add;

	public HandChangedCommand(boolean add, List<Card> added) {
		super("非同步動畫", "手牌變更");
		if (added.isEmpty()) {
			throw new IllegalArgumentException();
		}
		this.add = add;
		this.cards = new ArrayList<>(added);
	}

	public HandChangedCommand(boolean add, Card added) {
		super("非同步動畫", "手牌變更");
		if (added == null) {
			throw new NullPointerException();
		}
		this.add = add;
		this.cards = List.of(added);
	}
}
