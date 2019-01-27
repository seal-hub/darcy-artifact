module Mail {
    requires MailAPI;
    provides de.qaware.mail.MailSender with de.qaware.mail.impl.MailSenderImpl;
}
