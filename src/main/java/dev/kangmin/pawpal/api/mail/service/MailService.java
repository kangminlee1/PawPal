package dev.kangmin.pawpal.api.mail.service;

import dev.kangmin.pawpal.domain.dog.service.DogService;
import dev.kangmin.pawpal.domain.member.Member;
import dev.kangmin.pawpal.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    @Value("${spring.mail.username}")
    private String MAIL_SENDER;

    //1세 이하 아기 강아지 달에 1회
    //2~7세 성년 강아지 반년에 1회
    //노령견 3개월에 1회
    private final JavaMailSender mailSender;
    private final MemberService memberService;
    private final DogService dogService;

    public void sendCycleHealthCheckReminder() {

    }

    //Test
    public void sendMail(String to, String subject, String text) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        mailMessage.setFrom(MAIL_SENDER);

        try {
            mailSender.send(mailMessage);
            log.info("메일이 성공적으로 발송되었습니다.");
        } catch (MailException e) {
            log.error("메일 전송 중 오류가 발생했습니다. : {}", e.getMessage());
            throw e;
        }
    }

}
