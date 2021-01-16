package com.visionous.dms.configuration;

import java.util.Collection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.service.AccountUserDetailService;

/**
 * @author delimeta
 *
 */
@Configuration
@EnableWebSecurity
public class AuthConfiguration extends WebSecurityConfigurerAdapter {
	private final Log logger = LogFactory.getLog(AuthConfiguration.class);

	private AccountUserDetailService accountUserDetailService;
	
	/**
	 * @param accountUserDetailService
	 */
	@Autowired
	public AuthConfiguration(AccountUserDetailService accountUserDetailService) {
		this.accountUserDetailService = accountUserDetailService;
	}
	
	/**
	 *
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		logger.debug("Custom Configure(HttpSecurity) will override the default configuration of WebSecurityConfigurerAdapter");
		
		http
			.authorizeRequests()			
				.antMatchers(LandingPages.LOGIN.value(), 
						LandingPages.REGISTER.value()).anonymous()
				.antMatchers(LandingPages.INDEX.value(), 
						LandingPages.DASHBOARD.value()).access("hasRole('ROLE_USER')")
				
				.antMatchers(LandingPages.DEFAULT.value()).access("hasRole('ROLE_PERSONNEL') or hasRole('ROLE_ADMIN')")
				.antMatchers(LandingPages.CUSTOMER.value()).access("hasRole('ROLE_PERSONNEL') or hasRole('ROLE_ADMIN')")
				.antMatchers(LandingPages.ADMIN.value()+"/**").access("hasRole('ROLE_ADMIN')") //all of Admin pages
				.and()
			.formLogin()
				.usernameParameter("username")
				.passwordParameter("password")
				.loginPage(LandingPages.LOGIN.value())
                .defaultSuccessUrl(LandingPages.DEFAULT.value())
                .failureUrl(LandingPages.LOGIN.value()+"?error")
                .failureForwardUrl(LandingPages.LOGIN.value()+"?error")
                .permitAll()
                .and()
			.logout()
            	.logoutRequestMatcher(new AntPathRequestMatcher(LandingPages.LOGOUT.value()))  
                .logoutSuccessUrl(LandingPages.LOGIN.value())
                .invalidateHttpSession(true)        // set invalidation state when logout
                .deleteCookies("JSESSIONID")
				.permitAll();
				
	}
	
	/**
	 * @return
	 */
	@Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

	/**
	 * @return
	 */
	@Bean
	DaoAuthenticationProvider authenticationProvider(){
	    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
	    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
	    daoAuthenticationProvider.setUserDetailsService(accountUserDetailService);
	    
	    //Lambda call to GrantedAuthoritiesMapper.mapAuthorities(Collection<? extends GrantedAuthority> authorities)
	    daoAuthenticationProvider.setAuthoritiesMapper((Collection<? extends GrantedAuthority> authorities) -> {				
			SimpleAuthorityMapper authorityMapper = new SimpleAuthorityMapper();
			authorityMapper.setPrefix("ROLE_"); // Add 'Role_' Prefix to all authorities/roles.
			return authorityMapper.mapAuthorities(authorities);
	    });
	    
	    return daoAuthenticationProvider;
	}
	
	/**
	 * @param auth
	 * @throws Exception
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.authenticationProvider(authenticationProvider())
			.jdbcAuthentication()
			.passwordEncoder(passwordEncoder());
	}
}