package com.linking.push_notification.service;

import com.linking.push_notification.domain.NoticeType;
import com.linking.push_notification.domain.PushNotification;
import com.linking.push_notification.persistence.PushNotificationRepository;
import com.linking.push_settings.persistence.PushSettingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.management.Notification;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final PushSettingsRepository pushSettingsRepository;
    private final PushNotificationRepository pushNotificationRepository;
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    // todo 메일 전송
    public void sendEmail(PushNotification push) throws MessagingException {
        Context context = new Context();
        context.setVariable("receiver", push.getUser().getFullName());
        context.setVariable("sender", push.getSender());
        context.setVariable("projectName", push.getProject().getProjectName());
        context.setVariable("title", push.getBody());
        context.setVariable("webLink", "https://wangtak.tistory.com/27");
        if (push.getNoticeType().equals(NoticeType.PAGE))
            context.setVariable("temp", " 확인 요청");
        else
            context.setVariable("temp", " 완료 요청");

        String htmlMessage = templateEngine.process("email.html", context);

        MimeMessage mail = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mail, true, "UTF-8"); // 2번째 인자는 multipart 여부
        try {
            mimeMessageHelper.setFrom(new InternetAddress("saessack2019@gmail.com", "Linking", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        mimeMessageHelper.setTo(push.getUser().getEmail());
        mimeMessageHelper.setSubject("사용자 요청 메일");
        mimeMessageHelper.setText(htmlMessage, true);
        mimeMessageHelper.addInline("logo", new ClassPathResource("static/img/linking-logo.png"));
        javaMailSender.send(mail);
    }
}
