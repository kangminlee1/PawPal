package dev.kangmin.pawpal.api.mail.service;

import dev.kangmin.pawpal.api.mail.utils.ThymeleafUtil;
import dev.kangmin.pawpal.domain.dog.Dog;
import dev.kangmin.pawpal.domain.dog.repository.DogRepository;
import dev.kangmin.pawpal.domain.dog.service.DogService;
import dev.kangmin.pawpal.domain.member.Member;
import dev.kangmin.pawpal.domain.member.repository.MemberRepository;
import dev.kangmin.pawpal.domain.member.service.MemberService;
import dev.kangmin.pawpal.global.error.exception.CustomException;
import dev.kangmin.pawpal.global.error.exception.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
    private final DogRepository dogRepository;
    private final TemplateEngine templateEngine;
    private final ThymeleafUtil thymeleafUtil;

    public void sendMail(
            Context context, String to, String htmlPath, String subject
    ) {
        String html = templateEngine.process(htmlPath, context);
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(MAIL_SENDER);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.SEND_MAIL_ERROR);
        }
    }

    @Scheduled(cron = "0 0 0 * * *") // 매일 00:00
    public void sendHealthCareNotification() {
        log.info("건강 검진 리마인더 메일 전송 스케줄 실행");

        //대상 강아지 찾고
        List<Dog> dogList = dogRepository.findAllByNextHealthCheckDateBefore(LocalDateTime.now());

        LocalDateTime now = LocalDateTime.now();
        for (Dog dog : dogList) {
            LocalDateTime nextDate;
            if (dog.getAge() <= 1) {
                nextDate = now.plusMonths(1);
            } else if (dog.getAge() <= 6) {
                nextDate = now.plusMonths(6);
            }else{
                nextDate = now.plusMonths(3);
            }
        }

        dogList.parallelStream().forEach(dog -> {
            Member member = dog.getMember();

            Context context = thymeleafUtil.initHealthNotification(member.getName(), dog.getName());
            String subject = (String) context.getVariable("subject");

            sendMailAsync(context, member.getEmail(), "mail/check", subject);
        });

    }

    @Async
    public void sendMailAsync(Context context, String to, String htmlPath, String subject) {
        sendMail(context, to, htmlPath, subject);

    }

}
