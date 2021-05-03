/**
 * 
 */
package com.visionous.dms.configuration;

import java.util.List;
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
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring5.ISpringTemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import com.visionous.dms.pojo.GlobalSettings;
import com.visionous.dms.service.GlobalSettingsService;

/**
 * @author delimeta
 *
 */
@Configuration
@EnableWebMvc
public class DmsMvcConfigurationAdapter implements WebMvcConfigurer  {
	private GlobalSettingsService globalSettingService;
	
	/**
	 * 
	 */
	@Autowired
	public DmsMvcConfigurationAdapter(GlobalSettingsService globalSettingService) {
		this.globalSettingService = globalSettingService;
	}

	@Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource msgSrc = new ReloadableResourceBundleMessageSource();
        msgSrc.setBasenames("classpath:i18n/home");
        msgSrc.setDefaultEncoding("UTF-8");
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
	
	@Bean
    public GlobalSettings globalSettings() {
        List<GlobalSettings> allSettings = globalSettingService.findAll();
        if(!allSettings.isEmpty()) {
        	return allSettings.get(0);
        }
        return null;
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
    }

}