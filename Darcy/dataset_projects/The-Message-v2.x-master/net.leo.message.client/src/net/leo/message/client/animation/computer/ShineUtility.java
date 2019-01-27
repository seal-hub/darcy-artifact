package net.leo.message.client.animation.computer;

import java.util.List;
import net.leo.message.client.animation.executor.ShineGear;
import net.leo.message.client.animation.executor.ShineInfo;

public class ShineUtility {

	public static Runnable getShineOutRunnable(List<? extends Risable> paths, ShineGear gear) {
		if (gear == null) {
			throw new NullPointerException();
		}
		ShineInfo info = new ShineInfo();
		paths.forEach(p -> {
			p.getOrigin().poll(p.getCard().getElement());
			info.addShineItem(p.getOrigin(), -1);
		});
		return () -> gear.run(info);
	}

	public static Runnable getShineInRunnable(List<? extends Descendable> paths, ShineGear gear) {
		if (gear == null) {
			throw new NullPointerException();
		}
		ShineInfo info = new ShineInfo();
		paths.forEach(p -> {
			p.getDestination().offer(p.getCard().getElement());
			info.addShineItem(p.getDestination(), 1);
		});
		return () -> gear.run(info);
	}

	private ShineUtility() {
	}
}