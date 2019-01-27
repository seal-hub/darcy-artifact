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

/**
 *
 * @author K.Koike
 */
public class BhParams {

	public static final int MAX_QUEUE_SIZE = 2048;
	public static final int POP_SEND_DATA_TIMEOUT = 3;	//!< BunnyHopへの送信データキューの読み出しタイムアウト(sec)
	public static final int PUSH_SEND_DATA_TIMEOUT = 3;	//!< BunnyHopへの送信データキューの書き込みタイムアウト(sec)
	public static final int PUSH_RECV_DATA_TIMEOUT = 3;	//!< BunnyHopからの受信データキューの書き込みタイムアウト (sec)
	public static final int LOG_FILE_SIZE_LIMIT = 1024 * 1024;	//!< ログファイル1つあたりの最大バイト数
	public static final int MAX_LOG_FILE_NUM = 4;	//!< ログファイルの最大個数

	public static class BhProgram {
		public static final String INOUT_MODULE_NAME = "inout";
		public static final String EXEC_PATH = "execPath";
		public static final String RIM_TCP_PORT_SUFFIX = "@RmiTcpPort";	//BhProgram実行環境との通信に使うRMIオブジェクトを探す際のTCPポート
	}

	/**
	 * ファイルパス関連のパラメータ
	 */
	public static class Path {
		public static final String SCRIPT_DIR = "Compiled";
		public static final String LOG_DIR = "BhExecEnvLog";
		public static final String LOG_FILE_NAME = "msg";
	}
}
