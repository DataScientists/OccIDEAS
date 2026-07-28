package org.occideas.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

  @Value("${smtp.host}")
  private String host;

  @Value("${smtp.port}")
  private int port;

  @Value("${smtp.username}")
  private String username;

  @Value("${smtp.password}")
  private String password;

  @Value("${smtp.enableTLS}")
  private boolean enableTLS;

  @Bean
  public JavaMailSenderImpl javaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(host);
    mailSender.setPort(port);
    mailSender.setUsername(username);
    mailSender.setPassword(password);

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", String.valueOf(enableTLS));

    return mailSender;
  }
}
