package net.gaven.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.gaven.service.IMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author: lee
 * @create: 2021/8/5 9:39 上午
 **/
@Slf4j
@Service
public class MailServiceImpl implements IMailService {
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.from}")
    private String from;

    @Override
    public void sendMsg(String to, String subject, String content) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(from);

        mailMessage.setTo(to);

        mailMessage.setSubject(subject);

        mailMessage.setText(content);
        mailSender.send(mailMessage);
        log.info("send e-mail success[{}]" + mailMessage);
    }
}
