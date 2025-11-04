package dev.kangmin.pawpal.api.mail.service;

import dev.kangmin.pawpal.api.mail.utils.ThymeleafUtil;
import dev.kangmin.pawpal.domain.dog.Dog;
import dev.kangmin.pawpal.domain.healthrecord.HealthRecord;
import dev.kangmin.pawpal.domain.healthrecord.service.HealthRecordService;
import dev.kangmin.pawpal.domain.member.Member;
import dev.kangmin.pawpal.domain.member.service.MemberService;
import dev.kangmin.pawpal.global.error.exception.CustomException;
import dev.kangmin.pawpal.global.error.exception.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

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

    private final TemplateEngine templateEngine;
    private final ThymeleafUtil thymeleafUtil;

    private final HealthRecordService healthRecordService;
    private final MemberService memberService;

    /**
     * 메일 전송
     * @param context
     * @param to
     * @param htmlPath
     * @param subject
     */
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

    //스케쥴로 보내기  ->  redis로
    /**
     * 다음 건강 검진 예정일이 다가왔을때 알림 발송
     * @param memberId
     * @param healthId
     */
    public void sendHealthCareNotification(Long memberId, Long healthId) {
        log.info("건강 검진 리마인더 메일 전송 스케줄 실행");

        Member member = memberService.findMemberByMemberId(memberId);
        HealthRecord healthRecord = healthRecordService.findByHealthRecordId(healthId);

        Context context = thymeleafUtil.initHealthNotification(member.getName(), healthRecord.getDog().getName());
        String subject = (String) context.getVariable("subject");

        sendMail(context, member.getEmail(), "mail/check", subject);
    }
}
