package net.leo.message.client.domain;

import java.awt.Component;
import java.awt.Container;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import javax.swing.SwingUtilities;
import net.leo.message.base.bridge.command.Command;
import net.leo.message.base.bridge.command.action.Action;
import net.leo.message.base.bridge.command.animation.sync.CardDrawnCommand;
import net.leo.message.base.bridge.command.animation.sync.CardDroppedCommand;
import net.leo.message.base.bridge.command.animation.sync.CardMovedCommand;
import net.leo.message.base.bridge.command.animation.sync.CardUsedCommand;
import net.leo.message.base.bridge.command.animation.sync.CharFlippedCommand;
import net.leo.message.base.bridge.command.animation.sync.CondSetCommand;
import net.leo.message.base.bridge.command.animation.sync.DistributeCommand;
import net.leo.message.base.bridge.command.animation.sync.InitHandDrawnCommand;
import net.leo.message.base.bridge.command.animation.sync.IntelChangedCommand;
import net.leo.message.base.bridge.command.animation.sync.IntelFlippedAndReceivedCommand;
import net.leo.message.base.bridge.command.animation.sync.IntelFlippedCommand;
import net.leo.message.base.bridge.command.animation.sync.IntelMovedCommand;
import net.leo.message.base.bridge.command.animation.sync.IntelReceivedCommand;
import net.leo.message.base.bridge.command.animation.sync.IntelSentCommand;
import net.leo.message.base.bridge.command.animation.sync.SkillInvokedCommand;
import net.leo.message.base.bridge.command.animation.unsync.CharPeekedCommand;
import net.leo.message.base.bridge.command.animation.unsync.CharSetCommand;
import net.leo.message.base.bridge.command.animation.unsync.CondRemovedCommand;
import net.leo.message.base.bridge.command.animation.unsync.DeathCommand;
import net.leo.message.base.bridge.command.animation.unsync.DistributeStateCommand;
import net.leo.message.base.bridge.command.animation.unsync.HandChangedCommand;
import net.leo.message.base.bridge.command.animation.unsync.IdenShownCommand;
import net.leo.message.base.bridge.command.animation.unsync.StackDisabledCommand;
import net.leo.message.base.bridge.command.animation.unsync.StackExecutedCommand;
import net.leo.message.base.bridge.command.animation.unsync.TimeSetCommand;
import net.leo.message.base.bridge.command.data.CharData;
import net.leo.message.base.bridge.command.program.GameBeginningCommand;
import net.leo.message.base.bridge.command.program.PorgCheckCommand;
import net.leo.message.base.bridge.command.program.SkillInitCommand;
import net.leo.message.base.bridge.command.select.BasicSelectionCommand;
import net.leo.message.base.bridge.command.select.CardSelectionCommand;
import net.leo.message.base.bridge.command.select.CardViewCommand;
import net.leo.message.base.bridge.command.select.CharSelectionCommand;
import net.leo.message.base.bridge.command.select.IntelSendingCommand;
import net.leo.message.base.bridge.command.select.PsSkillSelctionCommand;
import net.leo.message.base.bridge.command.select.UsageCommand;
import net.leo.message.base.bridge.reply.BasicReply;
import net.leo.message.base.bridge.reply.MultiReply;
import net.leo.message.base.bridge.reply.Reply;
import net.leo.message.base.lang.Card;
import net.leo.message.client.animation.executor.AnimationLayer;
import net.leo.message.client.animation.executor.DistributeLayer;
import net.leo.message.client.animation.executor.IntelligenceLayer;
import net.leo.message.client.animation.executor.SyncController;
import net.leo.message.client.dialog.ButtonDialog;
import net.leo.message.client.dialog.CardOptionDialog;
import net.leo.message.client.dialog.CharSelectionDialog;
import net.leo.message.client.dialog.CheckDialog;
import net.leo.message.client.dialog.DialogLayer;
import net.leo.message.client.dialog.OrderedCheckDialog;
import net.leo.message.client.dialog.RadioDialog;
import net.leo.message.client.element.CharCard;
import net.leo.message.client.element.Death;
import net.leo.message.client.element.GameCard;
import net.leo.message.client.element.Skill;
import net.leo.message.client.hand.HandBox;
import net.leo.message.client.hand.HandView;
import net.leo.message.client.player.CountView;
import net.leo.message.client.player.PlayerBox;
import net.leo.message.client.player.PlayerView;
import net.leo.message.client.selection.ActionController;
import net.leo.message.client.selection.IntelController;
import net.leo.message.client.selection.PsActionController;
import net.leo.message.client.selection.UsageController;
import net.leo.message.client.skill.SkillBox;
import net.leo.message.client.utility.ArrowTarget;
import net.leo.message.client.utility.CharHint;
import static net.leo.message.client.utility.CharHint.toCharHint;
import net.leo.message.client.utility.HintLayer;
import net.leo.message.client.utility.Timable;
import net.leo.message.client.utility.Timer;

public final class Interpreter {

	private GameView gameView = null;
	private final Container gameComponent;
	private final Socket socket;
	private final ObjectInputStream ois;
	private final ObjectOutputStream oos;

	public Interpreter(Container container, Socket socket) throws IOException {
		if (container == null || socket == null) {
			throw new NullPointerException();
		}
		this.gameComponent = container;
		this.socket = socket;
		this.ois = new ObjectInputStream(socket.getInputStream());
		this.oos = new ObjectOutputStream(socket.getOutputStream());

		run();
	}

	private void anim(Command command) {
		switch (command.title) {
		case "初始抽牌":
			animInitDraw((InitHandDrawnCommand) command);
			return;
		case "情報發送":
			animIntelSent((IntelSentCommand) command);
			return;
		case "情報移動":
			animIntelMoved((IntelMovedCommand) command);
			return;
		case "情報翻面":
			animIntelFlipped((IntelFlippedCommand) command);
			return;
		case "情報替換":
			animIntelChanged((IntelChangedCommand) command);
			return;
		case "情報接收":
			animIntelReceived((IntelReceivedCommand) command);
			return;
		case "情報翻面接收":
			animIntelFlippedAndReceived((IntelFlippedAndReceivedCommand) command);
			return;
		case "卡牌移動":
			animCardsMoved((CardMovedCommand) command);
			return;
		case "卡牌抽取":
			animCardsDrawn((CardDrawnCommand) command);
			return;
		case "卡牌棄置":
			animCardsDropped((CardDroppedCommand) command);
			return;
		case "出牌":
			animCardUsed((CardUsedCommand) command);
			return;
		case "玩家翻面":
			animCharFlipped((CharFlippedCommand) command);
			return;
		case "設定狀態":
			animConditionSet((CondSetCommand) command);
			return;
		case "真偽發牌":
			animDistribute((DistributeCommand) command);
			return;
		case "發動技能":
			animSkillInvoked((SkillInvokedCommand) command);
			return;
		default:
			throw new IllegalArgumentException(command.title);
		}
	}

	private void animCardUsed(CardUsedCommand command) {
		//Determine target
		ArrowTarget target;
		if (command.target == -1) {
			target = null;
		}
		else if (command.target < 100) {
			target = gameView.getPlayerView(command.target);
		}
		else {
			target = gameView.getStackBox().get(command.target - 100);
		}

		//Run animation
		AnimationLayer animLayer = gameView.getAnimationLayer();
		PlayerView user = gameView.getPlayerView(command.user);
		GameCard card = GameCard.getInstance(command.card);
		animLayer.use(card, user, target);
	}

	private void animCardsDrawn(CardDrawnCommand command) {
		//Add animation
		AnimationLayer animLayer = gameView.getAnimationLayer();
		PlayerView receiver = gameView.getPlayerView(command.seat);
		command.cards.forEach(c -> {
			GameCard card = GameCard.getInstance(c);
			CountView dest = receiver.getCountView(command.handcard ? null : card.getColor());
			animLayer.addDrawAnimation(card, dest);
		});

		//Run animation
		animLayer.draw();
	}

	private void animCardsDropped(CardDroppedCommand command) {
		//Add animation
		AnimationLayer animLayer = gameView.getAnimationLayer();
		PlayerView receiver = gameView.getPlayerView(command.seat);
		command.cards.forEach(c -> {
			GameCard card = GameCard.getInstance(c);
			CountView dest = receiver.getCountView(command.handcard ? null : card.getColor());
			animLayer.addDropAnimation(card, dest);
		});

		//Run animation
		animLayer.drop();
	}

	private void animCardsMoved(CardMovedCommand command) {
		//Add animation
		AnimationLayer animLayer = gameView.getAnimationLayer();
		PlayerView oPlayer = gameView.getPlayerView(command.origin);
		PlayerView dPlayer = gameView.getPlayerView(command.dest);
		command.cards.forEach(c -> {

			//Determine origin and destination
			CountView origin = oPlayer.getCountView(command.orgHand ? null : c.getColor());
			CountView dest = dPlayer.getCountView(command.destHand ? null : c.getColor());

			//Add animation
			GameCard card = GameCard.getInstance(c);
			animLayer.addFlyAnimation(card, origin, dest);
		});

		//Run animation
		animLayer.fly();
	}

	private void animCharFlipped(CharFlippedCommand command) {
		PlayerBox animLayer = gameView.getPlayerBox();
		PlayerView player = gameView.getPlayerView(command.seat);
		animLayer.flipTo(player, command.animInfo);
	}

	private void animConditionSet(CondSetCommand command) {
		PlayerBox animLayer = gameView.getPlayerBox();
		animLayer.setCondition(command.animInfo);
	}

	private void animDistribute(DistributeCommand command) {
		GameCard card = GameCard.getInstance(command.card);
		CountView dest = gameView.getPlayerView(command.seat).getCountView(command.color);
		DistributeLayer animLayer = gameView.getDistributeLayer();
		animLayer.distribute(card, dest);
	}

	private void animInitDraw(InitHandDrawnCommand command) {
		//Add animation
		AnimationLayer animLayer = gameView.getAnimationLayer();
		CountView handView;
		for (int s = 0 ; s < gameView.nPlayer ; s++) {
			handView = gameView.getPlayerView(s).getCountView(null);
			for (int i = 0 ; i < command.nCard ; i++) {
				animLayer.addDrawAnimation(GameCard.COVERED_CARD, handView);
			}
		}

		//Run animation
		animLayer.draw();
	}

	private void animIntelChanged(IntelChangedCommand command) {
		IntelligenceLayer animLayer = gameView.getIntelligenceLayer();
		animLayer.changeTo(GameCard.getInstance(command.next));
	}

	private void animIntelFlipped(IntelFlippedCommand command) {
		IntelligenceLayer animLayer = gameView.getIntelligenceLayer();
		animLayer.flipTo(GameCard.getInstance(command.next));
	}

	private void animIntelFlippedAndReceived(IntelFlippedAndReceivedCommand command) {
		GameCard card = GameCard.getInstance(command.next);
		CountView dest = gameView.getPlayerView(command.dest).getCountView(command.next.getColor());
		IntelligenceLayer animLayer = gameView.getIntelligenceLayer();
		animLayer.flipAndReceive(card, dest);
	}

	private void animIntelMoved(IntelMovedCommand command) {
		IntelligenceLayer animLayer = gameView.getIntelligenceLayer();
		animLayer.moveTo(command.dest);
	}

	private void animIntelReceived(IntelReceivedCommand command) {
		CountView dest = gameView.getPlayerView(command.dest).getCountView(command.intel.getColor());
		IntelligenceLayer animLayer = gameView.getIntelligenceLayer();
		animLayer.receive(dest);
	}

	private void animIntelSent(IntelSentCommand command) {
		//Run animation
		GameCard card = GameCard.getInstance(command.intel);
		IntelligenceLayer animLayer = gameView.getIntelligenceLayer();
		animLayer.sendTo(card, command.origin, command.dest);

		//Decrease count of hands of sender by 1
		PlayerView sender = gameView.getPlayerView(command.origin);
		sender.getCountView(null).increase(-1);
	}

	private void animSkillInvoked(SkillInvokedCommand command) {
		//Determine target
		ArrowTarget target;
		PlayerView invoker;
		if (command.target == -1) {
			target = null;
			invoker = null;
		}
		else if (command.target < 100) {
			target = gameView.getPlayerView(command.target);
			invoker = gameView.getPlayerView(command.invoker);
		}
		else {
			target = gameView.getStackBox().get(command.target - 100);
			invoker = gameView.getPlayerView(command.invoker);
		}

		//Run animation
		AnimationLayer animLayer = gameView.getAnimationLayer();
		Skill skill = Skill.getInstance(CharCard.getInstance(command.character), command.name);
		animLayer.launch(skill, invoker, target);
	}

	private void basic(Command command) {
		switch (command.title) {
		case "進度確認":
			basicProgress((PorgCheckCommand) command);
		}
	}

	private void basicProgress(PorgCheckCommand command) {
		int id = command.id;
		reply(new BasicReply<>(id, id));
	}

	private void init(Command command) {
		switch (command.title) {
		case "遊戲開始":
			initGameStart((GameBeginningCommand) command);
			return;
		case "選擇角色":
			initCharSelection((CharSelectionCommand) command);
			return;
		case "顯示角色":
			initCharShown((CharSetCommand) command);
			return;
		case "註冊技能":
			initSkills((SkillInitCommand) command);
			return;
		}
		throw new IllegalArgumentException(command.title);
	}

	private void initCharSelection(CharSelectionCommand command) {
		//Set hints
		List<CharHint> hints = command.charData.stream().map(c -> toCharHint(c)).collect(Collectors.toList());

		//Set dialog and timer
		HintLayer hintLayer = gameView.getHintLayer();
		DialogLayer diaLayer = gameView.getDialogLayer();
		Timer timer = gameView.getTimer();
		timer.addAll(new HashSet<>(gameView.getAllPlayerViews()));

		CharSelectionDialog dialog;
		dialog = new CharSelectionDialog(hintLayer, hints);
		Runnable end = () -> {
			Rectangle bounds = dialog.getBounds();
			diaLayer.remove(dialog);
			diaLayer.repaint(bounds);
			hintLayer.hideHint();
		};
		dialog.setListener((source, selected) -> {
			timer.stop(false);
			reply(new BasicReply<>(command.id, selected.getName()));
			end.run();
		});
		timer.add(dialog);

		//Show dialog and start timer
		diaLayer.add(dialog);
		diaLayer.validate();
		timer.run(command.time, () -> {
			reply(command.getDefaultReply());
			end.run();
		});
	}

	private void initCharShown(CharSetCommand command) {
		int i = 0;
		for (CharData datum : command.charDatum) {
			if (datum == null) {
				i++;
				continue;
			}
			CharCard character = CharCard.getInstance(datum.character);
			CharHint hint = toCharHint(datum);
			PlayerView pv = gameView.getPlayerView(i++);
			pv.setApprChar(character, hint);
		}
	}

	private void initGameStart(GameBeginningCommand command) {
		String[] playerIds = command.names.toArray(new String[command.names.size()]);
		gameView = new GameView(playerIds, command.seat);
		gameComponent.add(gameView);
		gameComponent.validate();
	}

	private void initSkills(SkillInitCommand command) {
		SkillBox skillBox = gameView.getSkillBox();
		skillBox.init(command.skillData);
		skillBox.validate();
	}

	private void interpret(Command command) {
		System.out.println(command);
		try {
			switch (command.type) {
			case "基本":
				basic(command);
				return;
			case "初始":
				SwingUtilities.invokeAndWait(() -> init(command));
				return;
			case "同步動畫":
				SwingUtilities.invokeAndWait(() -> anim(command));
				SyncController.await();
				return;
			case "非同步動畫":
				SwingUtilities.invokeAndWait(() -> unsyncAnim(command));
				return;
			case "用牌階段":
				SwingUtilities.invokeAndWait(() -> usage(command));
				return;
			case "選擇":
				SwingUtilities.invokeAndWait(() -> select(command));
				return;
			default:
				throw new IllegalArgumentException(command.type);
			}
		}
		catch (InvocationTargetException | InterruptedException e) {
			//Should not happen
			throw new RuntimeException(e);
		}
	}

	private void reply(Reply reply) {
		try {
			oos.writeObject(reply);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(reply);
	}

	private void run() {
		try {
			while (true) {
				interpret((Command) ois.readObject());
			}
		}
		catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			try {
				oos.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			try {
				ois.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			try {
				socket.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void select(Command command) {
		switch (command.title) {
		case "檢視卡牌":
			selectView((CardViewCommand) command);
			return;
		case "一般選擇":
			selectNormal((BasicSelectionCommand) command);
			return;
		case "選擇卡牌":
		case "順選卡牌":
			selectCards((CardSelectionCommand) command);
			return;
		case "被動技能":
			selectPassiveSkill((PsSkillSelctionCommand) command);
			return;
		case "傳遞情報":
			selectIntel((IntelSendingCommand) command);
			return;
		default:
			throw new IllegalArgumentException();
		}
	}

	private void selectCard(CardSelectionCommand command) {
		//Set dialog
		Timer timer = gameView.getTimer();
		DialogLayer layer = gameView.getDialogLayer();
		RadioDialog dialog = new RadioDialog(command.inst1, command.candidates, command.enforcement);
		Runnable end = () -> {
			Rectangle bounds = dialog.getBounds();
			layer.remove(dialog);
			layer.repaint(bounds);
		};
		dialog.setListener((source, list) -> {
			timer.stop(false);
			if (list == null) {
				reply(command.getDefaultReply());
			}
			else {
				reply(new MultiReply<>(command.id, List.of(list.get(0).getElement())));
			}
			end.run();
		});

		//Show dialog and run timer
		timer.add(gameView.getPlayerView(gameView.mySeat));
		timer.add(dialog);
		layer.add(dialog);
		layer.validate();
		timer.run(command.time, () -> {
			reply(command.getDefaultReply());
			gameView.setGameListener(null);
			gameView.resetAll();
			end.run();
		});
	}

	private void selectCards(CardSelectionCommand command) {
		if (command.min == 1 && command.max == 1) {
			selectCard(command);
			return;
		}

		Component dialog;
		Timer timer = gameView.getTimer();

		//Determine type of dialog
		if (command.orderRelated) {
			dialog = new OrderedCheckDialog(command.inst1, command.candidates, command.inst2, command.min, command.max, command.enforcement);
		}
		else {
			dialog = new CheckDialog(command.inst1, command.candidates, command.min, command.max, command.enforcement);
		}

		//Set card selected listener
		DialogLayer layer = gameView.getDialogLayer();
		Runnable end = () -> {
			Rectangle bounds = dialog.getBounds();
			layer.remove(dialog);
			layer.repaint(bounds);
		};
		((CardOptionDialog) dialog).setListener((source, list) -> {
			timer.stop(false);
			if (list == null) {
				reply(command.getDefaultReply());
			}
			else {
				reply(new MultiReply<>(command.id, list.stream().map(c -> c.getElement()).collect(toList())));
			}
			end.run();
		});

		//Show dialog and run timer
		timer.add(gameView.getPlayerView(gameView.mySeat));
		timer.add((Timable) dialog);
		layer.add(dialog);
		layer.validate();
		timer.run(command.time, () -> {
			reply(command.getDefaultReply());
			gameView.setGameListener(null);
			gameView.resetAll();
			end.run();
		});
	}

	private void selectIntel(IntelSendingCommand command) {
		//Set timer
		Timer timer = gameView.getTimer();
		timer.add(gameView.getPlayerView(gameView.mySeat));
		timer.add(gameView.getInstruction());
		timer.add(gameView.getInstruction());

		//Run selection
		Map<Card, Action> handActions = new HashMap<>(command.candidates.size());
		Map<String, Action> skillActions = new HashMap<>(2);
		Set<Card> handSet = command.candidates.keySet().stream().filter(o -> o instanceof Card).map(o -> (Card) o).collect(toSet());
		Set<String> skillSet = command.candidates.keySet().stream().filter(o -> o instanceof String).map(o -> (String) o).collect(toSet());
		handSet.forEach(h -> handActions.put(h, command.candidates.get(h)));
		skillSet.forEach(s -> skillActions.put(s, command.candidates.get(s)));
		IntelController uc = new IntelController(handActions, skillActions, gameView);
		uc.setListener((decision) -> {
			timer.stop(false);
			if (decision == null) {
				reply(command.getDefaultReply());
			}
			else {
				reply(new BasicReply<>(command.id, decision));
			}
		});
		uc.run();

		//Run timer
		timer.run(command.time, () -> {
			reply(command.getDefaultReply());
			gameView.setGameListener(null);
			gameView.resetAll();
		});
	}

	private void selectNormal(BasicSelectionCommand command) {
		//Set timer
		Timer timer = gameView.getTimer();
		timer.add(gameView.getInstruction());
		timer.add(gameView.getPlayerView(gameView.mySeat));

		//Set button and listenerrun controller
		ActionController.execute(gameView, (decision) -> {
			timer.stop(false);
			if (decision == null) {
				reply(command.getDefaultReply());
			}
			else {
				reply(new BasicReply<>(command.id, decision));
			}
		}, command.action);

		//run timer
		timer.run(command.time, () -> {
			gameView.setGameListener(null);
			gameView.resetAll();
			reply(command.getDefaultReply());
		});
	}

	private void selectPassiveSkill(PsSkillSelctionCommand command) {
		//Set bar listener
		Timer timer = gameView.getTimer();
		PsActionController sc = new PsActionController(command.candidates, command.enforcement != null, gameView);
		sc.setListener((skilName, decision) -> {
			timer.stop(false);
			if (decision == null) {
				reply(command.getDefaultReply());
			}
			else {
				reply(new BasicReply<>(command.id, decision));
			}
			gameView.resetAll();
		});
		sc.run();

		//Run timer
		timer.add(gameView.getPlayerView(gameView.mySeat));
		timer.add(gameView.getInstruction());
		timer.run(command.time, () -> {
			gameView.setGameListener(null);
			gameView.resetAll();
			reply(command.getDefaultReply());
		});
	}

	private void selectView(CardViewCommand command) {
		DialogLayer layer = gameView.getDialogLayer();
		Timer timer = gameView.getTimer();

		//Set dialog
		ButtonDialog dialog = new ButtonDialog(command.title, GameCard.getInstance(command.card), command.inst);
		Runnable end = () -> {
			Rectangle bounds = dialog.getBounds();
			layer.remove(dialog);
			layer.repaint(bounds);
		};
		dialog.setListener((source, index) -> {
			timer.stop(false);
			reply(new BasicReply<>(command.id, index));
			end.run();
		});

		//Set timer
		timer.add(gameView.getPlayerView(gameView.mySeat));
		timer.add(dialog);

		//Add buttons into dialog
		command.buttonsText.forEach(buttonText -> {
			boolean enable = true;
			if (buttonText.endsWith("X")) {
				buttonText = buttonText.substring(0, buttonText.length() - 1);
				enable = false;
			}
			dialog.addButton(buttonText, enable);
		});

		//Show dialog and run timer
		layer.add(dialog);
		layer.validate();
		timer.run(command.time, () -> {
			timer.stop(false);
			reply(command.getDefaultReply());
			gameView.setGameListener(null);
			gameView.resetAll();
			end.run();
		});
	}

	private void unsyncAnim(Command command) {
		switch (command.title) {
		case "窺視":
			unsyncAnimCharPeeked((CharPeekedCommand) command);
			return;
		case "身分":
			unsyncAnimIdentityShown((IdenShownCommand) command);
			return;
		case "解除狀態":
			unsyncAnimCondRemoved((CondRemovedCommand) command);
			return;
		case "真偽莫辨":
			unsyncAnimDistributeState((DistributeStateCommand) command);
			return;
		case "時間軸":
			unsyncTimeRoll((TimeSetCommand) command);
			return;
		case "手牌變更":
			unsyncHandChanged((HandChangedCommand) command);
			return;
		case "死亡":
			unsyncDeath((DeathCommand) command);
			return;
		case "堆疊生效":
		case "堆疊鎖定":
		case "堆疊解鎖":
			unsyncAnimStack((StackExecutedCommand) command);
			return;
		case "堆疊失效":
			unsyncStackDisabled((StackDisabledCommand) command);
			return;
		}
		throw new IllegalArgumentException();
	}

	private void unsyncAnimCharPeeked(CharPeekedCommand command) {
		PlayerBox layer = gameView.getPlayerBox();
		layer.peek(command.info);
	}

	private void unsyncAnimCondRemoved(CondRemovedCommand command) {
		PlayerBox layer = gameView.getPlayerBox();
		layer.removeCondition(command.seats);
	}

	private void unsyncAnimDistributeState(DistributeStateCommand command) {
		if (command.count == -1) {
			gameView.getDistributeLayer().clear();
		}
		else {
			gameView.getDistributeLayer().prepare(command.count);
		}
	}

	private void unsyncAnimIdentityShown(IdenShownCommand command) {
		PlayerBox layer = gameView.getPlayerBox();
		layer.displayIdentity(command.animInfo);
	}

	private void unsyncAnimStack(StackExecutedCommand command) {
		switch (command.title) {
		case "堆疊鎖定":
			gameView.getStackBox().lock();
			return;
		case "堆疊生效":
			gameView.getStackBox().pollLast();
			return;
		case "堆疊解鎖":
			gameView.getStackBox().unlock();
			return;
		}
		throw new IllegalArgumentException();
	}

	private void unsyncDeath(DeathCommand command) {
		CharHint hint = CharHint.toCharHint(command.chardata);
		Death death = Death.getInstance(command.identity, command.dead);
		gameView.getPlayerView(command.seat).setDeath(CharCard.getInstance(command.chardata.character), hint, death);
	}

	private void unsyncHandChanged(HandChangedCommand command) {
		HandBox handBox = gameView.getHandBox();
		if (command.add) {
			List<HandView> cards = command.cards.stream().map(c -> new HandView(GameCard.getInstance(c))).collect(toList());
			handBox.offerAll(cards);
		}
		else {
			handBox.removeAll(command.cards);
		}
	}

	private void unsyncStackDisabled(StackDisabledCommand command) {
		gameView.getStackBox().disabled(command.diss);
	}

	private void unsyncTimeRoll(TimeSetCommand command) {
		//Gets time rolls set
		Set<? extends Timable> rolls = command.seats.stream().map(i -> gameView.getPlayerView(i)).collect(toSet());

		//Invoke
		Timer timer = gameView.getTimer();
		if (command.time == -1) {
			//Remove candidates
			timer.removeAll(rolls);
			gameView.getInstruction().setInstruction(null);
		}
		else {
			//Start timer
			gameView.getInstruction().setInstruction(command.inst);
			timer.addAll(rolls);
			timer.run(command.time, null);
		}
	}

	private void usage(Command command) {
		switch (command.title) {
		case "出牌":
			usageStart((UsageCommand) command);
			return;
		case "旁觀":
			usageByStand((UsageCommand) command);
			return;
		case "終止":
			usageEnd((UsageCommand) command);
			return;
		}
		throw new IllegalArgumentException();
	}

	private void usageByStand(UsageCommand command) {
		//Run timer
		Timer timer = gameView.getTimer();
		command.participants.forEach(p -> {
			timer.add(gameView.getPlayerView(p));
		});
		timer.run(command.time, null);
	}

	@SuppressWarnings("unused")
	private void usageEnd(UsageCommand command) {
		Timer timer = gameView.getTimer();
		timer.stop(false);
		gameView.setGameListener(null);
		gameView.resetAll();
	}

	private void usageStart(UsageCommand command) {
		//Set timer
		Timer timer = gameView.getTimer();
		timer.addAll(command.participants.stream().map(i -> gameView.getPlayerView(i)).collect(toSet()));
		timer.add(gameView.getInstruction());

		//Run selection
		Map<Card, Action> handActions = new HashMap<>(command.candidates.size());
		Map<String, Action> skillActions = new HashMap<>(2);
		Set<Card> handSet = command.candidates.keySet().stream().filter(o -> o instanceof Card).map(o -> (Card) o).collect(toSet());
		Set<String> skillSet = command.candidates.keySet().stream().filter(o -> o instanceof String).map(o -> (String) o).collect(toSet());
		handSet.forEach(h -> handActions.put(h, command.candidates.get(h)));
		skillSet.forEach(s -> skillActions.put(s, command.candidates.get(s)));
		UsageController uc = new UsageController(handActions, skillActions, gameView);
		uc.setListener((decision) -> {
			timer.stop(false);
			if (decision == null) {
				reply(command.getDefaultReply());
			}
			else {
				reply(new BasicReply<>(command.id, decision));
			}
		});
		uc.run();

		//Run timer
		timer.run(command.time, () -> {
			reply(command.getDefaultReply());
			gameView.resetAll();
		});
	}
}
