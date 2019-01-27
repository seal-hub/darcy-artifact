package sample.client;

import java.io.IOException;
import java.util.ServiceLoader;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.plugin.Plugin;
import sample.client.impl.HelloWorldPlugin;

/**
 * App主类，加载并使用插件
 * @author 李佳明 https://github.com/pkpk1234
 * @date 2017-12-2017/12/4
 */
public class Main extends Application {

	/**
	 * 初始化界面，并加载插件
	 * @return
	 * @throws IOException
	 */
	private Scene initScene() throws IOException {
		Group root = new Group();
		Scene scene = new Scene(root, 800, 600);

		//加载插件到Tab视图中
		TabPane tabPane = initPlugins();

		BorderPane borderPane = new BorderPane();
		borderPane.prefHeightProperty().bind(scene.heightProperty());
		borderPane.prefWidthProperty().bind(scene.widthProperty());
		borderPane.setCenter(tabPane);
		root.getChildren().add(borderPane);

		return scene;
	}

	/**
	 * 添加默认插件，并加载添加到Classpath中的插件
	 * @return
	 */
	private TabPane initPlugins() {
		TabPane tabPane = new TabPane();
		Plugin plugin = new HelloWorldPlugin();
		tabPane.getTabs().add(plugin.getPluginTab());
		loadPlugins(tabPane);
		return tabPane;
	}

	/**
	 * 从Classpath中搜索并加载插件
	 * @param tabPane
	 */
	private void loadPlugins(TabPane tabPane) {
		//搜索插件
		ServiceLoader<Plugin> loader = ServiceLoader.load(Plugin.class);
		//遍历插件并添加到tab视图中
		for (Plugin plugin : loader) {
			tabPane.getTabs().add(plugin.getPluginTab());
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("ServiceLoader Demo");
		primaryStage.setScene(initScene());
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
