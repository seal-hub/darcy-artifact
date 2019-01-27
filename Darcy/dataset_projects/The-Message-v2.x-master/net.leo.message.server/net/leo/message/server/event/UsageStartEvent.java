package net.leo.message.server.event;

import java.util.Objects;
import net.leo.message.server.game.Stage;

public class UsageStartEvent implements GameEvent {

	public static final UsageStartEvent STAGE_USAGE = new UsageStartEvent(Stage.USAGE);
	public static final UsageStartEvent STAGE_PASSING = new UsageStartEvent(Stage.INTEL_PASSING);
	public static final UsageStartEvent STAGE_ARRIVED = new UsageStartEvent(Stage.INTEL_ARRIVED);

	public final Stage stage;

	public UsageStartEvent(Stage stage) {
		this.stage = Objects.requireNonNull(stage);
	}
}
