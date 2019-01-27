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
package net.seapanda.bunnyhop.undo;

import java.util.Deque;
import java.util.LinkedList;
import net.seapanda.bunnyhop.common.BhParams;

/**
 * undo/redo時に UserOperationCommand クラスを操作するクラス
 * @author Koike
 */
public class UserOpeCmdManager {

	Deque<UserOperationCommand> undoStack = new LinkedList<>();	//Undo できるコマンドのスタック
	Deque<UserOperationCommand> redoStack = new LinkedList<>();	//Redo できるコマンドのスタック
	
	/**
	 * Undo の対象になるコマンドを追加する
	 * @param cmd Undo の対象になるコマンド
	 */
	public void pushUndoCommand(UserOperationCommand cmd) {
		
		undoStack.addLast(cmd);
		if(undoStack.size() > BhParams.NUM_TIMES_MAX_UNDO)
			undoStack.removeFirst();
		
		redoStack.clear();
	}
	
	public void undo() {
		
		if (undoStack.isEmpty())
			return;
		
		UserOperationCommand invCmd = undoStack.removeLast().doInverseOperation();
		redoStack.addLast(invCmd);
	}

	public void redo() {
		
		if (redoStack.isEmpty())
			return;
		
		UserOperationCommand invCmd = redoStack.removeLast().doInverseOperation();
		undoStack.addLast(invCmd);
	}
}


