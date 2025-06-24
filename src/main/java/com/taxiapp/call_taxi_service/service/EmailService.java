package com.taxiapp.call_taxi_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.taxiapp.call_taxi_service.controller.ReportController;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import java.io.File;
import java.nio.file.Files;
import java.util.Properties;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}") // Sender Email
    private String senderEmail;

    public void sendEmailWithAttachmentExcel(String[] to, String subject, String body, File[] attachments)
            throws MessagingException {
        final String senderPassword = "izbs ugag drtk llyh"; // Change to your email password

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));

            // Add multiple recipients
            Address[] recipientAddresses = new Address[to.length];
            for (int i = 0; i < to.length; i++) {
                recipientAddresses[i] = new InternetAddress(to[i]);
            }
            message.setRecipients(Message.RecipientType.TO, recipientAddresses);
            message.setSubject(subject);

            // Create email body
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // Attach multiple files
            for (File file : attachments) {
                if (file != null && file.exists()) {
                    logger.info("Attaching file : " + file.getAbsolutePath());
                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    attachmentPart.attachFile(file); // ✅ Fix: Use full file path
                    multipart.addBodyPart(attachmentPart);
                } else {
                    logger.warn("File not found or invalid: " + (file != null ? file.getAbsolutePath() : "null"));
                }
            }
            message.setContent(multipart);
            // Send email
            Transport.send(message);
            logger.info("Email sent successfully with attachments!");
        } catch (Exception e) {
            logger.error("Error sending email", e);
        }

    }

    public void sendEmailWithAttachmentPDF(String[] to, String subject, String text, File attachment) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(senderEmail);
        helper.setTo(to); // Send to multiple recipients
        helper.setSubject(subject);
        helper.setText(text);

        // Add attachment
        if (attachment != null && attachment.exists()) {
            InputStreamSource attachmentSource = new ByteArrayResource(Files.readAllBytes(attachment.toPath()));
            helper.addAttachment(attachment.getName(), attachmentSource);
        }

        mailSender.send(message);
        System.out.println("Email Sent Successfully!");
    }
}
