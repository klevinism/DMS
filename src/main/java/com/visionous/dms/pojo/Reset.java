/**
 * 
 */
package com.visionous.dms.pojo;

import java.util.Date;

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

import com.visionous.dms.configuration.helpers.DateUtil;

/**
 * @author delimeta
 *
 */
@Entity
public class Reset {
	private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RESET_SEQ")
    @SequenceGenerator(sequenceName = "reset_seq", allocationSize = 1, name = "RESET_SEQ")
    private Long id;
    
    private String token;
  
    @OneToOne(targetEntity = Account.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "account_id")
    private Account account;
    
    @Column(name="expiredate")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE)
    private Date expiryDate;

	/**
	 * @param account2
	 * @param token2
	 */
	public Reset(Account account, String token) {
		this.account =  account;
		this.token = token;
		this.expiryDate = DateUtil.calculateExpiryDate(60);
	}
	
	/**
	 * 
	 */
	public Reset() {
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
	public Date getExpiryDate() {
		return expiryDate;
	}

	/**
	 * @param expiryDate the expiryDate to set
	 */
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
}
