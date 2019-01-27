package net.leo.message.base.bridge.command.select;

import java.util.ArrayList;
import java.util.List;
import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.bridge.reply.MultiReply;
import net.leo.message.base.bridge.reply.NullReply;
import net.leo.message.base.bridge.reply.Reply;
import net.leo.message.base.lang.Card;

public class CardSelectionCommand extends Command implements Selection {

	public final boolean orderRelated;
	public final List<Card> candidates;
	public final boolean enforcement;
	public final String inst1, inst2;
	public final int min, max;
	public final int time;
	public final int id;

	public CardSelectionCommand(String inst1, String inst2, List<Card> candidates, int min, int max, boolean enforcement, int time, int id) {
		super("選擇", "順選卡牌");
		if (inst1 == null || inst2 == null) {
			throw new NullPointerException();
		}
		int size = candidates.size();
		if (min < 0 || max < 0 || min > max || (enforcement && min > size) || time < 0) {
			throw new IllegalArgumentException();
		}
		this.inst1 = inst1;
		this.inst2 = inst2;
		this.candidates = new ArrayList<>(candidates);
		this.min = min;
		this.max = max;
		this.enforcement = enforcement;
		this.time = time;
		this.orderRelated = true;
		this.id = id;
	}

	public CardSelectionCommand(String inst1, List<Card> candidates, int min, int max, boolean enforcement, int time, int id) {
		super("選擇", "選擇卡牌");
		if (inst1 == null) {
			throw new NullPointerException();
		}
		int size = candidates.size();
		if (min < 0 || max < 0 || min > max || (enforcement && min > size) || time < 0) {
			throw new IllegalArgumentException();
		}
		this.inst1 = inst1;
		this.inst2 = null;
		this.candidates = new ArrayList<>(candidates);
		this.min = min;
		this.max = max;
		this.enforcement = enforcement;
		this.time = time;
		this.orderRelated = false;
		this.id = id;
	}

	public CardSelectionCommand(String inst, List<Card> candidates, boolean enforcement, int time, int id) {
		super("選擇", "選擇卡牌");
		if (inst == null) {
			throw new NullPointerException();
		}
		if (candidates.isEmpty() && enforcement || time < 0) {
			throw new IllegalArgumentException();
		}
		this.inst1 = inst;
		this.inst2 = null;
		this.candidates = new ArrayList<>(candidates);
		this.min = 1;
		this.max = 1;
		this.enforcement = enforcement;
		this.time = time;
		this.orderRelated = false;
		this.id = id;
	}

	@Override
	public Reply getDefaultReply() {
		if (!enforcement) {
			return new NullReply(id);
		}

		List<Card> contributes = candidates.subList(0, min);
		return new MultiReply<>(id, contributes);
	}

	@Override
	public int getMessageId() {
		return id;
	}

	@Override
	public boolean isMadantory() {
		return enforcement;
	}

	@Override
	public boolean isReplyValid(Reply reply) {
		if (reply instanceof NullReply) {
			return !enforcement && id == reply.id;
		}

		if (reply instanceof MultiReply) {
			return candidates.containsAll(((MultiReply) reply).values);
		}

		return false;
	}
}
