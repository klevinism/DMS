/**
 * 
 */
package com.visionous.dms.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
	private Long id;
	
	private String servicetype;
	
	private String servicecomment;
	
	private Date servicedate;
	
	@Column(name = "historyid", insertable = false, updatable = false)
	private Long historyId;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name="historyid")
	private History history;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name="personnelid")
	private Personnel personnel;

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
	 * @return the servicetype
	 */
	public String getServicetype() {
		return servicetype;
	}

	/**
	 * @param servicetype the servicetype to set
	 */
	public void setServicetype(String servicetype) {
		this.servicetype = servicetype;
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
	
}
