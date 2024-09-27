/**
 * 
 */
package com.visionous.dms.pojo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.*;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.visionous.dms.configuration.helpers.DmsCore;

/**
 * @author delimeta
 *
 */
@Entity
@Table(name="subscription_list")
public class Subscription implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = DmsCore.SERIAL_VERSION_UID;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUBSCRIPTION_LIST_SEQ")
    @SequenceGenerator(sequenceName = "subscription_list_seq", allocationSize = 1, name = "SUBSCRIPTION_LIST_SEQ")    
    private Long id;
	
	private String name;
	
	@Column(length=5000)
	private String description;
	
	private int price;
	
	private boolean active = false;
	
	@Column(name="image_url")
	private String imageUrl;
	
	@Column(name="action_url")
	private String actionUrl;

	@Column(name="info_url")
	private String infoUrl;
	
	@DateTimeFormat (pattern="dd-MM-YYYY")
	private LocalDateTime addeddate;
	
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.REFRESH) 
    @JoinTable(name = "subscription_restrictions",
            joinColumns = @JoinColumn(name = "subscriptionid"),
            inverseJoinColumns = @JoinColumn(name = "restrictionid"))
    private List<Restrictions> restrictions  = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy="subscription",  fetch = FetchType.LAZY)
    private List<SubscriptionHistory> subscriptionHistory;

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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(int price) {
		this.price = price;
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
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * @param imageUrl the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	/**
	 * @return the actionUrl
	 */
	public String getActionUrl() {
		return actionUrl;
	}

	/**
	 * @param actionUrl the actionUrl to set
	 */
	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}

	/**
	 * @return the infoUrl
	 */
	public String getInfoUrl() {
		return infoUrl;
	}

	/**
	 * @param infoUrl the infoUrl to set
	 */
	public void setInfoUrl(String infoUrl) {
		this.infoUrl = infoUrl;
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
	 * @return the restrictions
	 */
	public List<Restrictions> getRestrictions() {
		return restrictions;
	}

	/**
	 * @param restrictions the restrictions to set
	 */
	public void setRestrictions(List<Restrictions> restrictions) {
		this.restrictions = restrictions;
	}

	/**
	 * @return the subscriptionHistory
	 */
	public List<SubscriptionHistory> getSubscriptionHistory() {
		return subscriptionHistory;
	}

	/**
	 * @param subscriptionHistory the subscriptionHistory to set
	 */
	public void setSubscriptionHistory(List<SubscriptionHistory> subscriptionHistory) {
		this.subscriptionHistory = subscriptionHistory;
	}
	
	/**
	 * @param pageName
	 * @return true if {@link Restrictions} List contains filtered page name.  
	 */
	public boolean hasRestrictionsByPageName(String pageName) {
		return !this.getRestrictionsByPageName(pageName).isEmpty();
	}
	
	
	/**
	 * @param pageName
	 * @return List {@link Restrictions} filtered by page name; 
	 */
	public List<Restrictions> getRestrictionsByPageName(String pageName) {
		return this.getRestrictions().parallelStream().filter(restriction -> restriction.getRestrictionPage().contains(pageName)).collect(Collectors.toList());
	}
	
	/**
	 * @param pageName
	 * @return The total restriction amount filtered by page name.
	 */
	public int getSumOfRestrictionsAmountByPageName(String pageName) {
		return this.getRestrictionsByPageName(pageName).stream()
		.map(restriction -> restriction.getRestrictionAmount())
		.reduce(0, Integer::sum);
	}
	
	/**
	 * @param subscription
	 */
	public void setSubscription(Subscription subscription) {
		this.id = subscription.id;
		this.actionUrl = subscription.actionUrl;
		this.active = subscription.active;
		this.addeddate = subscription.addeddate;
		this.description = subscription.description;
		this.imageUrl = subscription.imageUrl;
		this.infoUrl = subscription.infoUrl;
		this.name = subscription.name;
		this.price = subscription.price;
		this.restrictions = subscription.restrictions;
		this.subscriptionHistory = subscription.subscriptionHistory;
	}
}