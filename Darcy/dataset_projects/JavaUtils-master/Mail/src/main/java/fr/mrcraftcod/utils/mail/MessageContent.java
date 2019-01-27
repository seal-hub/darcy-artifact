package fr.mrcraftcod.utils.mail;

import java.awt.*;
/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 02/09/2016.
 *
 * @author Thomas Couchoud
 * @since 2016-09-02
 */
public class MessageContent
{
	private boolean hasText;
	private boolean hasHTML;
	private boolean hasImage;
	private boolean hasVideo;
	private String textContent;
	private String htmlContent;

	public MessageContent()
	{
		this.hasText = false;
		this.hasHTML = false;
		this.hasImage = false;
		this.hasVideo = false;
		this.textContent = "";
		this.htmlContent = "";
	}

	@Override
	public String toString()
	{
		StringBuilder stringBuilder = new StringBuilder("MessageContent@").append(this.hashCode());
		stringBuilder.append("\nTextContent:").append("\n\t").append(this.getTextContent());
		stringBuilder.append("\nHTMLContent:").append("\n\t").append(this.getHTMLContent());
		return stringBuilder.toString();
	}

	public MessageContent appendTextContent(String textContent)
	{
		this.hasText = true;
		this.textContent += textContent;
		return this;
	}

	public String getTextContent()
	{
		return this.textContent;
	}

	public boolean hasText()
	{
		return this.hasText;
	}

	public MessageContent appendHTMLContent(String htmlContent)
	{
		this.hasHTML = true;
		this.htmlContent += htmlContent;
		return this;
	}

	public String getHTMLContent()
	{
		return this.htmlContent;
	}

	public boolean hasHTML()
	{
		return this.hasHTML;
	}

	public MessageContent addImage(Image image)
	{
		this.hasImage = true;
		return this;
	}

	public boolean hasImage()
	{
		return this.hasImage;
	}

	public MessageContent addVideo(Video video)
	{
		this.hasVideo = true;
		return this;
	}

	public boolean hasVideo()
	{
		return this.hasVideo;
	}
}
