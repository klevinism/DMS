/**
 * 
 */
package com.visionous.dms.pojo;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.visionous.dms.configuration.helpers.DmsCoreVersion;

/**
 * @author delimeta
 *
 */
@Entity
public class Customer implements Serializable{
    
    /**
	 *  
	 */
	private static final long serialVersionUID = DmsCoreVersion.SERIAL_VERSION_UID;
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUSTOMER_SEQ2")
    @SequenceGenerator(sequenceName = "customer_seq2", allocationSize = 1, name = "CUSTOMER_SEQ2")
    private Long id;
	
	@DateTimeFormat(iso = ISO.DATE)
	private Date registerdate;

	@Valid
	@MapsId
	@OneToOne(mappedBy = "customer", optional = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="id")
	private Account account;
    
	@JsonIgnore
    @OneToOne(mappedBy="customer")
    private History customerHistory;
    
	@JsonIgnore
    @OneToOne(mappedBy="customer")
    private Questionnaire questionnaire;
    
	@JsonIgnore
    @OneToOne(mappedBy="customer")
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
	 * @return the registerdate
	 */
	public Date getRegisterdate() {
		return registerdate;
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
	 * @param registerdate the registerdate to set
	 */
	public void setRegisterdate(Date registerdate) {
		this.registerdate = registerdate;
	}

	/**
	 * @param registerdate the registerdate to set
	 */
	public void setRegisterdate(String registerdate) {
		try {
			this.registerdate = new SimpleDateFormat("DD-MMM-YY").parse(registerdate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	 * @return the questionnaire
	 */
	public Questionnaire getQuestionnaire() {
		return questionnaire;
	}

	/**
	 * @param questionnaire the questionnaire to set
	 */
	public void setQuestionnaire(Questionnaire questionnaire) {
		this.questionnaire = questionnaire;
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
