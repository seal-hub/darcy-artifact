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
package net.seapanda.bunnyhop.common;

import java.util.Objects;

/**
 * タプル
 * @author K.Koike
 * */
public class Pair<T1, T2> {

	public final T1 _1;
	public final T2 _2;

	public Pair(T1 _1, T2 _2) {
		this._1 = _1;
		this._2 = _2;
	}

	static private boolean equals(Object obj1, Object obj2) {
		return (obj1 == null) ? (obj2 == null) : obj1.equals(obj2);
	}

	@Override
	public boolean equals(Object obj) {

		if (!(obj instanceof Pair))
			return false;

		Pair<?, ?> pair = (Pair<?, ?>)obj;
		return equals(_1, pair._1) && equals(_2, pair._2);
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 79 * hash + Objects.hashCode(this._1);
		hash = 79 * hash + Objects.hashCode(this._2);
		return hash;
	}
}
