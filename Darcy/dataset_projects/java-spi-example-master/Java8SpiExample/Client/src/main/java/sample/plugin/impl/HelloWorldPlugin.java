package sample.plugin.impl;

import javafx.scene.Group;
import javafx.scene.control.Tab;
import javafx.scene.text.*;
import sample.plugin.Plugin;

import static javafx.scene.paint.Color.GREEN;
import static javafx.scene.paint.Color.ORANGE;
import static javafx.scene.paint.Color.RED;

/**
 * @author 李佳明 https://github.com/pkpk1234
 * @date 2017-12-2017/12/4
 */
public class HelloWorldPlugin implements Plugin {

	private String name;

	public HelloWorldPlugin() {
		this.name = "Hello ServiceLoader";
	}

	@Override
	public String getPluginName() {
		return this.name;
	}

	@Override
	public Tab getPluginTab() {
		Tab tab = new Tab();
		tab.setText(getPluginName());
		String family = "Helvetica";
		double size = 50;

		TextFlow textFlow = new TextFlow();
		textFlow.setLayoutX(40);
		textFlow.setLayoutY(40);
		Text text1 = new Text("Hello ");
		text1.setFont(Font.font(family, size));
		text1.setFill(RED);
		Text text2 = new Text("ServiceLoader");
		text2.setFill(ORANGE);
		text2.setFont(Font.font(family, FontWeight.BOLD, size));
		Text text3 = new Text(" World");
		text3.setFill(GREEN);
		text3.setFont(Font.font(family, FontPosture.ITALIC, size));
		textFlow.getChildren().addAll(text1, text2, text3);
		Group group = new Group(textFlow);

		tab.setContent(group);
		return tab;
	}
}
