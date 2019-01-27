/**
 * Copyright 2017 K.Koike
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.seapanda.bunnyhop.control;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import net.seapanda.bunnyhop.bhprogram.BhProgramExecEnvError;
import net.seapanda.bunnyhop.bhprogram.LocalBhProgramManager;
import net.seapanda.bunnyhop.bhprogram.RemoteBhProgramManager;
import net.seapanda.bunnyhop.bhprogram.common.BhProgramData;
import net.seapanda.bunnyhop.common.BhParams;
import net.seapanda.bunnyhop.common.Pair;
import net.seapanda.bunnyhop.common.Point2D;
import net.seapanda.bunnyhop.common.tools.MsgPrinter;
import net.seapanda.bunnyhop.compiler.BhCompiler;
import net.seapanda.bunnyhop.compiler.CompileOption;
import net.seapanda.bunnyhop.message.BhMsg;
import net.seapanda.bunnyhop.message.MsgData;
import net.seapanda.bunnyhop.message.MsgTransporter;
import net.seapanda.bunnyhop.model.BhNode;
import net.seapanda.bunnyhop.model.Workspace;
import net.seapanda.bunnyhop.model.WorkspaceSet;
import net.seapanda.bunnyhop.root.BunnyHop;
import net.seapanda.bunnyhop.undo.UserOperationCommand;
import net.seapanda.bunnyhop.view.BhNodeCategoryListView;

/**
 * 画面上部のボタンのコントローラクラス
 * @author K.Koike
 */
public class MenuOperationController {

	private @FXML VBox menuOpViewBase;	//!< ボタンの基底ペイン
	private @FXML Button copyBtn;	 //!< コピーボタン
	private @FXML Button cutBtn;	 //!< カットボタン
	private @FXML Button pasteBtn;	 //!< ペーストボタン
	private @FXML Button deleteBtn; //!< デリートボタン
	private @FXML Button undoBtn;	 //!< アンドゥボタン
	private @FXML Button redoBtn;	 //!< リドゥボタン
	private @FXML Button zoomInBtn;	 //!< ズームインボタン
	private @FXML Button zoomOutBtn; //!< ズームアウトボタン
    private @FXML Button widenBtn;	//!< ワークスペース拡張ボタン
	private @FXML Button narrowBtn;	//!< ワークスペース縮小ボタン
	private @FXML Button addWorkspaceBtn; //!< ワークスペース追加ボタン
	private @FXML ToggleButton remotLocalSelectBtn;	//!< リモート/ローカル選択ボタン
	private @FXML Button executeBtn;	//!< 実行ボタン
	private @FXML Button terminateBtn;	//!< 終了ボタン
	private @FXML Button connectBtn;	//!< 接続ボタン
	private @FXML Button disconnectBtn;	//!< 切断ボタン
	private @FXML TextField ipAddrTextField;	//IPアドレス入力欄
	private @FXML TextField unameTextField; //!< ユーザ名
	private @FXML PasswordField passwordTextField;	//!< ログインパスワード
	private @FXML Button sendBtn;	//!< 送信ボタン
	private @FXML TextField stdInTextField;	//!< 標準入力テキストフィールド

	private final AtomicBoolean preparingForExecution = new AtomicBoolean(false);	//非同期でBhProgramの実行環境準備中の場合true
	private final AtomicBoolean preparingForTermination = new AtomicBoolean(false);	//非同期でBhProgramの実行環境終了中の場合true
	private final AtomicBoolean connecting = new AtomicBoolean(false);	//非同期で接続中の場合true
	private final AtomicBoolean disconnecting = new AtomicBoolean(false);	//非同期で切断中の場合true
	private final ExecutorService waitTaskExec = Executors.newCachedThreadPool();	//非同期処理完了待ちタスクを実行する

	/**
	 * イベントハンドラをセットする
	 * @param wss ワークスペースセット
	 * @param workspaceSetTab ワークスペース表示用のタブペイン
	 * @param bhNodeCategoryListView ノードカテゴリ選択ビュー
	 */
	public void init(
		WorkspaceSet wss,
		TabPane workspaceSetTab,
		BhNodeCategoryListView bhNodeCategoryListView) {

		setCutHandler(wss);	//カット
		setCopyHandler(wss);	//コピー
		setPasteHandler(wss, workspaceSetTab);	//ペースト
		setDeleteHandler(wss);	//デリート
		setUndoHandler(wss);	//アンドゥ
		setRedoHandler(wss);	//リドゥ
		setZoomInHandler(wss, bhNodeCategoryListView);	//ズームイン
		setZoomOutHandler(wss, bhNodeCategoryListView);	//ズームアウト
		setWidenHandler(wss);	//ワークスペースの範囲拡大
		setNarrowHandler(wss);	//ワークスペースの範囲縮小
		setAddWorkspaceHandler(wss);//ワークスペース追加
		setExecuteHandler(wss);
		setTerminateHandler();
		setConnectHandler();
		setDisconnectHandler();
		setSendHandler();
		setRemoteLocalSelectHandler();
	}

	/**
	 * コピーボタン押下時のイベントハンドラを登録する
	 * @param wss イベント時に操作するワークスペースセット
	 */
	private void setCopyHandler(WorkspaceSet wss) {

		copyBtn.setOnAction(action -> {
		Workspace currentWS = wss.getCurrentWorkspace();
			if (currentWS == null)
				return;
			wss.addNodeListReadyToCopy(currentWS.getSelectedNodeList());
		});
	}

	/**
	 * カットボタン押下時のイベントハンドラを登録する
	 * @param wss イベント時に操作するワークスペースセット
	 */
	private void setCutHandler(WorkspaceSet wss) {

		cutBtn.setOnAction(action -> {
			Workspace currentWS = wss.getCurrentWorkspace();
			if (currentWS == null)
				return;
			wss.addNodeListReadyToCut(currentWS.getSelectedNodeList());
		});
	}

	/**
	 * ペーストボタン押下時のイベントハンドラを登録する
	 * @param wss イベント時に操作するワークスペースセット
	 * @param workspaceSetTab ワークスペースセットに対応するタブペイン
	 */
	private void setPasteHandler(WorkspaceSet wss, TabPane workspaceSetTab) {

		pasteBtn.setOnAction(action -> {
			Workspace currentWS = wss.getCurrentWorkspace();
			if (currentWS == null)
				return;

			javafx.geometry.Point2D pos = workspaceSetTab.localToScene(0, workspaceSetTab.getHeight() / 2.0);
			MsgData localPos = MsgTransporter.INSTANCE.sendMessage(BhMsg.SCENE_TO_WORKSPACE, new MsgData(pos.getX(), pos.getY()), currentWS);
			double pastePosX = localPos.doublePair._1 + BhParams.REPLACED_NODE_POS * 2;
			double pastePosY = localPos.doublePair._2;
			wss.paste(currentWS, new Point2D(pastePosX, pastePosY));
		});
	}

	/**
	 * デリートボタン押下時のイベントハンドラを登録する
	 * @param wss イベント時に操作するワークスペースセット
	 */
	private void setDeleteHandler(WorkspaceSet wss) {

		deleteBtn.setOnAction(action -> {
			Workspace currentWS = wss.getCurrentWorkspace();
			if (currentWS == null)
				return;
			UserOperationCommand userOpeCmd = new UserOperationCommand();
			currentWS.deleteNodes(currentWS.getSelectedNodeList(), userOpeCmd);
			BunnyHop.INSTANCE.pushUserOpeCmd(userOpeCmd);
		});
	}

	/**
	 * アンドゥボタン押下時のイベントハンドラを登録する
	 * @param wss イベント時に操作するワークスペースセット
	 */
	private void setUndoHandler(WorkspaceSet wss) {

		undoBtn.setOnAction(action -> {
			MsgTransporter.INSTANCE.sendMessage(BhMsg.UNDO, wss);
		});
	}

	/**
	 * リドゥボタン押下時のイベントハンドラを登録する
	 * @param wss イベント時に操作するワークスペースセット
	 */
	private void setRedoHandler(WorkspaceSet wss) {

		redoBtn.setOnAction(action -> {
			MsgTransporter.INSTANCE.sendMessage(BhMsg.REDO, wss);
		});
	}

	/**
	 * ズームインボタン押下時のイベントハンドラを登録する
	 * @param wss イベント時に操作するワークスペースセット
	 * @param bhNodeCategoryListView ノードカテゴリ選択ビュー
	 */
	private void setZoomInHandler(WorkspaceSet wss, BhNodeCategoryListView bhNodeCategoryListView) {

		zoomInBtn.setOnAction(action -> {

			if (bhNodeCategoryListView.isAnyShowed()) {
				bhNodeCategoryListView.zoomAll(true);
				return;
			}

			Workspace currentWS = wss.getCurrentWorkspace();
			if (currentWS == null)
				return;
			MsgTransporter.INSTANCE.sendMessage(BhMsg.ZOOM, new MsgData(true), currentWS);
		});
	}

	/**
	 * ズームインボタン押下時のイベントハンドラを登録する
	 * @param wss イベント時に操作するワークスペースセット
	 * @param bhNodeCategoryListView ノードカテゴリ選択ビュー
	 */
	private void setZoomOutHandler(WorkspaceSet wss, BhNodeCategoryListView bhNodeCategoryListView) {

		zoomOutBtn.setOnAction(action -> {

			if (bhNodeCategoryListView.isAnyShowed()) {
				bhNodeCategoryListView.zoomAll(false);
				return;
			}

			Workspace currentWS = wss.getCurrentWorkspace();
			if (currentWS == null)
				return;
			MsgTransporter.INSTANCE.sendMessage(BhMsg.ZOOM, new MsgData(false), currentWS);
		});
	}

	/**
	 * ワークスペース拡大ボタンのイベントハンドラを登録する
	 * @param wss イベント時に操作するワークスペースセット
	 */
	private void setWidenHandler(WorkspaceSet wss) {

		widenBtn.setOnAction(action -> {
			Workspace currentWS = wss.getCurrentWorkspace();
			if (currentWS == null)
				return;
			MsgTransporter.INSTANCE.sendMessage(BhMsg.CHANGE_WORKSPACE_VIEW_SIZE, new MsgData(true), currentWS);
		});
	}

	/**
	 * ワークスペース縮小ボタンのイベントハンドラを登録する
	 * @param wss イベント時に操作するワークスペースセット
	 */
	private void setNarrowHandler(WorkspaceSet wss) {

		narrowBtn.setOnAction(action -> {
			Workspace currentWS = wss.getCurrentWorkspace();
			if (currentWS == null)
				return;
			MsgTransporter.INSTANCE.sendMessage(BhMsg.CHANGE_WORKSPACE_VIEW_SIZE, new MsgData(false), currentWS);
		});
	}

	/**
	 * ワークスペース追加ボタンのイベントハンドラを登録する
	 * @param wss イベント時に操作するワークスペースセット
	 */
	private void setAddWorkspaceHandler(WorkspaceSet wss) {

		addWorkspaceBtn.setOnAction(action -> {
			String defaultWsName = "ワークスペース" + (wss.getWorkspaceList().size()+1);
			TextInputDialog dialog = new TextInputDialog(defaultWsName);
			dialog.setTitle("ワークスペースの作成");
			dialog.setHeaderText(null);
			dialog.setContentText("ワークスペース名を入力してください");
			dialog.getDialogPane().getStylesheets().addAll(BunnyHop.INSTANCE.getAllStyles());
			Optional<String> inputText = dialog.showAndWait();
			inputText.ifPresent(wsName -> {
				UserOperationCommand userOpeCmd = new UserOperationCommand();
				BunnyHop.INSTANCE.addNewWorkSpace(wsName,
					BhParams.DEFAULT_WORKSPACE_WIDTH,
					BhParams.DEFAULT_WORKSPACE_HEIGHT,
					userOpeCmd);
				BunnyHop.INSTANCE.pushUserOpeCmd(userOpeCmd);
			});
		});
	}

	/**
	 * 実行ボタンのイベントハンドラを登録する
	 * @param wss イベント時に操作するワークスペースセット
	 */
	private void setExecuteHandler(WorkspaceSet wss) {

		executeBtn.setOnAction(action -> {

			if (preparingForExecution.get()) {
				MsgPrinter.INSTANCE.errMsgForUser("!! 実行準備中 !!\n");
				return;
			}

			Optional<Pair<List<BhNode>, BhNode>> nodesToCompile_nodeToExecOpt = prepareForCompilation(wss);
			Optional<Future<Boolean>> futureOpt = nodesToCompile_nodeToExecOpt.flatMap(nodesToCompile_nodeToExec -> {
				//コンパイル
				CompileOption option = new CompileOption(isLocalHost(), true, true, true);
				List<BhNode> nodesToCompile = nodesToCompile_nodeToExec._1;
				BhNode nodeToExec = nodesToCompile_nodeToExec._2;
				Optional<Path> execFilePath = BhCompiler.INSTANCE.compile(nodeToExec, nodesToCompile, option);
				return execFilePath.flatMap(filePath -> {
					if (isLocalHost()) {
						return LocalBhProgramManager.INSTANCE.executeAsync(filePath, BhParams.ExternalApplication.LOLCAL_HOST);
					}
					else {
						return RemoteBhProgramManager.INSTANCE.executeAsync(
							filePath, ipAddrTextField.getText(), unameTextField.getText(), passwordTextField.getText());
					}
				});
			});

			futureOpt.ifPresent(future -> {
				preparingForExecution.set(true);
				waitTaskExec.submit(() ->{
					try { future.get(); }
					catch(InterruptedException | ExecutionException e){}
					preparingForExecution.set(false);
				});
			});
		});
	}

	/**
	 * 終了ボタンのイベントハンドラを登録する
	 */
	private void setTerminateHandler() {

		terminateBtn.setOnAction(action -> {
			if (preparingForTermination.get()) {
				MsgPrinter.INSTANCE.errMsgForUser("!! 終了準備中 !!\n");
				return;
			}

			Optional<Future<Boolean>> futureOpt;
			if (isLocalHost())
				futureOpt = LocalBhProgramManager.INSTANCE.terminateAsync();
			else
				futureOpt = RemoteBhProgramManager.INSTANCE.terminateAsync();

			futureOpt.ifPresent(future -> {
				preparingForTermination.set(true);
				waitTaskExec.submit(() ->{
					try {future.get();}
					catch(InterruptedException | ExecutionException e){}
					preparingForTermination.set(false);
				});
			});
		});
	}

	/**
	 * 切断ボタンのイベントハンドラを登録する
	 */
	private void setDisconnectHandler() {
		disconnectBtn.setOnAction(action -> {
			if (disconnecting.get()) {
				MsgPrinter.INSTANCE.errMsgForUser("!! 切断準備中 !!\n");
				return;
			}

			Optional<Future<Boolean>> futureOpt;
			if (isLocalHost())
				futureOpt = LocalBhProgramManager.INSTANCE.disconnectAsync();
			else
				futureOpt = RemoteBhProgramManager.INSTANCE.disconnectAsync();

			futureOpt.ifPresent(future -> {
				disconnecting.set(true);
				waitTaskExec.submit(() ->{
					try {future.get();}
					catch(InterruptedException | ExecutionException e){}
					disconnecting.set(false);
				});
			});
		});
	}

	/**
	 * 接続ボタンのイベントハンドラを登録する
	 */
	private void setConnectHandler() {
		connectBtn.setOnAction(action -> {

			if (connecting.get()) {
				MsgPrinter.INSTANCE.errMsgForUser("!! 接続準備中 !!\n");
				return;
			}

			Optional<Future<Boolean>> futureOpt;
			if (isLocalHost())
				futureOpt = LocalBhProgramManager.INSTANCE.connectAsync();
			else
				futureOpt = RemoteBhProgramManager.INSTANCE.connectAsync();

			futureOpt.ifPresent(future -> {
				connecting.set(true);
				waitTaskExec.submit(() ->{
					try {future.get();}
					catch(InterruptedException | ExecutionException e){}
					connecting.set(false);
				});
			});
		});
	}

	/**
	 * 送信ボタンのイベントハンドラを登録する
	 */
	private void setSendHandler() {
		sendBtn.setOnAction(action -> {
			BhProgramExecEnvError errCode;
			if (isLocalHost())
				errCode = LocalBhProgramManager.INSTANCE.sendAsync(
					new BhProgramData(BhProgramData.TYPE.INPUT_STR, stdInTextField.getText()));
			else
				errCode = RemoteBhProgramManager.INSTANCE.sendAsync(
					new BhProgramData(BhProgramData.TYPE.INPUT_STR, stdInTextField.getText()));

			switch (errCode) {
				case SEND_QUEUE_FULL:
					MsgPrinter.INSTANCE.errMsgForUser("!! 送信失敗 (送信データ追加失敗) !!\n");
					break;

				case SEND_WHEN_DISCONNECTED:
					MsgPrinter.INSTANCE.errMsgForUser("!! 送信失敗 (未接続) !!\n");
					break;

				case SUCCESS:
					MsgPrinter.INSTANCE.errMsgForUser("-- 送信完了 --\n");
					break;

				default:
					throw new AssertionError("invalid " + BhProgramExecEnvError.class.getSimpleName() + " " + errCode);
			}
		});
	}

	/**
	 * リモート/セレクトを切り替えた時のボタン
	 */
	private void setRemoteLocalSelectHandler() {


		remotLocalSelectBtn.selectedProperty().addListener((observable, oldVal, newVal) -> {
			if (newVal) {
				ipAddrTextField.setDisable(false);
				unameTextField.setDisable(false);
				passwordTextField.setDisable(false);
				remotLocalSelectBtn.setText("リモート");
			}
			else {
				ipAddrTextField.setDisable(true);
				unameTextField.setDisable(true);
				passwordTextField.setDisable(true);
				remotLocalSelectBtn.setText("ローカル");
			}
		});
	}

	/**
	 * IPアドレス入力欄にローカルホストが指定してある場合true を返す
	 */
	private boolean isLocalHost() {
		return !remotLocalSelectBtn.isSelected();
	}

	/**
	 * コンパイル前の準備をする
	 * @return コンパイル対象ノードと実行対象ノードのペア
	 */
	private Optional<Pair<List<BhNode>, BhNode>> prepareForCompilation(WorkspaceSet wss) {

		//実行対象があるかどうかのチェック
		Workspace currentWS = wss.getCurrentWorkspace();
		HashSet<BhNode> selectedNodeList = currentWS.getSelectedNodeList();
		if (selectedNodeList.size() != 1) {
			MsgPrinter.INSTANCE.alert(AlertType.ERROR, "実行対象の選択", null,"実行対象を一つ選択してください");
			return Optional.empty();
		}

		// 実行対象以外を非選択に.
		BhNode nodeToExec = selectedNodeList.toArray(new BhNode[selectedNodeList.size()])[0].findRootNode();
		UserOperationCommand userOpeCmd = new UserOperationCommand();
		currentWS.clearSelectedNodeList(userOpeCmd);
		currentWS.addSelectedNode(nodeToExec, userOpeCmd);
		BunnyHop.INSTANCE.pushUserOpeCmd(userOpeCmd);

		//コンパイル対象ノードを集める
		List<BhNode> nodesToCompile = new ArrayList<>();
		wss.getWorkspaceList().forEach(ws -> {
			ws.getRootNodeList().forEach(node -> {
				nodesToCompile.add(node);
			});
		});
		nodesToCompile.remove(nodeToExec);
		return Optional.of(new Pair<>(nodesToCompile, nodeToExec));
	}

	/**
	 * ユーザメニュー操作のイベントを起こす
	 * @param op 基本操作を表す列挙子
	 */
	public void fireEvent(MENU_OPERATION op) {

		switch (op) {
			case COPY:
				copyBtn.fire();
				break;

			case CUT:
				cutBtn.fire();
				break;

			case PASTE:
				pasteBtn.fire();
				break;

			case DELETE:
				deleteBtn.fire();
				break;

			case UNDO:
				undoBtn.fire();
				break;

			case REDO:
				redoBtn.fire();
				break;

			default:
				throw new AssertionError("invalid menu operation " + op);
		}
	}

	public enum MENU_OPERATION {
		COPY,
		CUT,
		PASTE,
		DELETE,
		UNDO,
		REDO,
	}
}

















