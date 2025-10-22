package dev.kangmin.pawpal.api.mail.service;

import dev.kangmin.pawpal.domain.dog.service.DogService;
import dev.kangmin.pawpal.domain.member.Member;
import dev.kangmin.pawpal.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    //1세 이하 아기 강아지 달에 1회
    //2~7세 성년 강아지 반년에 1회
    //노령견 3개월에 1회
    private final JavaMailSender javaMailSender;
    private final MemberService memberService;
    private final DogService dogService;

    public void sendCycleHealthCheckReminder() {

    }

}
