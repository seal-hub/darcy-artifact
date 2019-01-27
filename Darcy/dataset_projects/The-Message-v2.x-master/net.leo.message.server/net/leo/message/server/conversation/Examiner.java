package net.leo.message.server.conversation;

import net.leo.message.base.bridge.command.select.Selection;
import net.leo.message.base.bridge.reply.BasicReply;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.base.bridge.reply.Reply;

@FunctionalInterface
public interface Examiner {

	/*static <T> ExamResult<T> requiresDecision(Reply reply, Class decsClz) {
		if (reply == null) {
			throw new NullPointerException();
		}

		if (!BasicReply.class.isInstance(reply)) {
			return ExamResult.FAIL;
		}

		Object value = ((BasicReply) reply).value;
		if (!Decision.class.isInstance(value)) {
			return ExamResult.FAIL;
		}

		return requiresDecision((Decision<?>) value, decsClz);
	}

	static <T> ExamResult<T> requiresDecisionNonNull(Reply reply, Class decsClz) {
		if (reply == null) {
			throw new NullPointerException();
		}

		if (!BasicReply.class.isInstance(reply)) {
			return ExamResult.FAIL;
		}

		Object value = ((BasicReply) reply).value;
		if (!Decision.class.isInstance(value)) {
			return ExamResult.FAIL;
		}

		return requiresDecisionNonNull((Decision<?>) value, decsClz);
	}

	static <T> ExamResult<T> requiresDecisionNotEmpty(Reply reply, Class decsClz) {
		if (reply == null) {
			throw new NullPointerException();
		}

		if (!BasicReply.class.isInstance(reply)) {
			return ExamResult.FAIL;
		}

		Object value = ((BasicReply) reply).value;
		if (!Decision.class.isInstance(value)) {
			return ExamResult.FAIL;
		}

		return requiresDecisionNotEmpty((Decision<?>) value, decsClz);
	}

	static <T> ExamResult<T> requiresDecision(Decision<?> decs, Class decsClz) {
		if (decs == null) {
			return ExamResult.FAIL;
		}

		List dlist = decs.contributes;
		if (dlist == null || dlist.isEmpty() || decsClz.isInstance(dlist.get(0))) {
			return new ExamResult<>((Decision<T>) decs);
		}
		return ExamResult.FAIL;
	}

	static <T> ExamResult<T> requiresDecisionNonNull(Decision<?> decs, Class decsClz) {
		if (decs == null) {
			return ExamResult.FAIL;
		}

		List dlist = decs.contributes;
		if (dlist == null) {
			return ExamResult.FAIL;
		}
		if (dlist.isEmpty() || decsClz.isInstance(dlist.get(0))) {
			return new ExamResult<>((Decision<T>) decs);
		}
		return ExamResult.FAIL;
	}

	static <T> ExamResult<T> requiresDecisionNotEmpty(Decision<?> decs, Class decsClz) {
		if (decs == null) {
			return ExamResult.FAIL;
		}

		List dlist = decs.contributes;
		if (dlist == null || dlist.isEmpty()) {
			return ExamResult.FAIL;
		}

		if (decsClz.isInstance(dlist.get(0))) {
			return new ExamResult<>((Decision<T>) decs);
		}
		return ExamResult.FAIL;
	}*/


	static boolean checkSelection(Reply reply, Selection selection) {
		return selection.isReplyValid(reply);
	}

	/*static <T> ExamResult<T> checkDecision(Reply reply, Selection selection) {
		if (!selection.isReplyValid(reply)) {
			return ExamResult.FAIL;
		}

		return new ExamResult<>((Decision<T>) ((BasicReply) reply).value);
	}*/

	boolean examine(Reply reply);
}
