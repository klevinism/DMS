package com.visionous.dms.pojo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.visionous.dms.configuration.helpers.DmsCoreVersion;
import com.visionous.dms.configuration.helpers.annotations.ValidEmail;

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACC_SEQ")
    @SequenceGenerator(sequenceName = "account_seq", allocationSize = 1, name = "ACC_SEQ")
    private Long id;
	
	private String name;
	
	private String surname;
	
	private int age;
	
	private String gender;
	
	@ValidEmail(message = "Email must be valid, example@example.com")
    @NotNull
    @NotEmpty
	private String email;
	
	private Long phone;
    
    private String username;
    
    private String password;
    
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL) 
    @JoinTable(name = "authority",
            joinColumns = @JoinColumn(name = "accountid"),
            inverseJoinColumns = @JoinColumn(name = "roleid"))
    private Set<Role> roles  = new HashSet<>();
    
    private boolean enabled;
    
    private boolean active;
    
    @JsonIgnore
    @OneToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @PrimaryKeyJoinColumn
    @Nullable
	private Personnel personnel;
    
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
    @OneToOne(optional = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @PrimaryKeyJoinColumn
    @Nullable
	private Customer customer;

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
		this.personnel = new Personnel();
		this.customer = new Customer();
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}

	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the phone
	 */
	public Long getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(Long phone) {
		this.phone = phone;
	}
	
	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
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
		this.password = new BCryptPasswordEncoder().encode(password);
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
	
	public void addRole(Role role) {
		this.roles.add(role);
		role.getAccounts().add(this);
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

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", name=" + name + ", surname=" + surname + ", age=" + age + ", gender=" + gender
				+ ", email=" + email + ", phone=" + phone + ", username=" + username + ", password=" + password
				+ ", enabled=" + enabled + ", active=" + active 
				+ "]";
	}

}