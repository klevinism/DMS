/**
 * 
 */
package com.visionous.dms.pojo;

import java.io.Serializable;
import java.util.Date;

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

import com.visionous.dms.configuration.helpers.DmsCoreVersion;

/**
 * @author delimeta
 *
 */
@Entity
public class Record  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = DmsCoreVersion.SERIAL_VERSION_UID;
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RECORD_SEQ")
    @SequenceGenerator(sequenceName = "record_seq", allocationSize = 1, name = "RECORD_SEQ")
	private Long id;
	
	@Column(name = "serviceid", insertable = false, updatable =false)
	private Long serviceId;
	
	@Column(name = "personnelid", insertable = false, updatable =false)
	private Long personnelId;
	
	private String servicedetail;
	
	private String servicecomment;
	
	private String attachments;
	
	@DateTimeFormat (pattern="dd-MMM-YYYY")
	private Date servicedate;
	
	@Column(name = "historyid", insertable = false, updatable = false)
	private Long historyId;
	
	@Column(name = "toothid", insertable = false, updatable = false)
	private Long toothId;
	
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
	@JoinColumn(name = "toothid")
	private Teeth tooth;
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
	 * @return the toothId
	 */
	public Long getToothId() {
		return toothId;
	}

	/**
	 * @param toothId the toothId to set
	 */
	public void setToothId(Long toothId) {
		this.toothId = toothId;
	}

	/**
	 * @return the tooth
	 */
	public Teeth getTooth() {
		return tooth;
	}

	/**
	 * @param tooth the tooth to set
	 */
	public void setTooth(Teeth tooth) {
		this.tooth = tooth;
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

}
