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

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.rmi.server.RMIClientSocketFactory;

/**
 * リモート通信用のソケットを作成するファクトリ
 * @author K.Koike
 */
public class RemoteClientSocketFactory implements RMIClientSocketFactory, Serializable {
	
	private int id;	//!< 同一性確認のためのID
	
	/**
	 * コンストラクタ
	 * @param id オブジェクトの同一性確認のためのID
	 */
	public RemoteClientSocketFactory(int id) {
		this.id = id;
	}
	
	@Override
	public Socket createSocket(String host, int port) throws IOException {
		Socket socket = null;
		try {
			socket = new Socket(host, port);
		}
		catch(IOException e) {
			throw new IOException();
		}
		return socket;
	}
	
	@Override
	public int hashCode() {
		return id;
    }

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		return (getClass() == obj.getClass()) && (id == ((RemoteClientSocketFactory)obj).id);
    }
}

