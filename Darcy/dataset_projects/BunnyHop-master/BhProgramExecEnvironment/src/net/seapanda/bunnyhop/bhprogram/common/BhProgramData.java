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

import java.io.Serializable;

/**
 * BunnyHopとスクリプトの実行環境間で送受信されるデータ
 * @author K.Koike
 */
public class BhProgramData implements Serializable {
	
	public final TYPE type;
	public final String str;
	
	public BhProgramData(TYPE type, String str) {
		this.type = type;
		this.str = str;
	}
	
	/**
	 * データの種類
	 */
	public enum TYPE {
		OUTPUT_STR,
		INPUT_STR,
	}
}
