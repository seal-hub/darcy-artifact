package net.leo.message.base.bridge.command.select;

import java.io.Serializable;
import java.util.Random;
import net.leo.message.base.bridge.reply.Reply;

public interface Selection extends Serializable {

	Random RAND = new Random();

	static int randomMessageId() {
		return RAND.nextInt();
	}

	Reply getDefaultReply();

	int getMessageId();

	boolean isMadantory();

	boolean isReplyValid(Reply reply);
}
