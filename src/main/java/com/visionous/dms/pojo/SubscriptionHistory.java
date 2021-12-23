/**
 * 
 */
package com.visionous.dms.pojo;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.visionous.dms.configuration.helpers.DmsCore;

/**
 * @author delimeta
 *
 */
@Entity
public class SubscriptionHistory implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = DmsCore.SERIAL_VERSION_UID;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUBSCRIPTION_HISTORY_SEQ")
    @SequenceGenerator(sequenceName = "subscription_history_seq", allocationSize = 1, name = "SUBSCRIPTION_HISTORY_SEQ")    
    private Long id;
	
	@Column(name = "subscription_id", updatable = false, insertable = false)
	private Long subscriptionId;
	
	@Column(name = "global_settings_id", updatable = false, insertable = false)
	private Long globalSettingsId;
	
	@Column(name = "business_id", updatable = false, insertable = false)
	private Long businessId;
	
	@Column(name = "subscription_start_date")
	private LocalDateTime subscriptionStartDate;
	
	@Column(name = "subscription_end_date")
	private LocalDateTime subscriptionEndDate;

	@DateTimeFormat (pattern="dd-MM-YYYY")
	private LocalDateTime addeddate;
	
	private boolean active;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL,  optional = false)
	@JoinColumn(name="global_settings_id")
	private GlobalSettings globalSettings;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH )
	@JoinColumn(name = "subscription_id")
	private Subscription subscription;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.REFRESH )
	@JoinColumn(name = "business_id")
	private Business business;

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
	 * @return the subscriptionStartDate
	 */
	public LocalDateTime getSubscriptionStartDate() {
		return subscriptionStartDate;
	}

	/**
	 * @param subscriptionStartDate the subscriptionStartDate to set
	 */
	public void setSubscriptionStartDate(LocalDateTime subscriptionStartDate) {
		this.subscriptionStartDate = subscriptionStartDate;
	}

	/**
	 * @return the subscriptionEndDate
	 */
	public LocalDateTime getSubscriptionEndDate() {
		return subscriptionEndDate;
	}

	/**
	 * @param subscriptionEndDate the subscriptionEndDate to set
	 */
	public void setSubscriptionEndDate(LocalDateTime subscriptionEndDate) {
		this.subscriptionEndDate = subscriptionEndDate;
	}

	/**
	 * @return the addeddate
	 */
	public LocalDateTime getAddeddate() {
		return addeddate;
	}

	/**
	 * @param addeddate the addeddate to set
	 */
	public void setAddeddate(LocalDateTime addeddate) {
		this.addeddate = addeddate;
	}
	
	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return the globalSettings
	 */
	public GlobalSettings getGlobalSettings() {
		return globalSettings;
	}

	/**
	 * @param globalSettings the globalSettings to set
	 */
	public void setGlobalSettings(GlobalSettings globalSettings) {
		this.globalSettings = globalSettings;
	}

	/**
	 * @return the subscription
	 */
	public Subscription getSubscription() {
		return subscription;
	}

	/**
	 * @param subscription the subscription to set
	 */
	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}

	/**
	 * @return
	 */
	public Business getBusiness() {
		return business;
	}

	/**
	 * @param business
	 */
	public void setBusiness(Business business) {
		this.business = business;
	}
	
}
