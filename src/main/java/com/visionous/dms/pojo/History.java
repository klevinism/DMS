/**
 * 
 */
package com.visionous.dms.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;

import org.springframework.format.annotation.DateTimeFormat;

import com.visionous.dms.configuration.helpers.DmsCore;

/**
 * @author delimeta
 *
 */
@Entity
public class History implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = DmsCore.SERIAL_VERSION_UID;
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HIS_SEQ")
    @SequenceGenerator(sequenceName = "history_seq", allocationSize = 1, name = "HIS_SEQ")
	private Long id;

	@Column(name="customerid", insertable = false, updatable = false)
	private Long customerId;
	
	@Column(name="supervisorid", insertable = false, updatable = false)
	private Long supervisorId;
	
	@DateTimeFormat (pattern="dd-MMM-YYYY")
	private Date startdate;

	@OneToOne(optional = false, cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "supervisorid")
	private Personnel supervisor;
	
	@OneToMany(mappedBy = "history", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@PrimaryKeyJoinColumn
	private Set<Record> records;

	@OneToOne(optional = false, cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "customerid")
	private Customer customer;

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
	 * @return the supervisorId
	 */
	public Long getSupervisorId() {
		return supervisorId;
	}

	/**
	 * @param supervisorId the supervisorId to set
	 */
	public void setSupervisorId(Long supervisorId) {
		this.supervisorId = supervisorId;
	}

	/**
	 * @return the startdate
	 */
	public Date getStartdate() {
		return startdate;
	}

	/**
	 * @param startdate the startdate to set
	 */
	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	/**
	 * @return the supervisor
	 */
	public Personnel getSupervisor() {
		return supervisor;
	}

	/**
	 * @param supervisor the supervisor to set
	 */
	public void setSupervisor(Personnel supervisor) {
		this.supervisor = supervisor;
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
	 * @param newRecord
	 */
	public void addRecord(Record newRecord) {
		this.records.add(newRecord);
	}
	
	/**
	 * @return the customerId
	 */
	public Long getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
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

	

}
