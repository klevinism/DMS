/**
 * 
 */
package com.visionous.dms.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.visionous.dms.configuration.helpers.DmsCoreVersion;
import com.visionous.dms.configuration.helpers.annotations.ValidEmail;

/**
 * @author delimeta
 *
 */
@Entity
@Table(name = "global_settings")
public class GlobalSettings implements Serializable{
    
    /**
	 *  
	 */
	private static final long serialVersionUID = DmsCoreVersion.SERIAL_VERSION_UID;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GLOBAL_SETTINGS_SEQ")
    @SequenceGenerator(sequenceName = "global_settings_seq", allocationSize = 1, name = "GLOBAL_SETTINGS_SEQ")
    private Long id;
	
	@Column(name = "business_name")
	private String businessName;
	
	@Column(name = "business_image")
	private String businessImage;
	
	@Column(name = "business_days")
	private String businessDays;
	
	@Column(name = "business_times")
	private String businessTimes;
	
	@ValidEmail(message = "Email must be valid, example@example.com")
	@NotEmpty(message = "Enter an email")
	@Column(name = "business_email")
	private String businessEmail;
	
	@NotEmpty(message = "Enter a password")
	@Column(name = "business_password")
	private String businessPassword;

	@Column(name = "appointmentsTimesSplit")
	private Integer appointmentTimeSplit;
	
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
	 * @return the businessName
	 */
	public String getBusinessName() {
		return businessName;
	}

	/**
	 * @param businessName the businessName to set
	 */
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	/**
	 * @return the businessImage
	 */
	public String getBusinessImage() {
		return businessImage;
	}

	/**
	 * @param businessImage the businessImage to set
	 */
	public void setBusinessImage(String businessImage) {
		this.businessImage = businessImage;
	}

	/**
	 * @return the businessDays
	 */
	public String getBusinessDays() {
		return businessDays;
	}

	/**
	 * @param businessDays the businessDays to set
	 */
	public void setBusinessDays(String businessDays) {
		this.businessDays = businessDays;
	}

	/**
	 * @return the businessTimes
	 */
	public String getBusinessTimes() {
		return businessTimes;
	}

	/**
	 * @param businessTimes the businessTimes to set
	 */
	public void setBusinessTimes(String businessTimes) {
		this.businessTimes = businessTimes;
	}

	/**
	 * @return the businessEmail
	 */
	public String getBusinessEmail() {
		return businessEmail;
	}

	/**
	 * @param businessEmail the businessEmail to set
	 */
	public void setBusinessEmail(String businessEmail) {
		this.businessEmail = businessEmail;
	}

	/**
	 * @return the businessPassword
	 */
	public String getBusinessPassword() {
		return businessPassword;
	}

	/**
	 * @param businessPassword the businessPassword to set
	 */
	public void setBusinessPassword(String businessPassword) {
		this.businessPassword = businessPassword;
	}

	/**
	 * @return the appointmentTimeSplit
	 */
	public Integer getAppointmentTimeSplit() {
		return appointmentTimeSplit;
	}

	/**
	 * @param appointmentTimeSplit the appointmentTimeSplit to set
	 */
	public void setAppointmentTimeSplit(Integer appointmentTimeSplit) {
		this.appointmentTimeSplit = appointmentTimeSplit;
	}
	
	
	
}
