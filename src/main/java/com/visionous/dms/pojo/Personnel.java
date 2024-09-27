/**
 * 
 */
package com.visionous.dms.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.o2dent.lib.accounts.entity.Account;
import com.visionous.dms.configuration.helpers.DmsCore;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import java.io.Serializable;
import java.util.Set;

/**
 * @author delimeta
 *
 */
@Entity
public class Personnel implements Serializable{

	private static final long serialVersionUID = DmsCore.SERIAL_VERSION_UID;

	@Id
    private Long id;
	
	private String type;
	
	@JsonIgnore
    @OneToMany(mappedBy="supervisor")
    private Set<History> customerHistory;
    
    @JsonIgnore
    @OneToMany(mappedBy = "personnel", fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	private Set<Record> records;

    @JsonIgnore
    @OneToMany(mappedBy="personnel",  fetch = FetchType.LAZY)
    private Set<Appointment> appointments;

    
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
	 * @return the customerHistory
	 */
	public Set<History> getCustomerHistory() {
		return customerHistory;
	}

	/**
	 * @param customerHistory the customerHistory to set
	 */
	public void setCustomerHistory(Set<History> customerHistory) {
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
	public Set<Appointment> getAppointments() {
		return appointments;
	}

	/**
	 * @param appointments the appointment to set
	 */
	public void setAppointments(Set<Appointment> appointments) {
		this.appointments = appointments;
	}

}
