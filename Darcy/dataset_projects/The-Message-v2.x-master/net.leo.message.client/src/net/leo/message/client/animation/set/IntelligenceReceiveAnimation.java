package net.leo.message.client.animation.set;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Queue;
import net.leo.message.client.element.GameCard;
import net.leo.message.client.animation.order.AnimationOrder;
import net.leo.message.client.animation.order.BasicAnimationOrder;
import net.leo.message.client.animation.unit.AnimationUnit;
import net.leo.message.client.animation.unit.BasicFlipUnit;
import net.leo.message.client.animation.unit.BasicMoveUnit;
import net.leo.message.client.animation.unit.MoveFlipUnit;
import net.leo.message.client.animation.unit.ResizableMoveUnit;
import net.leo.message.client.player.CountView;

public class IntelligenceReceiveAnimation implements Animation {

	private final Component comp;

	private GameCard intel1 = null, intel2 = null;
	private Rectangle bottom = null, top = null;
	private CountView dest = null;
	private int nFrame1 = -1, nFrame2 = -1, delay1 = -1, delay2 = -1;


	public void descendTo(CountView dest, int nFrame, int pause) {
		if (dest == null) {
			throw new NullPointerException();
		}
		if (nFrame < 0 || pause < 0) {
			throw new IllegalArgumentException();
		}
		this.dest = dest;
		this.nFrame2 = nFrame;
		this.delay2 = pause;
	}

	public void flipTo(GameCard next, Rectangle top, int nFrame, int delay) {
		if (next == null) {
			throw new NullPointerException();
		}
		riseTo(top, nFrame, delay);
		intel2 = next;
	}

	@Override
	public void offerToQueue(Queue<? super AnimationOrder> queue) {
		if (intel1 == null || intel2 == null || bottom == null || top == null || dest == null || nFrame1 < 0 || nFrame2 < 0 || delay1 < 0 || delay2 < 0) {
			throw new IllegalStateException();
		}
		Dimension size = top.getSize();

		boolean flip = intel2 != null;
		final AnimationUnit rise;
		// no rise
		if (bottom.equals(top)) {
			if (flip) {
				rise = new BasicFlipUnit(intel1.getImage(size), intel2.getImage(size), top, nFrame1, comp);
			}
			else {
				rise = null;
			}
		}
		else {
			if (flip) {
				rise = new MoveFlipUnit(intel1.getImage(size), intel2.getImage(size), bottom, top, nFrame1, comp);
			}
			else {
				if (bottom.getSize().equals(size)) {
					rise = new BasicMoveUnit(intel1.getImage(size), bottom.getLocation(), top.getLocation(), size, nFrame1, comp);
				}
				else {
					rise = new ResizableMoveUnit(intel1.getImage(size), bottom, top, nFrame1, comp);
				}
			}
		}

		final GameCard received = flip ? intel2 : intel1;
		final AnimationUnit descend = new ResizableMoveUnit(received.getImage(size), top, dest.getBoundsFitCardRelativeTo(comp), nFrame2, comp);

		if (rise == null) {
			queue.add(new BasicAnimationOrder(descend, delay1));
		}
		else {
			queue.add(new BasicAnimationOrder(rise, delay1));
			queue.add(new BasicAnimationOrder(descend, delay2));
		}
	}

	public void riseTo(Rectangle top, int nFrame, int delay) {
		if (top == null) {
			throw new NullPointerException();
		}
		if (nFrame < 0 || delay < 0) {
			throw new IllegalStateException();
		}
		this.top = top.getBounds();
		this.nFrame1 = nFrame;
		this.delay1 = delay;
		this.intel2 = null;
	}

	public IntelligenceReceiveAnimation(GameCard orgIntel, Rectangle bottom, Component comp) {
		if (orgIntel == null || bottom == null || comp == null) {
			throw new NullPointerException();
		}
		this.intel1 = orgIntel;
		this.bottom = bottom.getBounds();
		this.comp = comp;
	}
}
