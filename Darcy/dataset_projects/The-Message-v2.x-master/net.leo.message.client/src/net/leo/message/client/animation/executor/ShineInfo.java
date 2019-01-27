package net.leo.message.client.animation.executor;

import java.util.HashMap;
import java.util.Map;
import net.leo.message.client.player.CountView;

public class ShineInfo {

	final Map<CountView, Integer> map = new HashMap<>(1);

	void changeText() {
		for (Map.Entry<CountView, Integer> entry : map.entrySet()) {
			entry.getKey().increase(entry.getValue());
		}
	}

	public void addShineItem(CountView comp, int increment) {
		if (comp == null) {
			throw new NullPointerException();
		}
		if (increment == 0) {
			return;
		}
		Integer i = map.get(comp);
		if (i == null) {
			i = 0;
		}
		map.put(comp, i + increment);
	}

	public ShineInfo() {
	}

	public ShineInfo(CountView v, int increment) {
		addShineItem(v, increment);
	}
}
