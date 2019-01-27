package de.qaware.mail.client;

import de.qaware.mail.MailSender;
import java.util.ServiceLoader;

public class MailClient {

	public static void main(String [] args) throws Exception {
		// MailSender mail = new de.qaware.mail.impl.MailSenderImpl(); // --> should not work
		MailSender mail = ServiceLoader.load(MailSender.class).iterator().next();
                
                // Reflection does not work
                // mail = (MailSender) Class.forName("de.qaware.mail.impl.MailSenderImpl").getConstructors()[0].newInstance();
                
                // (MailSender) Class.forName("de.qaware.mail.impl.MailSenderImpl").newInstance();
		mail.sendMail("johannes@qaware.de", "A message from JavaModule System");

                // does not work
                System.out.println(Class.forName("de.qaware.mail.impl.MailSenderImpl"));
	}	
}
