/**
 * 
 */
package com.visionous.dms.configuration;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * @author delimeta
 *
 */
@Configuration
@EnableWebMvc
public class DmsMvcConfigurationAdapter implements WebMvcConfigurer  {
	private SubscriptionInterceptor subscriptionInterceptor;
	
	/**
	 * 
	 */
	@Autowired
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
	public Validator getValidator() {
	     return validator();
	}
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
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

}