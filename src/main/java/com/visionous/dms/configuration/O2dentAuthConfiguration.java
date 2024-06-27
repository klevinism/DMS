package com.visionous.dms.configuration;

import com.o2dent.security.bundle.authentication.access.O2AuthoritiesMapper;
import com.o2dent.security.bundle.authentication.access.O2OidcAccountService;
import com.visionous.dms.configuration.helpers.LandingPages;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
/**
 * @author delimeta
 *
 */

@ComponentScan(basePackages = {"com.o2dent.*"})
@Configuration
@EnableWebSecurity
@EntityScan("com.o2dent.*")
public class O2dentAuthConfiguration{
	private final Log logger = LogFactory.getLog(O2dentAuthConfiguration.class);
	private final AuthenticationOidcService authenticationOidcService;
	private final KeycloakLogoutHandler keycloakLogoutHandler;

	/**
	 * @param o2OidcAccountService
	 * @param authenticationOidcService
	 * @param keycloakLogoutHandler
	 */
	public O2dentAuthConfiguration(O2OidcAccountService o2OidcAccountService, KeycloakLogoutHandler keycloakLogoutHandler,
								   AuthenticationOidcService authenticationOidcService) {
		this.authenticationOidcService = authenticationOidcService;
		this.keycloakLogoutHandler = keycloakLogoutHandler;
	}
	
	/**
	 *
	 */
	@Bean
	protected SecurityFilterChain resourceServerFilterChain(HttpSecurity http) throws Exception {
		logger.debug("Custom Configure(HttpSecurity) will override the default configuration of WebSecurityConfigurerAdapter");
		
		http
				.authorizeRequests()
				.requestMatchers("/my-health-check").anonymous()
				.requestMatchers(LandingPages.INDEX.value(),
						LandingPages.DASHBOARD.value()).access("hasRole('ROLE_USER')")
				.requestMatchers(LandingPages.NEW_APPOINTMENT.value()).access("hasRole('ROLE_PERSONNEL') or hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER')")
				.requestMatchers(LandingPages.BUSINESS.value() + "/**").authenticated()
				.requestMatchers(LandingPages.HOME.value()).access("hasRole('ROLE_PERSONNEL') or hasRole('ROLE_ADMIN') or hasRole('ROLE_oxydent-user')")
				.requestMatchers(LandingPages.HISTORY.value()).access("hasRole('ROLE_PERSONNEL') or hasRole('ROLE_ADMIN')")
				.requestMatchers(LandingPages.CUSTOMER.value() + "/**").access("hasRole('ROLE_PERSONNEL') or hasRole('ROLE_ADMIN')")
				.requestMatchers(LandingPages.ACCOUNT.value() + "/**").access("hasRole('ROLE_PERSONNEL') or hasRole('ROLE_ADMIN')")
				.requestMatchers(LandingPages.AGENDA.value() + "/**").access("hasRole('ROLE_PERSONNEL')  or hasRole('ROLE_ADMIN')") //all of Admin pages
				.requestMatchers(LandingPages.ADMIN.value() + "/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_oxydent-user')"); //all of Admin pages
//				.and().formLogin(login -> {
//					login.usernameParameter("username")
//							.passwordParameter("password")
//							.loginPage(LandingPages.LOGIN.value())
//							.defaultSuccessUrl(LandingPages.BUSINESS.value())
//							.failureUrl(LandingPages.LOGIN.value()+"?error")
//							.permitAll();
//				})
//				.rememberMe(config -> {
//					config.key("uniqueAndSecret").tokenValiditySeconds(86400);
//				})
//				.logout(logout->{
//					logout.logoutRequestMatcher(new AntPathRequestMatcher(LandingPages.LOGOUT.value()))
//							.logoutSuccessUrl(LandingPages.LOGIN.value())
//							.invalidateHttpSession(true)        // set invalidation state when logout
//							.deleteCookies("remember-me","JSESSIONID");
//				});

		http.oauth2ResourceServer((oauth2) -> oauth2
				.jwt(Customizer.withDefaults()));

		http.oauth2Login(oauth2 ->
				oauth2.userInfoEndpoint(userInfoEndpointConfig ->
					userInfoEndpointConfig
							.oidcUserService(this.authenticationOidcService)
							.userAuthoritiesMapper(this.userAuthoritiesMapperForKeycloak())))
				.logout(logout->{
					logout.logoutRequestMatcher(new AntPathRequestMatcher(LandingPages.LOGOUT.value()))
							.addLogoutHandler(keycloakLogoutHandler)
							.clearAuthentication(true)
							.invalidateHttpSession(true)         // set invalidation state when logout;
							.logoutSuccessUrl(LandingPages.HOME.value());
				});

		return http.build();
	}

	@Bean
	public GrantedAuthoritiesMapper userAuthoritiesMapperForKeycloak() {
		return new O2AuthoritiesMapper();
	}
}