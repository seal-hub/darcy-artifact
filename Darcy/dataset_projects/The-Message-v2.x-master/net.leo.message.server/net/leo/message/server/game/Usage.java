package net.leo.message.server.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.bridge.command.action.Action;
import net.leo.message.base.bridge.command.action.FuncCardAction;
import net.leo.message.base.bridge.command.action.PlayerAction;
import net.leo.message.base.bridge.command.animation.sync.CardUsedCommand;
import net.leo.message.base.bridge.command.animation.unsync.HandChangedCommand;
import net.leo.message.base.bridge.command.animation.unsync.StackDisabledCommand;
import net.leo.message.base.bridge.command.select.Selection;
import net.leo.message.base.bridge.command.select.UsageCommand;
import net.leo.message.base.bridge.reply.BasicReply;
import net.leo.message.base.bridge.reply.Decision;
import net.leo.message.base.bridge.reply.NullReply;
import net.leo.message.base.lang.Card;
import net.leo.message.base.lang.CardColor;
import net.leo.message.base.lang.CardFunction;
import net.leo.message.base.lang.Condition;
import net.leo.message.base.lang.IntelligenceType;
import net.leo.message.server.conversation.Examiner;
import net.leo.message.server.conversation.MultiMessenger;
import net.leo.message.server.conversation.PlayerReplyPair;
import net.leo.message.server.conversation.SingleMessenger;
import net.leo.message.server.event.GameEvent;
import net.leo.message.server.event.UsageStartEvent;
import net.leo.message.server.skill.ExecutableSkill;
import net.leo.message.server.skill.Skill;
import net.leo.message.server.stack.Burn;
import net.leo.message.server.stack.Counteract;
import net.leo.message.server.stack.Decode;
import net.leo.message.server.stack.Distribute;
import net.leo.message.server.stack.Executable;
import net.leo.message.server.stack.ExecutableCard;
import net.leo.message.server.stack.Intercept;
import net.leo.message.server.stack.LockOn;
import net.leo.message.server.stack.Prove;
import net.leo.message.server.stack.Return;
import net.leo.message.server.stack.Stack;
import net.leo.message.server.stack.Trap;

/**
 * @author Parabola
 */
public class Usage {

	private static final int USAGE_TIME = 12000;

	/**
	 * Queries if a card is usable.
	 * @param func function of this card
	 * @param user owner of this card
	 * @param game game surroundings
	 * @return true if this card can be used
	 */
	public static boolean isCardUsable(CardFunction func, Player user, Game game) {
		Stage stage = game.getStage();

		switch (func) {
		case LOCK_ON:
		case PROVE:
		case DISTRIBUTE:
			return (stage == Stage.USAGE || stage == Stage.INTEL_PASSING)
					&& game.getRoundPlayer() == user;

		case TRAP:
			return stage == Stage.INTEL_PASSING;

		case BURN:
			return true;

		case DECODE:
			return stage == Stage.INTEL_ARRIVED
					&& game.getIntelligence().getLocation() == user;

		case RETURN:
			Intelligence il = game.getIntelligence();
			if (stage != Stage.INTEL_ARRIVED) {
				return false;
			}
			if (il.getSender() == user) {
				return false;
			}
			if (il.getType() == IntelligenceType.EXPRESS) {
				return false;
			}
			if (il.getLocation() != user) {
				return false;
			}
			Condition uCond = user.getCondition();
			if (uCond == Condition.LOCKED || uCond == Condition.ACCEPTED) {
				return false;
			}
			return true;

		case COUNTERACT:
			return true;

		case INTERCEPT:
			return stage == Stage.INTEL_PASSING
					&& game.getIntelligence().getSender() != user;
		}

		//Should not happen
		throw new IllegalArgumentException();
	}

	/**
	 * Gets the targets of a card.
	 * @param func function of this card
	 * @param user owner of this card
	 * @param game game surroundings
	 * @return an integer list indicating the targets of this card
	 * @throws IllegalArgumentException if function type of this card is {@link CardFunction#DECODE}, {@link CardFunction#DISTRIBUTE}, {@link CardFunction#INTERCEPT} or
	 *                                  {@link CardFunction#RETURN}.
	 */
	public static List<Integer> getCardTargets(CardFunction func, Player user, Game game) {
		switch (func) {
		case LOCK_ON:
		case PROVE:
			return game.getPlayers()
			           .stream()
			           .filter(t -> user != t)
			           .map(t -> t.getSeat())
			           .collect(toList());

		case TRAP:
			return game.getPlayers()
			           .stream()
			           .filter(t -> user != t && game.getIntelligence().getSender() != t)
			           .map(t -> t.getSeat())
			           .collect(toList());

		case BURN:
			return game.getPlayers()
			           .stream()
			           .filter(t -> t.getIntels()
			                         .stream()
			                         .anyMatch(i -> i.getColor() == CardColor.BLACK))
			           .map(t -> t.getSeat())
			           .collect(toList());

		case COUNTERACT:
			Stack stack = game.getStack();
			return stack.getAll()
			            .stream()
			            .filter(e -> e instanceof ExecutableCard)
			            .map(e -> stack.getIndexOfFuncCard((ExecutableCard) e))
			            .collect(toList());
		}

		throw new IllegalArgumentException();
	}

	public static Action getActionOfFunction(Player player, CardFunction func, Game game) {
		switch (func) {
		case LOCK_ON:
		case PROVE:
		case BURN:
		case TRAP:
			List<Integer> targets = getCardTargets(func, player, game);
			return new PlayerAction(1, 1, targets, false, "請選擇要" + func.getName() + "的玩家。", null);

		case COUNTERACT:
			targets = getCardTargets(func, player, game);
			return new FuncCardAction(1, 1, targets, false, "請選擇要" + func.getName() + "的卡牌。", null);

		default:
			return null;
		}
	}

	public static void sendUsageCommand(Player user, Decision<Card> decision, CardFunction func, Game game) {

		//Remove user's hand
		Card card = decision.contributes.get(0);
		user.getHands().remove(card);
		SingleMessenger.command(user, new HandChangedCommand(false, card));

		//Determines card-used command
		Command usedCmd;
		switch (func) {
		case LOCK_ON:
		case PROVE:
		case BURN:
		case TRAP:
		case COUNTERACT:
			int intTarget = (Integer) decision.nextDecision.contributes.get(0);
			usedCmd = new CardUsedCommand(card, user.getSeat(), intTarget);
			break;

		case DISTRIBUTE:
		case INTERCEPT:
		case DECODE:
		case RETURN:
			usedCmd = new CardUsedCommand(card, user.getSeat());
			break;

		default:
			throw new IllegalArgumentException();
		}

		//Sends card-used command
		MultiMessenger.commandAll(game.getPlayers(), usedCmd);

		//Determines the executable object
		Executable exe = null;
		switch (func) {
		case DISTRIBUTE:
			exe = new Distribute(user, card);
			break;
		case INTERCEPT:
			exe = new Intercept(user, card);
			break;
		case DECODE:
			exe = new Decode(user, card);
			break;
		case RETURN:
			exe = new Return(user, card);
			break;
		case COUNTERACT:
			exe = new Counteract(user, card, game.getStack().getFuncCardByIndex((Integer) decision.nextDecision.contributes.get(0)));
			break;
		case LOCK_ON:
			exe = new LockOn(user, card, game.getPlayer((Integer) decision.nextDecision.contributes.get(0)));
			break;
		case BURN:
			exe = new Burn(user, card, game.getPlayer((Integer) decision.nextDecision.contributes.get(0)));
			break;
		case TRAP:
			exe = new Trap(user, card, game.getPlayer((Integer) decision.nextDecision.contributes.get(0)));
			break;
		case PROVE:
			exe = new Prove(user, card, game.getPlayer((Integer) decision.nextDecision.contributes.get(0)));
			break;
		}

		//Adds to stack
		Stack stack = game.getStack();
		stack.add(exe);
	}

	private final Game game;
	private final int msgId = Selection.randomMessageId();
	private final Set<Player> parts;

	Usage(Game game) {
		this.game = Objects.requireNonNull(game);
		this.parts = new HashSet<>(game.getAlives()); //TODO who particiaptes??
	}

	private Map<Object, Action> getCandidatesOfPlayer(Player player) {
		Map<Object, Action> map = new HashMap<>();

		//Determines actions of hand cards
		for (CardFunction func : CardFunction.values()) {
			putCandidatesOfFunction(map, player, func);
		}

		//Determines actions of skills
		putCandidatesOfSkills(map, player);

		return map;
	}

	private UsageCommand getUsageCommandOfPlayer(Player user, Set<Integer> parts, int msgId) {
		//Determines candidates of hand cards
		Map<Object, Action> candidates = getCandidatesOfPlayer(user);
		return new UsageCommand(candidates, parts, USAGE_TIME, msgId);
	}

	private PlayerReplyPair getUsageReply(Map<Player, UsageCommand> commandMap) {
		//Sets listener
		MultiMessenger msg = new MultiMessenger(msgId, game.getPlayers(), (source, reply) -> Examiner.checkSelection(reply, commandMap.get(source)), USAGE_TIME);

		//Listens to reply
		PlayerReplyPair prp;
		Set<Player> partsCopy = new HashSet<>(this.parts);
		while ((prp = msg.next()) != null) {

			//Someone passes
			if (NullReply.class.isInstance(prp.getReply())) {

				//If everyone passes, returns null
				if (partsCopy.remove(prp.getSource()) && partsCopy.isEmpty()) {
					return null;
				}
			}

			//Someone used a card, so returns this reply
			else {
				return prp;
			}
		}

		//Time's up and no one replies, so returns null
		return null;
	}

	private void putCandidatesOfFunction(Map<? super Card, ? super Action> res, Player player, CardFunction func) {
		if (!isCardUsable(func, player, game)) {
			return;
		}

		Action action = getActionOfFunction(player, func, game);
		player.getHands()
		      .stream()
		      .filter(h -> h.getFunction() == func)
		      .forEach(h -> res.put(h, action));
	}

	private void putCandidatesOfSkills(Map<? super String, ? super Action> res, Player player) {
		GameEvent e = new UsageStartEvent(game.getStage());
		player.getCharacter()
		      .getSkills()
		      .stream()
		      .filter(s -> s.inform(game, player, e) > 0 && s.isInvokable(game, player, e))
		      .forEach(s -> res.put(s.getName(), s.prepare(game, player, e)));
	}

	private Map<Player, UsageCommand> sendAndGetUsageStartCommand() {
		//Gets seats of participants
		Set<Integer> iParts = parts.stream().map(p -> p.getSeat()).collect(toSet());
		UsageCommand byStandCmd = new UsageCommand(iParts, USAGE_TIME);
		Map<Player, UsageCommand> commandMap = new HashMap<>(iParts.size());

		//Determine participants
		game.getPlayers().forEach(p -> {
			if (parts.contains(p)) {
				commandMap.put(p, getUsageCommandOfPlayer(p, iParts, msgId));
			}
			else {
				commandMap.put(p, byStandCmd);
			}
		});

		//Sends command
		game.getPlayers().forEach(p -> {
			SingleMessenger.command(p, commandMap.get(p));
		});

		return commandMap;
	}

	private void sendDisabledCardCommand() {
		List<Integer> diss = game.getStack().disable();
		Command dissCmd = new StackDisabledCommand(diss);
		MultiMessenger.commandAll(game.getPlayers(), dissCmd);
	}

	private void sendUsageCommand(PlayerReplyPair prp) {
		Player user = prp.getSource();

		//Determines this is a card decision or a skill decision
		Decision<?> desc = ((BasicReply<Decision<?>>) prp.getReply()).value;
		Object cont = desc.contributes.get(0);

		//It's a card decision
		if (cont instanceof Card) {
			CardFunction func = ((Card) cont).getFunction();
			sendUsageCommand(user, (Decision<Card>) desc, func, game);
		}

		//It's a skill decision
		else {
			Skill invoked =
					user.getCharacter()
					    .getSkills()
					    .stream()
					    .filter(s -> s.getName().equals(cont))
					    .findFirst()
					    .get();
			invoked.invoke(game, user, desc.nextDecision, null);

			//Puts into stack if this skill is executable skill
			if (invoked instanceof ExecutableSkill) {
				game.getStack().add((Executable) invoked);
			}
		}

		sendDisabledCardCommand();
	}

	private void sendUsageStopCommand() {
		MultiMessenger.commandAll(game.getPlayers(), UsageCommand.STOP);
	}

	/**
	 * Starts a single usage and gets an {@link Executable} object indicating what a player has used.
	 * @return true if someone used a card or invoked a skill
	 */
	public boolean startUsage() {
		Map<Player, UsageCommand> commandMap = sendAndGetUsageStartCommand();
		PlayerReplyPair playerAndReply = getUsageReply(commandMap);
		sendUsageStopCommand();

		//Somebody used card or invoked skills
		if (playerAndReply != null) {
			sendUsageCommand(playerAndReply);
			return true;
		}

		//Nobody had actions
		return false;
	}
}
