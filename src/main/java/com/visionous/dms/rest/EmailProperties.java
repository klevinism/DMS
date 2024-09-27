package com.visionous.dms.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@EnableConfigurationProperties
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "spring.mail")
public class EmailProperties {
    private String host;
    private String port;
    private String username;
    private String password;
    private String protocol;
    private String defaultEncoding;
    @Autowired
    private MailConfigurations properties;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getDefaultEncoding() {
        return defaultEncoding;
    }

    public void setDefaultEncoding(String defaultEncoding) {
        this.defaultEncoding = defaultEncoding;
    }

    public MailConfigurations getProperties() {
        return properties;
    }

    public void setProperties(MailConfigurations properties) {
        this.properties = properties;
    }

    public Properties getAdditionalMailProperties(){
        Properties props = new Properties();
        props.put("mail.smtp.from", properties.getFrom());
        props.put("mail.smtp.auth", properties.getAuth());
        props.put("mail.smtp.socketFactory.port", properties.getSocketFactoryPort());
        props.put("mail.smtp.ssl.enabled", properties.getEnableSsl());
        props.put("mail.smtp.starttls.enable", properties.getEnableStartTls());
        props.put("mail.smtp.starttls.required", properties.getRequiredStartTls());
        return props;
    }
}