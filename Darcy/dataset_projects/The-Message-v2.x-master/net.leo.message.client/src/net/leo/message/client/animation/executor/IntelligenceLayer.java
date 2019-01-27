package net.leo.message.client.animation.executor;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JLayeredPane;
import net.leo.message.client.animation.set.Animation;
import net.leo.message.client.animation.set.ChangeAnimation;
import net.leo.message.client.animation.set.FlipAnimation;
import net.leo.message.client.animation.set.IntelligenceReceiveAnimation;
import net.leo.message.client.animation.set.MoveAnimation;
import net.leo.message.client.domain.GameView;
import net.leo.message.client.element.GameCard;
import net.leo.message.client.player.CountView;
import net.leo.message.client.player.PlayerBox;
import net.leo.message.client.utility.CalculationUtility;
import static net.leo.message.client.utility.CalculationUtility.round;

/**
 * A layer that plays intelligence concerning animation.
 * @author Leo Zeng
 */
public class IntelligenceLayer extends JLayeredPane implements IntelligenceTable, ComponentListener {

	/**
	 * Pause time between intelligence shown and moved and rised and descended.
	 */
	public static final int TIME_PAUSE = 500;
	/**
	 * Count of frame of intelligence changed animation.
	 */
	public static final int N_CHANGE = 800 / Animation.UPDATE;
	/**
	 * Count of frame of intelligence risen stage.
	 */
	public static final int N_RISE = 1000 / Animation.UPDATE;
	/**
	 * Count of frame of intelligence descended stage.
	 */
	public static final int N_DESCEND = 200 / Animation.UPDATE;


	private final int mySeat, nPlayer;
	private final GameView.Locator locator;
	private final AnimationGear animationGear = new AnimationGear(1);
	private GameCard intel = null;
	private int location = -1;
	private final ShineGear shineGear;
	private final AnimationPainter painter = animationGear.createPainter();

	/**
	 * Constructs a layer playing intelligence animation.
	 * @param mySeat  client's seat
	 * @param nPlayer count of players in this game
	 * @param locator locator
	 */
	public IntelligenceLayer(int mySeat, int nPlayer, GameView.Locator locator, ShineGear gear) {
		super();
		if (locator == null || gear == null) {
			throw new NullPointerException();
		}
		this.mySeat = mySeat;
		this.nPlayer = nPlayer;
		this.locator = locator;
		this.shineGear = gear;
		addComponentListener(this);
	}

	private Rectangle getIntelBounds(int seat) {
		return new Rectangle(getIntelLocation(seat), getIntelSize());
	}

	private Point getIntelLocation(int seat) {
		int absSeat = PlayerBox.toAbsoluteSeat(seat, mySeat, nPlayer);
		if (absSeat == 0) {
			return locator.iMe();
		}

		switch (nPlayer) {
		case 9:
			switch (absSeat) {
			case 1:
				return locator.iRight1();
			case 2:
				return locator.iRight3();
			case 3:
				return locator.iTop7();
			case 4:
				return locator.iTop5();
			case 5:
				return locator.iTop3();
			case 6:
				return locator.iTop1();
			case 7:
				return locator.iLeft3();
			case 8:
				return locator.iLeft1();
			}
		case 8:
			switch (absSeat) {
			case 1:
				return locator.iRight1();
			case 2:
				return locator.iRight3();
			case 3:
				return locator.iTop6();
			case 4:
				return locator.iTop4();
			case 5:
				return locator.iTop2();
			case 6:
				return locator.iLeft3();
			case 7:
				return locator.iLeft1();
			}
		case 7:
			switch (absSeat) {
			case 1:
				return locator.iRight2();
			case 2:
				return locator.iTop7();
			case 3:
				return locator.iTop5();
			case 4:
				return locator.iTop3();
			case 5:
				return locator.iTop1();
			case 6:
				return locator.iLeft2();
			}
		case 6:
			switch (absSeat) {
			case 1:
				return locator.iRight2();
			case 2:
				return locator.iTop6();
			case 3:
				return locator.iTop4();
			case 4:
				return locator.iTop2();
			case 5:
				return locator.iLeft2();
			}
		case 5:
			switch (absSeat) {
			case 1:
				return locator.iRight2();
			case 2:
				return locator.iTop5();
			case 3:
				return locator.iTop3();
			case 4:
				return locator.iLeft2();
			}
		case 4:
			switch (absSeat) {
			case 1:
				return locator.iRight2();
			case 2:
				return locator.iTop4();
			case 3:
				return locator.iLeft2();
			}
		case 3:
			switch (absSeat) {
			case 1:
				return locator.iTop5();
			case 2:
				return locator.iTop3();
			}
		case 2:
			return locator.iTop4();
		}

		throw new InternalError();
	}

	private Dimension getIntelSize() {
		return locator.getIntelSize();
	}

	private int getSpeed() {
		if ((double) getWidth() / getHeight() < 0.75d) {
			return round(getWidth() * 0.6);
		}
		else {
			return round(getHeight() * 0.8);
		}
	}

	private Rectangle getTopBounds(int seat) {
		Rectangle bottom = getIntelBounds(seat);
		Dimension rise = getTopCardSize();
		int x = (int) (bottom.getCenterX()) - rise.width / 2;
		int y = (int) (bottom.getCenterY()) - rise.height / 2;
		return new Rectangle(new Point(x, y), rise);
	}

	private Dimension getTopCardSize() {
		return locator.getCardSize();
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (!painter.paintAnimation(g) && intel != null) {
			Rectangle bounds = getIntelBounds(location);
			g.drawImage(intel.getImage(bounds.getSize()), bounds.x, bounds.y, bounds.width, bounds.height, null);
		}
	}


	/**
	 * Plays an intelligence changed animation.
	 * @param next the new intelligence
	 * @throws IllegalStateException if no intelligence is on the table
	 */
	@Override
	public void changeTo(GameCard next) {
		if (intel == null) {
			throw new IllegalStateException();
		}

		Rectangle bounds = getIntelBounds(location);
		painter.addAnimation(new ChangeAnimation(intel.getImage(bounds.getSize()), next.getImage(bounds.getSize()), bounds, N_CHANGE, 0, this));
		animationGear.run(true, null);
		intel = next;
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentResized(ComponentEvent e) {
		if (intel != null) {
			repaint();
		}
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

	/**
	 * Flips intelligence and lets a net.leo.message.client.player receive it,
	 * @param next the other side of intelligence
	 * @param dest destination
	 * @throws IllegalStateException if no intelligence is on the table
	 */
	@Override
	public void flipAndReceive(GameCard next, CountView dest) {
		if (intel == null) {
			throw new IllegalStateException();
		}

		IntelligenceReceiveAnimation anim = new IntelligenceReceiveAnimation(intel, getIntelBounds(location), this);
		anim.flipTo(next, getTopBounds(location), N_RISE, 0);
		anim.descendTo(dest, N_DESCEND, TIME_PAUSE);

		painter.addAnimation(anim);
		animationGear.run(true, () -> {
			dest.offer(next.getElement());
			shineGear.run(new ShineInfo(dest, 1));
			intel = null;
			location = -1;
		});
	}

	/**
	 * Flips intelligence.
	 * @param next the other side of intelligence
	 * @throws IllegalStateException if no intelligence is on the table
	 */
	@Override
	public void flipTo(GameCard next) {
		if (intel == null) {
			throw new IllegalStateException();
		}

		Rectangle bottom = getIntelBounds(location);
		Rectangle top = getTopBounds(location);
		FlipAnimation flip = new FlipAnimation(intel.getImage(top.getSize()), next.getImage(top.getSize()), bottom, top, N_RISE, 0, this);
		MoveAnimation desc = new MoveAnimation(next.getImage(top.getSize()), top, bottom, N_DESCEND, 0, this);
		painter.addAnimation(flip);
		painter.addAnimation(desc);
		animationGear.run(true, null);
		intel = next;
	}

	/**
	 * Moves intelligence to another location. If the destination is same to the current location, no animation and no space time.
	 * @param dest seat of destination net.leo.message.client.player
	 * @throws IllegalStateException if no intelligence is on the table
	 */
	@Override
	public void moveTo(int dest) {
		if (intel == null) {
			throw new IllegalStateException();
		}

		if (location == dest) {
			SyncController.inform();
			return;
		}

		Rectangle org = getIntelBounds(location), des = getIntelBounds(dest);
		int nFrame = CalculationUtility.speedToFrameCount(org.getLocation(), des.getLocation(), getSpeed());
		MoveAnimation move = new MoveAnimation(intel.getImage(org.getSize()), org, des, nFrame, 0, this);
		painter.addAnimation(move);
		animationGear.run(true, null);
		location = dest;
	}

	/**
	 * Lets a net.leo.message.client.player receive intelligence
	 * @param dest seat of net.leo.message.client.player
	 * @throws IllegalStateException if no intelligence is on the table
	 */
	@Override
	public void receive(CountView dest) {
		if (intel == null) {
			throw new IllegalStateException();
		}

		Rectangle top = getTopBounds(location);
		MoveAnimation rise = new MoveAnimation(intel.getImage(top.getSize()), getIntelBounds(location), top, N_RISE, 0, this);
		MoveAnimation desc = new MoveAnimation(intel.getImage(top.getSize()), top, dest.getBoundsFitCardRelativeTo(this), N_DESCEND, TIME_PAUSE, this);

		painter.addAnimation(rise);
		painter.addAnimation(desc);
		animationGear.run(true, () -> {
			dest.offer(intel.getElement());
			shineGear.run(new ShineInfo(dest, 1));
			intel = null;
			location = -1;
		});
	}

	/**
	 * Lets a net.leo.message.client.player send an intelligence to another net.leo.message.client.player
	 * @param intel intelligence card
	 * @param org   seat of net.leo.message.client.player who sends intelligence
	 * @param dest  seat of destination net.leo.message.client.player
	 * @throws IllegalStateException if there has been an intelligence on the table
	 */
	@Override
	public void sendTo(GameCard intel, int org, int dest) {
		if (this.intel != null) {
			throw new IllegalStateException();
		}
		if (org == dest) {
			throw new IllegalArgumentException();
		}

		Rectangle orgr = getIntelBounds(org), desr = getIntelBounds(dest);
		int nFrame = CalculationUtility.speedToFrameCount(orgr.getLocation(), desr.getLocation(), getSpeed());
		MoveAnimation move = new MoveAnimation(intel.getImage(orgr.getSize()), orgr, desr, nFrame, TIME_PAUSE, this);
		painter.addAnimation(move);
		animationGear.run(true, null);
		this.intel = intel;
		location = dest;
	}
}

