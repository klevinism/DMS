package com.visionaus.dms.service;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.visionaus.dms.configuration.AccountUserDetail;
import com.visionaus.dms.pojo.Account;
import com.visionaus.dms.repository.AccountRepository;

/**
 * @author delimeta
 *
 */
@Service("userDetailsService")
public class AccountUserDetailService implements UserDetailsService{
	private final Log logger = LogFactory.getLog(AccountUserDetailService.class);
	
	private AccountRepository accountRepository;
	
	@Autowired
	private AccountUserDetailService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}
	
	/**
	 * @param username
	 */
	@Override
	public UserDetails loadUserByUsername(String username){
		Account acc = accountRepository.findByUsername(username);
		if(acc == null) {
			throw new UsernameNotFoundException(username);
		}
		
		Set<String> authorityList = new HashSet<>();
		authorityList.add(acc.getTerms());
		
		logger.info(acc);
		logger.info(" acc active = >>>");
		logger.info(acc.isActive());

		Set<GrantedAuthority> authorities = buildUserAuthority(authorityList);
		
		return buildUserForAuthentication(acc, authorities);
	}
	
	// Returns Account instead of spring.springframework.security.core.userdetails.User
    /**
     * @param user
     * @param authorities
     * @return
     */
    private AccountUserDetail buildUserForAuthentication(Account user,
        Set<GrantedAuthority> authorities) {
    	return new AccountUserDetail(user, true, true, true, authorities);
    }

    /**
     * @param userRoles
     * @return
     */
    private Set<GrantedAuthority> buildUserAuthority(Set<String> userRoles) {

        Set<GrantedAuthority> setAuths = new HashSet<>();
        for (String userRole : userRoles) {
            setAuths.add(new SimpleGrantedAuthority(userRole));
        }

        return setAuths;
    }


}
