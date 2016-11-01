package com.livingprogress.mentorme.controllers;

import com.livingprogress.mentorme.utils.Helper;
import com.livingprogress.mentorme.exceptions.ConfigurationException;
import com.livingprogress.mentorme.exceptions.MentorMeException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

/**
 * The BaseEmail REST controller to provide email related methods.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseEmailController {

    /**
     * The java mail sender.
     */
    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * The velocity engine.
     */
    @Autowired
    private VelocityEngine velocityEngine;

    /**
     * The default from email address.
     */
    @Value("${mail.from}")
    private String fromAddress;

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        Helper.checkConfigNotNull(javaMailSender, "javaMailSender");
        Helper.checkConfigNotNull(velocityEngine, "velocityEngine");
        Helper.checkConfigState(Helper.isEmail(fromAddress), "fromAddress should be valid email address!");
    }

    /**
     * Render email template with template name and model params.
     *
     * @param name  the template name
     * @param model the model params.
     * @return the email template after render with model params
     */
    protected String getTemplate(String name, Map<String, Object> model) {
        return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, name, Helper.UTF8, model);
    }


    /**
     * Send email with to email address, email name and model params.
     *
     * @param toEmail   the to email address.
     * @param emailName the email name.
     * @param model     the model params.
     * @throws MentorMeException throws if error to send email.
     */
    protected void sendEmail(String toEmail, String emailName, Map<String, Object> model) throws MentorMeException {
        try {
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail);
            helper.setTo(toEmail);
            helper.setFrom(fromAddress);
            helper.setSubject(getTemplate(emailName + "/subject.vm", model));
            helper.setText(getTemplate(emailName + "/body.vm", model), true);
            javaMailSender.send(mail);
        } catch (MessagingException e) {
            throw new MentorMeException("Error to send email!", e);
        }
    }
}
