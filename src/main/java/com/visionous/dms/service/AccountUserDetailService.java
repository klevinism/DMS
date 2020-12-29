package com.visionous.dms.service;

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

import com.visionous.dms.configuration.AccountUserDetail;
import com.visionous.dms.pojo.Account;
import com.visionous.dms.pojo.Role;
import com.visionous.dms.repository.AccountRepository;


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
		
		Set<GrantedAuthority> authorities = buildUserAuthority(acc.getRoles());
		
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
    private Set<GrantedAuthority> buildUserAuthority(Set<Role> userRoles) {

        Set<GrantedAuthority> setAuths = new HashSet<>();
        for (Role userRole : userRoles) {
            setAuths.add(new SimpleGrantedAuthority(userRole.getName()));
        }

        return setAuths;
    }


}
