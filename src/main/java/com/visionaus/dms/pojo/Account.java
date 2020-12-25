package com.visionaus.dms.pojo;
/**
 * @author delimeta
 *
 */
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotBlank;

@Entity
public class Account {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_ID_SEQ")
    @SequenceGenerator(sequenceName = "user_id_seq", allocationSize = 1, name = "USER_ID_SEQ")    
    private Long id;
    
    @NotBlank(message = "Username is mandatory")
    private String username;
    
    @NotBlank(message = "Password is mandatory")
    private String password;

    private String terms;
    
    private boolean enabled;

	/**
	 * @param username
	 * @param password
	 * @param terms
	 * @param enabled
	 */
	public Account(@NotBlank(message = "Username is mandatory") String username,
			@NotBlank(message = "Password is mandatory") String password, String terms, boolean enabled) {
		super();
		this.username = username;
		this.password = password;
		this.terms = terms;
		this.enabled = enabled;
	}

	/**
	 * 
	 */
	public Account() {
		super();
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
	public String getTerms() {
		return terms;
	}

	/**
	 * @param terms
	 */
	public void setTerms(String terms) {
		this.terms = terms;
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", terms=" + terms + "]";
	}

	/**
	 * @return
	 */
	public boolean getEnabled() {
		return enabled;
	}

	/**
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
}