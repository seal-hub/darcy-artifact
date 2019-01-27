package net.leo.message.server.conversation;

import java.util.List;
import net.leo.message.base.bridge.reply.Decision;

public class ExamResult<T> {

	public static final ExamResult FAIL = new ExamResult();


	private final Decision<T> decs;
	private final boolean valid;

	public ExamResult(Decision<T> decs) {
		if (decs == null) {
			throw new NullPointerException();
		}
		this.valid = true;
		this.decs = decs;
	}

	private ExamResult() {
		this.valid = false;
		this.decs = null;
	}

	public Decision<T> decision() {
		return decs;
	}

	public T first() {
		if (decs == null) {
			return null;
		}
		return decs.contributes.get(0);
	}

	public List<T> list() {
		if (decs == null) {
			return null;
		}
		return decs.contributes;
	}

	public boolean pass() {
		return valid;
	}
}
