package com.jhkwak.productservice.service.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    // 계정
    @Value("${spring.mail.username}")
    private String username;

    @Async
    public void sendEmail(String email, String subject, String sendText) {
         
        // 메일 링크가 클릭되지 않아 Mime 형식으로 변경
        // SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        // simpleMailMessage.setTo(email);         // 메일 수신자
        // simpleMailMessage.setSubject(subject);  // 메일 제목
        // simpleMailMessage.setText(sendText);    // 메일 본문 내용
        // simpleMailMessage.setFrom(this.username);

        // javaMailSender.send(simpleMailMessage);

        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(sendText, true);  // 두 번째 매개변수로 true를 전달하여 HTML 형식으로 설정
            helper.setFrom(this.username);
            javaMailSender.send(message);
        }
        catch (MessagingException m ){

        }

    }
}
