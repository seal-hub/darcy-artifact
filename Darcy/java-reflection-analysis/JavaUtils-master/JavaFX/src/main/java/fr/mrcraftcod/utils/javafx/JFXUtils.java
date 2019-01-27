package fr.mrcraftcod.utils.javafx;

import fr.mrcraftcod.utils.base.ImageUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.function.Supplier;

public class JFXUtils
{
	public static Optional<File> askDirectory()
	{
		return askDirectory(null);
	}

	public static Optional<File> askDirectory(File defaultFolder)
	{
		try
		{
			return launchJFX(() -> {
				DirectoryChooser directoryChooser = new DirectoryChooser();
				directoryChooser.setTitle("Select directory");
				if(defaultFolder != null && defaultFolder.exists())
					directoryChooser.setInitialDirectory(defaultFolder);
				return directoryChooser.showDialog(new Stage());
			});
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		return Optional.empty();
	}

	public static Optional<File> askFile()
	{
		return askFile(null);
	}

	public static Optional<File> askFile(File defaultFile)
	{
		try
		{
			return launchJFX(() -> {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Select file");
				if(defaultFile != null && defaultFile.exists())
					fileChooser.setInitialDirectory(defaultFile);
				return fileChooser.showOpenDialog(new Stage());
			});
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		return Optional.empty();
	}

	public static Optional<List<File>> askFiles()
	{
		return askFiles(null);
	}

	public static Optional<List<File>> askFiles(File defaultFile)
	{
		try
		{
			return launchJFX(() -> {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Select file");
				if(defaultFile != null && defaultFile.exists())
					fileChooser.setInitialDirectory(defaultFile);
				return fileChooser.showOpenMultipleDialog(new Stage());
			});
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		return Optional.empty();
	}

	public WritableImage getImage(URL url, int width, int height)
	{
		try
		{
			return SwingFXUtils.toFXImage(ImageUtils.resizeBufferedImage(ImageIO.read(url), width, height), null);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static<T> Optional<T> launchJFX(Supplier<T> supplier) throws InterruptedException
	{
		final SimpleObjectProperty<T> result = new SimpleObjectProperty<>(null);
		boolean implicitExit = Platform.isImplicitExit();
		Platform.setImplicitExit(false);
		CountDownLatch latch = new CountDownLatch(1);
		SwingUtilities.invokeLater(() ->
		{
			new JFXPanel();
			Platform.runLater(() -> {
				result.set(supplier.get());
				latch.countDown();
			});
		});
		latch.await();
		Platform.setImplicitExit(implicitExit);
		return Optional.ofNullable(result.get());
	}
}
