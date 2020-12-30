package com.visionous.dms.pojo;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotBlank;

import com.visionous.dms.configuration.helpers.DmsCoreVersion;

/**
 * @author delimeta
 *
 */
@Entity
public class Account implements Serializable{
    
    /**
	 *  
	 */
	private static final long serialVersionUID = DmsCoreVersion.SERIAL_VERSION_UID;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_ID_SEQ")
    @SequenceGenerator(sequenceName = "user_id_seq", allocationSize = 1, name = "USER_ID_SEQ")    
    private Long id;
    
    @NotBlank(message = "Username is mandatory")
    private String username;
    
    @NotBlank(message = "Password is mandatory")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "authority",
            joinColumns = @JoinColumn(name = "accountid", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "roleid", referencedColumnName = "id"))
    private Set<Role> roles;
    
    private boolean enabled;
    
    private boolean active;
    
	@OneToOne(mappedBy = "account", fetch = FetchType.EAGER)
	@PrimaryKeyJoinColumn
	private Personnel personnel;
    

	/**
	 * @param username
	 * @param password
	 * @param terms
	 * @param enabled
	 */
	public Account(@NotBlank(message = "Username is mandatory") String username,
			@NotBlank(message = "Password is mandatory") String password, Set<Role> roles, boolean enabled, boolean active) {
		this.username = username;
		this.password = password;
		this.roles = roles;
		this.enabled = enabled;
		this.active = active;
	}
	
	/**
	 * @param account
	 */
	public Account(Account account) {
		this.username = account.username;
		this.password = account.password;
		this.roles = account.roles;
		this.enabled = account.enabled;
		this.active = account.active;
	}
	
	/**
	 * Default constructor needed for entity
	 */
	public Account() {
	}

	/**
	 * @param id
	 */
	public void setId(Long id){
		this.id = id;
	}
	/**
	 * @return
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return
	 */
	public Set<Role> getRoles() {
		return roles;
	}

	/**
	 * @param terms
	 */
	public void setRoles(Set<Role> authority) {
		this.roles = authority;
	}

	/**
	 * @return the personnel
	 */
	public Personnel getPersonnel() {
		return personnel;
	}

	/**
	 * @param personnel the personnel to set
	 */
	public void setPersonnel(Personnel personnel) {
		this.personnel = personnel;
	}

	/**
	 * @return
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @param active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return active boolean
	 */
	public boolean isActive() {
		return active;
	}
}