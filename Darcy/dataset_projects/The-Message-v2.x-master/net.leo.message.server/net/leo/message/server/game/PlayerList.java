package net.leo.message.server.game;

import java.util.LinkedList;

public class PlayerList extends LinkedList<Player> {

	public LinkedList<Player> getAlivesFrom(Player first) {
		int size = size();
		int n = 0;

		LinkedList<Player> copy = new LinkedList<>(this);
		while (copy.getFirst() != first) {
			copy.addLast(copy.pollFirst());
			if (++n >= size) {
				throw new IllegalArgumentException();
			}
		}
		return copy;
	}

	public Player nextAlive(Player player) {
		int index = indexOf(player);
		if (index == -1) {
			throw new IllegalArgumentException();
		}
		if (index == size() - 1) {
			index = 0;
		}
		else {
			index++;
		}
		Player next = get(index);
		return next.isAlive() == Player.ALIVE ? next : nextAlive(next);
	}

	public Player previousAlive(Player player) {
		int index = indexOf(player);
		if (index == -1) {
			throw new IllegalArgumentException();
		}
		if (index == 0) {
			index = size() - 1;
		}
		else {
			index--;
		}
		Player prev = get(index);
		return prev.isAlive() == Player.ALIVE ? prev : previousAlive(prev);
	}
}
