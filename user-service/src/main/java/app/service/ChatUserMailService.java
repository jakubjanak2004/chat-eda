package app.service;

import lombok.RequiredArgsConstructor;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
// todo delete
public class ChatUserMailService {
//    private final JavaMailSender mailSender;
//    @Value("${app.mail.from}")
//    private String from;
//
//    public void sendWelcomeEmail(@Valid UserCreatedEvent userCreatedEvent) {
//        String toEmail = userCreatedEvent.email();
//        String username = userCreatedEvent.username();
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom(from);
//        message.setTo(toEmail);
//        message.setSubject(String.format("Welcome %s", username));
//        message.setText(String.format("Hi %s welcome to chat application", username));
//
//        // TODO not sending email right now
////        mailSender.send(message);
//    }
}
