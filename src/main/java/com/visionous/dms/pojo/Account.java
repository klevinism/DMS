package com.visionous.dms.pojo;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.visionous.dms.configuration.helpers.DmsCore;
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
	private static final long serialVersionUID = DmsCore.SERIAL_VERSION_UID;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACC_SEQ")
    @SequenceGenerator(sequenceName = "account_seq", allocationSize = 1, name = "ACC_SEQ")
    private Long id;
	
    @NotNull(message = "Name should not be empty")
    @NotEmpty(message = "Name should not be empty")
	private String name;
	
    @NotNull(message = "Surname should not be empty")
    @NotEmpty(message = "Surname should not be empty")
	private String surname;
	
    @NotNull(message = "Age should not be empty")
	private int age;
	
    @NotNull(message = "Pick a gender")
	private String gender;
	
	@Valid
	@ValidEmail(message = "Email must be valid, example@example.com")
    @NotNull(message = "Email must not be null")
	private String email;
	
	@NotNull(message = "Enter a phone number")
	private Long phone;
    
	@NotNull(message = "Enter a username")
	@NotEmpty(message = "Enter a username")
    private String username;
    
	@Valid
	@NotNull(message = "Enter a password")
	@NotEmpty(message = "Enter a password")
	@NotBlank(message = "Enter a password")
    private String password;
    
	@NotNull(message = "Enter birthday")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE)
	private Date birthday;
	
	private String image;

	@NotNull(message = "Enter address")
	@NotEmpty(message = "Enter address")
	private String address;

	@NotNull(message = "Enter city")
	@NotEmpty(message = "Enter city")
	private String city;
	
	@NotNull(message = "Enter country")
	@NotEmpty(message = "Enter country")
	private String country;
	
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER) 
    @JoinTable(name = "authority",
            joinColumns = @JoinColumn(name = "accountid"),
            inverseJoinColumns = @JoinColumn(name = "roleid"))
    private List<Role> roles  = new ArrayList<>();
    
    private boolean enabled;
    
    private boolean active;
    
    @OneToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    @Nullable
	private Verification verification;
    
    
    @OneToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    @Nullable
	private Reset reset;
    
	@Valid
    @JsonIgnore
    @OneToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @PrimaryKeyJoinColumn
    @Nullable
	private Personnel personnel;
    
	@Valid
    @JsonIgnore
    @OneToOne(optional = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
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
			@NotBlank(message = "Password is mandatory") String password, List<Role> roles, boolean enabled, boolean active) {
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
		this.id = account.id;
		this.name = account.name;
		this.surname = account.surname;
		this.username = account.username;
		this.password = account.password;
		this.address = account.address;
		this.age = account.age;
		this.birthday = account.birthday;
		this.city = account.city;
		this.country = account.country;
		this.customer = account.customer;
		this.personnel = account.personnel;
		this.email = account.email;
		this.gender = account.gender;
		this.roles = account.roles;
		this.image = account.image;
		this.phone = account.phone;
		this.reset = account.reset;
		this.verification = account.verification;
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
		this.password = password;
	}

	/**
	 * @return
	 */
	public List<Role> getRoles() {
		return roles;
	}

	/**
	 * @param terms
	 */
	public void setRoles(List<Role> authority) {
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

	/**
	 * @return the birthday
	 */
	public Date getBirthday() {
		return birthday;
	}

	/**
	 * @param registerdate the registerdate to set
	 */
	public void setBirthday(String birthday) {
		try {
			this.birthday = new SimpleDateFormat("DD-MMM-YYYY").parse(birthday);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @param birthday the birthday to set
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	/**
	 * @return the reset
	 */
	public Reset getReset() {
		return reset;
	}

	/**
	 * @param reset the reset to set
	 */
	public void setReset(Reset reset) {
		this.reset = reset;
	}

	/**
	 * @return the verification
	 */
	public Verification getVerification() {
		return verification;
	}

	/**
	 * @param verification the verification to set
	 */
	public void setVerification(Verification verification) {
		this.verification = verification;
	}
	
	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", name=" + name + ", surname=" + surname + ", age=" + age + ", gender=" + gender
				+ ", email=" + email + ", phone=" + phone + ", username=" + username + ", password=" + password
				+ ", birthday=" + birthday + ", image=" + image + ", address=" + address + ", city=" + city
				+ ", country=" + country + ", enabled=" + enabled + ", active=" + active + "]";
	}

}