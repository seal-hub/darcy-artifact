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
package net.seapanda.bunnyhop.model;

import java.io.Serializable;
import java.util.Objects;
import net.seapanda.bunnyhop.common.BhParams;

/**
 * BhノードID
 * @author K.Koike
 */
public class BhNodeID implements Serializable {
	
	public static final BhNodeID NONE = new BhNodeID("");	//!< BhNodeIDが存在しないことを表す
	public static final BhNodeID REF_INIT = new BhNodeID(BhParams.BhModelDef.ATTR_VALUE_INITIAL_BHNODE_ID);	//!< 初期ノードを参照することを意味するID
	String id;
	
	/**
	 * コンストラクタ
	 * @param id 識別子名
	 */
	private BhNodeID(String id) {
		this.id = id;
	}
	
	/**
	 * BhノードIDを作成する
	 * @param id 識別子名
	 * @return BhノードID
	 */
	public static BhNodeID createBhNodeID(String id) {
		return new BhNodeID(id == null ? "" : id);
	}
	
	@Override
	public String toString() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		return (getClass() == obj.getClass()) && (id.equals(((BhNodeID)obj).id));
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 11 * hash + Objects.hashCode(this.id);
		return hash;
	}	
}
