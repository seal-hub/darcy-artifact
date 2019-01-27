package net.leo.message.client.animation.order;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import net.leo.message.client.animation.unit.AnimationUnit;

public class MultiAnimationOrder implements AnimationOrder {

	public final List<AnimationUnit> units;
	public final int delay;
	public final int nFrame;

	@Override
	public int getDelay() {
		return delay;
	}

	@Override
	public int getFrameCount() {
		return nFrame;
	}

	@Override
	public void goToEnd() {
		for (AnimationUnit u : units) {
			u.goToEnd();
		}
	}

	@Override
	public void hide() {
		for (AnimationUnit u : units) {
			u.hide();
		}
	}

	@Override
	public void nextFrame() {
		for (AnimationUnit u : units) {
			u.nextFrame();
		}
	}

	@Override
	public void paintFrame(Graphics g) {
		for (AnimationUnit u : units) {
			u.paintFrame(g);
		}
	}

	@Override
	public void show() {
		for (AnimationUnit u : units) {
			u.show();
		}
	}

	@Override
	public void stop() {

	}

	public MultiAnimationOrder(List<? extends AnimationUnit> units, int delay) {
		if (units.isEmpty() || delay < 0) {
			throw new NullPointerException();
		}
		this.units = new ArrayList<>(units);
		this.delay = delay;
		this.nFrame = units.get(0).getFrameCount();
	}
}
