package net.leo.message.server.conversation;

import net.leo.message.base.bridge.reply.Reply;
import net.leo.message.server.game.Player;

public class PlayerReplyPair {

	private Player source;
	private Reply reply;

	public PlayerReplyPair(Player player, Reply reply) {
		this.source = player;
		this.reply = reply;
	}

	public Reply getReply() {
		return reply;
	}

	public Player getSource() {
		return source;
	}
}
