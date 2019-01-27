package net.leo.message.client.utility;

import java.awt.Rectangle;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import net.leo.message.base.lang.Card;
import net.leo.message.client.element.GameCard;

/**
 * A box that shows cards.
 * @param <T> type of card view
 * @author Leo Zeng
 */
public interface CardBox<T extends CardView> {

	/**
	 * Removes all the cards from this box.
	 */
	void clear();

	/**
	 * Gets the count of cards in this box.
	 * @return the count of cards in this box
	 */
	int count();

	/**
	 * Gets the card in this box by its index
	 * @throws ArrayIndexOutOfBoundsException if index is out of bound
	 */
	T get(int index);

	/**
	 * Gets all the card in this box.
	 */
	List<T> getAll();

	/**
	 * Queries in advance the bounds of the card which is about to be added in.
	 */
	Rectangle getNextBounds();

	/**
	 * Queries the index of card in this box.
	 * @param card card which index is queried
	 * @return index of card, or -1 if this box dost not contain this card
	 */
	int indexOf(T card);

	/**
	 * Queries if this box is empty.
	 */
	boolean isEmpty();

	/**
	 * Queries if this box is locked.
	 */
	boolean isLocked();

	/**
	 * Locks this box. If this box is locked, it will not react to the mouse move, and location of each card will never change when cards are added or removed.
	 */
	void lock();

	/**
	 * Adds a card into this box. If this box is locked, locations of cards will not change.
	 * @param card the card to be added
	 * @throws NullPointerException if card is null
	 */
	void offer(T card);

	/**
	 * Adds a plenty of cards into this box. If this box is locked, locations of cards will not change.
	 * @param cards cards to be added and can be empty
	 * @throws NullPointerException if list is null or it contains null
	 */
	void offerAll(Collection<? extends T> cards);

	/**
	 * Removes a card from this box. If this box does not contains that card, nothing happens. If this box is locked, location of each card will not change.
	 * @param card the card to be removed
	 * @throws NullPointerException if card is null
	 */
	void poll(T card);

	/**
	 * Removes a plenty of cards from this box. If this box is locked, location of each card will not change.
	 * @param cards cards to be removed
	 * @throws NullPointerException if list is null or it contains null
	 */
	void pollAll(Collection<? extends T> cards);

	/**
	 * To remove a card from this box. If this box does not contains that card, nothing happens. If this box is locked, location of each card will not change. If this box doesn't
	 * contain the given card, nothing will happen.
	 * @param card the card to be removed
	 */
	void remove(Card card);

	/**
	 * Removes a card from this box by its index.
	 * @param index index of card
	 * @throws IndexOutOfBoundsException if index is out of bound
	 */
	void poll(int index);

	/**
	 * Removes a plenty of cards from this box. If this box is locked, location of each card will not change.
	 * @param cards cards to be removed
	 * @throws NullPointerException if the list is null or it contains null
	 */
	void removeAll(Collection<Card> cards);

	/**
	 * Sorts the cards in this box.
	 */
	void sort();

	/**
	 * Sorts the cards in this box by a given comparator.
	 * @throws NullPointerException if comparator is null
	 */
	void sort(Comparator<T> comparator);

	/**
	 * Unlocks this box. When a box is unlocked, location of each card will be renewed.
	 */
	void unlock();
}
