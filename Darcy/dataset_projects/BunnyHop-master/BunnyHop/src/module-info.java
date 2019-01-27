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
module net.seapanda.bunnyhop {
    requires java.rmi;
    requires java.scripting;
    requires jdk.scripting.nashorn;
    requires transitive javafx.graphics;
    requires java.desktop;
	requires javafx.controls;
	requires javafx.fxml;
	requires java.base;
	requires transitive java.xml;

	exports net.seapanda.bunnyhop.root;
	exports net.seapanda.bunnyhop.model;
	exports net.seapanda.bunnyhop.model.templates;
	exports net.seapanda.bunnyhop.model.connective;
	exports net.seapanda.bunnyhop.modelhandler;
	exports net.seapanda.bunnyhop.modelprocessor;
	exports net.seapanda.bunnyhop.model.imitation;
	exports net.seapanda.bunnyhop.common;
	exports net.seapanda.bunnyhop.common.tools;
	exports net.seapanda.bunnyhop.message;
	exports net.seapanda.bunnyhop.bhprogram.common;	//[java -jar BhProgramExecEnv.jar] を self contained の Javaから呼ぶために必要
	opens net.seapanda.bunnyhop.root to javafx.fxml;
	opens net.seapanda.bunnyhop.view to javafx.fxml;
	opens net.seapanda.bunnyhop.control to javafx.fxml;
}
