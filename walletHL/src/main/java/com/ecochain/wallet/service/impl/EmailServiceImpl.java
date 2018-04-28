package com.ecochain.wallet.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.ecochain.config.EmailConfig;
import com.ecochain.util.Pair;
import com.ecochain.wallet.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {
    
    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private EmailConfig emailConfig;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendSimpleMail(String sendTo, String titel, String content) {
        log.info("sendSimpleMail====sendTo:"+sendTo+",titel="+titel+",content="+content);
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailConfig.getEmailFrom());
        message.setTo(sendTo);
        message.setSubject(titel);
        message.setText(content);
        mailSender.send(message);
    }

    public void sendAttachmentsMail(String sendTo, String titel, String content, List<Pair<String, File>> attachments) {
        log.info("sendAttachmentsMail====sendTo:"+sendTo+",titel="+titel+",content="+content);
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(emailConfig.getEmailFrom());
            helper.setTo(sendTo);
            helper.setSubject(titel);
            helper.setText(content);

            for (Pair<String, File> pair : attachments) {
                helper.addAttachment(pair.getLeft(), new FileSystemResource(pair.getRight()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        mailSender.send(mimeMessage);
    }

    public boolean sendTemplateMail(String sendTo, String titel, Map<String, Object> content,
            List<Pair<String, File>> attachments, String templateName) {
        log.info("sendAttachmentsMail====sendTo:"+sendTo+",titel="+titel+",content="+content);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        
        Assert.hasText(templateName,"templateName  is null");

        try {
            
//            if (templateName.endsWith(".vm")){
//                templateName = templateName.replace(".vm", "");
//            }
//            
            Locale locale = LocaleContextHolder.getLocale();
//            String language = locale.getLanguage();
//            String country = locale.getCountry();
//            String variant = locale.getVariant();
//            StringBuilder temp = new StringBuilder(templateName);
//            List<String> result = new ArrayList<String>(1);
//            temp.append('_');
//            if (language.length() > 0) {
//                temp.append(language);
//                result.add(0, temp.toString());
//            }
//
//            temp.append('_');
//            if (country.length() > 0) {
//                temp.append(country);
//                result.add(0, temp.toString());
//            }
//
//            if (variant.length() > 0 && (language.length() > 0 || country.length() > 0)) {
//                temp.append('_').append(variant);
//                result.add(0, temp.toString());
//            }
//
//            templateName = result.get(0) + ".vm";
//            log.debug("templateName===" + templateName);
            
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(emailConfig.getEmailFrom());
            helper.setTo(sendTo);
            helper.setSubject(titel);
            //给上下文绑定数据
            final Context ctx = new Context(locale);
            ctx.setVariable("context", "adjkajsldasdasd");
            ctx.setVariables(content);
            
            String text = templateEngine.process(templateName, ctx);
            helper.setText(text, true);

            if (attachments != null) {
                
                for (Pair<String, File> pair : attachments) {
                    helper.addAttachment(pair.getLeft(), new FileSystemResource(pair.getRight()));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        mailSender.send(mimeMessage);
        return true;
    }
    
    public boolean sendTemplateMail(String sendTo, String titel, Map<String, Object> content,
            String templateName) {
        return sendTemplateMail(sendTo, titel, content, null, templateName);
    }
}
