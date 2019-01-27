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
package net.seapanda.bunnyhop.programexecenv;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import net.seapanda.bunnyhop.bhprogram.common.BhProgramData;

/**
 * 実行時スクリプトの入出力を取り扱うクラス
 * @author K.Koike
 */
public class ScriptInOut {

	private final BlockingQueue<BhProgramData> sendDataList;	//!< BunnyHopへの送信データキュー
	private final BlockingQueue<String> stdInDataList = new ArrayBlockingQueue<>(BhParams.MAX_QUEUE_SIZE);
	private final AtomicBoolean connected;	//!< BunnyHopとの接続状況を取得する関数

	/**
	 * @param sendDataList BunnyHopへの送信データキュー
	 * @param connected BunnyHopとの接続状況を取得する関数
	 */
	public ScriptInOut(
		BlockingQueue<BhProgramData> sendDataList,
		AtomicBoolean connected) {

		this.sendDataList = sendDataList;
		this.connected = connected;
	}

	/**
	 * BunnyHop側で出力するデータを送信データキューに追加する
	 * @param str 出力する文字列
	 */
	public void println(String str) {

		if (!connected.get())
			return;

		boolean add = false;
		BhProgramData data = new BhProgramData(BhProgramData.TYPE.OUTPUT_STR, str);
		while (!add) {
			try {
				add = sendDataList.offer(data, BhParams.PUSH_SEND_DATA_TIMEOUT, TimeUnit.SECONDS);
				if (!connected.get()) {
					sendDataList.clear();
					return;
				}
			}
			catch(InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
		}
	}

	/**
	 * 標準入力に入力された文字をリストに追加する
	 * @param input 標準入力に入力された文字
	 */
	public void addStdInData(String input) {
		stdInDataList.offer(input);
	}

	/**
	 * 標準入力のデータを読み取って返す
	 * @return 標準入力に入力された文字
	 */
	public String scan() {

		String data = "";
		try {
			data = stdInDataList.take();
		}
		catch(InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		return data;
	}
}
