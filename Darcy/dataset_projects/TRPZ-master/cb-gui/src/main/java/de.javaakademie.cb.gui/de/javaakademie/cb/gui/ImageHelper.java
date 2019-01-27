package de.javaakademie.cb.gui;

import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * ImageHelper.
 * 
 * @author Guido.Oelmann
 */
public class ImageHelper {

	public static ImageView getImage(InputStream stream) {
		Image image = new Image(stream);
		ImageView view = new ImageView(image);
		view.setPreserveRatio(true);
		return view;
	}

}