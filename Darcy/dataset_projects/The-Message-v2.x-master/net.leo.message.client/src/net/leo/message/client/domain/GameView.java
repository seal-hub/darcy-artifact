package net.leo.message.client.domain;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import javax.swing.JLayeredPane;
import net.leo.message.client.animation.executor.AnimationLayer;
import net.leo.message.client.animation.executor.DistributeLayer;
import net.leo.message.client.animation.executor.IntelligenceLayer;
import net.leo.message.client.dialog.DialogLayer;
import net.leo.message.client.hand.HandBox;
import net.leo.message.client.instruction.InstBox;
import net.leo.message.client.player.HoleLayer;
import net.leo.message.client.player.PlayerBox;
import static net.leo.message.client.player.PlayerBox.requiresLegalSeat;
import net.leo.message.client.player.PlayerView;
import net.leo.message.client.skill.SkillBox;
import net.leo.message.client.stack.ArrowLayer;
import net.leo.message.client.stack.StackBox;
import static net.leo.message.client.utility.CalculationUtility.round;
import net.leo.message.client.utility.HintLayer;
import net.leo.message.client.utility.Timer;

/**
 * A view that shows the whole game and includes all game relating components.
 * @author Leo Zeng
 */
public final class GameView extends JLayeredPane {

	private static final Integer LAYER_BOX = 20;
	private static final Integer LAYER_INTELLIGENCE = 30;
	private static final Integer LAYER_ARROW = 40;
	private static final Integer LAYER_DISTRIBUTE = 50;
	private static final Integer LAYER_ANIMATION = 60;
	private static final Integer LAYER_HOLE = 70;
	private static final Integer LAYER_INSTRUCTION = 80;
	private static final Integer LAYER_DIALOG = 90;
	private static final Integer LAYER_HINT = 100;

	/**
	 * A locator which helps locate all components. Method name beginning width letter 'i' is relating to locations of intelligence, while those beginning with letter 'p' are to
	 * locations of net.leo.message.client.player views.
	 * @author Leo Zeng
	 */
	public class Locator {

		private boolean isVerticalDisplayMode() {
			Dimension size = getPlayerSize();
			int space = getMargin();
			return getHeight() > size.height * 4 + space * 4;
		}

		private boolean isViewTooTall() {
			return (double) getHeight() / getWidth() > 0.75d;
		}

		/**
		 * Gets the standard card size on the screen.
		 * @return the standard card size on the screen
		 */
		public Dimension getCardSize() {
			final int height = round((getHeight() - pMe().y) * 0.571428571429);
			final int width = height * 3 / 4;
			return new Dimension(width, height);
		}

		/**
		 * Gets the size of game view.
		 * @return size of game view
		 */
		public Dimension getFullScreen() {
			return getSize();
		}

		/**
		 * Gets the standard intelligence size on the screen.
		 * @return the standard intelligence size on the screen
		 */
		public Dimension getIntelSize() {
			int width, height;
			if (isVerticalDisplayMode()) {
				width = round(getWidth() * 0.06);
				height = width * 4 / 3;
			}
			else {
				height = round(getHeight() * 0.10666666667);
				width = height * 3 / 4;
			}
			return new Dimension(width, height);
		}

		/**
		 * @return margin
		 */
		public int getMargin() {
			return Math.min(round(getHeight() / 75d), round(getWidth() / 100d));
		}

		/**
		 * Gets the standard size of net.leo.message.client.player view on the screen.
		 * @return the standard size of net.leo.message.client.player view on the screen
		 */
		public Dimension getPlayerSize() {
			int width, height;
			// too wide
			if (isViewTooTall()) {
				width = round(getWidth() * 0.155);
				height = width * 240 / 186;
			}
			// too tall
			else {
				height = round(getHeight() * 0.26666666666);
				width = height * 186 / 240;
			}
			return new Dimension(width, height);
		}

		public Point iLeft1() {
			Point v = pLeft1();
			Dimension size = getPlayerSize();
			int x = v.x + size.width + getMargin();
			int y = v.y + size.height - getIntelSize().height;
			return new Point(x, y);
		}

		public Point iLeft2() {
			Point v = pLeft2();
			Dimension size = getPlayerSize();
			int x = v.x + size.width + getMargin();
			int y = v.y + size.height - getIntelSize().height;
			return new Point(x, y);
		}

		public Point iLeft3() {
			Point v = pLeft3();
			Dimension size = getPlayerSize();
			int x = v.x + size.width + getMargin();
			int y = v.y + size.height - getIntelSize().height;
			return new Point(x, y);
		}

		public Point iMe() {
			Dimension intelSize = getIntelSize();
			int x = (getWidth() - intelSize.width) / 2;
			int y = pMe().y - getMargin() - intelSize.height;
			return new Point(x, y);
		}

		public Point iRight1() {
			Point v = pRight1();
			Dimension size = getPlayerSize();
			Dimension intelSize = getIntelSize();
			int x = pRight1().x - getMargin() - intelSize.width;
			int y = v.y + size.height - getIntelSize().height;
			return new Point(x, y);
		}

		public Point iRight2() {
			Point v = pRight2();
			Dimension size = getPlayerSize();
			Dimension intelSize = getIntelSize();
			int x = pRight1().x - getMargin() - intelSize.width;
			int y = v.y + size.height - getIntelSize().height;
			return new Point(x, y);
		}

		public Point iRight3() {
			Point v = pRight3();
			Dimension size = getPlayerSize();
			Dimension intelSize = getIntelSize();
			int x = v.x - getMargin() - intelSize.width;
			int y = v.y + size.height - getIntelSize().height;
			return new Point(x, y);
		}

		public Point iTop1() {
			Dimension size = getPlayerSize();
			int y = getMargin() * 2 + size.height;
			int x = pTop1().x + (size.width - getIntelSize().width) / 2;
			return new Point(x, y);
		}

		public Point iTop2() {
			Dimension size = getPlayerSize();
			int y = getMargin() * 2 + size.height;
			int x = pTop2().x + (size.width - getIntelSize().width) / 2;
			return new Point(x, y);
		}

		public Point iTop3() {
			Dimension size = getPlayerSize();
			int y = getMargin() * 2 + size.height;
			int x = pTop3().x + (size.width - getIntelSize().width) / 2;
			return new Point(x, y);
		}

		public Point iTop4() {
			Dimension size = getPlayerSize();
			int y = getMargin() * 2 + size.height;
			int x = pTop4().x + (size.width - getIntelSize().width) / 2;
			return new Point(x, y);
		}

		public Point iTop5() {
			Dimension size = getPlayerSize();
			int y = getMargin() * 2 + size.height;
			int x = pTop5().x + (size.width - getIntelSize().width) / 2;
			return new Point(x, y);
		}

		public Point iTop6() {
			Dimension size = getPlayerSize();
			int y = getMargin() * 2 + size.height;
			int x = pTop6().x + (size.width - getIntelSize().width) / 2;
			return new Point(x, y);
		}

		public Point iTop7() {
			Dimension size = getPlayerSize();
			int y = getMargin() * 2 + size.height;
			int x = pTop7().x + (size.width - getIntelSize().width) / 2;
			return new Point(x, y);
		}

		public Point pLeft1() {
			Dimension size = getPlayerSize();
			int space = getMargin();
			int x = space, y;
			if (isVerticalDisplayMode()) {
				y = (getHeight() + space) / 2;
			}
			else {
				y = pMe().y - size.height - space;
			}
			return new Point(x, y);
		}

		public Point pLeft2() {
			int x = getMargin();
			int y = (getHeight() - getPlayerSize().height) / 2;
			return new Point(x, y);
		}

		public Point pLeft3() {
			int space = getMargin();
			int x = space;
			int y = pLeft1().y - getPlayerSize().height - space;
			return new Point(x, y);
		}

		public Point pMe() {
			Dimension size = getPlayerSize();
			int space = getMargin();
			int x = space;
			int y = getHeight() - size.height - space;
			return new Point(x, y);
		}

		public Point pRight1() {
			int x = getWidth() - getPlayerSize().width - getMargin();
			int y = pLeft1().y;
			return new Point(x, y);
		}

		public Point pRight2() {
			int x = getWidth() - getPlayerSize().width - getMargin();
			int y = pLeft2().y;
			return new Point(x, y);
		}

		public Point pRight3() {
			int x = getWidth() - getPlayerSize().width - getMargin();
			int y = pLeft3().y;
			return new Point(x, y);
		}

		public Point pTop1() {
			int space = getMargin();
			int x = (getWidth() - space) / 2 - (space + 2 * getPlayerSize().width);
			int y = space;
			return new Point(x, y);
		}

		public Point pTop2() {
			int space = getMargin();
			int width = getPlayerSize().width;
			int x = (getWidth() - width) / 2 - (space + width);
			int y = space;
			return new Point(x, y);
		}

		public Point pTop3() {
			int space = getMargin();
			int x = (getWidth() - space) / 2 - getPlayerSize().width;
			int y = space;
			return new Point(x, y);
		}

		public Point pTop4() {
			int space = getMargin();
			int width = getPlayerSize().width;
			int x = (getWidth() - width) / 2;
			int y = space;
			return new Point(x, y);
		}

		public Point pTop5() {
			int space = getMargin();
			int x = (getWidth() + space) / 2;
			int y = space;
			return new Point(x, y);
		}

		public Point pTop6() {
			int space = getMargin();
			int width = getPlayerSize().width;
			int x = (getWidth() + width) / 2 + space;
			int y = space;
			return new Point(x, y);
		}

		public Point pTop7() {
			int space = getMargin();
			int width = getPlayerSize().width;
			int x = (getWidth() + space) / 2 + space + width;
			int y = space;
			return new Point(x, y);
		}
	}

	private class GameViewLayout implements LayoutManager {

		private void layoutHandBox() {
			int h = locator.getCardSize().height;
			int m = locator.getMargin();
			int y1 = getHeight() - m;
			int x0 = locator.pMe().x + locator.getPlayerSize().width + m;
			int x1 = locator.pRight1().x - m;
			handBox.setBounds(x0, y1 - h, x1 - x0, h);
		}

		private void layoutInstruction() {
			int m = locator.getMargin();
			int x0 = locator.pMe().x + locator.getPlayerSize().width + m;
			int x1 = locator.pRight1().x - m;
			int y0 = locator.iMe().y + locator.getIntelSize().height + m;
			int y1 = getHeight() - m * 2 - locator.getCardSize().height;
			instBox.setBounds(x0, y0, x1 - x0, y1 - y0);
		}

		private void layoutLayersAndPlayerBox() {
			//Calculate bounds
			Rectangle bounds = getBounds();
			bounds.x = 0;
			bounds.y = 0;

			//Set bounds
			playerBox.setBounds(bounds);
			intelLayer.setBounds(bounds);
			animLayer.setBounds(bounds);
			distLayer.setBounds(bounds);
			arrowLayer.setBounds(bounds);
			dialogLayer.setBounds(bounds);
			hintLayer.setBounds(bounds);
			holeLayer.setBounds(bounds);
		}

		private void layoutSkillBox() {
			int m = locator.getMargin();
			int x1 = getWidth() - m;
			int x0 = locator.pRight1().x;
			int y1 = getHeight() - m;
			int y0 = locator.iMe().y + locator.getIntelSize().height + m;
			skillBox.setBounds(x0, y0, x1 - x0, y1 - y0);
		}

		private void layoutStackBox() {
			int m = locator.getMargin();
			Dimension intel = locator.getIntelSize();
			Dimension card = locator.getCardSize();
			int x0 = locator.iLeft1().x + intel.width + m;
			int x1 = locator.iRight1().x - m;
			int top = locator.iTop4().y + intel.height;
			int bottom = locator.iMe().y;
			int y0 = (top + bottom - card.height) / 2;
			stackBox.setBounds(x0, y0, x1 - x0, card.height);
		}

		@Override
		public void addLayoutComponent(String name, Component comp) {
		}

		@Override
		public void layoutContainer(Container parent) {
			layoutLayersAndPlayerBox();
			layoutSkillBox();
			layoutHandBox();
			layoutInstruction();
			layoutStackBox();
			dialogLayer.validate(); //???
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return new Dimension(0, 0);
		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			return new Dimension(1200, 900);
		}

		@Override
		public void removeLayoutComponent(Component comp) {
		}
	}

	private final Locator locator = new Locator();

	private InstBox instBox;
	private PlayerBox playerBox;
	private StackBox stackBox;
	private SkillBox skillBox;
	private HandBox handBox;

	private AnimationLayer animLayer;
	private IntelligenceLayer intelLayer;
	private DistributeLayer distLayer;
	private HoleLayer holeLayer;
	private ArrowLayer arrowLayer;
	private HintLayer hintLayer;
	private DialogLayer dialogLayer;

	private Timer timer;

	/**
	 * Client's seat.
	 */
	public final int mySeat;
	/**
	 * Count of players in this game.
	 */
	public final int nPlayer;

	/**
	 * Constructs a game view.
	 * @param playerIds id of each net.leo.message.client.player
	 * @param mySeat    client's seat
	 */
	public GameView(String[] playerIds, int mySeat) {
		super();
		requiresLegalSeat(mySeat, playerIds.length);
		this.mySeat = mySeat;
		this.nPlayer = playerIds.length;
		this.timer = new Timer();
		setLayout(new GameViewLayout());
		createGUI(playerIds, mySeat);
	}


	private void createGUI(String[] playerIds, int mySeat) {
		instBox = new InstBox();
		stackBox = new StackBox();
		handBox = new HandBox();
		//Constructs boxes
		hintLayer = new HintLayer();
		dialogLayer = new DialogLayer(locator);
		playerBox = new PlayerBox(mySeat, playerIds, hintLayer, locator, dialogLayer);
		skillBox = new SkillBox(hintLayer);

		//Constructs other layers
		arrowLayer = stackBox.getArrowLayer();
		intelLayer = new IntelligenceLayer(mySeat, nPlayer, locator, playerBox.getShineGear());
		distLayer = new DistributeLayer(locator, playerBox.getShineGear());
		animLayer = new AnimationLayer(stackBox, arrowLayer, locator, playerBox.getShineGear());

		//Add components
		add(instBox, LAYER_INSTRUCTION);
		add(playerBox, LAYER_BOX);
		add(handBox, LAYER_BOX);
		add(stackBox, LAYER_BOX);
		add(skillBox, LAYER_BOX);

		add(intelLayer, LAYER_INTELLIGENCE);
		add(animLayer, LAYER_ANIMATION);
		add(distLayer, LAYER_DISTRIBUTE);
		add(arrowLayer, LAYER_ARROW);
		add(dialogLayer, LAYER_DIALOG);
		add(hintLayer, LAYER_HINT);

		holeLayer = playerBox.getHoleLayer();
		add(holeLayer, LAYER_HOLE);
	}

	/**
	 * Gets all player views of this game.
	 * @return player views
	 */
	public List<PlayerView> getAllPlayerViews() {
		return playerBox.getAll();
	}

	/**
	 * Gets card animation layer.
	 * @return card animation layer
	 */
	public AnimationLayer getAnimationLayer() {
		return animLayer;
	}

	/**
	 * Gets dialog layer.
	 * @return dialog layer
	 */
	public DialogLayer getDialogLayer() {
		return dialogLayer;
	}

	/**
	 * Gets distribute layer.
	 * @return distribute layer
	 */
	public DistributeLayer getDistributeLayer() {
		return distLayer;
	}

	/**
	 * Gets the hand box of this game.
	 * @return hand box
	 */
	public HandBox getHandBox() {
		return handBox;
	}

	/**
	 * Gets hint layer of this game.
	 * @return hint layer
	 */
	public HintLayer getHintLayer() {
		return hintLayer;
	}

	/**
	 * Gets the instruction of this game.
	 * @return insturcion
	 */
	public InstBox getInstruction() {
		return instBox;
	}

	/**
	 * Gets intelligence layer.
	 * @return intelligence layer
	 */
	public IntelligenceLayer getIntelligenceLayer() {
		return intelLayer;
	}

	/**
	 * Gets the player box of this game.
	 * @return player box
	 */
	public PlayerBox getPlayerBox() {
		return playerBox;
	}

	/**
	 * Gets the net.leo.message.client.player view by its seat.
	 * @param seat net.leo.message.client.player's seat
	 * @return net.leo.message.client.player view
	 */
	public PlayerView getPlayerView(int seat) {
		return playerBox.get(seat);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(1200, 900);
	}

	/**
	 * Gets the skill box of this game.
	 * @return skill box
	 */
	public SkillBox getSkillBox() {
		return skillBox;
	}

	/**
	 * Gets the stack box of this game.
	 * @return stack box
	 */
	public StackBox getStackBox() {
		return stackBox;
	}

	/**
	 * Gets the timer of this game.
	 * @return timer
	 */
	public Timer getTimer() {
		return timer;
	}

	/**
	 * Stops all selection of boxes.
	 */
	public void resetAll() {
		playerBox.stopSelection();
		stackBox.stopSelection();
		handBox.stopSelection();
		skillBox.prompt(null);
		instBox.inactivate();
		if (dialogLayer.getComponentCount() > 0) {
			dialogLayer.removeAll();
			dialogLayer.repaint();
		}
	}

	/**
	 * Sets the listener of the whole game
	 * @param listener listener
	 */
	public void setGameListener(GameListener listener) {
		if (listener == null) {
			playerBox.setListener(null);
			stackBox.setListener(null);
			handBox.setListener(null);
			skillBox.setListener(null);
			instBox.setListener(null);
		}
		else {
			playerBox.setListener((source, status) -> {
				listener.onPlayerToggled(this, source, status);
			});
			stackBox.setListener((source, card, index, status) -> {
				listener.onFuncToggled(this, card, index, status);
			});
			handBox.setListener((source, status) -> {
				listener.onHandToggled(this, source, status);
			});
			skillBox.setListener((name) -> {
				listener.onSkillSelected(this, name);
			});
			instBox.setListener((source, index) -> {
				listener.onCompleted(this, index == 0);
			});
		}
	}
}
