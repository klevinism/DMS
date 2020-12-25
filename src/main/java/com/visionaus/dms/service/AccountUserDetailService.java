package com.visionaus.dms.service;
/**
 * @author delimeta
 *
 */
import java.util.HashSet;
import java.util.Set;

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

@Service("userDetailsService")
public class AccountUserDetailService implements UserDetailsService{
	
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
		
		Set<GrantedAuthority> authorities = buildUserAuthority(authorityList);
		
		return buildUserForAuthentication(acc, authorities);
	}
	
	// Returns Account instead of spring.springframework.security.core.userdetails.User
    private AccountUserDetail buildUserForAuthentication(Account user,
        Set<GrantedAuthority> authorities) {
        return new AccountUserDetail(user.getUsername(), user.getPassword(),
            user.getEnabled(), true, true, true, authorities);
    }

    private Set<GrantedAuthority> buildUserAuthority(Set<String> userRoles) {

        Set<GrantedAuthority> setAuths = new HashSet<>();

        for (String userRole : userRoles) {
            setAuths.add(new SimpleGrantedAuthority(userRole));
        }

        return setAuths;
    }


}
