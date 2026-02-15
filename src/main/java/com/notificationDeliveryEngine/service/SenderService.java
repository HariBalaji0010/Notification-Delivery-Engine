package com.notificationDeliveryEngine.service;


import com.notificationDeliveryEngine.dto.MessagePayload;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SenderService {

    private final JavaMailSender mailSender;

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.whatsapp.from}")
    private String fromNumber;

    @Value("${spring.mail.username}")
    private String fromEmail;

    // ---------------- EMAIL ----------------
    public void sendEmail(MessagePayload payload) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(payload.getTo());
            helper.setSubject(payload.getSubject());
            helper.setText(payload.getBody(), false);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send EMAIL", e);
        }
    }

    // ---------------- WHATSAPP ----------------
    public void sendWhatsapp(MessagePayload payload) {
        Twilio.init(accountSid, authToken);

        Message.creator(
                new PhoneNumber("whatsapp:" + payload.getTo()),
                new PhoneNumber(fromNumber),
                payload.getBody()
        ).create();
    }
}
