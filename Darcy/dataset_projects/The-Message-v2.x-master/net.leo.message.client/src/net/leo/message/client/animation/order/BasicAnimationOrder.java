package net.leo.message.client.animation.order;

import java.awt.Graphics;
import net.leo.message.client.animation.unit.AnimationUnit;

public class BasicAnimationOrder implements AnimationOrder {

	private final AnimationUnit unit;
	private final int delay;

	@Override
	public void hide() {
		unit.hide();
	}

	@Override
	public int getDelay() {
		return delay;
	}

	@Override
	public int getFrameCount() {
		return unit.getFrameCount();
	}

	public void goToEnd() {
		unit.goToEnd();
	}

	@Override
	public void nextFrame() {
		unit.nextFrame();
	}

	@Override
	public void paintFrame(Graphics g) {
		unit.paintFrame(g);
	}

	@Override
	public void show() {
		unit.show();
	}

	@Override
	public void stop() {
		unit.hide();
	}

	public BasicAnimationOrder(AnimationUnit unit, int delay) {
		if (unit == null) {
			throw new NullPointerException();
		}
		if (delay < 0) {
			throw new NullPointerException();
		}
		this.unit = unit;
		this.delay = delay;
	}
}
