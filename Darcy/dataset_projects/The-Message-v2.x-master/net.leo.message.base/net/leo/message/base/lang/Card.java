package net.leo.message.base.lang;

import static net.leo.message.base.lang.CardColor.BLACK;
import static net.leo.message.base.lang.CardColor.BLUE;
import static net.leo.message.base.lang.CardColor.RED;
import static net.leo.message.base.lang.CardFunction.BURN;
import static net.leo.message.base.lang.CardFunction.COUNTERACT;
import static net.leo.message.base.lang.CardFunction.DECODE;
import static net.leo.message.base.lang.CardFunction.DISTRIBUTE;
import static net.leo.message.base.lang.CardFunction.INTERCEPT;
import static net.leo.message.base.lang.CardFunction.LOCK_ON;
import static net.leo.message.base.lang.CardFunction.RETURN;
import static net.leo.message.base.lang.CardFunction.TRAP;
import static net.leo.message.base.lang.Identity.BLUE_TEAM;
import static net.leo.message.base.lang.Identity.PASSER_BY;
import static net.leo.message.base.lang.Identity.RED_TEAM;
import static net.leo.message.base.lang.IntelligenceType.DOCUMENT;
import static net.leo.message.base.lang.IntelligenceType.EXPRESS;
import static net.leo.message.base.lang.IntelligenceType.SECRET;


/**
 * A game card. A card has a only field id, used to store the data of this card. Singleton skills is used here. As a result, no constructors are provided. Call getCard or
 * getCardById method instead.
 * @author Leo Zeng
 */
public enum Card {


	COVERED_CARD(),
	COVERED_PROVE_CARD(),
	COVERED_DOCUMENT(),
	COVERED_SECRET(),
	COVERED_EXPRESS(),
	RED_PROVE_RED_PLUS(SECRET, RED, RED_TEAM, 1),
	RED_PROVE_BLUE_PLUS(SECRET, RED, BLUE_TEAM, 1),
	RED_PROVE_PASSERBY_PLUS(SECRET, RED, PASSER_BY, 1),
	BLUE_PROVE_RED_PLUS(SECRET, BLUE, RED_TEAM, 1),
	BLUE_PROVE_BLUE_PLUS(SECRET, BLUE, BLUE_TEAM, 1),
	BLUE_PROVE_PASSERBY_PLUS(SECRET, BLUE, PASSER_BY, 1),
	BLACK_PROVE_RED_PLUS(SECRET, BLACK, RED_TEAM, 1),
	BLACK_PROVE_BLUE_PLUS(SECRET, BLACK, BLUE_TEAM, 1),
	BLACK_PROVE_PASSERBY_PLUS(SECRET, BLACK, PASSER_BY, 1),
	RED_PROVE_RED_MINUS(SECRET, RED, RED_TEAM, -1),
	RED_PROVE_BLUE_MINUS(SECRET, RED, BLUE_TEAM, -1),
	RED_PROVE_PASSERBY_MINUS(SECRET, RED, PASSER_BY, -1),
	BLUE_PROVE_RED_MINUS(SECRET, BLUE, RED_TEAM, -1),
	BLUE_PROVE_BLUE_MINUS(SECRET, BLUE, BLUE_TEAM, -1),
	BLUE_PROVE_PASSERBY_MINUS(SECRET, BLUE, PASSER_BY, -1),
	BLACK_PROVE_RED_MINUS(SECRET, BLACK, RED_TEAM, -1),
	BLACK_PROVE_BLUE_MINUS(SECRET, BLACK, BLUE_TEAM, -1),
	BLACK_PROVE_PASSERBY_MINUS(SECRET, BLACK, PASSER_BY, -1),
	RED_LOCK_ON(LOCK_ON, SECRET, RED),
	BLUE_LOCK_ON(LOCK_ON, SECRET, BLUE),
	BLACK_LOCK_ON(LOCK_ON, SECRET, BLACK),
	RED_TRAP(TRAP, SECRET, RED),
	BLUE_TRAP(TRAP, SECRET, BLUE),
	BLACK_TRAP(TRAP, SECRET, BLACK),
	RED_DECODE(DECODE, SECRET, RED),
	BLUE_DECODE(DECODE, SECRET, BLUE),
	BLACK_DECODE(DECODE, SECRET, BLACK),
	RED_INTERCEPT(INTERCEPT, EXPRESS, RED),
	BLUE_INTERCEPT(INTERCEPT, EXPRESS, BLUE),
	BLACK_INTERCEPT(INTERCEPT, EXPRESS, BLACK),
	RED_BURN(BURN, EXPRESS, RED),
	BLUE_BURN(BURN, EXPRESS, BLUE),
	BLACK_BURN(BURN, EXPRESS, BLACK),
	RED_COUNTERACT(COUNTERACT, EXPRESS, RED),
	BLUE_COUNTERACT(COUNTERACT, EXPRESS, BLUE),
	BLACK_COUNTERACT(COUNTERACT, EXPRESS, BLACK),
	RED_RETURN(RETURN, DOCUMENT, RED),
	BLUE_RETURN(RETURN, DOCUMENT, BLUE),
	BLACK_RETURN(RETURN, DOCUMENT, BLACK),
	RED_DISTRIBUTE(DISTRIBUTE, DOCUMENT, RED),
	BLUE_DISTRIBUTE(DISTRIBUTE, DOCUMENT, BLUE),
	BLACK_DISTRIBUTE(DISTRIBUTE, DOCUMENT, BLACK);

	private static final long serialVersionUID = -136357833079097894L;

	private final CardFunction func;
	private final CardColor color;
	private final IntelligenceType type;
	private final Identity target;
	private final int nDrawn;

	Card(CardFunction func, IntelligenceType type, CardColor color) {
		this.func = func;
		this.color = color;
		this.type = type;
		this.target = null;
		this.nDrawn = 0;
	}

	Card(IntelligenceType type, CardColor color, Identity target, int nDrawn) {
		this.func = CardFunction.PROVE;
		this.color = color;
		this.type = type;
		this.target = target;
		this.nDrawn = nDrawn;
	}

	Card() {
		this.func = null;
		this.color = null;
		this.type = null;
		this.target = null;
		this.nDrawn = 0;
	}

	/**
	 * Gets the color of this card. If this is a covered card, return NONE.
	 */
	public CardColor getColor() {
		return color;
	}

	/**
	 * Gets the number of hands that the receiver should draw or throw (represented by a negative number) if this is a <i>prove</i> card; otherwise 0 is returned.
	 */
	public int getDrawNumber() {
		return nDrawn;
	}

	/**
	 * Gets the function of this card. If this is a covered card, returns NONE.
	 */
	public CardFunction getFunction() {
		return func;
	}

	/**
	 * Gets the team tested if this is a <i>prove</i> card; otherwise {@link Identity#NONE} is returned.
	 */
	public Identity getProveTarget() {
		return target;
	}

	/**
	 * Gets the passing way of this card. If this is a covered card, return NONE.
	 */
	public IntelligenceType getType() {
		return type;
	}

	/**
	 * Queries if this card is covered.
	 */
	public boolean isCoveredCard() {
		return func == null;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + name() + "]";
	}
}
