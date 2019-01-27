package net.leo.message.server.conversation;

import net.leo.message.base.bridge.reply.Reply;
import net.leo.message.server.game.Player;

@FunctionalInterface
public interface ReplyChecker {

	boolean check(Player source, Reply reply);

}
