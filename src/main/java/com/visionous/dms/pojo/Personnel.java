/**
 * 
 */
package com.visionous.dms.pojo;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import com.visionous.dms.configuration.helpers.DmsCoreVersion;

/**
 * @author delimeta
 *
 */
@Entity
public class Personnel implements Serializable{

	private static final long serialVersionUID = DmsCoreVersion.SERIAL_VERSION_UID;

	@Id
    private Long id;
	
	private String type;
	
	@MapsId
	@OneToOne(mappedBy = "personnel", optional = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="id")
	private Account account;
    
    @OneToOne(mappedBy="supervisor", optional = true)
    private History customerHistory;

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
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
	 * @return the customerHistory
	 */
	public History getCustomerHistory() {
		return customerHistory;
	}

	/**
	 * @param customerHistory the customerHistory to set
	 */
	public void setCustomerHistory(History customerHistory) {
		this.customerHistory = customerHistory;
	}
}
