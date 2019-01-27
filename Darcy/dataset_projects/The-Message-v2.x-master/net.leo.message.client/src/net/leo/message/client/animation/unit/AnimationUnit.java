package net.leo.message.client.animation.unit;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import net.leo.message.client.animation.order.BasicAnimationOrder;
import net.leo.message.client.animation.set.Animation;

public abstract class AnimationUnit {

	protected final Image image;

	private final Component comp;
	private final int nFrame;
	private int currentFrame = 0;
	private volatile boolean visible = true;

	protected final void callRepaint() {
		Rectangle r = getClip();
		comp.repaint(r.x, r.y, r.width, r.height);
	}

	protected abstract Rectangle getClip();

	protected final double getProgressRate() {
		return (double) currentFrame / nFrame;
	}

	protected abstract void paint(Graphics g);



	public Animation createAnimation(int delay) {
		return q -> q.offer(new BasicAnimationOrder(this, delay));
	}

	public final int getFrameCount() {
		return nFrame;
	}

	public final void goToEnd() {
		if (currentFrame >= nFrame) {
			return;
		}
		callRepaint();
		currentFrame = nFrame;
		callRepaint();
	}

	public final void hide() {
		visible = false;
		callRepaint();
	}

	public final void nextFrame() {
		if (currentFrame >= nFrame) {
			return;
		}
		callRepaint();
		currentFrame++;
		callRepaint();
	}

	public final void paintFrame(Graphics g) {
		if (visible) {
			paint(g);
		}
	}

	public final void show() {
		visible = true;
		callRepaint();
	}

	public AnimationUnit(Image image, int nFrame, Component comp) {
		if (comp == null || image == null) {
			throw new NullPointerException();
		}
		if (nFrame <= 0) {
			throw new IllegalArgumentException();
		}
		this.nFrame = nFrame;
		this.image = image;
		this.comp = comp;
	}

}
