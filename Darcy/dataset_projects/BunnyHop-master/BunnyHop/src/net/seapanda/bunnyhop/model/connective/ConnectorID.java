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
package net.seapanda.bunnyhop.model.connective;

import java.io.Serializable;
import java.util.Objects;

/**
 * コネクタID
 * @author K.Koike
 */
public class ConnectorID implements Serializable {

	public static final ConnectorID NONE = new ConnectorID("");	//!< コネクタIDが存在しないことを表す
	String id;

	/**
	 * コンストラクタ
	 * @param id 識別子名
	 */
	private ConnectorID(String id) {
		this.id = id;
	}

	/**
	 * コネクタIDを作成する
	 * @param id 識別子名
	 * @return コネクタID
	 */
	public static ConnectorID createCnctrID(String id) {
		return new ConnectorID(id == null ? "" : id);
	}

	@Override
	public String toString() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj == null)
			return false;

		if (getClass() != obj.getClass())
			return false;

		ConnectorID cnctrID = (ConnectorID)obj;
		return id == null ? (cnctrID.id == null) : (id.equals(cnctrID.id));
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 97 * hash + Objects.hashCode(this.id);
		return hash;
	}
}
