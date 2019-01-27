package fr.mrcraftcod.utils.mail;

import fr.mrcraftcod.utils.base.Log;
import javax.imageio.ImageIO;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
public class MailUtils
{
	public static void sendMail(Session session, String emailFrom, String fromName, String to, String object, String body) throws MessagingException, UnsupportedEncodingException
	{
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(emailFrom, fromName));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		message.setSubject(object);
		message.setText(body);
		Transport.send(message);
	}

	public static Optional<MessageContent> getMessageContent(Message message)
	{
		try
		{
			Object content = message.getContent();
			if(content instanceof String)
				return Optional.of(new MessageContent().appendTextContent(content.toString()));
			else if(content instanceof Multipart)
			{
				MessageContent messageContent = new MessageContent();
				Multipart parts = (Multipart) content;
				for (int i = 0; i < parts.getCount(); i++) {
					BodyPart part = parts.getBodyPart(i);
					try
					{
						switch(part.getContentType().split(";")[0].toUpperCase())
						{
							case "TEXT/PLAIN":
								messageContent.appendTextContent(part.getContent().toString());
								break;
							case "TEXT/HTML":
								messageContent.appendHTMLContent(part.getContent().toString());
								break;
							case "IMAGE/JPEG":
								messageContent.addImage(ImageIO.read(part.getInputStream()));
								break;
							case "VIDEO/MP4":
								messageContent.addVideo(null);
								break;
							default:
								Log.info("Unrecognized part " + part.getContentType());
						}
					}
					catch(Exception e)
					{
						Log.warning("Error getting multipart " + i + ": " + part, e);
					}
				}
				return Optional.of(messageContent);
			}
		}
		catch(MessagingException | IOException e)
		{
			e.printStackTrace();
		}
		return Optional.empty();
	}
}
