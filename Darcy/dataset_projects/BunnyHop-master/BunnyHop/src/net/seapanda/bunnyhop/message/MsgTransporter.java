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
package net.seapanda.bunnyhop.message;

/**
 * メッセージを送信するクラス
 * @author K.Koike
 * */
public class MsgTransporter {

	private MsgTransporter() {};
	public static final MsgTransporter INSTANCE = new MsgTransporter();	//!< シングルトンインスタンス

	/**
	 * msgReceptionWindows に対応するそれぞれの MsgProcessor に順番にメッセージを送信する<br>
	 * 2つめ以降のmsgReceptionWindowには, 1つ前のMsgProcessorの処理結果であるMsgDataが渡される.
	 * @param msg 送信メッセージ
	 * @param data 1番目のMsgReceptionWindow に渡されるメッセージ
	 * @param msgReceptionWindows メッセージ投函先
	 * @return 最後のメッセージ送信先から返されるデータ
	 * */
	public MsgData sendMessage(BhMsg msg, MsgData data, MsgReceptionWindow... msgReceptionWindows) {

		for (MsgReceptionWindow msgReceptionWindow : msgReceptionWindows) {
			data = msgReceptionWindow.passMsg(msg, data);
		}
		return data;
	}

	/**
	 * msgReceptionWindows に対応するそれぞれの MsgProcessor に順番にメッセージを送信する
	 * @param msg 送信メッセージ
	 * @param msgReceptionWindows メッセージ投函先
	 * @return 最後のメッセージ送信先から返されるデータ
	 * */
	public MsgData sendMessage(BhMsg msg, MsgReceptionWindow... msgReceptionWindows) {

		MsgData data = null;
		for (MsgReceptionWindow msgReceptionWindow : msgReceptionWindows) {
			data = msgReceptionWindow.passMsg(msg, data);
		}
		return data;
	}
}
















