package dev.kangmin.pawpal.api.mail.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
@Slf4j
public class ThymeleafUtil {

    public Context initHealthNotification(String memberName, String dogName) {
        Context context = new Context();
        String subject = dogName + " 건강검진 알림!";
        context.setVariable("memberName", memberName);
        context.setVariable("dogName", dogName);
        context.setVariable("subject", subject);

        return context;
    }

}
