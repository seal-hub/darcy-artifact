package net.leo.message.server.event;

import java.util.Objects;
import net.leo.message.server.game.Player;

public class PlayerDeadEvent  implements GameEvent{

	public final Player murderer, dier;

	public PlayerDeadEvent(Player murderer, Player dier) {
		this.murderer = Objects.requireNonNull(murderer);
		this.dier = Objects.requireNonNull(dier);
	}
}
