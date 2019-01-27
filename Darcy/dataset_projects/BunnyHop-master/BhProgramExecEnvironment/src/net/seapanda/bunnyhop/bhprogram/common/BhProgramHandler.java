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
package net.seapanda.bunnyhop.bhprogram.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * スクリプトとBunnyHop間でデータを送受信するクラス
 * @author K.Koike
 */
public interface BhProgramHandler extends Remote {
	
	/**
	 * 引数で指定したスクリプトを実行する
	 * @param fileName 実行ファイル名
	 * @return 実行に成功した場合true
	 */
	public boolean runScript(String fileName) throws RemoteException;
	
	/**
	 * BunnyHopとの通信を切断する
	 */
	public void disconnect() throws RemoteException;
	
	/**
	 * BunnyHopとの通信を始める
	 */
	public void connect() throws RemoteException;
	
	/**
	 * スクリプト実行環境に向けてデータを送る
	 * @param data 送信するデータ. nullは駄目.
	 * @return 送信に成功した場合true
	 */
	public boolean sendDataToScript(BhProgramData data) throws RemoteException;
	
	/**
	 * スクリプト実行環境からデータを受信する
	 * @return 受信データ. 受信に失敗した場合もしくは受信データがなかった場合null
	 */
	public BhProgramData recvDataFromScript() throws RemoteException;
}
