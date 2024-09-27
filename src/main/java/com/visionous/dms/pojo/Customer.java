package com.visionous.dms.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.o2dent.lib.accounts.entity.Account;
import com.visionous.dms.configuration.helpers.DmsCore;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author delimeta
 *
 */
@Entity
public class Customer implements Serializable{
    
    /**
	 *  
	 */
	private static final long serialVersionUID = DmsCore.SERIAL_VERSION_UID;
	
	@Id
    private Long id;
	
	@DateTimeFormat(iso = ISO.DATE)
	private Date registerdate;
    
	@JsonIgnore
    @OneToOne(mappedBy="customer")
    private History customerHistory;
    
	@JsonIgnore
    @OneToOne(mappedBy="customer")
    private Questionnaire questionnaire;
    
	@JsonIgnore
    @OneToMany(mappedBy="customer")
    private Set<Appointment> appointment;
    
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
	public Set<Appointment> getAppointment() {
		return appointment;
	}

	/**
	 * @param appointment the appointment to set
	 */
	public void setAppointment(Set<Appointment> appointment) {
		this.appointment = appointment;
	}

	/**
	 * @param appointment
	 */
	public void addAppointment(Appointment appointment) {
		this.appointment.add(appointment);
	}
	
	public boolean hasCustomerHistory() {
		return this.customerHistory != null;
	}

	/**
	 * @return
	 */
	public boolean hasQuestionnaire() {
		return this.questionnaire != null;
	}
}
