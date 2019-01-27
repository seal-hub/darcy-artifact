package net.leo.message.server.skill;

import net.leo.message.base.lang.CardColor;
import net.leo.message.base.lang.CardFunction;

/**
 * 換日
 */
public class S2002 extends ConversionSkill {

	public S2002() {
		super(c -> c.getColor() == CardColor.BLACK, CardFunction.LOCK_ON);
	}

	@Override
	public String description() {
		return "你的黑色牌可以當作鎖定使用。";
	}

	@Override
	public String getName() {
		return "換日";
	}
}
