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

/**
 * イミテーションの接続先の識別子
 * @author K.Koike
 */
public class ImitationConnectionPos implements Serializable {
	
	public static final ImitationConnectionPos NONE = new ImitationConnectionPos("");	//!< イミテーション接続位置が存在しないことを表す
	private final String pos;
		
	/**
	 * コンストラクタ
	 * @param point 接続先名
	 */
	private ImitationConnectionPos(String point) {
		this.pos = point;
	}
	
	/**
	 * イミテーション接続位置の識別子を作成する
	 * @param point イミテーション接続位置名
	 * @return イミテーション接続位置の識別子
	 */
	public static ImitationConnectionPos createImitCnctPoint(String point) {
		return new ImitationConnectionPos(point == null ? "" : point);
	}

	@Override
	public String toString() {
		return pos;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		return (getClass() == obj.getClass()) && (pos.equals(((ImitationConnectionPos)obj).pos));
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 71 * hash + Objects.hashCode(this.pos);
		return hash;
	}
}
