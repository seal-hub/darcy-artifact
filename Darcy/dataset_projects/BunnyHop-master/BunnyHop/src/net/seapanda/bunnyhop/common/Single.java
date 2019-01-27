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
 * @author K.Koike
 */
public class Single<T>{

	public T content;

	public Single(T content) {
		this.content = content;
	};

	public Single() {};

	@Override
	public boolean equals(Object obj) {

		if (!(obj instanceof Single))
			return false;

		Single<?> single = (Single<?>)obj;
		return (content == null) ? (single.content == null) : content.equals(single.content);
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 59 * hash + Objects.hashCode(this.content);
		return hash;
	}
}
