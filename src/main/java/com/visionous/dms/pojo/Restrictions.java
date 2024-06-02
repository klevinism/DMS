/**
 * 
 */
package com.visionous.dms.pojo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.visionous.dms.configuration.helpers.DmsCore;

/**
 * @author delimeta
 *
 */
@Entity
public class Restrictions implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = DmsCore.SERIAL_VERSION_UID;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RESTRICTIONS_SEQ")
    @SequenceGenerator(sequenceName = "restrictions_seq", allocationSize = 1, name = "RESTRICTIONS_SEQ")    
    private Long id;
	

	private String name;
	
	private String description;
	
	@Column(name = "restriction_page")
	private String restrictionPage;
	
	@Column(name = "restriction_type")
	private String restrictionType;
	
	@Column(name = "restriction_amount")
	private Integer restrictionAmount;
	
	@DateTimeFormat (pattern="dd-MM-YYYY")
	private LocalDateTime addeddate;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
    @ManyToMany(mappedBy = "restrictions", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private List<Subscription> subscriptions;

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
	 * @return the restrictionPage
	 */
	public String getRestrictionPage() {
		return restrictionPage;
	}

	/**
	 * @param restrictionPage the restrictionPage to set
	 */
	public void setRestrictionPage(String restrictionPage) {
		this.restrictionPage = restrictionPage;
	}

	/**
	 * @return the restrictionType
	 */
	public String getRestrictionType() {
		return restrictionType;
	}

	/**
	 * @param restrictionType the restrictionType to set
	 */
	public void setRestrictionType(String restrictionType) {
		this.restrictionType = restrictionType;
	}

	/**
	 * @return the restrictionAmount
	 */
	public Integer getRestrictionAmount() {
		return restrictionAmount;
	}

	/**
	 * @param restrictionAmount the restrictionAmount to set
	 */
	public void setRestrictionAmount(Integer restrictionAmount) {
		this.restrictionAmount = restrictionAmount;
	}

	/**
	 * @return the subscriptions
	 */
	public List<Subscription> getSubscriptions() {
		return subscriptions;
	}

	/**
	 * @param subscriptions the subscriptions to set
	 */
	public void setSubscriptions(List<Subscription> subscriptions) {
		this.subscriptions = subscriptions;
	}
	
}
