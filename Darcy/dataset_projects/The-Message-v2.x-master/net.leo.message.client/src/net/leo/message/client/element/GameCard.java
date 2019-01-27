package net.leo.message.client.element;

import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import net.leo.message.base.lang.Card;
import net.leo.message.base.lang.CardColor;
import net.leo.message.base.lang.CardFunction;
import net.leo.message.base.lang.IntelligenceType;

/**
 * A game card.
 * @author Leo Zeng
 * @see Card
 */
public enum GameCard implements Comparable<GameCard> {

	COVERED_CARD(Card.COVERED_CARD),
	COVERED_PROVE_CARD(Card.COVERED_PROVE_CARD),
	COVERED_EXPRESS(Card.COVERED_EXPRESS),
	COVERED_SECRET(Card.COVERED_SECRET),
	COVERED_DOCUMENT(Card.COVERED_DOCUMENT),

	RED_BURN(Card.RED_BURN),
	RED_COUNTERACT(Card.RED_COUNTERACT),
	RED_DECODE(Card.RED_DECODE),
	RED_DISTRIBUTE(Card.RED_DISTRIBUTE),
	RED_INTERCEPT(Card.RED_INTERCEPT),
	RED_LOCK_ON(Card.RED_LOCK_ON),
	RED_PROVE_BLUE_MINUS(Card.RED_PROVE_BLUE_MINUS),
	RED_PROVE_BLUE_PLUS(Card.RED_PROVE_BLUE_PLUS),
	RED_PROVE_PASSERBY_MINUS(Card.RED_PROVE_PASSERBY_MINUS),
	RED_PROVE_PASSERBY_PLUS(Card.RED_PROVE_PASSERBY_PLUS),
	RED_PROVE_RED_MINUS(Card.RED_PROVE_RED_MINUS),
	RED_PROVE_RED_PLUS(Card.RED_PROVE_RED_PLUS),
	RED_RETURN(Card.RED_RETURN),
	RED_TRAP(Card.RED_TRAP),

	BLUE_BURN(Card.BLUE_BURN),
	BLUE_COUNTERACT(Card.BLUE_COUNTERACT),
	BLUE_DECODE(Card.BLUE_DECODE),
	BLUE_DISTRIBUTE(Card.BLUE_DISTRIBUTE),
	BLUE_INTERCEPT(Card.BLUE_INTERCEPT),
	BLUE_LOCK_ON(Card.BLUE_LOCK_ON),
	BLUE_PROVE_BLUE_MINUS(Card.BLUE_PROVE_BLUE_MINUS),
	BLUE_PROVE_BLUE_PLUS(Card.BLUE_PROVE_BLUE_PLUS),
	BLUE_PROVE_PASSERBY_MINUS(Card.BLUE_PROVE_PASSERBY_MINUS),
	BLUE_PROVE_PASSERBY_PLUS(Card.BLUE_PROVE_PASSERBY_PLUS),
	BLUE_PROVE_RED_MINUS(Card.BLUE_PROVE_RED_MINUS),
	BLUE_PROVE_RED_PLUS(Card.BLUE_PROVE_RED_PLUS),
	BLUE_RETURN(Card.BLUE_RETURN),
	BLUE_TRAP(Card.BLUE_TRAP),

	BLACK_BURN(Card.BLACK_BURN),
	BLACK_COUNTERACT(Card.BLACK_COUNTERACT),
	BLACK_DECODE(Card.BLACK_DECODE),
	BLACK_DISTRIBUTE(Card.BLACK_DISTRIBUTE),
	BLACK_INTERCEPT(Card.BLACK_INTERCEPT),
	BLACK_LOCK_ON(Card.BLACK_LOCK_ON),
	BLACK_PROVE_BLUE_MINUS(Card.BLACK_PROVE_BLUE_MINUS),
	BLACK_PROVE_BLUE_PLUS(Card.BLACK_PROVE_BLUE_PLUS),
	BLACK_PROVE_PASSERBY_MINUS(Card.BLACK_PROVE_PASSERBY_MINUS),
	BLACK_PROVE_PASSERBY_PLUS(Card.BLACK_PROVE_PASSERBY_PLUS),
	BLACK_PROVE_RED_MINUS(Card.BLACK_PROVE_RED_MINUS),
	BLACK_PROVE_RED_PLUS(Card.BLACK_PROVE_RED_PLUS),
	BLACK_RETURN(Card.BLACK_RETURN),
	BLACK_TRAP(Card.BLACK_TRAP);

	/**
	 * Ratio of width and height of game card.
	 */
	public static final double RATIO_WIDTH_HEIGHT = 0.75d;


	private static Image createDefaultImage(Card card) {
		try {
			return ImageIO.read(getURL(card));
		}
		catch (IOException e) {
			System.err.println(card.toString());
			throw new IllegalArgumentException(e);
		}
	}

	private static URL getURL(Card card) {
		String fileName = null;
		switch (card) {
		case COVERED_CARD:
			fileName = "遊戲牌";
			break;
		case COVERED_EXPRESS:
			fileName = "直達";
			break;
		case COVERED_DOCUMENT:
			fileName = "文本";
			break;
		case COVERED_SECRET:
			fileName = "密電";
			break;
		case COVERED_PROVE_CARD:
			fileName = "試探";
		}
		if (fileName != null) {
			return GameCard.class.getResource("/image/gamecard/" + fileName + ".png");
		}

		fileName = "";
		fileName += card.getColor().getName();
		fileName += card.getFunction().getSimpleName();
		if (card.getFunction() == CardFunction.PROVE) {
			fileName += card.getProveTarget().getSimpleName();
			fileName += card.getDrawNumber() > 0 ? "+1" : "-1";
		}
		return GameCard.class.getResource("/image/gamecard/" + fileName + ".png");
	}

	private static void requiresNonCoveredCard(GameCard card) {
		if (card.isCoveredCard()) {
			throw new IllegalCardException();
		}
	}

	/**
	 * Gets a game card by a content object.
	 * @param card content object
	 * @return a game card
	 */
	public static GameCard getInstance(Card card) {
		for (GameCard gc : values()) {
			if (gc.cardBody == card) {
				return gc;
			}
		}
		return null;
	}

	private final Card cardBody;
	private transient Image defaultImage;
	private transient Map<Dimension, Image> imagePool;

	GameCard(Card cardBody) {
		this.cardBody = cardBody;
	}

	/**
	 * Gets the color of this game card.
	 * @throws IllegalCardException if this is a covered card
	 * @see Card
	 */
	public CardColor getColor() {
		requiresNonCoveredCard(this);
		return cardBody.getColor();
	}

	/**
	 * Gets the number of card drawn by this prove card's effect. Positive number represents drawing cards, while negative number dropping cards.
	 * @return the number of card drawn
	 * @throws IllegalCardException if this card is not a prove card
	 * @throws IllegalCardException if this is a covered card
	 */
	public int getDrawNumber() {
		requiresNonCoveredCard(this);
		if (cardBody.getFunction() != CardFunction.PROVE) {
			throw new IllegalCardException();
		}
		return cardBody.getDrawNumber();
	}

	/**
	 * Gets the card element.
	 * @return card element
	 */
	public Card getElement() {
		return cardBody;
	}

	/**
	 * Gets the function of this card.
	 * @return the function of this card
	 * @throws IllegalCardException if this is a covered card
	 * @see Card
	 */
	public CardFunction getFunction() {
		requiresNonCoveredCard(this);
		return cardBody.getFunction();
	}

	/**
	 * Gets an image of given size.
	 * @param size image size
	 * @return image
	 */
	public synchronized Image getImage(Dimension size) {
		if (imagePool == null) {
			imagePool = new HashMap<>(4);
		}
		Image scaled = imagePool.get(size);
		if (scaled == null) {
			if (defaultImage == null) {
				defaultImage = createDefaultImage(cardBody);
			}
			scaled = defaultImage.getScaledInstance(size.width, size.height, Image.SCALE_AREA_AVERAGING);
			imagePool.put(size, scaled);
		}
		return scaled;
	}

	/**
	 * Gets the target identity of this prove card.
	 * @return the target identity of this prove card
	 * @throws IllegalCardException if this card is not a prove card or is a covered card
	 */
	public IdenCard getProveTarget() {
		requiresNonCoveredCard(this);
		if (cardBody.getFunction() != CardFunction.PROVE) {
			throw new IllegalCardException();
		}
		return IdenCard.getInstance(cardBody.getProveTarget());
	}

	/**
	 * Gets the intelligence type of this card.
	 * @return intelligence type of this card
	 * @throws IllegalCardException if this is a covered card
	 * @see Card
	 */
	public IntelligenceType getType() {
		requiresNonCoveredCard(this);
		return cardBody.getType();
	}

	/**
	 * Queries if this card is a covered card, a covered prove, or a covered intelligence.
	 * @return true if this card is a covered card, covered prove card, or covered intelligence.
	 */
	public boolean isCoveredCard() {
		return cardBody.isCoveredCard();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + name() + "]";
	}
}
