package net.leo.message.server.event;

import java.util.Objects;
import net.leo.message.server.game.Player;

public class PlayerDyingEvent  implements GameEvent{

	public final Player dier, murderer;

	public PlayerDyingEvent(Player murderer, Player dier) {
		this.dier = Objects.requireNonNull(dier);
		this.murderer = Objects.requireNonNull(murderer);
	}
}
