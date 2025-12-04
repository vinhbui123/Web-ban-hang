package vn.edu.hcmuaf.fit.Web_ban_hang.utils;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailUtil {
    private static final String EMAIL = "sonagaming112@gmail.com";
    private static final String PASSWORD = "xyrb umkf vdths lqjo";

    public static void sendEmail(String toEmail, String subject, String content) {
        System.out.println("Đang gửi email đến: " + toEmail);
        System.out.println("Tiêu đề: " + subject);
        System.out.println("Nội dung: " + content);

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465"); // SSL: 465, TLS: 587
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.enable", "true"); // Bật SSL

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);
            System.out.println("Email đã gửi thành công!");

        } catch (MessagingException e) {
            System.err.println("Gửi email thất bại: " + e.getMessage());
        }
    }

}


