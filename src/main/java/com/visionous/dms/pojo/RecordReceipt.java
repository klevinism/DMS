/**
 * 
 */
package com.visionous.dms.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.visionous.dms.configuration.helpers.DmsCore;

/**
 * @author delimeta
 *
 */
@Entity
@Table(name="record_receipt")
public class RecordReceipt implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = DmsCore.SERIAL_VERSION_UID;
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RECORD_RECEIPT_SEQ")
    @SequenceGenerator(sequenceName = "record_receipt_seq", allocationSize = 1, name = "RECORD_RECEIPT_SEQ")
	private Long id;
	
	private boolean payed;
	
	private double total;
	
	private double vat;

	@JsonIgnore
	@OneToOne(mappedBy = "receipt", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	private Record record;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="recordReceiptId")
	private List<RecordReceiptItem> receiptItems = new ArrayList<>();
	
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
	 * @return the payed
	 */
	public boolean isPayed() {
		return payed;
	}

	/**
	 * @param payed the payed to set
	 */
	public void setPayed(boolean payed) {
		this.payed = payed;
	}

	/**
	 * @return the total
	 */
	public double getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(double total) {
		this.total = total;
	}
	
	/**
	 * @return the vat
	 */
	public double getVat() {
		return vat;
	}

	/**
	 * @param vat the vat to set
	 */
	public void setVat(double vat) {
		this.vat = vat;
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
	 * @return the receiptItems
	 */
	public List<RecordReceiptItem> getReceiptItems() {
		return receiptItems;
	}

	/**
	 * @param receiptItems the receiptItems to set
	 */
	public void setReceiptItems(List<RecordReceiptItem> receiptItems) {
		this.receiptItems = receiptItems;
	}

	/**
	 * @param newRecordReceiptItem
	 */
	public void addReceiptItem(RecordReceiptItem newRecordReceiptItem) {
		this.receiptItems.add(newRecordReceiptItem);
	}

	@Override
	public String toString() {
		return "RecordReceipt [id=" + id + ", payed=" + payed + ", total=" + total + ", vat=" + vat + ", record="
				+ record + ", receiptItems=" + receiptItems + "]";
	}


	
	
	
}
