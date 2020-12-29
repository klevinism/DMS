/**
 * 
 */
package com.visionous.dms.configuration;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.visionous.dms.configuration.helpers.DmsCoreVersion;
import com.visionous.dms.pojo.Account;

/**
 * @author delimeta
 *
 */
public class AccountUserDetail extends Account implements UserDetails{
	private final Log logger = LogFactory.getLog(AccountUserDetail.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = DmsCoreVersion.SERIAL_VERSION_UID;
	
	private boolean accountNonExpired;
	private boolean credentialsNonExpired;
	private Collection<? extends GrantedAuthority> authorities;
	private boolean accountNonLocked;
	
	
	/**
	 * @param account
	 * @param accountNonExpired
	 * @param credentialsNonExpired
	 * @param accountNonLocked
	 * @param authorities
	 */
	public AccountUserDetail(Account account, boolean accountNonExpired, 
			boolean credentialsNonExpired, boolean accountNonLocked, 
			Collection<? extends GrantedAuthority> authorities) {
		
		super(account);
		
		this.accountNonExpired = accountNonExpired;
		this.credentialsNonExpired = credentialsNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.accountNonLocked = accountNonExpired;
		this.authorities = authorities;
	}
	
	/**
	 * @return authorities Collection<? extends GrantedAuthority>
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	/**
	 * @return accountNonExpired boolean
	 */
	@Override
	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	/**
	 * @return accountNonLocked boolean
	 */
	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	/**
	 * @return credentialsNonExpired boolean
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

}
