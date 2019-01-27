package fr.mrcraftcod.utils.javafx.components;

import fr.mrcraftcod.utils.base.FileUtils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.File;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 21/02/2017.
 *
 * @author Thomas Couchoud
 * @since 2017-02-21
 */
public class FileField extends TextField
{
	private final SimpleObjectProperty<File> selectedFile;

	public FileField(boolean allowNull, File file)
	{
		super();
		setEditable(false);
		setText("None");
		selectedFile = new SimpleObjectProperty<>(null);
		selectedFile.addListener(((observable, oldValue, newValue) -> {
			if(!allowNull && (newValue == null || !newValue.exists()))
				selectedFile.set(oldValue);
			else
				setText(newValue == null ? "None" : newValue.getAbsolutePath());
		}));
		selectedFile.set(file == null ? FileUtils.getHomeFolder() : file);
		setOnMouseClicked(e -> {
			setDisable(true);
			DirectoryChooser directoryChooser = new DirectoryChooser();
			directoryChooser.setTitle("Select directory");
			directoryChooser.setInitialDirectory(selectedFile.get());
			selectedFile.set(directoryChooser.showDialog(new Stage()));
			setDisable(false);
		});
	}

	public File getFile()
	{
		return selectedFile.get();
	}
}
