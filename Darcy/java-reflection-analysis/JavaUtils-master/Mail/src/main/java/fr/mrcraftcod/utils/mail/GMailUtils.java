package fr.mrcraftcod.utils.mail;

import fr.mrcraftcod.utils.base.Log;
import javax.mail.*;
import javax.mail.event.MessageCountEvent;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public class GMailUtils
{
	private static final String GMAIL_SMTP_HOST = "smtp.gmail.com";

	public static void sendGMail(String user, String password, String from, String to, String object, String body) throws MessagingException, UnsupportedEncodingException
	{
		MailUtils.sendMail(getGMailSession(user, password), user, from, to, object, body);
	}

	public static Session getGMailSession(String user, String password)
	{
		Properties properties = System.getProperties();
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.host", GMAIL_SMTP_HOST);
		properties.put("mail.smtp.port", "587");
		return Session.getInstance(properties, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});
	}

	public static Store getGMailStore(String user, String password) throws MessagingException
	{
		Store store = getGMailSession(user, password).getStore("imaps");
		store.connect(GMAIL_SMTP_HOST, user, password);
		return store;
	}
	
	public static GMailFetcher fetchGMailFolder(String user, String password, String folder, Consumer<MessageCountEvent> callback) throws IllegalStateException, MessagingException
	{
		return new GMailFetcher(getGMailStore(user, password), folder, callback);
	}
	
	public static GMailFetcher fetchGMailFolder(String user, String password, String folder, ExecutorService executor, Consumer<MessageCountEvent> callback) throws IllegalStateException, MessagingException
	{
		return new GMailFetcher(getGMailStore(user, password), folder, executor, callback);
	}

	public static boolean forward(String user, String password, String fromName, String to, String toName, Message message)
	{
		return forward(user, password, fromName, to, toName, message, "Fwd: ", "");
	}

	public static boolean forward(String user, String password, String fromName, String to, String toName, Message message, String subjectPrefix, String header)
	{
		return forward(getGMailSession(user, password), user, fromName, to, toName, message, subjectPrefix, header);
	}

	public static boolean forward(Session session, String user, String fromName, String to, String toName, Message message, String subjectPrefix, String header)
	{
		try
		{
			Message forwardMessage = new MimeMessage(session);
			forwardMessage.setSubject(subjectPrefix + (message.getSubject() == null ? "" : message.getSubject()));
			forwardMessage.setFrom(new InternetAddress(user, fromName));
			forwardMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to, toName));
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(header);
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			try
			{
				Object content = message.getContent();
				if(content instanceof String)
				{
					BodyPart parts = new MimeBodyPart();
					parts.setText(String.valueOf(content));
					multipart.addBodyPart(parts);
				}
				else if(content instanceof Multipart)
				{
					Multipart parts = (Multipart) content;
					for (int i = 0; i < parts.getCount(); i++) {
						multipart.addBodyPart(parts.getBodyPart(i));
					}
				}
			}
			catch(MessagingException | IOException e)
			{
				e.printStackTrace();
			}
			forwardMessage.setContent(multipart);
			Transport.send(forwardMessage);
			return true;
		}
		catch(Exception e)
		{
			Log.warning("Failed to forward message", e);
			return false;
		}
	}
}
