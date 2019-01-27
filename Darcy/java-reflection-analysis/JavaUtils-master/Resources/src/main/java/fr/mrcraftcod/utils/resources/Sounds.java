package fr.mrcraftcod.utils.resources;

import fr.mrcraftcod.utils.base.Log;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;

public class Sounds
{
	private final ResourceElement resource;
	private final String path;

	public Sounds(ResourceElement resource, String name)
	{
		this.resource =resource;
		this.path = name;
	}

	public void playSound(ResourcesBase resourcesBase)
	{
		new Thread(() -> {
			try
			{
				final Clip clip = AudioSystem.getClip();
				AudioInputStream inputStream = AudioSystem.getAudioInputStream(resourcesBase.getResource(resource, Sounds.this.path));
				clip.open(inputStream);
				clip.start();
				clip.addLineListener(event -> {
					if(event.getType() == LineEvent.Type.STOP)
						clip.close();
				});
			}
			catch(Exception e)
			{
				Log.warning("Couldn't play sound " + Sounds.this.path, e);
			}
		}).start();
	}
}