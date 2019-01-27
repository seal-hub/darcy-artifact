package net.leo.message.server.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.bridge.command.action.Action;
import net.leo.message.base.bridge.command.action.ButtonAction;
import net.leo.message.base.bridge.command.action.PlayerAction;
import net.leo.message.base.bridge.command.animation.sync.InitHandDrawnCommand;
import net.leo.message.base.bridge.command.animation.unsync.CharPeekedCommand;
import net.leo.message.base.bridge.command.animation.unsync.CharSetCommand;
import net.leo.message.base.bridge.command.animation.unsync.CondRemovedCommand;
import net.leo.message.base.bridge.command.animation.unsync.HandChangedCommand;
import net.leo.message.base.bridge.command.animation.unsync.IdenShownCommand;
import net.leo.message.base.bridge.command.animation.unsync.TimeSetCommand;
import net.leo.message.base.bridge.command.data.CharData;
import net.leo.message.base.bridge.command.program.GameBeginningCommand;
import net.leo.message.base.bridge.command.program.SkillInitCommand;
import net.leo.message.base.bridge.command.select.BasicSelectionCommand;
import net.leo.message.base.bridge.command.select.CharSelectionCommand;
import net.leo.message.base.bridge.command.select.IntelSendingCommand;
import net.leo.message.base.bridge.command.select.Selection;
import net.leo.message.base.bridge.reply.BasicReply;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.base.bridge.reply.Reply;
import net.leo.message.base.lang.Card;
import net.leo.message.base.lang.Character;
import net.leo.message.base.lang.Condition;
import net.leo.message.base.lang.Identity;
import net.leo.message.base.lang.IntelligenceType;
import static net.leo.message.base.lang.IntelligenceType.EXPRESS;
import net.leo.message.server.character.CharacterCard;
import net.leo.message.server.conversation.Examiner;
import net.leo.message.server.conversation.MultiMessenger;
import net.leo.message.server.conversation.PlayerReplyPair;
import net.leo.message.server.conversation.ReplyChecker;
import net.leo.message.server.conversation.SingleMessenger;
import net.leo.message.server.event.IntelSendingEvent;
import net.leo.message.server.net.Client;
import net.leo.message.server.stack.Stack;

/**
 * Game surroundings
 * @author Parabola
 */
public class Game {

	private Operator operator = new Operator(this);
	private LinkedList<Card> deadwood = new LinkedList<>();
	private Deck deck = new Deck(deadwood);
	private Intelligence intel = new Intelligence();
	private PlayerList players;
	private int nPlayer;
	private Player roundPlayer;
	private Stage stage;
	private Stack stack = new Stack(this);

	public Game(List<Client> clients) {
		gameInit(clients);
		gameStart();
	}


	private void gameInit(List<Client> clients) {
		initSurroundings(clients);
		initStart();
		initSetChars();
		initIdentities();
		initDraw();
	}

	private void gameStart() {
		roundPlayer = players.get(0);
		stage = Stage.START;

		for ( ; ; ) {
			switch (stage) {
			case START:
				stage = Stage.DRAW; //TODO
				continue;

			case DRAW:
				stageHandDrawing();
				continue;

			case USAGE:
				stageUsage();
				continue;

			case INTEL_SENDING:
				stageIntelSending();
				continue;

			case INTEL_PASSING:
				stageIntelPassing();
				continue;

			case INTEL_ARRIVED:
				stageIntelArrived();
				continue;

			case INTEL_RECEIVING:
				stageIntelReceiving();
				continue;

			default:
				throw new IllegalArgumentException(stage.name());
			}
		}
	}

	private void initDraw() {
		//Send draw animation command
		Command cmd = new InitHandDrawnCommand(3);
		MultiMessenger.commandAll(players, cmd);

		//Add hands and send command
		List<Card> hands;
		for (Player p : players) {
			hands = deck.draw(3);
			p.getHands().addAll(hands);
			SingleMessenger.command(p, new HandChangedCommand(true, hands));
		}
		MultiMessenger.checkPreogress(players);
	}

	private void initIdentities() {
		players.forEach(p -> {
			SingleMessenger.command(p, new IdenShownCommand(p.getIdentity(), p.getSeat(), true));
		});
		MultiMessenger.checkPreogress(players);
	}

	private void initSetChars() {
		//Send Character command
		List<CharacterCard> chars = Initializer.characters(nPlayer * 2);
		LinkedList<CharacterCard> copy = new LinkedList<>(chars);
		int msgId = Selection.randomMessageId();
		for (Player p : players) {
			CharacterCard c1 = copy.poll();
			CharacterCard c2 = copy.poll();
			Reply dfReply = new BasicReply<>(msgId, c1.getName());
			List<CharData> charData = new ArrayList<>(2);
			charData.add(c1.getCharacterData());
			charData.add(c2.getCharacterData());
			p.command(new CharSelectionCommand(25000, dfReply, charData, msgId));
		}

		//Listen to reply
		Set<Player> listened = new HashSet<>(players);
		ReplyChecker checker = ((source, reply) -> {
			if (!BasicReply.class.isInstance(reply)) {
				return false;
			}
			if (!String.class.isInstance(((BasicReply) reply).value)) {
				return false;
			}

			int seat = source.getSeat();
			String value = (String) ((BasicReply) reply).value;
			return (chars.get(seat * 2).getName().equals(value) || chars.get(seat * 2 + 1).getName().equals(value))
					&& listened.remove(source);
		});
		MultiMessenger mm = new MultiMessenger(msgId, new HashSet<>(listened), checker, 25000);
		PlayerReplyPair prp;
		while (!listened.isEmpty()) {
			prp = mm.next();
			if (prp == null) {
				break; //Time's up
			}

			BasicReply<String> reply = (BasicReply<String>) prp.getReply();
			Player source = prp.getSource();
			String charName = reply.value;
			source.setCharacter(CharacterCard.getInstance(Character.getInstanceByName(charName)));
		}

		//Check players who are over-time
		for (Player p : players) {
			if (p.getCharacter() == null) {
				p.setCharacter(chars.get(p.getSeat() * 2));
			}
		}

		//Send character set and skills-init command
		List<CharData> data = new ArrayList<>(players.size());
		for (Player p : players) {
			CharacterCard c = p.getCharacter();
			data.add(c.isPublic() ? c.getCharacterData() : null);
		}
		Command cmd = new CharSetCommand(data);
		for (Player p : players) {
			SingleMessenger.command(p, cmd);
			if (!p.getCharacter().isPublic()) {
				SingleMessenger.command(p, new CharPeekedCommand(p.getSeat(), p.getCharacter().getCharacterData()));
			}
			SingleMessenger.command(p, new SkillInitCommand(p.getCharacter().getCharacterData().skillData));
		}
	}

	private void initStart() {
		//Send game start command
		int i = 0;
		List<String> playerIds = players.stream().map(p -> p.getName()).collect(toList());
		for (Player p : players) {
			SingleMessenger.command(p, new GameBeginningCommand(playerIds, i++));
		}
		MultiMessenger.checkPreogress(players);
	}

	private void initSurroundings(List<Client> clients) {
		//Set seats
		//TODO (testing) Collections.shuffle(clients);

		//Set identities
		nPlayer = clients.size();
		List<Identity> idens = Initializer.identities(nPlayer);

		//Set players
		players = new PlayerList();
		int i = 0;
		Player player;
		for (Client cn : clients) {
			player = new Player(cn, i);
			player.setIdentity(idens.get(i));
			players.add(player);
			i++;
		}
	}

	private void nextRound() {
		roundPlayer = players.nextAlive(roundPlayer);
		players.stream().filter(p -> p.isAlive() == Player.ALIVE).forEach(p -> p.setCondition(null));
		Command cmd = new CondRemovedCommand(players.stream().filter(p -> p.isAlive() == Player.ALIVE).map(p -> p.getSeat()).collect(toSet()));
		MultiMessenger.commandAll(players, cmd);
	}

	private int runEntireUsage() {
		for ( ; ; ) {

			//Using cards
			for ( ; ; ) {
				if (!runSingleUsage()) {
					break;
				}
			}

			//Stack runs
			int externalEffect = stack.execute();
			if (externalEffect == Stack.ONE_MORE_USAGE) {
				continue;
			}

			return externalEffect;
		}
	}

	private boolean runSingleUsage() {
		return new Usage(this).startUsage();
	}

	private void stageHandDrawing() {
		operator.handDrawn(roundPlayer, 2);
		stage = Stage.USAGE;
	}

	private void stageIntelArrived() {
		int rs = runEntireUsage();
		if (rs == Stack.INTELLIGENCE_RETURNED) {
			Player next = intel.getNext(players);
			operator.intelMoved(next);
			stage = Stage.INTEL_PASSING;
		}
		else {
			stage = Stage.INTEL_RECEIVING;
		}
	}

	private void stageIntelPassing() {
		runEntireUsage();

		Player loc = intel.getLocation();
		if (loc.getCondition() == Condition.TRAPED) {
			Player next = intel.getNext(players);
			operate().intelMoved(next);
		}
		else {
			stage = Stage.INTEL_ARRIVED;
		}
	}

	private void stageIntelReceiving() {
		Player inquired = intel.getLocation();

		//Determines if this receipt is madantory
		boolean received;
		Condition cond = inquired.getCondition();
		if (inquired == intel.getSender() || cond == Condition.LOCKED || cond == Condition.ACCEPTED) {

			//The receipt is mandantory and the client cannot reject
			received = true;
		}

		else {
			//This receipt is not madantory
			//Asks the client if she/he would receive the intelligence
			int iSeat = inquired.getSeat();

			//Sends an intlliegnce-sent command to all players
			int msgId = Selection.randomMessageId();
			ButtonAction action = new ButtonAction(List.of("接收", "不接收"), "是否接收這張情報？", null);
			BasicSelectionCommand cmd = new BasicSelectionCommand(12000, action, msgId);
			Command cmdEx = new TimeSetCommand(12000, "等待玩家 " + iSeat + " 選擇接收情報。", iSeat);
			MultiMessenger.commandOneAndOthers(players, inquired, cmd, cmdEx);

			//Listens to reply from clients
			SingleMessenger msg = new SingleMessenger(msgId, inquired, 12000, reply -> Examiner.checkSelection(reply, cmd));
			BasicReply<Decision<Integer>> reply = msg.reply((BasicReply<Decision<Integer>>) cmd.getDefaultReply());
			int answer = reply.value.contributes.get(0);
			received = answer == 0;

			//Sends a timer-interrupted command to all players
			Command stopCmd = new TimeSetCommand(-1, null, iSeat);
			MultiMessenger.commandEx(players, inquired, stopCmd);
		}

		//Determines the next stage
		if (received) {
			operator.intelReceived(inquired);
			nextRound();
			stage = Stage.START;
		}
		else {
			Player next = intel.getNext(players);
			operator.intelMoved(next);
			stage = Stage.INTEL_PASSING;
		}
	}

	private void stageIntelSending() {
		List<Card> handList = roundPlayer.getHands();
		final int msgId = Selection.randomMessageId();
		Map<Object, Action> candidates = new HashMap<>(handList.size() + 2);

		//Determines candidates of hand cards
		List<Integer> expressCandidates = players.stream()
		                                         .filter(p -> p != roundPlayer)
		                                         .map(p -> p.getSeat())
		                                         .collect(toList());
		Action expAction = new PlayerAction(1, 1, expressCandidates, false, "請選擇直達目標。", null);
		Set<Card> handSet = new HashSet<>(handList);
		handSet.forEach(h -> candidates.put(h, h.getType() == EXPRESS ? expAction : null));

		//Determines candidates of skills
		IntelSendingEvent e = new IntelSendingEvent();
		roundPlayer.getCharacter()
		           .getSkills()
		           .stream()
		           .filter(skill -> skill.inform(this, roundPlayer, e) > 0)
		           .forEach(skill -> candidates.put(skill.getName(), skill.prepare(this, roundPlayer, e)));

		//Sends command
		IntelSendingCommand cmd = new IntelSendingCommand(candidates, 12000, msgId);
		Command cmdEx = new TimeSetCommand(12000, "等待玩家 " + roundPlayer.getSeat() + " 傳遞情報。", roundPlayer.getSeat());
		MultiMessenger.commandOneAndOthers(players, roundPlayer, cmd, cmdEx);

		//Listens and gets a selection reply
		SingleMessenger msg = new SingleMessenger(msgId, roundPlayer, 12000, reply -> Examiner.checkSelection(reply, cmd));
		BasicReply<Decision<Card>> reply = msg.reply((BasicReply<Decision<Card>>) cmd.getDefaultReply());
		MultiMessenger.commandAll(players, new TimeSetCommand(-1, null, roundPlayer.getSeat()));

		//Sends animation command
		final Card intel = reply.value.contributes.get(0);
		IntelligenceType type = intel.getType();
		Player dest = type == EXPRESS ? players.get(((Decision<Integer>) reply.value.nextDecision).contributes.get(0)) : players.nextAlive(roundPlayer);
		operator.intelSent(intel, roundPlayer, dest, intel.getType() != IntelligenceType.DOCUMENT, intel.getType());
		stage = Stage.INTEL_PASSING;
	}

	private void stageUsage() {
		runEntireUsage();
		stage = Stage.INTEL_SENDING;
	}


	public List<Player> getAlives() {
		return players.stream()
		              .filter(p -> p.isAlive() == Player.ALIVE)
		              .collect(toList());
	}

	public LinkedList<Card> getDeadwood() {
		return deadwood;
	}

	public Deck getDeck() {
		return deck;
	}

	public Intelligence getIntelligence() {
		return intel;
	}

	public Player getPlayer(int seat) {
		return players.get(seat);
	}

	public int getPlayerCount() {
		return nPlayer;
	}

	public PlayerList getPlayers() {
		return players;
	}

	public Player getRoundPlayer() {
		return roundPlayer;
	}

	public Stack getStack() {
		return stack;
	}

	public Stage getStage() {
		return stage;
	}

	public Operator operate() {
		return operator;
	}
}
