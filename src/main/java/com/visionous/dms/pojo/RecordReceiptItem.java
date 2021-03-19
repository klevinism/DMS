/**
 * 
 */
package com.visionous.dms.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.visionous.dms.configuration.helpers.DmsCore;

/**
 * @author delimeta
 *
 */

@Entity
@Table(name="record_receipt_item")
public class RecordReceiptItem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = DmsCore.SERIAL_VERSION_UID;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RECORD_RECEIPT_ITEM_SEQ")
    @SequenceGenerator(sequenceName = "record_receipt_item_seq", allocationSize = 1, name = "RECORD_RECEIPT_ITEM_SEQ")
	private Long id;
	
	@Column(name = "servicetypeid", insertable = false, updatable =false)
	private Long serviceTypeId;
	
	@Column(name = "receiptid", insertable = false, updatable =false)
	private Long recordReceiptId;
	
	@Column(name = "amount")
	private int amount;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="servicetypeid", nullable=false)
	private ServiceType serviceType;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="receiptid", nullable=false)
	private RecordReceipt receipt;

	/**
	 * 
	 */
	public RecordReceiptItem() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param receipt
	 * @param serviceType
	 */
	public RecordReceiptItem(RecordReceipt receipt, ServiceType serviceType) {
		this.recordReceiptId = receipt.getId();
		this.serviceTypeId = serviceType.getId();
		this.receipt = receipt;
		this.serviceType = serviceType;
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
	 * @return the serviceTypeId
	 */
	public Long getServiceTypeId() {
		return serviceTypeId;
	}

	/**
	 * @param serviceTypeId the serviceTypeId to set
	 */
	public void setServiceTypeId(Long serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	/**
	 * @return the recordReceiptId
	 */
	public Long getRecordReceiptId() {
		return recordReceiptId;
	}

	/**
	 * @param recordReceiptId the recordReceiptId to set
	 */
	public void setRecordReceiptId(Long recordReceiptId) {
		this.recordReceiptId = recordReceiptId;
	}

	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(int amount) {
		this.amount = amount;
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
}
