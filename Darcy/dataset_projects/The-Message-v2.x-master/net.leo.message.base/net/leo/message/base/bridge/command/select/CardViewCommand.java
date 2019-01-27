package net.leo.message.base.bridge.command.select;

import java.util.Collections;
import java.util.List;
import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.bridge.reply.BasicReply;
import net.leo.message.base.bridge.reply.Reply;
import net.leo.message.base.lang.Card;

public class CardViewCommand extends Command implements Selection {

	public final Card card;
	public final String inst, title;
	public final int time;
	public final List<String> buttonsText;
	public final int id;

	public CardViewCommand(Card card, String title, String inst, int time, List<String> buttonsText, int id) {
		super("選擇", "檢視卡牌");
		if (card == null || title == null || buttonsText == null) {
			throw new NullPointerException();
		}
		if (time < 0) {
			throw new IllegalArgumentException();
		}
		if (buttonsText.stream().allMatch(t -> t.endsWith("X"))) {
			throw new IllegalArgumentException();
		}
		this.card = card;
		this.inst = inst;
		this.title = title;
		this.time = time;
		this.buttonsText = Collections.unmodifiableList(buttonsText);
		this.id = id;
	}

	@Override
	public Reply getDefaultReply() {
		int i = 0;
		for (String t : buttonsText) {
			if (!t.endsWith("X")) {
				break;
			}
			i++;
		}

		return new BasicReply<>(id, i);
	}

	@Override
	public int getMessageId() {
		return id;
	}

	@Override
	public boolean isMadantory() {
		return true;
	}

	@Override
	public boolean isReplyValid(Reply reply) {
		if (reply instanceof BasicReply) {
			if (((BasicReply) reply).value instanceof Integer) {
				int index = (int) ((BasicReply) reply).value;
				if (index >= 0 && index < buttonsText.size()) {
					return !buttonsText.get(index).endsWith("X");
				}
			}
		}

		return false;
	}
}
