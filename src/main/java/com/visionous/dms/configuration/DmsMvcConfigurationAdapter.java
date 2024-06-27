/**
 * 
 */
package com.visionous.dms.configuration;

import com.o2dent.lib.accounts.Configurations;
import com.visionous.dms.rest.EmailProperties;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;


/**
 * @author delimeta
 *
 */
@Configuration
@EnableWebMvc
@Import(Configurations.class)
@ComponentScan(basePackages = { "com.o2dent.*" })
@EntityScan("com.*")
public class DmsMvcConfigurationAdapter implements WebMvcConfigurer {
	private SubscriptionInterceptor subscriptionInterceptor;
	
	/**
	 * 
	 */
	public DmsMvcConfigurationAdapter(SubscriptionInterceptor subscriptionInterceptor) {
		this.subscriptionInterceptor = subscriptionInterceptor;
	}

	@Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource msgSrc = new ReloadableResourceBundleMessageSource();
        msgSrc.setBasenames("classpath:i18n/home");
        msgSrc.setDefaultEncoding("iso-8859-1");
        return msgSrc;
    }
	
	@Bean
	public LocalValidatorFactoryBean validator() {
	     LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
	     validatorFactoryBean.setValidationMessageSource(messageSource());
	     return validatorFactoryBean;
	}

	@Override
    @Lazy
    public void addResourceHandlers(@Lazy ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/static/assets/");
        registry.addResourceHandler("/resources/records/img/**").addResourceLocations("file:tmp/records/");
        registry.addResourceHandler("/resources/personnel/img/**").addResourceLocations("file:tmp/personnel/");
        registry.addResourceHandler("/resources/customer/img/**").addResourceLocations("file:tmp/customer/");
        registry.addResourceHandler("/resources/business/img/**").addResourceLocations("file:tmp/business/");
    }
	
    @Bean
    public LocaleResolver localeResolver() { 
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(Locale.US);
        resolver.setCookieName("myI18N_cookie");
        return resolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
        registry.addInterceptor(subscriptionInterceptor);
    }



//    @RefreshScope
//    @Bean
//    public JavaMailSender javaMailSender(EmailProperties properties) {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setUsername(properties.getUsername());
//        mailSender.setPassword(properties.getPassword());
//        mailSender.setHost(properties.getHost());
//        mailSender.setPort(Integer.parseInt(properties.getPort()));
//        mailSender.setProtocol(properties.getProtocol());
//        mailSender.setDefaultEncoding(properties.getDefaultEncoding());
//        mailSender.setJavaMailProperties(properties.getAdditionalMailProperties());
//
//        Session session = Session.getInstance(mailSender.getJavaMailProperties(), new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(mailSender.getUsername(), mailSender.getPassword());
//            }
//        });
//
//        mailSender.setSession(session);
//        return mailSender;
//    }

}