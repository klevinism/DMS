package com.visionous.dms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.visionous.dms.configuration.AccountUserDetail;
import com.visionous.dms.pojo.Account;
import com.visionous.dms.pojo.Role;


/**
 * @author delimeta
 *
 */
@Service("userDetailsService")
public class AccountUserDetailService implements UserDetailsService{
	private final Log logger = LogFactory.getLog(AccountUserDetailService.class);
	
	private AccountService accountService;
	
	@Autowired
	private AccountUserDetailService(AccountService accountService) {
		this.accountService = accountService;
	}
	
	/**
	 * @param username
	 */
	@Override
	public UserDetails loadUserByUsername(String username){
		List<GrantedAuthority> authorities = new ArrayList<>();
		AccountUserDetail accountUserDetail = null;
		Optional<Account> acc = accountService.findByUsernameOrEmail(username); 
		
		if(acc.isPresent()) {
			authorities.addAll(buildUserAuthority(acc.get().getRoles()));
			accountUserDetail = buildUserForAuthentication(acc.get(), authorities);
		}
		
		return accountUserDetail;
	}
	
    /** 
     * @param user 
     * @param authorities
     * @return Returns {@link Account} instead of {@link User} 
     */
    private AccountUserDetail buildUserForAuthentication(Account user,
        List<GrantedAuthority> authorities) {
    	return new AccountUserDetail(user, true, true, true, authorities);
    }

    /**
     * Builds custom Set of {@link GrantedAuthority}
     * @param list
     * @return Set of {@link GrantedAuthority}
     */
    private List<GrantedAuthority> buildUserAuthority(List<Role> list) {

        List<GrantedAuthority> setAuths = new ArrayList<>();
        for (Role userRole : list) {
            setAuths.add(new SimpleGrantedAuthority(userRole.getName()));
        }

        return setAuths;
    }


}
