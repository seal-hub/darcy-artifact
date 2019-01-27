package net.leo.message.server.game;

import java.io.IOException;
import java.util.Collection;
import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.bridge.reply.Reply;
import net.leo.message.base.lang.Condition;
import net.leo.message.base.lang.Identity;
import net.leo.message.server.character.CharacterCard;
import net.leo.message.server.conversation.PlayerReplyPair;
import net.leo.message.server.net.Client;

public class Player implements Target {

	public static final int DEATH = 1;
	public static final int LOSE = 2;
	public static final int ALIVE = 3;

	private int alive = ALIVE;
	private Identity identity = null;
	private Condition cond = null;
	private CardList intels = new CardList();
	private CardList hands = new CardList();
	private int seat;
	private Client client;
	private Collection<PlayerReplyPair> dest = null;
	private CharacterCard character = null;

	public Player(Client client, int seat) {
		this.client = client;
		this.seat = seat;
		listenThread();
	}

	@Deprecated
	private void listenThread() {
		new Thread(() -> {
			Reply reply;
			try {
				while (true) {
					reply = client.reply();
					if (dest != null) {
						dest.add(new PlayerReplyPair(this, reply));
					}
				}
			}
			catch (IOException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}).start();
	}



	public void command(Command command) {
		try {
			client.command(command);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public CharacterCard getCharacter() {
		return character;
	}

	public Condition getCondition() {
		return cond;
	}

	public CardList getHands() {
		return hands;
	}

	public Identity getIdentity() {
		return identity;
	}

	public CardList getIntels() {
		return intels;
	}

	public String getName() {
		//TODO
		return "Sam";
	}

	public int getSeat() {
		return seat;
	}

	public int isAlive() {
		return alive;
	}



	public void listen(Collection<PlayerReplyPair> dest) {
		this.dest = dest;
	}

	public void setAlive(int alive) {
		this.alive = alive;
	}

	public void setCharacter(CharacterCard character) {
		this.character = character;
	}

	public void setCondition(Condition cond) {
		this.cond = cond;
	}

	public void setIdentity(Identity identity) {
		this.identity = identity;
	}
}
