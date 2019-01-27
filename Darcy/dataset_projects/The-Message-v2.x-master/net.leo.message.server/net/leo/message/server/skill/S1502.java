package net.leo.message.server.skill;

import java.util.Set;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.base.lang.CardFunction;
import net.leo.message.server.event.GameEvent;
import net.leo.message.server.game.Game;
import net.leo.message.server.game.Player;
import net.leo.message.server.game.Target;

/**
 * 預謀
 */
public class S1502 extends ConversionSkill {

	public S1502() {
		super(Set.of(CardFunction.TRAP), CardFunction.LOCK_ON);
	}

	@Override
	public String description() {
		return "你的調虎離山可以當作鎖定使用。";
	}

	@Override
	public String getName() {
		return "預謀";
	}

}
