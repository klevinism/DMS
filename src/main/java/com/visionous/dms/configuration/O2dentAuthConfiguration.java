package com.visionous.dms.configuration;

import java.util.Collection;

import com.visionous.dms.pojo.Customer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.service.AccountUserDetailService;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author delimeta
 *
 */
@EnableWebSecurity
@Configuration
public class O2dentAuthConfiguration{
	private final Log logger = LogFactory.getLog(O2dentAuthConfiguration.class);

	private AccountUserDetailService accountUserDetailService;
	
	/**
	 * @param accountUserDetailService
	 */
	public O2dentAuthConfiguration(AccountUserDetailService accountUserDetailService) {
		this.accountUserDetailService = accountUserDetailService;
	}
	
	/**
	 *
	 */
	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
		logger.debug("Custom Configure(HttpSecurity) will override the default configuration of WebSecurityConfigurerAdapter");
		
		http
				.authorizeRequests()

				.requestMatchers(LandingPages.REGISTER.value()).anonymous()
				.requestMatchers("/my-health-check").anonymous()
				.requestMatchers(LandingPages.INDEX.value(),
						LandingPages.DASHBOARD.value()).access("hasRole('ROLE_USER')")
				.requestMatchers(LandingPages.NEW_APPOINTMENT.value()).access("hasRole('ROLE_PERSONNEL') or hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER')")
				.requestMatchers(LandingPages.BUSINESS.value() + "/**").authenticated()
				.requestMatchers(LandingPages.HOME.value()).access("hasRole('ROLE_PERSONNEL') or hasRole('ROLE_ADMIN')")
				.requestMatchers(LandingPages.HISTORY.value()).access("hasRole('ROLE_PERSONNEL') or hasRole('ROLE_ADMIN')")
				.requestMatchers(LandingPages.CUSTOMER.value() + "/**").access("hasRole('ROLE_PERSONNEL') or hasRole('ROLE_ADMIN')")
				.requestMatchers(LandingPages.ACCOUNT.value() + "/**").access("hasRole('ROLE_PERSONNEL') or hasRole('ROLE_ADMIN')")
				.requestMatchers(LandingPages.AGENDA.value() + "/**").access("hasRole('ROLE_PERSONNEL')  or hasRole('ROLE_ADMIN')") //all of Admin pages
				.requestMatchers(LandingPages.ADMIN.value() + "/**").access("hasRole('ROLE_ADMIN')") //all of Admin pages
				.and().formLogin(login -> {
					login.usernameParameter("username")
							.passwordParameter("password")
							.loginPage(LandingPages.LOGIN.value())
							.defaultSuccessUrl(LandingPages.BUSINESS.value())
							.failureUrl(LandingPages.LOGIN.value()+"?error")
							.permitAll();
				})
				.rememberMe(config -> {
					config.key("uniqueAndSecret").tokenValiditySeconds(86400);
				})
				.logout(logout->{
					logout.logoutRequestMatcher(new AntPathRequestMatcher(LandingPages.LOGOUT.value()))
							.logoutSuccessUrl(LandingPages.LOGIN.value())
							.invalidateHttpSession(true)        // set invalidation state when logout
							.deleteCookies("remember-me","JSESSIONID");
				});

		return http.build();
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
	@Lazy
	DaoAuthenticationProvider authenticationProvider(BCryptPasswordEncoder passwordEncoder){
	    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
	    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
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
	@Bean
	@Lazy
	public AuthenticationManager configureGlobal(@Lazy AuthenticationManagerBuilder auth, DaoAuthenticationProvider authenticationProvider, BCryptPasswordEncoder passwordEncoder) throws Exception {
		auth
				.authenticationProvider(authenticationProvider)
				.jdbcAuthentication()
				.passwordEncoder(passwordEncoder);
		return auth.build();
	}
}