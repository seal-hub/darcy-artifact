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
package net.seapanda.bunnyhop.modelhandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.seapanda.bunnyhop.model.BhNode;
import net.seapanda.bunnyhop.undo.UserOperationCommand;

/**
 * 遅延削除を実装するクラス
 * @author KO\oike
 */
public class DelayedDeleter {

	public static final DelayedDeleter INSTANCE = new DelayedDeleter();	//!< シングルトンインスタンス
	private List<BhNode> deletionCandidateNodeList = new ArrayList<>();	//!< 特定のタイミングで削除するノードのリスト.
	
	private DelayedDeleter(){}
	
	/**
	 * 削除候補のノードを追加する<br>
	 * 登録するリストは, GUI, 4分木, ワークスペースからの削除が済んでいること
	 * @param candidate 削除候補のノード
	 */
	public void addDeletionCandidate(BhNode candidate) {
		deletionCandidateNodeList.add(candidate);
	}
	
	/**
	 * 引数で指定した削除候補のノードを削除する
	 * @param candidate 削除するノード
	 * @param userOpeCmd undo用コマンドオブジェクト
	 * @return 引数で指定したノードを削除した場合trueを返す.  削除候補リストに無いノードを指定したときは, 何もせずfalseが返る.
	 */
	public boolean deleteCandidate(BhNode candidate, UserOperationCommand userOpeCmd) {
		
		boolean canDelete = deletionCandidateNodeList.contains(candidate);
		if(canDelete) {
			candidate.delete(userOpeCmd);
		}
		return canDelete;
	}
	
	/**
	 * 削除候補のノードを全て削除する
	 * @param userOpeCmd undo用コマンドオブジェクト
	 */
	public void deleteCandidates(UserOperationCommand userOpeCmd) {

		deletionCandidateNodeList.forEach(
			candidate -> {
				if (!candidate.isInWorkspace()) {
					candidate.delete(userOpeCmd);
				}
			});
		deletionCandidateNodeList.clear();
	}
	
	/**
	 * 削除候補リストの中に引数で指定したノードが入っているか調べる
	 * @param node このノードが削除候補リストの中に入っているか調べる
	 * @return 削除候補リストの中に引数で指定したノードが入っている場合true
	 */
	public boolean containsInCandidateList(BhNode node) {
		return deletionCandidateNodeList.contains(node);
	}
	
	/**
	 * 削除候補のリストを返す
	 * @return 削除候補のリスト
	 */
	public Collection<BhNode> getDeletionCadidateList() {
		return deletionCandidateNodeList;
	}	
}
