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
 * BhMsgの投函窓口
 * @author K.Koike
 */
public interface MsgReceptionWindow {
	
	/**
	 * MsgProcessorを登録する
	 * @param processor メッセージを処理するオブジェクト
	 */
	public void setMsgProcessor(MsgProcessor processor);
	
	/**
	 * 引数で指定したメッセージをMsgProcessorに渡して, 処理結果を返す
	 * @param msg 処理するメッセージ
	 * @param data 処理するデータ
	 * @return 処理結果
	 */
	public MsgData passMsg(BhMsg msg, MsgData data);
}
