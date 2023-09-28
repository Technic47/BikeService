package ru.kuznetsov.emailsender.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class SpringConfig{
    @Value("${spring.mail.host}")
    private String smtpHost;
    @Value("${spring.mail.port}")
    private String smtpPort;
    @Value("${spring.mail.username}")
    private String smtpUserName;
    @Value("${spring.mail.password}")
    private String smtpUserPass;

    @Bean(name = "MainExecutor")
    public ExecutorService getMainService() {
        int coreCount = Runtime.getRuntime().availableProcessors();
        return Executors.newFixedThreadPool(coreCount);
    }

    @Bean(name = "AdditionExecutor")
    public ExecutorService getAdditionService() {
        return Executors.newFixedThreadPool(2);
    }

    //email sender setUp
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(smtpHost);
        mailSender.setPort(Integer.parseInt(smtpPort));
        mailSender.setUsername(smtpUserName);
        mailSender.setPassword(smtpUserPass);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        return mailSender;
    }
}