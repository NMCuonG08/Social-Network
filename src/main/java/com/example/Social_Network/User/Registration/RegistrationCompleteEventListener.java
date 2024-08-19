package com.example.Social_Network.User.Registration;

import com.example.Social_Network.User.IUserService;
import com.example.Social_Network.User.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.standard.expression.MessageExpression;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Component
@RequiredArgsConstructor

public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    private static final Logger log = LoggerFactory.getLogger(RegistrationCompleteEventListener.class);


    private final IUserService userService;
    private  final JavaMailSender mailSender;

    private User theUser;


    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        theUser = event.getUser();
        String verificationToken = UUID.randomUUID().toString();
        userService.saveUserVerificationToken(theUser,verificationToken);
        String url = event.getApplicationUrl() + "/register/verifyEmail?token=" + verificationToken;
        try {
            sendVerificationEmail(url);
        }
        catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        log.info("Click the link to verify your registration:{}", url);
    }
    private void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject ="Email verification";
        String senderName = " Social Network Register ";
        String mailContent = "<p> Hi, " + theUser.getEmail() + ", </p>"
                + "<p> Thank you for registration with us, " + " " +
                "Please, follow the link below to complete your registration! </p>" +
                "<a href=\"" + url + "\">Verify your email to activate yout account!</a>" +
                "<p> Thank you <br> Users Registration Portal Service ";
        MimeMessage message =mailSender.createMimeMessage();
        var mailHelper =new MimeMessageHelper(message);
        mailHelper.setFrom("nmcuongg2004@gmail.com", senderName);
        mailHelper.setTo(theUser.getEmail());
        mailHelper.setSubject(subject);
        mailHelper.setText(mailContent,true);
        mailSender.send(message);
    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }
}
