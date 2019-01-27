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
package net.seapanda.bunnyhop.model.imitation;

import java.io.Serializable;
import java.util.Objects;
import net.seapanda.bunnyhop.common.BhParams;

/**
 * 作成するイミテーションを識別するためのID
 * @author K.Koike
 */
public class ImitationID implements Serializable {
	
	public static final ImitationID NONE = new ImitationID("");	//!< イミテーションIDが存在しないことを表す
	public static final ImitationID MANUAL = new ImitationID(BhParams.BhModelDef.ATTR_VALUE_IMIT_ID_MANUAL);	//!< イミテーション手動作成時のID
	private final String id;

	/**
	 * コンストラクタ
	 * @param id 識別子名
	 */
	private ImitationID(String id) {	
		this.id = id;
	}
	
	/**
	 * イミテーションIDを作成する
	 * @param id 識別子名
	 * @return イミテーションID
	 */
	public static ImitationID createImitID(String id) {
		return new ImitationID(id == null ? "" : id);
	}
	
	@Override
	public String toString() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		return (getClass() == obj.getClass()) && (id.equals(((ImitationID)obj).id));
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 59 * hash + Objects.hashCode(this.id);
		return hash;
	}
}
