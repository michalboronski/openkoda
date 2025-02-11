/*
MIT License

Copyright (c) 2016-2024, Openkoda CDX Sp. z o.o. Sp. K. <openkoda.com>

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation
the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice
shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR
A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.openkoda.core.service.email;

import com.openkoda.model.EmailConfig;
import com.openkoda.repository.EmailConfigRepository;
import jakarta.inject.Inject;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * Wrapper for a default JavaMailSender implementation. This one uses SMTP parameters stored in DB and uses them - if they exist.
 * If some of those parameters are not in DB, then it uses default values privided through properties (spring.mail...)
 * 
 * @author mboronski
 */
@Service
public class EmailConfigJavaMailSender extends JavaMailSenderImpl implements JavaMailSender {

    @Inject private EmailConfigRepository emailConfigRepository;
    
    private EmailConfig emailConfig;
    
    @Override
    public String getHost() {
        return StringUtils.defaultIfBlank(emailConfig != null ? emailConfig.getHost() : null, super.getHost());
    }
    
    @Override
    public String getPassword() {
        return StringUtils.defaultIfBlank(emailConfig != null ? emailConfig.getPassword() : null, super.getPassword());
    }
    
    @Override
    public int getPort() {
        if(emailConfig != null && emailConfig.getPort() != null) {
            return emailConfig.getPort();
        }
        
        return super.getPort();
    }
    
    @Override
    public String getProtocol() {
        if(emailConfig != null && emailConfig.getSsl() != null) {
            return Boolean.TRUE.equals(emailConfig.getSsl()) ? "smtps" : "smtp";
        }
        
        return StringUtils.defaultIfBlank(emailConfig != null ? emailConfig.getProtocol() : null, super.getProtocol());
    }
    
    @Override
    public String getUsername() {
        return StringUtils.defaultIfBlank(emailConfig != null ? emailConfig.getUsername() : null, super.getUsername());
    }
    
    @Override
    public Properties getJavaMailProperties() {
        Properties mailProps = new Properties(super.getJavaMailProperties());
        if(emailConfig != null) {
            if(emailConfig.getSmtpAuth() != null) {
                mailProps.setProperty("spring.mail.properties.mail.smtp.auth", emailConfig.getSmtpAuth().toString());
            }
            
            if(emailConfig.getSsl() != null) {
                mailProps.setProperty("spring.mail.smtp.ssl.enable", emailConfig.getSmtpAuth().toString());
            }
            
            if(emailConfig.getStarttls() != null) {
                mailProps.setProperty("spring.mail.properties.mail.smtp.starttls.enabl", emailConfig.getStarttls().toString());
            }
        }

        return mailProps;
    }
    
    @Override
    protected void doSend(MimeMessage[] mimeMessages, Object[] originalMessages) throws MailException {
        emailConfig = emailConfigRepository.findAll().stream().findFirst().orElse(null);
        super.doSend(mimeMessages, originalMessages);
    }
}