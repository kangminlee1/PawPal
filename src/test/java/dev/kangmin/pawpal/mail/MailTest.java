package dev.kangmin.pawpal.mail;

import dev.kangmin.pawpal.api.mail.service.MailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MailTest {

    @Autowired
    private MailService mailService;

    @Test
    void sendMailTest() {
        mailService.sendMail("gangmin6520@gmail.com", "전송 되었나유?", "spring mail test");
    }

}
