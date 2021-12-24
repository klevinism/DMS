/**
 * 
 */
package com.visionous.dms.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.visionous.dms.configuration.helpers.DmsCore;

/**
 * @author delimeta
 *
 */
@Entity
@Table(name="service_type")
public class ServiceType implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = DmsCore.SERIAL_VERSION_UID;
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SERVICE_TYPE_SEQ")
    @SequenceGenerator(sequenceName = "service_type_seq", allocationSize = 1, name = "SERVICE_TYPE_SEQ")
	private Long id;
	
	private String name;

	@DateTimeFormat (pattern="dd-MM-YYYY")
	private Date addeddate;
	
	private int price;
	
	@Column(name="global_settings_id", insertable = false, updatable = false)
	private Long globalSettingsId;
	
    @JsonIgnore
	@OneToOne(mappedBy = "serviceType", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	private Record record;
    
    @JsonIgnore
    @OneToMany(mappedBy="serviceType", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private List<Appointment> appointment = new ArrayList<>();
	
    @JsonIgnore
	@OneToMany(cascade = CascadeType.REFRESH, mappedBy="serviceType")
	private List<RecordReceiptItem> receipts = new ArrayList<>();
    
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "global_settings_id")
	private GlobalSettings globalSettings;
	
	/**
	 * 
	 */
	public ServiceType() {
		// TODO Auto-generated constructor stub
	}
	
	public ServiceType(String name) {
		this.name = name;
		this.addeddate = new Date();
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
	 * @return
	 */
	public Long getGlobalSettingsId() {
		return globalSettingsId;
	}

	/**
	 * @param businessSettingsId
	 */
	public void setGlobalSettingsId(Long globalSettingsId) {
		this.globalSettingsId = globalSettingsId;
	}

	/**
	 * @return the record
	 */
	public Record getRecord() {
		return record;
	}

	/**
	 * @param record the record to set
	 */
	public void setRecord(Record record) {
		this.record = record;
	}

	/**
	 * @return the appointment
	 */
	public List<Appointment> getAppointment() {
		return appointment;
	}

	/**
	 * @param appointment the appointment to set
	 */
	public void setAppointment(List<Appointment> appointment) {
		this.appointment = appointment;
	}

	/**
	 * @return the receipts
	 */
	public List<RecordReceiptItem> getReceipts() {
		return receipts;
	}

	/**
	 * @param receipts the receipts to set
	 */
	public void setReceipts(List<RecordReceiptItem> receipts) {
		this.receipts = receipts;
	}

	/**
	 * @return
	 */
	public GlobalSettings getGlobalSettings() {
		return this.globalSettings;
	}

	/**
	 * @param globalSettings
	 */
	public void setGlobalSettings(GlobalSettings globalSettings) {
		this.globalSettings = globalSettings;
	}
	
}
