package net.leo.message.server.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import static java.util.stream.Collectors.toList;
import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.bridge.command.action.Action;
import net.leo.message.base.bridge.command.action.HandAction;
import net.leo.message.base.bridge.command.action.IntelAction;
import net.leo.message.base.bridge.command.action.IntelLocation;
import net.leo.message.base.bridge.command.action.PlayerAction;
import net.leo.message.base.bridge.command.animation.sync.CardDrawnCommand;
import net.leo.message.base.bridge.command.animation.sync.CardDroppedCommand;
import net.leo.message.base.bridge.command.animation.sync.CardMovedCommand;
import net.leo.message.base.bridge.command.animation.sync.CharFlippedCommand;
import net.leo.message.base.bridge.command.animation.sync.CondSetCommand;
import net.leo.message.base.bridge.command.animation.sync.DistributeCommand;
import net.leo.message.base.bridge.command.animation.sync.IntelChangedCommand;
import net.leo.message.base.bridge.command.animation.sync.IntelFlippedAndReceivedCommand;
import net.leo.message.base.bridge.command.animation.sync.IntelFlippedCommand;
import net.leo.message.base.bridge.command.animation.sync.IntelMovedCommand;
import net.leo.message.base.bridge.command.animation.sync.IntelReceivedCommand;
import net.leo.message.base.bridge.command.animation.sync.IntelSentCommand;
import net.leo.message.base.bridge.command.animation.unsync.DistributeStateCommand;
import net.leo.message.base.bridge.command.animation.unsync.HandChangedCommand;
import net.leo.message.base.bridge.command.animation.unsync.TimeSetCommand;
import net.leo.message.base.bridge.command.select.BasicSelectionCommand;
import net.leo.message.base.bridge.command.select.CardSelectionCommand;
import net.leo.message.base.bridge.command.select.CardViewCommand;
import net.leo.message.base.bridge.command.select.Selection;
import net.leo.message.base.bridge.reply.BasicReply;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.base.bridge.reply.MultiReply;
import net.leo.message.base.bridge.reply.NullReply;
import net.leo.message.base.bridge.reply.Reply;
import net.leo.message.base.lang.Card;
import net.leo.message.base.lang.CardColor;
import net.leo.message.base.lang.CardFunction;
import net.leo.message.base.lang.Condition;
import net.leo.message.base.lang.Identity;
import net.leo.message.base.lang.IntelligenceType;
import net.leo.message.server.conversation.Examiner;
import net.leo.message.server.conversation.MultiMessenger;
import net.leo.message.server.conversation.SingleMessenger;
import net.leo.message.server.event.CardInvokingEvent;
import net.leo.message.server.event.GameEvent;
import net.leo.message.server.event.IntelObtainedEvent;
import net.leo.message.server.event.IntelReceviedEvent;
import net.leo.message.server.event.ProveInvokingEvent;
import net.leo.message.server.skill.Skill;
import net.leo.message.server.stack.ExecutableCard;

/**
 * An operator that operates all the behaviour of a game. {@link Skill} should not directly operate a {@link Game} object but via the operator of
 * such game.
 * @author Parabola
 */
public class Operator {

	/**
	 * The game object which owns this operator; in other words, the game object which is being operated.
	 */
	private final Game game;

	/**
	 * Constructs a operator.
	 * @param game game object which is operated by this operator
	 */
	Operator(Game game) {
		if (game == null) {
			throw new NullPointerException();
		}
		this.game = game;
	}

	/**
	 * Invokes a card (intelligence) burnt action and the following-up events.
	 * @param player player whose intelligence are burnt
	 * @param cards  intelligence burnt
	 */
	public void cardBurnt(Player player, List<Card> cards) {
		if (cards.isEmpty()) {
			return;
		}

		//Removes intelligence
		player.getIntels().removeAll(cards);
		game.getDeadwood().addAll(cards);

		//Sends command
		Command cmd = new CardDroppedCommand(cards, false, player.getSeat());
		MultiMessenger.commandAll(game.getPlayers(), cmd);
	}

	/**
	 * Lets a player get an intelligence from another player and invokes the following-up events. All cards are public.
	 * @param card     intelligence card
	 * @param origin   origin player
	 * @param orgHand  true indicating this intelligence is from hand card; otherwise intelligence
	 * @param dest     player who gets intelligence
	 * @param destHand true indicating this intelligence is to hand card; otherwise intelligence
	 */
	public void cardMoved(Card card, Player origin, boolean orgHand, Player dest, boolean destHand) {
		cardMoved(List.of(card), origin, orgHand, dest, destHand);
	}

	/**
	 * Lets a player get intelligences from another player and invokes the following-up events. All cards are public.
	 * @param cards    intelligence cards
	 * @param origin   origin player
	 * @param orgHand  true indicating this intelligence is from hand card; otherwise intelligence
	 * @param dest     player who gets intelligence
	 * @param destHand true indicating this intelligence is to hand card; otherwise intelligence
	 */
	public void cardMoved(List<Card> cards, Player origin, boolean orgHand, Player dest, boolean destHand) {
		if (cards.isEmpty()) {
			return;
		}

		//Removes hand cards
		if (orgHand) {
			SingleMessenger.command(origin, new HandChangedCommand(false, cards));
			origin.getHands().removeAll(cards);
		}

		//Send animation command
		Command cmd = new CardMovedCommand(origin.getSeat(), orgHand, cards, dest.getSeat(), destHand);
		MultiMessenger.commandAll(game.getPlayers(), cmd);

		//Invokes following-up events
		if (destHand) {

			//Adds hand cards
			SingleMessenger.command(dest, new HandChangedCommand(true, cards));
			dest.getHands().addAll(cards);
		}
		else {
			inform(new IntelObtainedEvent(dest, cards));
		}
	}

	/**
	 * Puts a player's intelligence on the top of the deck and invokes the following-up events.
	 */
	public void cardRecycled(Player player, Card intel) {
		cardsRecycled(player, List.of(intel));
	}

	/**
	 * Lets a player view a card and queries his/her reply.
	 * @param player  player who is about to view a card
	 * @param card    card viewed
	 * @param title   title of this card-viewed action
	 * @param inst    instruction shown to <tt>player</tt>
	 * @param instEx  instruction shown to all the other players
	 * @param options options that the player can reply; option which ends with character 'X' is not the candidates of this reply
	 * @return replt of the player; an integer indicating the index of option
	 */
	public int cardViewed(Player player, Card card, String title, String inst, String instEx, List<String> options) {

		//Send command
		int msgId = Selection.randomMessageId();
		int time = 6000 + 3000 * options.size();
		CardViewCommand cmd = new CardViewCommand(card, title, inst, time, options, msgId);
		Command cmdEx = new TimeSetCommand(time, instEx, player.getSeat());
		MultiMessenger.commandOneAndOthers(game.getPlayers(), player, cmd, cmdEx);

		//Get reply
		SingleMessenger msg = new SingleMessenger(msgId, player, time, reply -> Examiner.checkSelection(reply, cmd));
		BasicReply<Integer> reply = msg.reply((BasicReply<Integer>) cmd.getDefaultReply());

		//Let timer stop
		Command stopCmd = new TimeSetCommand(-1, null, player.getSeat());
		MultiMessenger.commandAll(game.getPlayers(), stopCmd);
		return reply.value;
	}

	public void cardsRecycled(Player player, List<Card> cards) {
		if (cards.isEmpty()) {
			return;
		}

		//Removes intelligence
		player.getIntels().removeAll(cards);
		game.getDeck().put(cards);

		//Sends command
		Command cmd = new CardDroppedCommand(cards, false, player.getSeat());
		MultiMessenger.commandAll(game.getPlayers(), cmd);
	}

	/**
	 * Covers someone's character card
	 * @param player player whose character card is covered.
	 */
	public void charCovered(Player player) {
		player.getCharacter().setPublicity(false);
		Command cmd = new CharFlippedCommand(player.getSeat());
		MultiMessenger.commandAll(game.getPlayers(), cmd);
	}

	/**
	 * Opens someone's character card.
	 * @param player player whose chatacter card is opened.
	 */
	public void charOpened(Player player) {
		player.getCharacter().setPublicity(true);
		Command cmd = new CharFlippedCommand(player.getSeat(), player.getCharacter().getCharacterData());
		MultiMessenger.commandAll(game.getPlayers(), cmd);
	}

	/**
	 * Lets a player draw cards and invokes the following-up events. Cards are not public.
	 * @param player player who draws cards
	 * @param count  count of cards shold be drawn
	 * @return a card list indicating the hand cards the player has drawn
	 */
	public List<Card> handDrawn(Player player, int count) {
		if (count < 0) {
			throw new IllegalArgumentException();
		}

		if (count == 0) {
			return new ArrayList<>();
		}

		//Checks empty
		Deck deck = game.getDeck();
		List<Card> cards = deck.draw(count);
		if (cards.isEmpty()) {
			return cards;
		}

		//Sends animation command
		Command animCmd = new CardDrawnCommand(Collections.nCopies(count, Card.COVERED_CARD), true, player.getSeat());
		MultiMessenger.commandAll(game.getPlayers(), animCmd);

		//Sends hand-cards-added command
		SingleMessenger.command(player, new HandChangedCommand(true, cards));
		player.getHands().addAll(cards);

		return cards;
	}

	/**
	 * Lets a player drop her/his hands
	 * @param player player whose hand card is dropped
	 * @param card   hand card dropped
	 */
	public void handDropped(Player player, Card card) {
		handDropped(player, List.of(card));
	}

	/**
	 * Lets a player drop her/his hands.
	 * @param cards hand cardss dropped
	 */
	public void handDropped(Player player, List<Card> cards) {
		if (cards.isEmpty()) {
			return;
		}
		if (!player.getHands().containsAll(cards)) { //Implicitly check null
			throw new IllegalArgumentException();
		}

		List<Player> players = game.getPlayers();

		//Sends hand-cards-removed command
		SingleMessenger.command(player, new HandChangedCommand(false, cards));
		player.getHands().removeAll(cards);

		//Sends animation command
		Command cmd = new CardDroppedCommand(cards, true, player.getSeat());
		MultiMessenger.commandAll(players, cmd);
	}

	/**
	 * Lets a player put back its hand cards to the top of the deck.
	 * @param player player whose hand cards are put back
	 * @param hands  cards put back
	 */
	public void handRecycled(Player player, List<Card> hands) {
		if (!player.getHands().containsAll(hands)) { //Implictly check null
			throw new IllegalArgumentException();
		}

		//Remove hand cards
		SingleMessenger.command(player, new HandChangedCommand(false, hands));
		player.getHands().removeAll(hands);

		//Put back
		MultiMessenger.commandAll(game.getPlayers(), new CardDroppedCommand(Collections.nCopies(hands.size(), Card.COVERED_CARD), true, player.getSeat()));
		game.getDeck().put(hands);
	}

	/**
	 * Lets a player draw another player's hand. All hand cards are not public to other players.
	 * @param card   hand cards transferred
	 * @param origin player whose hand card is drawn
	 * @param dest   player who draws a hand card
	 */
	public void handTransfer(Card card, Player origin, Player dest) {
		handTransfer(List.of(card), origin, dest);
	}

	/**
	 * Lets players draw other players' hands. All hand cards are not public to other players.
	 * @param cards  hand cards to be transferred
	 * @param origin player whose hand card is drawn
	 * @param dest   player who draws a hand card
	 */
	public void handTransfer(List<Card> cards, Player origin, Player dest) {
		if (cards.isEmpty()) {
			return;
		}
		if (!origin.getHands().containsAll(cards)) { //Implicitly check null
			throw new IllegalArgumentException();
		}
		List<Player> players = game.getPlayers();

		//Sends hand-cards-removed command
		SingleMessenger.command(origin, new HandChangedCommand(false, cards));
		origin.getHands().removeAll(cards);

		//Sends animation command
		List<Card> covered = Collections.nCopies(cards.size(), Card.COVERED_CARD);
		Command cmd = new CardMovedCommand(origin.getSeat(), true, covered, dest.getSeat(), true);
		MultiMessenger.commandAll(players, cmd);

		//Sends hand-cards-added command
		SingleMessenger.command(dest, new HandChangedCommand(true, cards));
		dest.getHands().addAll(cards);
	}

	/**
	 * Informs all the players (characters) of an happened game event, in order of the action.
	 * @param event game event
	 */
	public void inform(GameEvent event) {
		List<Player> players = game.getPlayers().getAlivesFrom(game.getRoundPlayer());
		players.forEach(p -> p.getCharacter().inform(game, p, event));
	}

	/**
	 * Replaces intelligence by another card
	 * @param replacement new intelligence
	 */
	public void intelChanged(Card replacement) {
		Objects.requireNonNull(replacement);
		Intelligence intel = game.getIntelligence();

		//Sends command
		Card old = intel.getFace();
		Command cmd = new IntelChangedCommand(old, replacement);
		MultiMessenger.commandAll(game.getPlayers(), cmd);

		//Sets intelligence
		intel.setIntelligence(replacement);
	}

	/**
	 * Lets the inelligence flipped.
	 */
	public void intelFlipped() {
		Intelligence intel = game.getIntelligence();

		//Sends command
		Card old = intel.getFace();
		intel.setCovered(!intel.isCovered()); //Sets intelligence
		Card next = intel.getFace();
		Command cmd = new IntelFlippedCommand(old, next);
		MultiMessenger.commandAll(game.getPlayers(), cmd);
	}

	/**
	 * Lets an intelligence moved to another player and invokes the following-up events.
	 * @param dest destination player
	 */
	public void intelMoved(Player dest) {
		//Set locator
		Intelligence im = game.getIntelligence();
		im.setLocation(dest);

		//Send command
		Command move = new IntelMovedCommand(im.getIntelligence(), dest.getSeat());
		MultiMessenger.commandAll(game.getPlayers(), move);
	}

	/**
	 * Lets a player receive an intelligence and invokes the following-up events.
	 * @param receiver the player who is abount to receive intelligence
	 */
	public void intelReceived(Player receiver) {
		Intelligence il = game.getIntelligence();
		final Card intel = il.getIntelligence();

		//Send receve command
		Command recv;
		if (il.isCovered()) {
			Card org;
			IntelligenceType type = il.getType();
			switch (type) {
			case SECRET:
				org = Card.COVERED_SECRET;
				break;
			case EXPRESS:
				org = Card.COVERED_EXPRESS;
				break;
			case DOCUMENT:
				org = Card.COVERED_DOCUMENT;
				break;
			default:
				throw new IllegalArgumentException();
			}
			recv = new IntelFlippedAndReceivedCommand(org, intel, il.getLocation().getSeat());
		}
		else {
			recv = new IntelReceivedCommand(il.getLocation().getSeat(), intel);
		}
		MultiMessenger.commandAll(game.getPlayers(), recv);
		il.getLocation().getIntels().add(intel);

		//Reset locator and inform events
		List<Player> alives = game.getPlayers().getAlivesFrom(game.getRoundPlayer());
		GameEvent e = new IntelReceviedEvent(il.getSender(), receiver, intel);
		il.reset();
		alives.forEach(p -> p.getCharacter().inform(game, p, e));
	}

	/**
	 * Lets an intelligence be sent and invokes the following-up events.
	 * @param intel       intelligence card
	 * @param origin      player who sent an intelligence
	 * @param destination the destination of this intelligence
	 * @param covered     true indicating that this intelligence card is covered
	 * @param type        type of this intelligence
	 */
	public void intelSent(Card intel, Player origin, Player destination, boolean covered, IntelligenceType type) {
		//Remove hand
		origin.getHands().remove(intel);
		SingleMessenger.command(origin, new HandChangedCommand(false, intel));

		//Send command
		game.getIntelligence().sendTo(intel, type, covered, origin, destination);
		Card face;
		if (!covered) {
			face = intel;
		}
		else {
			switch (type) {
			case EXPRESS:
				face = Card.COVERED_EXPRESS;
				break;
			case SECRET:
				face = Card.COVERED_SECRET;
				break;
			case DOCUMENT:
				face = Card.COVERED_DOCUMENT;
				break;
			default:
				throw new IllegalArgumentException();
			}
		}
		Command sendCmd = new IntelSentCommand(origin.getSeat(), destination.getSeat(), face);
		MultiMessenger.commandAll(game.getPlayers(), sendCmd);
	}

	/**
	 * Lets a player make a cards-selection.
	 * @param player      player who is about to make selection
	 * @param min         the minimum count of cards should be selected
	 * @param max         the maximim count of cards can be selected
	 * @param enforcement true indicating that this selection is mandatory
	 * @param inst        an instruction shown to the <tt>player</tt>
	 * @param instEx      an instruction shown to all the other players
	 * @param candidates  cards can be selected
	 * @return a list indicating which cards are selected, or null (never happens when <tt>enforcement</tt> is true) if the player gave up this selection
	 */
	public List<Card> selectCards(Player player, int min, int max, boolean enforcement, String inst, String instEx, List<Card> candidates) {
		//Send command
		int msgId = Selection.randomMessageId();
		int time = 9000 + 3000 * max;
		CardSelectionCommand cmd = new CardSelectionCommand(inst, candidates, min, max, enforcement, time, msgId);
		Command cmdEx = new TimeSetCommand(time, instEx, player.getSeat());
		MultiMessenger.commandOneAndOthers(game.getPlayers(), player, cmd, cmdEx);

		//Get reply
		SingleMessenger msg = new SingleMessenger(msgId, player, time, reply -> Examiner.checkSelection(reply, cmd));
		Reply reply = msg.reply(cmd.getDefaultReply());

		//Stop timer
		Command stop = new TimeSetCommand(-1, null, player.getSeat());
		MultiMessenger.commandAll(game.getPlayers(), stop);

		if (reply instanceof NullReply) {
			return null;
		}
		return ((MultiReply) reply).values;
	}

	/**
	 * Lets a player make a cards-selection which is order sensitive.
	 * @param player      player who is about to make a selection
	 * @param min         the minimum count of cards should be selected
	 * @param max         the maximim count of cards can be selected
	 * @param enforcement true indicating that this selection is mandatory
	 * @param inst        an instruction shown to the <tt>player</tt>
	 * @param inst2       an extra instruction shown to <tt>player</tt> telliing him/her the meaning of the order
	 * @param instEx      an instruction shown to all the other players
	 * @param candidates  cards can be selected
	 * @return a list indicating which cards are selected, or null (never happens when <tt>enforcement</tt> is true) if the player gave up this selection
	 */
	public List<Card> selectCardsByOrder(Player player, int min, int max, boolean enforcement, String inst, String inst2, String instEx, List<Card> candidates) {
		//Send command
		int msgId = Selection.randomMessageId();
		int time = 9000 + 3000 * max;
		CardSelectionCommand cmd = new CardSelectionCommand(inst, inst2, candidates, min, max, enforcement, time, msgId);
		Command cmdEx = new TimeSetCommand(time, instEx, player.getSeat());
		MultiMessenger.commandOneAndOthers(game.getPlayers(), player, cmd, cmdEx);

		//Get reply
		SingleMessenger msg = new SingleMessenger(msgId, player, time, reply -> Examiner.checkSelection(reply, cmd));
		Reply reply = msg.reply(cmd.getDefaultReply());

		//Stop timer
		Command stop = new TimeSetCommand(-1, null, player.getSeat());
		MultiMessenger.commandAll(game.getPlayers(), stop);

		if (reply instanceof NullReply) {
			return null;
		}
		return ((MultiReply) reply).values;
	}

	/**
	 * Lets a player select a hand card.
	 * @param player      player who is about to make selection
	 * @param enforcement true inditcating that this selection is mandatory
	 * @param inst        an instruction shown to <tt>player</tt>
	 * @param instEx      an instruction shown to all the other players
	 * @param candidates  hand cards that can be selected
	 * @return the hand card selected, or null (never happens when <tt>enforcement</tt> is true) if the player gave up this selection
	 */
	public Card selectHand(Player player, boolean enforcement, String inst, String instEx, List<Card> candidates) {
		List<Card> contributes = selectHands(player, 1, 1, enforcement, inst, instEx, candidates);
		return contributes == null ? null : contributes.get(0);
	}

	/**
	 * Lets a player select hand cards.
	 * @param player      player who is about to make selection
	 * @param min         minimum count of selection
	 * @param max         maximum count of selection
	 * @param enforcement true indicatiting that this selection is mandatory
	 * @param inst        an instruction shown to <tt>player</tt>
	 * @param instEx      an instruction shown to all the other players
	 * @param candidates  hand cards that can be selected
	 * @return a card list indicating hand cards selected, or null (never happens when <tt>enforcement</tt> is true) if the player gave up this selection
	 */
	public List<Card> selectHands(Player player, int min, int max, boolean enforcement, String inst, String instEx, List<Card> candidates) {

		//Send command
		int msgId = Selection.randomMessageId();
		Action action = new HandAction(min, max, candidates, enforcement, inst, null);
		int time = 9000 + max * 3000;
		BasicSelectionCommand cmd = new BasicSelectionCommand(time, action, msgId);
		Command cmdEx = new TimeSetCommand(time, instEx, player.getSeat());
		MultiMessenger.commandOneAndOthers(game.getPlayers(), player, cmd, cmdEx);

		//Get reply
		SingleMessenger msg = new SingleMessenger(msgId, player, time, reply -> Examiner.checkSelection(reply, cmd));
		Reply reply = msg.reply(cmd.getDefaultReply());
		List<Card> conts;
		if (reply instanceof NullReply) {
			conts = null;
		}
		else {
			conts = ((BasicReply<Decision<Card>>) reply).value.contributes;
		}

		//Command timer stop
		MultiMessenger.commandAll(game.getPlayers(), new TimeSetCommand(-1, null, player.getSeat()));

		return conts;
	}

	/**
	 * Lets a player select an intelligence
	 * @param player      player who is about to make selection
	 * @param enforcement true inditcating that this selection is mandatory
	 * @param inst        an instruction shown to <tt>player</tt>
	 * @param instEx      an instruction shown to all the other players
	 * @param candidates  hand cards that can be selected
	 * @return the intelligence selected, or null (never happens when <tt>enforcement</tt> is true) if the player gave up this selection
	 */
	public IntelLocation selectIntel(Player player, boolean enforcement, String inst, String instEx, List<IntelLocation> candidates) {
		List<IntelLocation> contributes = selectIntels(player, 1, 1, enforcement, inst, instEx, candidates);
		return contributes == null ? null : contributes.get(0);
	}

	/**
	 * Lets a player select an intelligences
	 * @param player      player who is about to make selection
	 * @param min         minimum count of selection
	 * @param max         maximum count of selection
	 * @param enforcement true indicatiting that this selection is mandatory
	 * @param inst        an instruction shown to <tt>player</tt>
	 * @param instEx      an instruction shown to all the other players
	 * @param candidates  hand cards that can be selected
	 * @return a card list indicating intelligence selected, or null (never happens when <tt>enforcement</tt> is true) if the player gave up this selection
	 */
	public List<IntelLocation> selectIntels(Player player, int min, int max, boolean enforcement, String inst, String instEx, List<IntelLocation> candidates) {

		//Send command
		int msgId = Selection.randomMessageId();
		Action action = new IntelAction(min, max, candidates, enforcement, inst, null);
		int time = 9000 + max * 3000;
		BasicSelectionCommand cmd = new BasicSelectionCommand(time, action, msgId);
		Command cmdEx = new TimeSetCommand(time, instEx, player.getSeat());
		MultiMessenger.commandOneAndOthers(game.getPlayers(), player, cmd, cmdEx);

		//Get reply
		SingleMessenger msg = new SingleMessenger(msgId, player, time, reply -> Examiner.checkSelection(reply, cmd));
		Reply reply = msg.reply(cmd.getDefaultReply());
		List<IntelLocation> conts;
		if (reply instanceof NullReply) {
			conts = null;
		}
		else {
			conts = ((BasicReply<Decision<IntelLocation>>) reply).value.contributes;
		}

		//Command timer stop
		MultiMessenger.commandAll(game.getPlayers(), new TimeSetCommand(-1, null, player.getSeat()));

		return conts;
	}

	/**
	 * Lets a player select a player
	 * @param player      player who is about to make selection
	 * @param enforcement true inditcating that this selection is mandatory
	 * @param inst        an instruction shown to <tt>player</tt>
	 * @param instEx      an instruction shown to all the other players
	 * @param candidates  hand cards that can be selected
	 * @return the player who is selected, or null (never happens when <tt>enforcement</tt> is true) if the player gave up this selection
	 */
	public Player selectPlayer(Player player, boolean enforcement, String inst, String instEx, List<Player> candidates) {
		List<Player> contributes = selectPlayers(player, 1, 1, enforcement, inst, instEx, candidates);
		return contributes == null ? null : contributes.get(0);
	}

	/**
	 * Lets a player select players
	 * @param player      player who is about to make selection
	 * @param min         minimum count of selection
	 * @param max         maximum count of selection
	 * @param enforcement true indicatiting that this selection is mandatory
	 * @param inst        an instruction shown to <tt>player</tt>
	 * @param instEx      an instruction shown to all the other players
	 * @param candidates  hand cards that can be selected
	 * @return a player list indicating who are selected, or null (never happens when <tt>enforcement</tt> is true) if the player gave up this selection
	 */
	public List<Player> selectPlayers(Player player, int min, int max, boolean enforcement, String inst, String instEx, List<Player> candidates) {

		//Send command
		int msgId = Selection.randomMessageId();
		List<Integer> iCands = candidates.stream().map(p -> p.getSeat()).collect(toList());
		Action action = new PlayerAction(min, max, iCands, enforcement, inst, null);
		int time = 9000 + max * 3000;
		BasicSelectionCommand cmd = new BasicSelectionCommand(time, action, msgId);
		Command cmdEx = new TimeSetCommand(time, instEx, player.getSeat());
		MultiMessenger.commandOneAndOthers(game.getPlayers(), player, cmd, cmdEx);

		//Get reply
		SingleMessenger msg = new SingleMessenger(msgId, player, time, reply -> Examiner.checkSelection(reply, cmd));
		Reply reply = msg.reply(cmd.getDefaultReply());
		List<Integer> conts;
		if (reply instanceof NullReply) {
			conts = null;
		}
		else {
			conts = ((BasicReply<Decision<Integer>>) reply).value.contributes;
		}

		//Command timer stop
		MultiMessenger.commandAll(game.getPlayers(), new TimeSetCommand(-1, null, player.getSeat()));

		List<Player> players = game.getPlayers();
		return conts.stream().map(i -> players.get(i)).collect(toList());
	}

	/**
	 * Triggers a {@link CardFunction#BURN} effect and invokes the following-up events.
	 * @param card   function card or null
	 * @param user   player who triggers this effect
	 * @param target player who is the target of this burn action
	 */
	public void triggerBurn(Card card, Player user, Player target) {
		//Inform event
		GameEvent e = new CardInvokingEvent(user, card, CardFunction.BURN, target);
		inform(e);

		//Select intelligence
		List<Card> tBlackIntels = target.getIntels().stream().filter(i -> i.getColor() == CardColor.BLACK).collect(toList());
		if (tBlackIntels.isEmpty()) {
			return;
		}
		List<Card> conts = selectCards(user,
		                               1,
		                               1,
		                               true,
		                               "選擇一張要" + CardFunction.BURN.getName() + "的情報",
		                               "等待玩家 " + user.getSeat() + " " + CardFunction.BURN.getName() + "。",
		                               tBlackIntels);
		cardBurnt(target, conts);
	}

	/**
	 * Triggers a {@link CardFunction#COUNTERACT} effect and invokes the following-up events.
	 * @param card         function card or null
	 * @param user         player who triggers this effect
	 * @param counteracted the function card which is the target of this coutneract action
	 */
	public void triggerCounteract(Card card, Player user, ExecutableCard counteracted) {
		//Informs event
		GameEvent e = new CardInvokingEvent(user, card, CardFunction.COUNTERACT, counteracted);
		inform(e);

		//Sets effect
		counteracted.effective = false;
	}

	/**
	 * Triggers a {@link CardFunction#DECODE} effect and invokes the following-up events.
	 * @param card function card or null
	 * @param user player who triggers this effect
	 */
	public void triggerDecode(Card card, Player user) {
		//Inform event
		GameEvent e = new CardInvokingEvent(user, card, CardFunction.DECODE, null);
		inform(e);

		//Affect
		Card intel = game.getIntelligence().getIntelligence();
		cardViewed(user, intel, CardFunction.DECODE.getName(), "左邊是正在傳遞中的情報。", "等待玩家 " + user.getSeat() + " 檢視情報。", List.of("確定"));
	}

	/**
	 * Triggers a {@link CardFunction#DISTRIBUTE} effect and invokes the following-up events.
	 * @param card function card or null
	 * @param user player who triggers this effect
	 */
	public void triggerDistribute(Card card, Player user) {
		//Inform event
		GameEvent e = new CardInvokingEvent(user, card, CardFunction.DISTRIBUTE, null);
		inform(e);


		PlayerList players = game.getPlayers();

		//Shuffle
		int count = (int) players.stream().filter(p -> p.isAlive() == Player.ALIVE).count();
		List<Card> list = game.getDeck().draw(count);
		int nCard = list.size();
		if (nCard == 0) {
			return;
		}
		Collections.shuffle(list);
		count = Math.min(count, nCard);

		//Start command
		Command start = new DistributeStateCommand(count);
		MultiMessenger.commandAll(players, start);

		//Distribute
		Player player = game.getRoundPlayer();
		Command cmd;
		for (Card next : list) {
			cmd = new DistributeCommand(next, player.getSeat(), next.getColor());
			MultiMessenger.commandAll(players, cmd);
			player.getIntels().add(next);
			player = players.nextAlive(player);
		}

		//Finish
		MultiMessenger.commandAll(players, DistributeStateCommand.END);
	}

	/**
	 * Triggers an {@link CardFunction#INTERCEPT} effect and invokes the following-up events.
	 * @param card function card or null
	 * @param user player who triggers this effect
	 */
	public void triggerIntercept(Card card, Player user) {
		//Inform event
		GameEvent e = new CardInvokingEvent(user, card, CardFunction.INTERCEPT, null);
		inform(e);

		//Affect
		user.setCondition(Condition.ACCEPTED);
		Command condCmd = new CondSetCommand(user.getSeat(), Condition.ACCEPTED, true);
		MultiMessenger.commandAll(game.getPlayers(), condCmd);
		intelMoved(user);
	}

	/**
	 * Triggers a {@link CardFunction#LOCK_ON} effect and invokes the following-up events.
	 * @param card   function card or null
	 * @param user   player who triggers this effect
	 * @param target the player who is the target of this lock-on action
	 */
	public void triggerLockOn(Card card, Player user, Player target) {
		//Inform event
		GameEvent e = new CardInvokingEvent(user, card, CardFunction.LOCK_ON, target);
		inform(e);

		//Affect
		Command cmd = new CondSetCommand(target.getSeat(), Condition.LOCKED, true);
		MultiMessenger.commandAll(game.getPlayers(), cmd);
		target.setCondition(Condition.LOCKED);
	}

	/**
	 * Triggers a {@link CardFunction#PROVE} effect and invokes the following-up events.
	 * @param card   function card which function type is {@link CardFunction#PROVE}
	 * @param user   player who triggers this effect
	 * @param target the player who is the target of this prove action
	 * @throws IllegalArgumentException if function type of <tt>card</tt> is not {@link CardFunction#PROVE}
	 */
	public void triggerProve(Card card, Player user, Player target) {
		if (card.getFunction() != CardFunction.PROVE) {
			throw new IllegalArgumentException();
		}

		//Inform event
		ProveInvokingEvent e = new ProveInvokingEvent(user, card, target);
		inform(e);

		//Measure argument
		Identity prove = card.getProveTarget();
		List<String> btnText = new ArrayList<>(2);
		boolean addedProve = card.getDrawNumber() > 0;
		String answ1 = addedProve ? "抽一張牌" : "棄一張手牌";
		String answ2 = addedProve ? "其實我是臥底" : "我是一個好人";
		String inst = "如果你的身分是" + prove + "，你" + answ1 + "，否則你聲明「" + answ2 + "」。";
		btnText.add(answ1 + (e.admission ? "" : "X"));
		btnText.add(answ2 + (e.denial ? "" : "X"));
		String instEx = "等待玩家 " + target.getSeat() + " 回答" + CardFunction.PROVE.getName() + "。";
		int index = cardViewed(target, card, CardFunction.PROVE.getName(), inst, instEx, btnText);
		boolean hit = index == 0;

		if (addedProve) {
			if (hit) {
				handDrawn(target, 1);
			}

			//TODO 其實我是臥底
			else {

			}
		}
		else {
			if (hit) {
				if (target.getHands().isEmpty()) {
					//TODO 顯示我是好人文字
					return;
				}
				else {
					Card removed = selectHand(target, true, "棄置一張手牌。", "等待玩家 " + target.getSeat() + " 選擇棄牌。", target.getHands());
					handDropped(target, List.of(removed));
				}
			}

			//TODO 我是一個好人
			else {

			}
		}
	}

	/**
	 * Triggers a {@link CardFunction#RETURN} effect and invokes the following-up events.
	 * @param card function card or null
	 * @param user player who triggers this effect
	 */
	public void triggerReturn(Card card, Player user) {
		//Inform event
		GameEvent e = new CardInvokingEvent(user, card, CardFunction.RETURN, null);
		inform(e);

		//Affect
		Intelligence il = game.getIntelligence();
		il.setDirection(!il.getDirection());
	}

	/**
	 * Triggers a {@link CardFunction#TRAP} effect and invokes the following-up events.
	 * @param card   function card or null
	 * @param user   player who triggers this effect
	 * @param target the player who is the target of this trap action
	 */
	public void triggerTrap(Card card, Player user, Player target) {

		//Event inform
		GameEvent e = new CardInvokingEvent(user, card, CardFunction.TRAP, target);
		inform(e);

		//Sends command
		Command cmd = new CondSetCommand(target.getSeat(), Condition.TRAPED, true);
		MultiMessenger.commandAll(game.getPlayers(), cmd);
		target.setCondition(Condition.TRAPED);
	}
}

