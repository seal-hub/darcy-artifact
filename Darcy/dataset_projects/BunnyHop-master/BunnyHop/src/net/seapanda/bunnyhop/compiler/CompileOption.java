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
package net.seapanda.bunnyhop.compiler;

/**
 * コンパイルオプションを格納するクラス
 * @author K.Koike
 */
public class CompileOption {

	public final boolean local;	//!< ルーカルで実行するコードを生成する場合true
	public final boolean isDebug;	//!< デバッグ用コードを追加する場合true
	public final boolean handleException; //!< 例外処理を行う場合true
	public final boolean withComments;	//!< ソースコードにコメントを追加する場合true

	
	public CompileOption(
		boolean local,
		boolean isDebug,
		boolean handleException,
		boolean withComments) {
		this.local = local;
		this.isDebug = isDebug;
		this.handleException = handleException;
		this.withComments = withComments;
	}
}
