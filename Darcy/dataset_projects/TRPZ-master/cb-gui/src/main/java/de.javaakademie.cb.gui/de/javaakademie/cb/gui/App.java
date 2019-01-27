package de.javaakademie.cb.gui;

import java.io.InputStream;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * App.
 * 
 * @author Guido.Oelmann
 */
public class App extends Application {

	private final static String APP_TITLE = "Conference Board";

	private final static String LAYOUT = "resources/views/layout.fxml";

	private final static String CSS = "/resources/css/layout.css";

	@Override
	public void start(Stage primaryStage) throws Exception {
		InputStream layoutStream = App.class.getModule().getResourceAsStream(LAYOUT);
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(layoutStream);
		primaryStage.setTitle(APP_TITLE);
		primaryStage.setScene(new Scene(root));
		URL cssUrl = App.class.getResource(CSS);
		primaryStage.getScene().getStylesheets().add(cssUrl.toString());
		primaryStage.sizeToScene();
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
