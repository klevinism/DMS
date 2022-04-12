/**
 * 
 */
package com.visionous.dms.pojo;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.visionous.dms.configuration.helpers.DateUtil;

/**
 * @author delimeta
 *
 */
@Entity
public class Verification {
    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VERIFICATION_SEQ")
    @SequenceGenerator(sequenceName = "verification_seq", allocationSize = 1, name = "VERIFICATION_SEQ")
    private Long id;
    
    private String token;
  
    @OneToOne(targetEntity = Account.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "account_id")
    private Account account;
    
    @Column(name="expiredate")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiryDate;
    
    /**
	 * 
	 */
	public Verification() {
	}
    
	public Verification(Account account, String token) {
		this.account = account;
		this.token = token;
		this.expiryDate = DateUtil.calculateExpiryDate(60);
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
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
	 * @return the expiryDate
	 */
	public LocalDateTime getExpiryDate() {
		return expiryDate;
	}

	/**
	 * @param expiryDate the expiryDate to set
	 */
	public void setExpiryDate(LocalDateTime expiryDate) {
		this.expiryDate = expiryDate;
	}
}