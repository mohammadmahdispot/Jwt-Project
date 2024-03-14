package org.example;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.web.servlet.server.Session;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Date;
import java.util.Properties;

public class JWTEmailExample {

    private static final String SECRET_KEY = "mySecretKey";
    private static final long EXPIRATION_TIME = 3600000;

    public static void main(String[] args) {
        String jwtToken = createJWT("user@example.com");

        sendEmail("user@example.com", jwtToken);
    }

    private static String createJWT(String userEmail) {
        return Jwts.builder()
                .setSubject(userEmail)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    private static void sendEmail(String recipientEmail, String jwtToken) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "your_smtp_host");
        properties.put("mail.smtp.port", "your_smtp_port");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("your_email@example.com", "your_email_password");
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress("your_email@example.com"));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));

            message.setSubject("Your JWT Token");

            message.setText("Your JWT token is: " + jwtToken);

            Transport.send(message);

            System.out.println("JWT token sent successfully to " + recipientEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
