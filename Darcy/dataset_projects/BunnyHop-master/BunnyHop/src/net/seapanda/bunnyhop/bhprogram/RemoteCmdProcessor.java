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
package net.seapanda.bunnyhop.bhprogram;

import net.seapanda.bunnyhop.bhprogram.common.BhProgramData;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import net.seapanda.bunnyhop.common.BhParams;
import net.seapanda.bunnyhop.common.tools.MsgPrinter;

/**
 * リモート環境から受信したコマンドを処理する
 * @author K.koike
 */
public class RemoteCmdProcessor {

	private final BlockingQueue<BhProgramData> recvDataList = new ArrayBlockingQueue<>(BhParams.ExternalApplication.MAX_REMOTE_CMD_QUEUE_SIZE);
	private final ExecutorService remoteCmdExec = Executors.newSingleThreadExecutor();	//!< コマンド受信用
		
	public void init() {
		remoteCmdExec.submit(() -> {
			
			while (true) {
				
				BhProgramData data = null;
				try {
					data = recvDataList.poll(BhParams.ExternalApplication.POP_RECV_DATA_TIMEOUT, TimeUnit.SECONDS);
				}
				catch(InterruptedException e) {
					break;
				}
							
				if (data != null)
					processRemoteData(data);
			}
		});
	}
	
	/**
	 * リモート環境から受信したデータを処理する
	 * @param data リモート環境から受信したデータ. nullは駄目.
	 */
	private void processRemoteData(BhProgramData data) {
		
		switch(data.type) {
			case OUTPUT_STR:
				MsgPrinter.INSTANCE.msgForUser(data.str + "\n");
				break;
			default:
		}
	}
	
	/**
	 * 処理対象のリモートデータを追加する
	 * @param data 処理対象のリモートデータ
	 */
	public void addRemoteData(BhProgramData data) throws InterruptedException {
		recvDataList.put(data);
	}
	
	/**
	 * 処理対象のリモートデータを全て削除する
	 */
	public void clearRemoteDataList() {
		recvDataList.clear();
	}
	
	/**
	 * このオブジェクトの終了処理を行う
	 * @return 終了処理が成功した場合true
	 */
	public boolean end() {
		
		boolean success = false;
		remoteCmdExec.shutdownNow();
		try {
			success = remoteCmdExec.awaitTermination(BhParams.EXECUTOR_SHUTDOWN_TIMEOUT, TimeUnit.SECONDS);
		}
		catch(InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		return success;
	}
}
