/**
 * 
 */
package com.visionaus.dms.configuration;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.visionaus.dms.pojo.Account;

/**
 * @author delimeta
 *
 */
public class AccountUserDetail extends Account implements UserDetails{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -226938159041759468L;
	private boolean enabled;
	private boolean accountNonExpired;
	private boolean credentialsNonExpired;
	private Collection<? extends GrantedAuthority> authorities;
	private boolean accountNonLocked;

	/**
	 * @param username
	 * @param password
	 * @param enabled
	 * @param accountNonExpired
	 * @param credentialsNonExpired
	 * @param accountNonLocked
	 * @param authorities
	 */
	public AccountUserDetail(String username, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
		super.setUsername(username);
		super.setPassword(password);
		
		this.enabled = enabled;
		this.accountNonExpired = accountNonExpired;
		this.credentialsNonExpired = credentialsNonExpired;
		this.accountNonLocked = accountNonExpired;
		this.authorities = authorities;
	}
	
	/**
	 *
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	/**
	 *
	 */
	@Override
	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	/**
	 *
	 */
	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	/**
	 *
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

	/**
	 *
	 */
	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

}
