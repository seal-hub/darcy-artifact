package de.qaware.mail.impl;

import de.qaware.mail.MailSender;

public class MailSenderImpl implements MailSender {
	public void sendMail(String address, String message) {
		System.out.println("Sending mail to: " + address + " message: " + message);
	}
}
