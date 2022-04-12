package com.visionous.dms.pojo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.visionous.dms.configuration.helpers.DmsCore;
import com.visionous.dms.configuration.helpers.annotations.ValidURL;

import net.bytebuddy.implementation.bind.annotation.Empty;

/**
 * @author delimeta
 *
 */
@Entity
public class Business implements Serializable{
    
    /**
	 *  
	 */
	private static final long serialVersionUID = DmsCore.SERIAL_VERSION_UID;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BUS_SEQ")
    @SequenceGenerator(sequenceName = "business_seq", allocationSize = 1, name = "BUS_SEQ")
    private Long id;

	@NotEmpty
	private String name;
	
	@NotEmpty
	@Column(name = "subdomain_uri")
	private String subdomainUri;
	
	@ValidURL
	@Column(name = "business_url")
	private String businessUrl;
	
    @OneToOne(mappedBy="business",  fetch = FetchType.LAZY)
    private GlobalSettings globalSettings;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany
    @JoinTable(name = "business_accounts",
            joinColumns = @JoinColumn(name = "businessid"),
            inverseJoinColumns = @JoinColumn(name = "accountid"))
    private Set<Account> accounts;
    
    @OneToMany(mappedBy="business",fetch = FetchType.EAGER)
    private Set<SubscriptionHistory> subscriptionHistory;
    
    private boolean enabled;
    
	/**
	 * @return id Long
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id Long
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return name String
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name String
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return businessUrl String
	 */
	public String getBusinessUrl() {
		return businessUrl;
	}

	/**
	 * @param businessUrl String
	 */
	public void setBusinessUrl(String businessUrl) {
		this.businessUrl = businessUrl;
	}

	/**
	 * @return globalSettings GlobalSettings
	 */
	public GlobalSettings getGlobalSettings() {
		return globalSettings;
	}

	/**
	 * @param globalSettings GlobalSettings
	 */
	public void setGlobalSettings(GlobalSettings globalSettings) {
		this.globalSettings = globalSettings;
	}

	/**
	 * @return
	 */
	public Set<Account> getAccounts() {
		return accounts;
	}

	/**
	 * @param accounts
	 */
	public void setAccounts(Set<Account> accounts) {
		this.accounts = accounts;
	}

	/**
	 * @return
	 */
	public Set<SubscriptionHistory> getSubscriptionHistory() {
		return subscriptionHistory;
	}

	/**
	 * @param subscriptionHistory
	 */
	public void setSubscriptionHistory(Set<SubscriptionHistory> subscriptionHistory) {
		this.subscriptionHistory = subscriptionHistory;
	}
	
	
	/**
	 * @return enabled boolean
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled boolean
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	/**
	 * @return subdomainUri String
	 */
	public String getSubdomainUri() {
		return subdomainUri;
	}

	/**
	 * @param subdomainUri String
	 */
	public void setSubdomainUri(String subdomainUri) {
		this.subdomainUri = subdomainUri;
	}

	/**
	 * @return
	 */
	@JsonIgnore
	public Subscription getActiveSubscription() {
		Optional<SubscriptionHistory> subscriptionHistory = this.getSubscriptionHistory()
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