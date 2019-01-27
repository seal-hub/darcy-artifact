package fr.mrcraftcod.utils.base;

import javafx.beans.property.SimpleBooleanProperty;

public abstract class ThreadLoop extends Thread
{
	private SimpleBooleanProperty running = new SimpleBooleanProperty(true);
	private SimpleBooleanProperty pause = new SimpleBooleanProperty(false);
	
	@Override
	public void run()
	{
		onStart();
		while(!this.isInterrupted() && this.isRunning())
			try
			{
				if(!isPaused())
					this.loop();
				else
					Thread.sleep(500);
			}
			catch(Exception e)
			{
				Log.error("ThreadLoop unhandled exception in loop", e);
				break;
			}
	}
	
	public void onStart()
	{
	}
	
	public boolean isRunning()
	{
		return this.runningProperty().get();
	}
	
	public SimpleBooleanProperty runningProperty()
	{
		return this.running;
	}
	
	public boolean isPaused()
	{
		return this.pauseProperty().get();
	}
	
	public SimpleBooleanProperty pauseProperty()
	{
		return this.pause;
	}
	
	public void close()
	{
		this.interrupt();
		this.running.set(false);
		this.onClosed();
	}
	
	public void onClosed(){}
	
	;
	
	public abstract void loop() throws Exception;
	
	public void pause()
	{
		this.pauseProperty().set(true);
	}
	
	public void unpause()
	{
		this.pauseProperty().set(false);
	}
}
