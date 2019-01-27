package net.leo.message.client.animation.executor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import java.awt.Shape;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import net.leo.message.client.animation.set.Animation;
import static net.leo.message.client.animation.set.Animation.UPDATE;
import net.leo.message.client.animation.set.DrawAnimation;
import net.leo.message.client.animation.set.DropAnimation;
import net.leo.message.client.animation.set.FlyAnimation;
import net.leo.message.client.animation.set.MoveAnimation;
import net.leo.message.client.domain.GameView;
import net.leo.message.client.element.GameCard;
import net.leo.message.client.element.Skill;
import net.leo.message.client.player.CountView;
import net.leo.message.client.player.PlayerView;
import net.leo.message.client.stack.ArrowLayer;
import static net.leo.message.client.stack.ArrowLayer.Arrow.COLOR_ARROW;
import static net.leo.message.client.stack.ArrowLayer.Arrow.STROKE;
import net.leo.message.client.stack.FuncCardView;
import net.leo.message.client.stack.StackBox;
import net.leo.message.client.utility.ArrowTarget;
import net.leo.message.client.utility.CalculationUtility;
import static net.leo.message.client.utility.CalculationUtility.ratio;
import static net.leo.message.client.utility.CalculationUtility.round;

/**
 * A layer that plays card flying, drawn, dropped and used animation. Unless otherwise noted, any argument passed should not be null.
 * @author Leo Zeng
 */
public class AnimationLayer extends JLayeredPane {

	private static final int TIME_ARROW_PAUSE = 700;
	private static final int N_USE = 250 / Animation.UPDATE;
	private static final int TIME_FLY_PAUSE = 500;
	private static final int N_FLY_RISE = 333 / Animation.UPDATE;
	private static final int N_FLY_DESCEND = 200 / Animation.UPDATE;
	private static final int N_DROP_FADE = 800 / Animation.UPDATE;
	private static final int N_DROP_RISE = 333 / Animation.UPDATE;
	private static final int TIME_DRAW_PAUSE = 500;
	private static final int N_DRAW = 500 / Animation.UPDATE;
	private static final double RATIO_SLOW = 0.02d;
	private static final int N_SKILL_FAST = 100 / UPDATE;
	private static final int N_SKILL_SLOW = 1200 / UPDATE;

	private class ArrowShoot {

		private final Component layer;
		private final LinkedList<Shape> unpaintedArrows;
		private final LinkedList<Shape> paintedArrows = new LinkedList<>();

		private int update; // ms per arrow
		private int drawCount;
		private Timer timer;

		public ArrowShoot(Point from, Point to, Component layer) {
			if (layer == null || from == null || to == null) {
				throw new NullPointerException();
			}
			this.layer = layer;
			unpaintedArrows = new LinkedList<>(ArrowLayer.Arrow.calculateShapes(from, to));
			calculateUpdate(from, to);
		}

		private void calculateUpdate(Point from, Point to) {
			double dis = from.distance(to);
			double time = dis / getSpeed() * 1000; // ms
			int nArrow = unpaintedArrows.size();
			update = CalculationUtility.round(time / nArrow);
			drawCount = Math.max(1, Animation.UPDATE / update);
			update = Math.max(update, Animation.UPDATE);
		}

		private void callRepaint(Shape arrow) {
			Rectangle clip = STROKE.createStrokedShape(arrow).getBounds();
			layer.repaint(clip.x, clip.y, clip.width, clip.height);
		}

		private void drawArrow(Graphics2D g2d, Shape shape) {
			g2d.draw(shape);
		}

		private int getSpeed() {
			return (int) (getWidth() * 148d / 45);
		}

		private void invokeAddArrows() {
			timer = new Timer(update, e -> nextFrameAddArrow());
			timer.setInitialDelay(0);
			timer.start();
		}

		private void invokeRemoveArrows() {
			timer = new Timer(update, e -> nextFrameRemoveArrow());
			timer.setInitialDelay(TIME_ARROW_PAUSE);
			timer.start();
		}

		private void nextFrameAddArrow() {
			for (int i = 0 ; i < drawCount ; i++) {
				Shape arrow = unpaintedArrows.poll();
				if (arrow == null) {
					break;
				}
				paintedArrows.addLast(arrow);
				callRepaint(arrow);
			}
			if (unpaintedArrows.isEmpty()) {
				timer.stop();
				invokeRemoveArrows();
			}
		}

		private void nextFrameRemoveArrow() {
			for (int i = 0 ; i < drawCount ; i++) {
				Shape arrow = paintedArrows.poll();
				if (arrow == null) {
					break;
				}
				callRepaint(arrow);
			}
			if (paintedArrows.isEmpty()) {
				timer.stop();
				arrowShoots.remove(this);
				timer = null;   // release memory
			}
		}

		public void paint(Graphics g) {
			if (paintedArrows.isEmpty()) { // nothing needed to be painted
				return;
			}
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
			g2d.setStroke(STROKE);
			g2d.setColor(COLOR_ARROW);
			for (Shape p : paintedArrows) {
				drawArrow(g2d, p);
			}
		}

		public void start() {
			invokeAddArrows();
		}
	}

	private final GameView.Locator locator;
	private final AnimationGear gear = new AnimationGear(1);
	private final AnimationPainter painter = gear.createPainter();
	private final StackBox stackBox;
	private final List<ArrowShoot> arrowShoots = new LinkedList<>();
	private final ArrowLayer arrowLayer;
	private FlyAnimation flyAnim;
	private DropAnimation dropAnim;
	private DrawAnimation drawAnim;
	private final ShineGear shineGear;

	/**
	 * Constructs a card animation layer.
	 * @param stackBox   stack box to put function card used by players
	 * @param arrowLayer arrow layer to display arrows illustration
	 */
	public AnimationLayer(StackBox stackBox, ArrowLayer arrowLayer, GameView.Locator locator, ShineGear shineGear) {
		super();
		if (stackBox == null || arrowLayer == null || locator == null || shineGear == null) {
			throw new NullPointerException();
		}
		this.stackBox = stackBox;
		this.arrowLayer = arrowLayer;
		this.locator = locator;
		this.shineGear = shineGear;
	}

	private void renewStackBox(FuncCardView added) {
		stackBox.offer(added);
		stackBox.unlock();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		painter.paintAnimation(g);
		for (ArrowShoot as : arrowShoots) {
			as.paint(g);
		}
	}

	/**
	 * Registers a card drawn animation.
	 * @param card card drawn
	 * @param dest destination
	 */
	public void addDrawAnimation(GameCard card, CountView dest) {
		if (gear.isRunning()) {
			gear.stop();
		}
		if (drawAnim == null) {
			drawAnim = new DrawAnimation(N_DRAW, TIME_DRAW_PAUSE, this);
		}
		drawAnim.addAnimationItem(dest, card);
	}

	/**
	 * Registers a card dropped animation.
	 * @param card card dropped
	 * @param org  origin
	 */
	public void addDropAnimation(GameCard card, CountView org) {
		if (gear.isRunning()) {
			gear.stop();
		}
		if (dropAnim == null) {
			dropAnim = new DropAnimation(N_DROP_RISE, N_DROP_FADE, 0, 0, this);
		}
		dropAnim.addAnimationItem(org, card);
	}

	/**
	 * Registers a card flying animation.
	 * @param card card flying
	 * @param org  origin
	 * @param dest destination
	 */
	public void addFlyAnimation(GameCard card, CountView org, CountView dest) {
		if (gear.isRunning()) {
			gear.stop();
		}
		if (flyAnim == null) {
			flyAnim = new FlyAnimation(N_FLY_RISE, N_FLY_DESCEND, 0, TIME_FLY_PAUSE, this);
		}
		flyAnim.addAnimationItem(card, org, dest);
	}

	/**
	 * Plays an animation of cards drawn.
	 * @throws IllegalStateException if no drawing animation is registered
	 */
	public void draw() {
		if (drawAnim == null) {
			throw new IllegalStateException();
		}
		Runnable shine = drawAnim.getShineRunnable();
		painter.addAnimation(drawAnim);
		drawAnim = null;
		gear.run(true, shine);
	}

	/**
	 * Plays an animation of cards dropped.
	 * @throws IllegalStateException if no dropping animation is registered
	 */
	public void drop() {
		if (dropAnim == null) {
			throw new IllegalStateException();
		}
		painter.addAnimation(dropAnim);
		dropAnim.getShineOutRunnable().run();
		dropAnim = null;
		gear.run(true, null);
	}

	/**
	 * Plays an animation of cards flying.
	 * @throws IllegalStateException if no flying animation is registered
	 */
	public void fly() {
		if (flyAnim == null) {
			throw new IllegalStateException();
		}

		painter.addAnimation(flyAnim);
		Runnable out = flyAnim.getShineOutRunnable();
		Runnable in = flyAnim.getShineInRunnable();
		flyAnim = null;
		out.run();
		gear.run(true, in);
	}

	@Override
	public final Dimension getPreferredSize() {
		return new Dimension(1200, 900);
	}

	/**
	 * Gets the size of cards that are risen to the top,
	 * @return the size of cards that are risen to the top
	 */
	public final Dimension getRisenCardSize() {
		return locator.getCardSize();
	}

	/**
	 * Gets the shine gear of this game.
	 * @return a shine gear
	 */
	public ShineGear getShineGear() {
		return shineGear;
	}

	/**
	 * Plays a skill invoking animation.
	 * @param skill skill invoked
	 */
	public void launch(Skill skill, PlayerView invoker, ArrowTarget target) {

		//Measures skill image bounds
		int height = round(getHeight() / 3.5d);
		int width = round(height * Skill.RATIO_WIDTH_HEIGHT);
		int y = (getHeight() - height) / 2;
		Point p0 = new Point(getWidth(), y), p3 = new Point(-width, y), p1, p2;

		//Measures points at fast-slow-fast boundary
		p1 = ratio(p0, p3, (1 + RATIO_SLOW) / 2);
		p2 = ratio(p0, p3, (1 - RATIO_SLOW) / 2);

		//Runs animation
		Dimension size = new Dimension(width, height);
		painter.addAnimation(new MoveAnimation(skill.getImage(size), p0, p1, size, N_SKILL_FAST, 0, this));
		painter.addAnimation(new MoveAnimation(skill.getImage(size), p1, p2, size, N_SKILL_SLOW, 0, this));
		painter.addAnimation(new MoveAnimation(skill.getImage(size), p2, p3, size, N_SKILL_FAST, 0, this));
		gear.run(true, null);

		//Shoots arrow if should
		if (target != null && invoker != target) {
			Point userPoint = SwingUtilities.convertPoint(invoker, invoker.getTargetCenterPoint(), this);
			Point targetPoint = SwingUtilities.convertPoint((Component) target, target.getTargetCenterPoint(), this);
			ArrowShoot ac = new ArrowShoot(userPoint, targetPoint, this);
			arrowShoots.add(ac);
			ac.start();
		}
	}

	/**
	 * Plays an animation of using a function card.
	 * @param card   card used
	 * @param user   user
	 * @param target target net.leo.message.client.player, or null if no target
	 * @throws NullPointerException if argument card or user is null
	 */
	public void use(GameCard card, PlayerView user, ArrowTarget target) {
		if (card == null || user == null) {
			throw new NullPointerException();
		}

		//Locks stack box
		stackBox.lock();

		//Measures origin and destination bounds
		Rectangle org, dest;
		dest = stackBox.getNextBounds();
		Dimension cardSize = dest.getSize();
		dest.setLocation(SwingUtilities.convertPoint(stackBox, dest.getLocation(), this));
		org = new Rectangle();
		org.setLocation(SwingUtilities.convertPoint(user, user.getTargetCenterPoint(), this));
		org.setSize(cardSize);
		org.x -= org.width / 2;
		org.y -= org.height / 2;

		//Sets animation
		Animation anim = new MoveAnimation(card.getImage(org.getSize()), org, dest, N_USE, 0, this);
		painter.addAnimation(anim);
		FuncCardView added = new FuncCardView(card, user, target, arrowLayer);

		//Shoots arrow if should
		if (target != null && user != target) {
			Point userPoint = SwingUtilities.convertPoint(user, user.getTargetCenterPoint(), this);
			Point targetPoint = SwingUtilities.convertPoint((Component) target, target.getTargetCenterPoint(), this);
			ArrowShoot ac = new ArrowShoot(userPoint, targetPoint, this);
			arrowShoots.add(ac);
			ac.start();
		}

		//Lets count of hand of user -1
		//Plays animation and sets the end event
		user.getCountView(null).increase(-1);
		gear.run(true, () -> renewStackBox(added));
	}
}
