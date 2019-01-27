package net.leo.message.server.conversation;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.bridge.reply.Reply;
import net.leo.message.server.game.Player;

public class SingleMessenger {

	public static void command(Player player, Command command) {
		player.command(command);
	}

	private Player player;
	private BlockingQueue<PlayerReplyPair> queue = new LinkedBlockingQueue<>();
	private long deadline;
	private Examiner checker;
	private int msgId;

	public SingleMessenger(int msgId, Player player, int timeout, Examiner checker) {
		this.player = player;
		this.deadline = System.currentTimeMillis() + timeout + 2000/*buffer time*/;
		this.checker = checker;
		this.msgId = msgId;

		player.listen(queue);
	}

	public void close() {
		player.listen(null);
	}

	@SuppressWarnings("unchecked")
	public <T extends Reply> T reply(T defaultReply) {
		PlayerReplyPair next;
		while (true) {

			try {
				next = queue.poll(deadline - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
			}
			catch (InterruptedException e) {
				close();
				return defaultReply;
			}

			if (next == null) {
				close();
				return defaultReply;
			}

			Reply reply = next.getReply();
			Player source = next.getSource();
			if (reply.id == msgId && source == player && checker.examine(reply)) {
				close();
				return (T) reply;
			}
		}
	}
}

