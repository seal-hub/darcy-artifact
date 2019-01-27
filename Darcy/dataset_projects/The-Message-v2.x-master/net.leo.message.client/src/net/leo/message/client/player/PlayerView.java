package net.leo.message.client.player;

import java.awt.Color;
import static java.awt.Color.WHITE;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import net.leo.message.base.lang.CardColor;
import net.leo.message.client.animation.executor.AnimationPainter;
import net.leo.message.client.animation.set.Animation;
import static net.leo.message.client.animation.set.Animation.UPDATE;
import net.leo.message.client.animation.set.FadeAnimation;
import net.leo.message.client.animation.set.FlipAnimation;
import net.leo.message.client.animation.set.MoveAnimation;
import net.leo.message.client.element.CharCard;
import net.leo.message.client.element.CondMark;
import net.leo.message.client.element.Death;
import net.leo.message.client.element.IdenCard;
import static net.leo.message.client.player.PlayerBox.requiresLegalSeat;
import static net.leo.message.client.player.PlayerBox.toRelativeSeat;
import net.leo.message.client.selection.Selectable;
import net.leo.message.client.utility.ArrowTarget;
import static net.leo.message.client.utility.CalculationUtility.round;
import net.leo.message.client.utility.Hint;
import net.leo.message.client.utility.HintLayer;
import net.leo.message.client.utility.Timable;
import net.leo.message.client.utility.TimeRoll;

/**
 * A panel that display a net.leo.message.client.player in the game view. It contains a {@link CharView} , 4 {@link CountView} and a {@link TimeRoll}.
 * @author Leo Zeng
 */
public class PlayerView extends JComponent implements Comparable<PlayerView>, ArrowTarget, Timable, Selectable {

	private static final Color DARK = new Color(0f, 0f, 0f, 0.9f);
	private static final int N_COND = 200 / UPDATE;
	private static final int N_FADE_CHAR = 3000 / UPDATE;
	private static final int N_FADE_IDEN = 200 / UPDATE;
	private static final int N_FLIP_CHAR = 400 / UPDATE;
	private static final int N_FLIP_IDEN = 400 / UPDATE;
	private static final int TIME_FLIP_IDENTITY_PAUSE = 1000;
	private static final Color GREEN_LIGHT = new Color(0f, 1f, 0f, 0f);

	class CharView extends JComponent implements MouseInputListener {

		private boolean msl = false;
		private boolean mml = false;

		private CharView() {
			super();
			setBorder(BorderFactory.createLoweredBevelBorder());
		}

		private void checkListener() {
			boolean unknownChar = aprChar == CharCard.COVERED_CARD && realChar == CharCard.COVERED_CARD;
			boolean rmsl = unknownChar && iden == IdenCard.COVERED_CARD;
			boolean rmml = unknownChar;

			if (msl) {
				if (rmsl) {
					removeMouseListener(this);
					msl = false;
				}
			}
			else {
				if (!rmsl) {
					addMouseListener(this);
					msl = true;
				}
			}

			if (mml) {
				if (rmml) {
					removeMouseMotionListener(this);
					mml = false;
				}
			}
			else {
				if (!rmml) {
					addMouseMotionListener(this);
					mml = true;
				}
			}
		}

		private Rectangle getCondAnimInitBounds() {
			Rectangle end = getCondBounds();
			end.x -= end.width * 1.5;
			end.y -= end.height * 1.5;
			end.width *= 3;
			end.height *= 3;
			return end;
		}

		private Rectangle getCondBounds() {
			Insets inset = getInsets();
			int x = round((getWidth() - (inset.left + inset.right)) * 132d / 280) + inset.left;
			int h = (getHeight() - (inset.top + inset.bottom)) + inset.top;
			int y = round(h * 72d / 242);
			int len = round(h * 96d / 242);
			return new Rectangle(x, y, len, len);
		}

		private Rectangle getDeathBounds() {
			Insets inset = getInsets();
			int x = (getWidth() - inset.left - inset.right) * 50 / 283;
			int y = (getHeight() - inset.top + inset.bottom) * 2 / 3;
			int h = getHeight() / 4;
			int w = (int) (h * Death.RATIO_WIDTH_HEIGHT);
			return new Rectangle(x, y, w, h);
		}

		private Rectangle getIdenAnimBounds() {
			Dimension size = getSize();
			int width = round(size.width * 0.61875);
			int height = round(width / IdenCard.RATIO_WIDTH_HEIGHT);
			int x = size.width / 3;
			int y = (getHeight() - height) / 2;
			return new Rectangle(x, y, width, height);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			// If is playing flip animation, do not draw native character card.
			// Flip and peek animation should not play int the same time.
			if (!flipPainter.paintAnimation(g)) {
				//Measure size
				Insets inset = getInsets();
				Dimension size = new Dimension(getWidth() - (inset.left + inset.right), getHeight() - (inset.top + inset.bottom));

				// draw native character
				Image charImage;
				if (death != null) {
					charImage = aprChar.getDeath(size);
				}
				else if (realChar == CharCard.COVERED_CARD) {
					charImage = aprChar.getImage(size);
				}
				else {
					charImage = mouseOn ? realChar.getImage(size) : aprChar.getImage(size);
				}
				g.drawImage(charImage, inset.left, inset.top, size.width, size.height, null);

				//Draw death
				if (death != null) {
					Rectangle dBounds = getDeathBounds();
					g.drawImage(death.getImage(dBounds.getSize()), dBounds.x, dBounds.y, dBounds.width, dBounds.height, null);
				}

				//Always use peek painter if flip painter is not painting.
				peekPainter.paintAnimation(g);
			}

			// If is playing identity card animation, do not draw native identity card
			// If identity is unknown, nothing is drawn
			if (!idenPainter.paintAnimation(g) && mouseOn && iden != IdenCard.COVERED_CARD) {
				Rectangle bounds = getIdenAnimBounds();
				Dimension size = bounds.getSize();
				Image image = idenHidden ? iden.getTranslucentImage(size) : iden.getImage(size);
				g.drawImage(image, bounds.x, bounds.y, bounds.width, bounds.height, null);
			}

			//Draw condition
			if (!condPainter.paintAnimation(g) && cond != null) {
				Rectangle bounds = getCondBounds();
				g.drawImage(cond.getImage(bounds.getSize()), bounds.x, bounds.y, bounds.width, bounds.height, null);
			}

			//Draw selected effect
			if (selected) {
				Insets inset = getInsets();
				int realHeight = getHeight() - (inset.top + inset.bottom);
				int y0 = inset.top + (int) (realHeight * 0.52), y1 = getHeight() - inset.bottom;
				Paint grad = new GradientPaint(0, y0, GREEN_LIGHT, 0, y1, WHITE);

				Graphics2D g2d = (Graphics2D) g;
				g2d.setPaint(grad);
				g2d.fillRect(inset.left, y0, getWidth() - (inset.left + inset.right), y1 - y0);
			}
		}


		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mouseDragged(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			mouseOn = true;

			//Stop this peek animation if is playing. Only this.
			peekPainter.clear();

			repaint();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			mouseOn = false;
			repaint();
			hintLayer.hideHint();
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			hintLayer.showHint(SwingUtilities.convertPoint(this, e.getPoint(), hintLayer), hintImage);
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}
	}

	private class PlayerViewLayout implements LayoutManager {

		@Override
		public void addLayoutComponent(String name, Component comp) {
		}

		@Override
		public void layoutContainer(Container parent) {
			//Measure the ratio of actual size and designed size and get the min value of horizontal and vertical one
			int x, y, width, height;
			Dimension size = parent.getSize();
			Insets inset = getInsets();
			double ratioHor = (double) (size.width - inset.left + inset.right) / 168;
			double ratioVer = (double) (size.height - inset.top - inset.bottom) / 216;
			double ratio = Math.min(ratioHor, ratioVer);

			//Set id view
			x = round(ratio * 4);
			int sp = x;
			width = size.width - sp * 2;
			height = round(ratio * 18);
			idView.setBounds(sp, sp, width, height);

			//Set character view
			y = round(ratio * 25);
			height = round(ratio * 138);
			charView.setBounds(x, y, width, height);

			//Set identity card view
			int idnY = round(ratio * 166);
			width = round(ratio * 30);
			height = round(ratio * 46);
			idenView.setBounds(x, idnY, width, height);

			//Set counts view
			int len = width;
			y = round(ratio * 182);
			x = round(ratio * 69);
			bCount.setBounds(x, y, len, len);
			x = round(ratio * 101.5);
			kCount.setBounds(x, y, len, len);
			x = round(ratio * 134);
			hCount.setBounds(x, y, len, len);
			x = round(ratio * 36.5);
			rCount.setBounds(x, y, len, len);

			//Set time roll
			width = round(ratio * 127.5);
			height = round(ratio * 13.5);
			roll.setBounds(x, idnY, width, height);
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return new Dimension(0, 0);
		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			//Designed size
			return new Dimension(168, 216);
		}

		@Override
		public void removeLayoutComponent(Component comp) {
		}
	}

	/**
	 * This net.leo.message.client.player's seat.
	 */
	public final int seat;
	/**
	 * Seat of client.
	 */
	public final int mySeat;
	/**
	 * Count of players in this game.
	 */
	public final int nPlayer;
	/**
	 * This net.leo.message.client.player;s id.
	 */
	public final String playerId;

	AnimationPainter flipPainter;
	AnimationPainter peekPainter;
	AnimationPainter idenPainter;
	AnimationPainter condPainter;
	HintLayer hintLayer;
	IdView idView;
	IdenView idenView;
	TimeRoll roll;
	CountView rCount;
	CountView bCount;
	CountView kCount;
	CountView hCount;
	CharView charView;
	PlayerViewListener listener = null;

	private Death death = null;
	private boolean selected = false;
	private CharCard aprChar = CharCard.COVERED_CARD;
	private CharCard realChar = CharCard.COVERED_CARD;
	private IdenCard iden = IdenCard.COVERED_CARD;
	private CondMark cond = null;
	private Image hintImage = null;
	private boolean idenHidden = true;
	private volatile boolean mouseOn = false;

	PlayerView(int seat, int mySeat, int nPlayer, String playerId) {
		super();
		requiresLegalSeat(seat, nPlayer);
		requiresLegalSeat(mySeat, nPlayer);

		this.playerId = playerId;
		this.seat = seat;
		this.mySeat = mySeat;
		this.nPlayer = nPlayer;

		setLayout(new PlayerViewLayout());
		setForeground(DARK);
	}

	void addCondAnim(CondMark cond) {
		if (cond == null) {
			throw new NullPointerException();
		}
		Rectangle bottom = charView.getCondBounds();
		condPainter.addAnimation(new MoveAnimation(cond.getImage(bottom.getSize()), charView.getCondAnimInitBounds(), bottom, N_COND, 0, charView));
	}

	void createGUI() {

		this.idView = new IdView(playerId);
		this.idenView = new IdenView();
		this.roll = new TimeRoll(false);
		this.charView = new CharView();

		add(idView);
		add(charView);
		add(roll);
		add(idenView);
		add(hCount);
		add(rCount);
		add(bCount);
		add(kCount);
	}

	/**
	 * Flips this character card.
	 * @param character the next face of this character
	 * @param hint      hint of this character, or null
	 */
	void flipTo(CharCard character, Hint hint) {
		if (character == null) {
			throw new NullPointerException();
		}

		//Measure bounds and add animation
		Insets inset = charView.getInsets();
		Rectangle bounds = new Rectangle(inset.left, inset.top, charView.getWidth() - (inset.left + inset.right), charView.getHeight() - (inset.top + inset.bottom));
		Animation anim = new FlipAnimation(aprChar.getImage(bounds.getSize()), character.getImage(bounds.getSize()), bounds, N_FLIP_CHAR, 0, charView);
		flipPainter.addAnimation(anim);

		//Renew information
		aprChar = character;
		if (realChar == aprChar) {
			realChar = CharCard.COVERED_CARD;
		}
		hintImage = hint == null ? null : hint.createImage();
		charView.checkListener();
	}

	void peek(CharCard character, Hint hint) {
		if (character == null) {
			throw new NullPointerException();
		}

		//If the character peeked is same to the apparent animation, don't register any animation
		if (aprChar == character) {
			return;
		}

		//Measure bounds and add animation
		Insets inset = charView.getInsets();
		Rectangle bounds = new Rectangle(inset.left, inset.top, charView.getWidth() - (inset.left + inset.right), charView.getHeight() - (inset.top + inset.bottom));
		Animation anim = new FadeAnimation(character.getImage(bounds.getSize()), bounds, false, N_FADE_CHAR, 0, charView);
		peekPainter.addAnimation(anim);

		//Renew information
		realChar = character;
		hintImage = hint == null ? null : hint.createImage();
		charView.checkListener();
	}

	void setCond(CondMark cond) {
		if (this.cond == cond) {
			return;
		}
		this.cond = cond;
		charView.repaint(charView.getCondBounds());
	}

	void setIden(IdenCard iden, boolean hidden) {
		if (iden == null) {
			throw new NullPointerException();
		}

		//Measure bounds
		Rectangle bounds = charView.getIdenAnimBounds();
		Dimension size = bounds.getSize();
		Image image1 = hidden ? IdenCard.COVERED_CARD.getTranslucentImage(size) : IdenCard.COVERED_CARD.getImage(size);
		Image image2 = hidden ? iden.getTranslucentImage(size) : iden.getImage(size);

		//Register animations
		idenPainter.addAnimation(new FadeAnimation(image1, bounds, true, N_FADE_IDEN, 0, charView));
		idenPainter.addAnimation(new FlipAnimation(image1, image2, bounds, N_FLIP_IDEN, 0, charView));
		idenPainter.addAnimation(new FlipAnimation(image2, image1, bounds, N_FLIP_IDEN, TIME_FLIP_IDENTITY_PAUSE, charView));
		idenPainter.addAnimation(new FadeAnimation(image1, bounds, false, N_FADE_IDEN, 0, charView));

		//renew information
		if (iden == IdenCard.COVERED_CARD) {
			PlayerView.this.iden = null;
			idenHidden = true;
		}
		else {
			PlayerView.this.iden = iden;
			idenHidden = hidden;
		}
		charView.checkListener();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	@Override
	public int compareTo(PlayerView o) {
		//Less absolute seat is put behind
		int abs = toRelativeSeat(seat, mySeat, nPlayer);
		if (abs == 0) {
			abs = nPlayer;
		}
		int oAbs = toRelativeSeat(o.seat, mySeat, nPlayer);
		if (oAbs == 0) {
			oAbs = nPlayer;
		}
		return oAbs - abs;
	}

	/**
	 * Gets the count view of this net.leo.message.client.player.
	 * @param type count view type
	 */
	public CountView getCountView(CardColor type) {
		if (type == null) {
			return hCount;
		}
		else {
			switch (type) {
			case RED:
				return rCount;
			case BLUE:
				return bCount;
			case BLACK:
				return kCount;
			default:
				throw new InternalError();
			}
		}
	}

	@Override
	public Point getTargetCenterPoint() {
		Insets inset = getInsets();
		int width = getWidth() - (inset.left + inset.right);
		int height = getHeight() - (inset.top + inset.bottom);
		return new Point(inset.left + width / 2, inset.top + height / 2);
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param character
	 * @param hint
	 */
	public void setApprChar(CharCard character, Hint hint) {
		if (character == null) {
			throw new NullPointerException();
		}
		if (aprChar == character) {
			return;
		}

		aprChar = character;
		hintImage = hint == null ? null : hint.createImage();
		if (realChar == aprChar) {
			realChar = CharCard.COVERED_CARD;
		}
		charView.checkListener();
		repaint();
	}

	/**
	 * Lets this character die.
	 * @param death    death
	 * @param apparent character
	 * @param hint     new hint, or null
	 */
	public void setDeath(CharCard apparent, Hint hint, Death death) {
		this.aprChar = apparent;
		this.realChar = CharCard.COVERED_CARD;
		this.death = death;
		if (hint != null) {
			this.hintImage = hint.createImage();
		}
		charView.checkListener();
		repaint();
	}

	@Override
	public void setSelected(boolean status) {
		if (this.selected != status) {
			this.selected = status;
			repaint(); //TODO determine clip
		}
	}

	@Override
	public void update(double rate) {
		roll.update(rate);
	}
}