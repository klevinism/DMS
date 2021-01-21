/**
 * 
 */
package com.visionous.dms.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import org.springframework.format.annotation.DateTimeFormat;

import com.visionous.dms.configuration.helpers.DmsCoreVersion;

/**
 * @author delimeta
 *
 */
@Entity
public class Appointment implements Serializable{
    
    /**
	 *  
	 */
	private static final long serialVersionUID = DmsCoreVersion.SERIAL_VERSION_UID;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APPOINTMENT_SEQ")
    @SequenceGenerator(sequenceName = "appointment_seq", allocationSize = 1, name = "APPOINTMENT_SEQ")
    private Long id;
	
	@Column(name="customerid", updatable = false, insertable = false)
	private Long customerId;
	
	@Column(name="personnelid", updatable = false, insertable = false)
	private Long personnelId;

	@Column(name="appointmentdate")
	private Date appointmentDate;
	
	@DateTimeFormat (pattern="dd-MM-YYYY")
	private Date addeddate;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH )
	@JoinColumn(name = "customerid")
	private Customer customer;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH )
	@JoinColumn(name = "personnelid")
	private Personnel personnel;

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
	 * @return the personnelId
	 */
	public Long getPersonnelId() {
		return personnelId;
	}

	/**
	 * @param personnelId the personnelId to set
	 */
	public void setPersonnelId(Long personnelId) {
		this.personnelId = personnelId;
	}

	/**
	 * @return the appointmentDate
	 */
	public Date getAppointmentDate() {
		return appointmentDate;
	}

	/**
	 * @param appointmentDate the appointmetDate to set
	 */
	public void setAppointmentDate(Date appointmetDate) {
		this.appointmentDate = appointmetDate;
	}

	/**
	 * @return the addeddate
	 */
	public Date getAddeddate() {
		return addeddate;
	}

	/**
	 * @param addeddate the addeddate to set
	 */
	public void setAddeddate(Date addeddate) {
		this.addeddate = addeddate;
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

	@Override
	public String toString() {
		return "Appointment [id=" + id + ", customerId=" + customerId + ", personnelId=" + personnelId
				+ ", appointmentDate=" + appointmentDate + ", addeddate=" + addeddate + "]";
	}

}
