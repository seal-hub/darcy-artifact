package net.leo.message.server.conversation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import static java.util.stream.Collectors.toList;
import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.bridge.command.program.PorgCheckCommand;
import net.leo.message.base.bridge.command.select.Selection;
import net.leo.message.base.bridge.reply.Reply;
import net.leo.message.server.game.Player;

public class MultiMessenger {

	public static void checkPreogress(List<Player> players) {
		if (players.isEmpty()) {
			throw new IllegalArgumentException();
		}

		//Prepare
		int msgId = Selection.randomMessageId();
		int nPlayer = players.size();
		Set<Player> replied = new HashSet<>(nPlayer);

		//Listen
		MultiMessenger mm = new MultiMessenger(msgId, players, (player, reply) -> {
			return reply.id == msgId && replied.add(player);
		}, 60000);

		//Send commands
		players.forEach(p -> {
			p.command(new PorgCheckCommand(msgId));
		});

		//If time up or everyone replies, mission is completed
		for ( ; ; ) {
			if (mm.next() == null || replied.size() == nPlayer) {
				return;
			}
		}
	}

	public static void commandOneAndOthers(List<Player> players, Player main, Command cmd, Command cmdEx) {
		//Send message
		main.command(cmd);
		players.stream().filter(p -> p != main).forEach(p -> {
			p.command(cmdEx);
		});

		if (!Selection.class.isInstance(cmd) && !Selection.class.isInstance(cmdEx)) {
			checkPreogress(players);
		}
	}

	public static void commandEx(List<Player> players, Player ex, Command cmd) {
		List<Player> parts = players.stream().filter(p -> p != ex).collect(toList());

		//Send message
		parts.forEach(p -> {
			p.command(cmd);
		});

		if (!Selection.class.isInstance(cmd)) {
			checkPreogress(parts);
		}
	}

	public static void commandAll(List<Player> players, Command cmd) {
		//Send message
		players.forEach(p -> {
			p.command(cmd);
		});

		if (!Selection.class.isInstance(cmd)) {
			checkPreogress(players);
		}
	}

	private int msgId;
	private long deadline;
	private ReplyChecker checker;
	private List<Player> players;
	private LinkedBlockingQueue<PlayerReplyPair> queue = new LinkedBlockingQueue<>();

	public MultiMessenger(int msgId, Collection<Player> players, ReplyChecker checker, int timeout) {
		this.msgId = msgId;
		this.deadline = System.currentTimeMillis() + timeout + 2000 /*buffer time*/;
		this.checker = checker;
		this.players = new ArrayList<>(players);

		players.forEach(p -> {
			p.listen(queue);
		});
	}

	public void close() {
		players.forEach(p -> {
			p.listen(null);
		});
	}

	@SuppressWarnings("unchecked")
	public PlayerReplyPair next() {
		PlayerReplyPair next;
		while (true) {

			try {
				next = queue.poll(deadline - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
			}
			catch (InterruptedException e) {
				close();
				return null;
			}
			if (next == null) {
				close();
				return null;
			}

			Reply reply = next.getReply();
			Player source = next.getSource();
			if (reply.id == msgId && checker.check(source, reply)) {
				source.listen(null);
				players.remove(source);
				return next;
			}
		}
	}
}
