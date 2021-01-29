/**
 * 
 */
package com.visionous.dms.pojo;

import java.sql.Timestamp;
import java.util.Calendar;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE)
    private Date expiryDate;
    
    /**
	 * 
	 */
	public Verification() {
	}
    
	public Verification(Account account, String token) {
		this.account = account;
		this.account.setPassword(new BCryptPasswordEncoder().encode(account.getPassword()));
		this.token = token;
		this.expiryDate = calculateExpiryDate(60);
	}
    
    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
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