package jp.co.worksap.stm2018.jobhere.util;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
@EnableAsync
public class Mail {

    /*public static void send(String from, String to, String subject, String text) {
        new Thread(() -> {
            mail(from, to, subject, text);
        }).start();
    }*/

    //if use @Async, static should be deleted
    @Async
    public void send(String from, String to, String subject, String text) {
        Properties properties = System.getProperties();

        properties.put("mail.smtp.auth", "true");
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.host", "smtp.163.com");

        Session session = Session.getDefaultInstance(properties);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(text);
            Transport ts = session.getTransport();
            ts.connect("smtp.163.com", "chorespore@163.com", "zhaiguangchao");
            ts.sendMessage(message, message.getAllRecipients());
            ts.close();
            System.out.println(text);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

}
