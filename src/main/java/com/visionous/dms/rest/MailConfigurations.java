package com.visionous.dms.rest;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "spring.mail.properties.mail.smtp")
public class MailConfigurations {
    private String from;
    private String auth;
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String enableStartTls;
    @Value("${spring.mail.properties.mail.smtp.starttls.required}")
    private String requiredStartTls;
    @Value("${spring.mail.properties.mail.smtp.socketFactory.port}")
    private String socketFactoryPort;
    @Value("${spring.mail.properties.mail.smtp.ssl.enable}")
    private String enableSsl;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getEnableStartTls() {
        return enableStartTls;
    }

    public void setEnableStartTls(String enableStartTls) {
        this.enableStartTls = enableStartTls;
    }

    public String getRequiredStartTls() {
        return requiredStartTls;
    }

    public void setRequiredStartTls(String requiredStartTls) {
        this.requiredStartTls = requiredStartTls;
    }

    public String getSocketFactoryPort() {
        return socketFactoryPort;
    }

    public void setSocketFactoryPort(String socketFactoryPort) {
        this.socketFactoryPort = socketFactoryPort;
    }

    public String getEnableSsl() {
        return enableSsl;
    }

    public void setEnableSsl(String enableSsl) {
        this.enableSsl = enableSsl;
    }
}
