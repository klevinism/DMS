package com.visionous.dms.pojo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.visionous.dms.configuration.helpers.annotations.ValidEmail;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.visionous.dms.configuration.helpers.DmsCore;

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
	private static final long serialVersionUID = DmsCore.SERIAL_VERSION_UID;

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
	
	@Column(name = "business_password")
	private String businessPassword;

	@Column(name = "appointmentsTimesSplit")
	private Integer appointmentTimeSplit;
	
	@Column(name = "business_id")
	private Long businessId;
	
	@JsonIgnore
	@OneToMany(mappedBy = "globalSettingsId", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private Set<SubscriptionHistory> subscriptionHistorySet;

	@OneToMany(mappedBy = "globalSettingsId", fetch = FetchType.EAGER)
	private Set<ServiceType> serviceType;
	/**
	 * 
	 */
	public GlobalSettings() {
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
	
	public Integer getBusinessStartDay() {
		return Integer.parseInt(this.getBusinessDays().split(",")[0]);
	}
	
	public Integer getBusinessEndDay() {
		return Integer.parseInt(this.getBusinessDays().split(",")[1]);
	}
	
	public void setCurrentSetting(GlobalSettings newSettings) {
		this.id = newSettings.id;
		this.appointmentTimeSplit = newSettings.appointmentTimeSplit;
		this.businessDays = newSettings.businessDays;
		this.businessEmail = newSettings.businessEmail;
		this.businessImage = newSettings.businessImage;
		this.businessName = newSettings.businessName;
		this.businessPassword = newSettings.businessPassword;		
		this.businessTimes = newSettings.businessTimes;
	}
	
	/**
	 * @return
	 */
	public List<Integer> getWorkingBusinessDays(){
		String[] dayPeriod = getBusinessDays().split(",");
		List<Integer> workingDays = new ArrayList<>();
		int dayStartNr = Integer.parseInt(dayPeriod[0]);
		int dayEndNr = Integer.parseInt(dayPeriod[1]);
		
		for(int x=dayStartNr; x<=dayEndNr; x++) {
			if(dayStartNr < x || x < dayEndNr) {
				workingDays.add(x);	
			}
		}
		return workingDays;
	}
	
	/**
	 * @return
	 */
	public List<Integer> getNonBusinessDays(){
		String[] dayPeriod = getBusinessDays().split(",");
		List<Integer> daysDisabled = new ArrayList<>();
		int dayStartNr = Integer.parseInt(dayPeriod[0]);
		int dayEndNr = Integer.parseInt(dayPeriod[1]);
		
		for(int x=0; x<=6; x++) {
			if(dayStartNr > x || x > dayEndNr) {
				daysDisabled.add(x);	
			}
		}
		return daysDisabled;
	}
	
	/**
	 * Returns a string representing the starting time of business and formatted
	 * like: "18:30"
	 *  
	 * @return business end-time array 
	 */
	public String getBusinessStartTime() {
		return getBusinessTimes().split(",")[0];
	}

	/**
	 * Returns an array containing 2 elements representing hours and minutes in
	 * format ["hh","mm"].
	 * 
	 * Examples:
	 * 	["08","30"] => represents 08:30
	 *  
	 * @return business start-time array 
	 */
	public String[] getBusinessStartTimes() {
		return getBusinessStartTime().trim().split(":");
	}
	
	/**
	 * Returns a string representing the starting time of business and formatted
	 * like: "18:30"
	 *  
	 * @return business end-time array 
	 */
	public String getBusinessEndTime() {
		return getBusinessTimes().split(",")[1];
	}
	
	/**
	 * Returns an array containing 2 elements representing hours and minutes in
	 * format ["hh","mm"].
	 * 
	 * Examples:
	 * 	["18","30"] => represents 18:30 
	 *  
	 * @return business end-time array 
	 */
	public String[] getBusinessEndTimes() {
		return getBusinessEndTime().trim().split(":");
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

	/**
	 * @return businessId Long
	 */
	public Long getBusinessId() {
		return businessId;
	}

	/**
	 * @param businessId Long
	 */
	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	/**
	 * @return the subscriptions
	 */
	public Set<SubscriptionHistory> getSubscriptionHistorySet() {
		return subscriptionHistorySet;
	}

	/**
	 * @param subscriptionHistorySet the subscriptions to set
	 */
	public void setSubscriptionHistorySet(Set<SubscriptionHistory> subscriptionHistorySet) {
		this.subscriptionHistorySet = subscriptionHistorySet;
	}

	/**
	 * @return
	 */
	@JsonIgnore
	public Subscription getActiveSubscription() {
		Optional<SubscriptionHistory> subscriptionHistory = this.getSubscriptionHistorySet()
				.stream()
				.filter(subscription -> subscription.isActive()
						&& subscription.getSubscriptionEndDate().isAfter(LocalDateTime.now()))
				.findFirst();

		if(subscriptionHistory.isPresent()) {
			return subscriptionHistory.get().getSubscription();
		}else {
			return null;
		}
	}
}
