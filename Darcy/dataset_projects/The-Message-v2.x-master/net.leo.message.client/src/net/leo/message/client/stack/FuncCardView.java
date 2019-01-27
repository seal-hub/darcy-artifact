package net.leo.message.client.stack;

import java.awt.Insets;
import java.awt.Point;
import net.leo.message.client.element.GameCard;
import net.leo.message.client.player.PlayerView;
import net.leo.message.client.utility.ArrowTarget;
import net.leo.message.client.utility.OptionCardView;

/**
 * A function card that store the data of user and target and at the meanwhile used to be a label shows on the screen.
 * @author Leo Zeng
 */
@SuppressWarnings("serial")
public class FuncCardView extends OptionCardView implements ArrowTarget {

	private final PlayerView user;
	private final ArrowTarget target;

	/**
	 * Gets the target of this card.
	 * @return the target of this card, or return null if no target
	 */
	public ArrowTarget getTarget() {
		return target;
	}

	@Override
	public Point getTargetCenterPoint() {
		Insets inset = getInsets();
		int width = getWidth() - (inset.left + inset.right);
		int height = getHeight() - (inset.top + inset.bottom);
		return new Point(inset.left + width / 2, inset.top + height / 2);
	}

	/**
	 * Gets the user net.leo.message.client.player of this card.
	 * @return the user net.leo.message.client.player of this card
	 */
	public PlayerView getUser() {
		return user;
	}


	/**
	 * Constructs a new function card view.
	 * @param card   the card used
	 * @param user   the user
	 * @param target the target or null if no target
	 * @throws NullPointerException if card or user is null
	 */
	public FuncCardView(GameCard card, PlayerView user, ArrowTarget target, ArrowLayer arrowLayer) {
		super(card);
		if (user == null) {
			throw new NullPointerException();
		}
		this.user = user;
		this.target = target;
	}
}
