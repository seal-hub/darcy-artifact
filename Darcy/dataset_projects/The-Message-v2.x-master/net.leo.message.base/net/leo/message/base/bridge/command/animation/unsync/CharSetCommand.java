package net.leo.message.base.bridge.command.animation.unsync;

import java.util.ArrayList;
import java.util.List;
import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.bridge.command.data.CharData;

public class CharSetCommand extends Command {

	public final List<CharData> charDatum;

	public CharSetCommand(List<CharData> charDatum) {
		super("初始", "顯示角色");
		this.charDatum = new ArrayList<>(charDatum);
	}
}
