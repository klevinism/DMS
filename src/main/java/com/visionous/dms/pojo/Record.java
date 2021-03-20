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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.visionous.dms.configuration.helpers.DmsCore;

/**
 * @author delimeta
 *
 */
@Entity
public class Record  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = DmsCore.SERIAL_VERSION_UID;
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RECORD_SEQ")
    @SequenceGenerator(sequenceName = "record_seq", allocationSize = 1, name = "RECORD_SEQ")
	private Long id;
	
	@Column(name = "serviceid", insertable = false, updatable =false)
	private Long serviceId;
	
	@Column(name = "personnelid", insertable = false, updatable =false)
	private Long personnelId;
	
	@Column(name = "receiptid", insertable = false, updatable =false)
	private Long receiptId;
	
	private String servicedetail;
	
	private String servicecomment;
	
	private String attachments;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE)
	private Date servicedate;
	
	@Column(name = "historyid", insertable = false, updatable = false)
	private Long historyId;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL,  optional = false)
	@JoinColumn(name="historyid")
	private History history;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name="personnelid")
	private Personnel personnel;
	
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER) 
	@JoinColumn(name = "serviceid")
	private ServiceType serviceType;

	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER) 
	@JoinColumn(name = "receiptid")
	private RecordReceipt receipt;
	
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.REFRESH) 
    @JoinTable(name = "record_tooth",
            joinColumns = @JoinColumn(name = "record_id"),
            inverseJoinColumns = @JoinColumn(name = "tooth_id"))
    private List<Teeth> visitedTeeth  = new ArrayList<>();
	
	/**
	 * 
	 */
	public Record() {
	}
	
	/**
	 * @param history
	 * @param personnel
	 */
	public Record(History history, Personnel personnel) {
		this.history = history;
		this.historyId = history.getId();
		this.personnel = personnel;
		this.personnelId = personnel.getId();
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
	 * @return the historyId
	 */
	public Long getHistoryId() {
		return historyId;
	}

	/**
	 * @param historyId the historyId to set
	 */
	public void setHistoryId(Long historyId) {
		this.historyId = historyId;
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
	 * @return the servicecomment
	 */
	public String getServicecomment() {
		return servicecomment;
	}

	/**
	 * @param servicecomment the servicecomment to set
	 */
	public void setServicecomment(String servicecomment) {
		this.servicecomment = servicecomment;
	}
	
	/**
	 * @return the servicedetail
	 */
	public String getServicedetail() {
		return servicedetail;
	}

	/**
	 * @param servicedetail the servicedetail to set
	 */
	public void setServicedetail(String servicedetail) {
		this.servicedetail = servicedetail;
	}

	/**
	 * @return the servicedate
	 */
	public Date getServicedate() {
		return servicedate;
	}

	/**
	 * @param servicedate the servicedate to set
	 */
	public void setServicedate(Date servicedate) {
		this.servicedate = servicedate;
	}

	/**
	 * @return the history
	 */
	public History getHistory() {
		return history;
	}

	/**
	 * @param history the history to set
	 */
	public void setHistory(History history) {
		this.history = history;
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

	/**
	 * @return the serviceId
	 */
	public Long getServiceId() {
		return serviceId;
	}

	/**
	 * @param serviceId the serviceId to set
	 */
	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * @return the serviceType
	 */
	public ServiceType getServiceType() {
		return serviceType;
	}

	/**
	 * @param serviceType the serviceType to set
	 */
	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	/**
	 * @return the receiptId
	 */
	public Long getReceiptId() {
		return receiptId;
	}

	/**
	 * @param receiptId the receiptId to set
	 */
	public void setReceiptId(Long receiptId) {
		this.receiptId = receiptId;
	}

	/**
	 * @return the receipt
	 */
	public RecordReceipt getReceipt() {
		return receipt;
	}

	/**
	 * @param receipt the receipt to set
	 */
	public void setReceipt(RecordReceipt receipt) {
		this.receipt = receipt;
	}

	/**
	 * @return the attachments
	 */
	public String getAttachments() {
		return attachments;
	}

	/**
	 * @param attachments the attachments to set
	 */
	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}

	/**
	 * @return the visitedTeeth
	 */
	public List<Teeth> getVisitedTeeth() {
		return visitedTeeth;
	}

	/**
	 * @param visitedTeeth the visitedTeeth to set
	 */
	public void setVisitedTeeth(List<Teeth> visitedTeeth) {
		this.visitedTeeth = visitedTeeth;
	}

	/**
	 * @param tooth
	 */
	public void addVisitedTooth(Teeth tooth) {
		this.visitedTeeth.add(tooth);
	}
	
	
}
