package net.leo.message.client.player;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.JComponent;
import net.leo.message.base.bridge.command.data.CharAnimData;
import net.leo.message.base.bridge.command.data.CharData;
import net.leo.message.base.bridge.command.data.CondAnimData;
import net.leo.message.base.bridge.command.data.IdenAnimData;
import net.leo.message.base.lang.CardColor;
import net.leo.message.client.animation.executor.AnimationGear;
import net.leo.message.client.animation.executor.ShineGear;
import net.leo.message.client.dialog.DialogLayer;
import net.leo.message.client.domain.GameView;
import net.leo.message.client.element.CharCard;
import net.leo.message.client.element.CondMark;
import net.leo.message.client.element.IdenCard;
import net.leo.message.client.selection.SelectionSet;
import net.leo.message.client.utility.CharHint;
import net.leo.message.client.utility.HintLayer;

/**
 * A layer and box contains player views and play animations concerning of player views.
 * @author Leo Zeng
 */
public class PlayerBox extends JComponent implements SelectionSet<Integer> {

	/**
	 * The maximum number of players in a game.
	 */
	public static final int MAX_PLAYER_COUNT = 9;
	/**
	 * The minimum number of players in a game.
	 */
	public static final int MIN_PLAYER_COUNT = 2;


	private class PlayerViewLayerLayout implements LayoutManager {

		private PlayerView absPlayerView(int seat) {
			int absSeat = toRelativeSeat(seat, mySeat, nPlayer);
			return get(absSeat);
		}

		private void layout2() {
			absPlayerView(0).setLocation(locator.pMe());
			absPlayerView(1).setLocation(locator.pTop4());
		}

		private void layout3() {
			absPlayerView(0).setLocation(locator.pMe());
			absPlayerView(1).setLocation(locator.pTop5());
			absPlayerView(2).setLocation(locator.pTop3());
		}

		private void layout4() {
			absPlayerView(0).setLocation(locator.pMe());
			absPlayerView(1).setLocation(locator.pRight2());
			absPlayerView(2).setLocation(locator.pTop4());
			absPlayerView(3).setLocation(locator.pLeft2());
		}

		private void layout5() {
			absPlayerView(0).setLocation(locator.pMe());
			absPlayerView(1).setLocation(locator.pRight2());
			absPlayerView(2).setLocation(locator.pTop5());
			absPlayerView(3).setLocation(locator.pTop3());
			absPlayerView(4).setLocation(locator.pLeft2());
		}

		private void layout6() {
			absPlayerView(0).setLocation(locator.pMe());
			absPlayerView(1).setLocation(locator.pRight2());
			absPlayerView(2).setLocation(locator.pTop6());
			absPlayerView(3).setLocation(locator.pTop4());
			absPlayerView(4).setLocation(locator.pTop2());
			absPlayerView(5).setLocation(locator.pLeft2());
		}

		private void layout7() {
			absPlayerView(0).setLocation(locator.pMe());
			absPlayerView(1).setLocation(locator.pRight2());
			absPlayerView(2).setLocation(locator.pTop7());
			absPlayerView(3).setLocation(locator.pTop5());
			absPlayerView(4).setLocation(locator.pTop3());
			absPlayerView(5).setLocation(locator.pTop1());
			absPlayerView(6).setLocation(locator.pLeft2());
		}

		private void layout8() {
			absPlayerView(0).setLocation(locator.pMe());
			absPlayerView(1).setLocation(locator.pRight1());
			absPlayerView(1).setLocation(locator.pRight3());
			absPlayerView(2).setLocation(locator.pTop6());
			absPlayerView(3).setLocation(locator.pTop4());
			absPlayerView(4).setLocation(locator.pTop2());
			absPlayerView(5).setLocation(locator.pLeft3());
			absPlayerView(7).setLocation(locator.pLeft1());
		}

		private void layout9() {
			absPlayerView(0).setLocation(locator.pMe());
			absPlayerView(1).setLocation(locator.pRight1());
			absPlayerView(2).setLocation(locator.pRight3());
			absPlayerView(3).setLocation(locator.pTop7());
			absPlayerView(4).setLocation(locator.pTop5());
			absPlayerView(5).setLocation(locator.pTop3());
			absPlayerView(6).setLocation(locator.pTop1());
			absPlayerView(7).setLocation(locator.pLeft3());
			absPlayerView(8).setLocation(locator.pLeft1());
		}

		@Override
		public void addLayoutComponent(String name, Component comp) {
		}

		@Override
		public void layoutContainer(Container parent) {
			Dimension size = locator.getPlayerSize();
			for (PlayerView v : getAll()) {
				v.setSize(size);
			}
			switch (nPlayer) {
			case 2:
				layout2();
				break;
			case 3:
				layout3();
				break;
			case 4:
				layout4();
				break;
			case 5:
				layout5();
				break;
			case 6:
				layout6();
				break;
			case 7:
				layout7();
				break;
			case 8:
				layout8();
				break;
			case 9:
				layout9();
				break;
			}
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


	/**
	 * Converts seat number from an absolute location to a number relative to a player's seat.
	 * @param targetSeat seat of target
	 * @param mySeat     this players' seat
	 * @param nPlayer    count of players
	 * @return relative seat location
	 */
	public static int toAbsoluteSeat(int targetSeat, int mySeat, int nPlayer) {
		requiresLegalSeat(targetSeat, nPlayer);
		requiresLegalSeat(mySeat, nPlayer);
		targetSeat -= mySeat;
		if (targetSeat < 0) {
			targetSeat += nPlayer;
		}
		return targetSeat;
	}

	/**
	 * Converts seat number from a relative location to an absolute one.
	 * @param targetSeat seat of target
	 * @param mySeat     this players' seat
	 * @param nPlayer    count of players
	 * @return absolute seat location
	 */
	public static int toRelativeSeat(int targetSeat, int mySeat, int nPlayer) {
		requiresLegalSeat(targetSeat, nPlayer);
		requiresLegalSeat(mySeat, nPlayer);
		targetSeat += mySeat;
		if (targetSeat >= nPlayer) {
			targetSeat -= nPlayer;
		}
		return targetSeat;
	}

	/**
	 * Ensures a legal seat number.
	 * @param seat    this player's seat
	 * @param nPlayer count of players
	 * @throws IllegalArgumentException if any of argument is illegal
	 */
	public static void requiresLegalSeat(int seat, int nPlayer) {
		if (seat < 0 || seat >= nPlayer || nPlayer < MIN_PLAYER_COUNT || nPlayer > MAX_PLAYER_COUNT) {
			throw new IllegalArgumentException();
		}
	}

	private int mySeat;
	private int nPlayer;

	private List<PlayerView> playerViews;
	private ShineGear shineGear;      //Owner
	private AnimationGear condGear;   //Owner
	private AnimationGear flipGear;   //Owner
	private AnimationGear peekGear;   //Owner
	private AnimationGear idenGear;   //Owner
	private GameView.Locator locator; //given by game view
	private HoleLayer holeLayer;      //Owner
	private PlayerBoxListener listener = null;

	private Set<Integer> candidates;


	/**
	 * Constructs a layer containing several player views.
	 * @param mySeat    this player's seat
	 * @param playerIds ids of each player in order by seat number
	 * @param locator   game locator
	 */
	public PlayerBox(int mySeat, String[] playerIds, HintLayer hintLayer, GameView.Locator locator, DialogLayer diaLyaer) {
		if (hintLayer == null || locator == null) {
			throw new NullPointerException();
		}
		this.locator = locator;
		this.flipGear = new AnimationGear(playerIds.length);
		this.idenGear = new AnimationGear(playerIds.length);
		this.peekGear = new AnimationGear(playerIds.length);
		this.condGear = new AnimationGear(playerIds.length);
		this.shineGear = new ShineGear(playerIds.length * 4);
		this.holeLayer = new HoleLayer(this);

		setLayout(new PlayerViewLayerLayout());
		createGUI(mySeat, playerIds, hintLayer, diaLyaer);

		this.mySeat = mySeat;
		this.nPlayer = playerIds.length;
	}


	private void createGUI(int mySeat, String[] ids, HintLayer hintLayer, DialogLayer diaLayer) {
		playerViews = new ArrayList<>(ids.length);

		//Create count view builder
		CountView.Builder countBuilder = new CountView.Builder(shineGear, diaLayer);

		for (int seat = 0 ; seat < ids.length ; seat++) {
			//Create net.leo.message.client.player view
			PlayerView pv = new PlayerView(seat, mySeat, ids.length, ids[seat]);

			//set gui
			pv.kCount = countBuilder.build(CardColor.BLACK);
			pv.bCount = countBuilder.build(CardColor.BLUE);
			pv.rCount = countBuilder.build(CardColor.RED);
			pv.hCount = countBuilder.build(null);
			pv.flipPainter = flipGear.createPainter();
			pv.peekPainter = peekGear.createPainter();
			pv.idenPainter = idenGear.createPainter();
			pv.condPainter = condGear.createPainter();
			pv.hintLayer = hintLayer;
			pv.createGUI();

			pv.charView.addMouseListener(new MouseAdapter() {

				@Override
				public void mousePressed(MouseEvent e) {
					if (candidates != null && candidates.contains(pv.seat)) {
						pv.setSelected(!pv.isSelected());
						if (listener != null) {
							listener.onPlayerToggled(pv, pv.isSelected());
						}
					}
				}
			});

			//Add to this container
			playerViews.add(pv);
			add(pv);
		}
	}


	/**
	 * Plays a identity-cards-displayed animation and set identities. Not wait for this animation.
	 * @param animInfo animation information
	 */
	public void displayIdentity(Set<IdenAnimData> animInfo) {
		//Set animation
		animInfo.forEach(ai -> {
			IdenCard identity = IdenCard.getInstance(ai.identity);
			get(ai.seat).setIden(identity, ai.hidden);
		});

		//Run animation
		idenGear.run(false, null);
	}

	/**
	 * Plays a character-card-flipped animation and set real characters and waits for this animation.
	 * @param player player whose character card is flipped
	 * @param cd     character info
	 */
	public void flipTo(PlayerView player, CharData cd) {
		//Set animation
		CharCard character = CharCard.getInstance(cd == null ? null : cd.character);
		CharHint hint = CharHint.toCharHint(cd);
		player.flipTo(character, hint);

		//Run animation
		flipGear.run(true, null);
	}

	/**
	 * Gets the player view by its seat.
	 * @param seat player 's seat
	 * @return the player  view
	 */
	public PlayerView get(int seat) {
		return playerViews.get(seat);
	}

	/**
	 * Gets all net.leo.message.client.player views in order by their seats.
	 * @return all player views
	 */
	public List<PlayerView> getAll() {
		return new ArrayList<>(playerViews);
	}

	/**
	 * Gets the hole layer.
	 * @return hole layer
	 */
	public HoleLayer getHoleLayer() {
		return holeLayer;
	}

	/**
	 * Gets the shine gear of all count views.
	 * @return a shine gear
	 */
	public ShineGear getShineGear() {
		return shineGear;
	}

	/**
	 * Plays a character cards peeked animation and set its real characters. Not Wait for this animation.
	 * @param animInfo animation information.
	 */
	public void peek(Set<CharAnimData> animInfo) {
		//Set animation
		animInfo.forEach(ai -> {
			CharCard character = CharCard.getInstance(ai.charData.character);
			CharHint hint = CharHint.toCharHint(ai.charData);
			get(ai.seat).peek(character, hint);
		});

		//Run animation
		peekGear.run(false, null);
	}

	/**
	 * Removes condition for players by information
	 * @param info a set which contains seats of players whose conditions are removed
	 */
	public void removeCondition(Set<Integer> info) {
		info.forEach(i -> {
			get(i).setCond(null);
		});
	}

	@Override
	public void runSelection(Set<Integer> candidates) {
		//Set listener
		for (PlayerView pv : getAll()) {
			if (!candidates.contains(pv.seat)) {
				pv.listener = null;
				continue;
			}

			pv.listener = (source, status) -> {
				if (listener != null) {
					listener.onPlayerToggled(source, status);
				}
			};
		}

		//Show holes
		holeLayer.setHoles(candidates);

		this.candidates = candidates;
	}

	/**
	 * Sets conditions for players by information.
	 * @param info condition information
	 */
	public void setCondition(Set<CondAnimData> info) {
		//Set animation
		info.forEach(i -> {
			CondMark cond = CondMark.getInstance(i.cond);
			get(i.seat).addCondAnim(cond);
		});

		//Set finishing procedure
		Runnable end = () -> info.forEach(i -> {
			if (i.renew) {
				CondMark cond = CondMark.getInstance(i.cond);
				get(i.seat).setCond(cond);
			}
		});

		//Run timer
		condGear.run(true, end);
	}

	/**
	 * Sets the listener of this box.
	 * @param listener listener
	 */
	public void setListener(PlayerBoxListener listener) {
		this.listener = listener;
	}

	@Override
	public void stopSelection() {
		//Reset views
		getAll().forEach(pv -> {
			pv.listener = null;
			pv.setSelected(false);
		});

		//Clear holes
		holeLayer.setHoles(null);

		this.candidates = null;
	}
}
