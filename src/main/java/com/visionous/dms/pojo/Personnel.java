/**
 * 
 */
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
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.visionous.dms.configuration.helpers.DmsCore;

/**
 * @author delimeta
 *
 */
@Entity
public class Personnel implements Serializable{

	private static final long serialVersionUID = DmsCore.SERIAL_VERSION_UID;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PERSON_SEQ")
    @SequenceGenerator(sequenceName = "personnel_seq", allocationSize = 1, initialValue = 2, name = "PERSON_SEQ")
    private Long id;
	
	private String type;
	
	@Valid
	@MapsId
	@OneToOne(mappedBy = "personnel", optional = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="id")
	private Account account;
	
	@JsonIgnore
    @OneToOne(mappedBy="supervisor", optional = true)
    private History customerHistory;
    
    @JsonIgnore
    @OneToMany(mappedBy = "personnel", fetch = FetchType.EAGER)
	@PrimaryKeyJoinColumn
	private Set<Record> records;

    @JsonIgnore
    @OneToOne(mappedBy="personnel",  fetch = FetchType.LAZY)
    private Appointment appointment;
    
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

	/**
	 * @return the records
	 */
	public Set<Record> getRecords() {
		return records;
	}

	/**
	 * @param records the records to set
	 */
	public void setRecords(Set<Record> records) {
		this.records = records;
	}

	/**
	 * @return the appointment
	 */
	public Appointment getAppointment() {
		return appointment;
	}

	/**
	 * @param appointment the appointment to set
	 */
	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}
}
