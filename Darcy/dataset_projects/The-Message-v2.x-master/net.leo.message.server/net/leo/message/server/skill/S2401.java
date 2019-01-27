package net.leo.message.server.skill;

import java.util.Set;
import net.leo.message.base.lang.CardFunction;

/**
 * 亮劍
 */
public class S2401 extends ConversionSkill {

	public S2401() {
		super(Set.of(CardFunction.PROVE, CardFunction.RETURN), CardFunction.LOCK_ON);
	}

	@Override
	public String description() {
		return "你的試探或退回可以當作鎖定使用。";
	}

	@Override
	public String getName() {
		return "亮劍";
	}
}
