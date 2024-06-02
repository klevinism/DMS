/**
 * 
 */
package com.visionous.dms.configuration;

import java.util.Collection;

import com.o2dent.lib.accounts.entity.Account;
import com.o2dent.lib.accounts.entity.Business;
import com.visionous.dms.pojo.GlobalSettings;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.visionous.dms.configuration.helpers.DmsCore;

/**
 * @author delimeta
 *
 */
public class AccountUserDetail extends Account implements UserDetails{

	/**
	 * 
	 */
	private static final long serialVersionUID = DmsCore.SERIAL_VERSION_UID;
	
	private Account account;
	private Business currentBusiness;
	private boolean accountNonExpired;
	private boolean credentialsNonExpired;
	private Collection<? extends GrantedAuthority> authorities;
	private boolean accountNonLocked;
	private GlobalSettings currentBusinessSettings;

	private boolean isPersonnel;
	
	/**
	 * @param account
	 * @param accountNonExpired
	 * @param credentialsNonExpired
	 * @param accountNonLocked
	 * @param authorities
	 */
	public AccountUserDetail(Account account, boolean accountNonExpired, 
			boolean credentialsNonExpired, boolean accountNonLocked,
		 	GlobalSettings currentBusinessSettings,
			Collection<? extends GrantedAuthority> authorities) {
		
		super(account);

		this.account = account;
		
		this.accountNonExpired = accountNonExpired;
		this.credentialsNonExpired = credentialsNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.accountNonLocked = accountNonExpired;
		this.authorities = authorities;
		this.currentBusinessSettings = currentBusinessSettings;
	}
	
	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
	}

	/**
	 * @return
	 */
	public Business getCurrentBusiness() {
		return currentBusiness;
	}

	/**
	 *
	 * @param currentBusiness
	 */
	public void setCurrentBusiness(Business currentBusiness) {
		this.currentBusiness = currentBusiness;
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

	/**
	 *
	 * @return
	 */
	public GlobalSettings getCurrentBusinessSettings() {
		return currentBusinessSettings;
	}

	/**
	 *
	 * @param currentBusinessSettings
	 */
	public void setCurrentBusinessSettings(GlobalSettings currentBusinessSettings) {
		this.currentBusinessSettings = currentBusinessSettings;
	}

	/**
	 *
	 * @return boolean
	 */
	public boolean isPersonnel() {
		return isPersonnel;
	}

	/**
	 *
	 * @param isPersonnel boolean
	 */
	public void setPersonnel(boolean isPersonnel) {
		isPersonnel = isPersonnel;
	}
}
