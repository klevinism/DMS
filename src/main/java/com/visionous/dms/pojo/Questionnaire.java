/**
 * 
 */
package com.visionous.dms.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.visionous.dms.configuration.helpers.DmsCore;

/**
 * @author delimeta
 *
 */
@Entity
public class Questionnaire implements Serializable{

	private static final long serialVersionUID = DmsCore.SERIAL_VERSION_UID;
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QUESTIONNAIRE_SEQ")
    @SequenceGenerator(sequenceName = "questionnaire_seq", allocationSize = 1, name = "QUESTIONNAIRE_SEQ")
	private Long id;
	
	@Column(name="customerid", insertable = false, updatable =false)
	private Long customerId;

	@NotEmpty(message = "{alert.fieldEmpty}")
	@Column(columnDefinition="TEXT")
	private String signatureString;
	
	@NotEmpty(message = "{alert.fieldEmpty}")
	@Column(columnDefinition="TEXT")
	private String signatureImgPath;
	
	@NotNull(message = "{alert.fieldEmpty}")
	@Column(updatable = false)
	private boolean consented = Boolean.FALSE;
	
	@Column(name="addeddate")
	@DateTimeFormat (pattern="dd-MM-YYYY")
	private Date addedDate;
	
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name="customerid")
	private Customer customer;

	@Valid
	@OneToMany(mappedBy = "questionnaire", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private List<QuestionnaireResponse> questionnaireResponse;
	
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
	 * @return the customerId
	 */
	public Long getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
	/**
	 * @return
	 */
	public String getSignatureString() {
		return signatureString;
	}

	/**
	 * @param signatureString
	 */
	public void setSignatureString(String signatureString) {
		this.signatureString = signatureString;
	}

	/**
	 * @return
	 */
	public String getSignatureImgPath() {
		return signatureImgPath;
	}

	/**
	 * @param signatureImgPath
	 */
	public void setSignatureImgPath(String signatureImgPath) {
		this.signatureImgPath = signatureImgPath;
	}

	/**
	 * @return
	 */
	public boolean isConsented() {
		return consented;
	}

	/**
	 * @param consented
	 */
	public void setConsented(boolean consented) {
		this.consented = consented;
	}

	/**
	 * @return the addedDate
	 */
	public Date getAddedDate() {
		return addedDate;
	}

	/**
	 * @param addedDate the addedDate to set
	 */
	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}
	
	/**
	 * @return the questionnaireResponse
	 */
	public List<QuestionnaireResponse> getQuestionnaireResponse() {
		return questionnaireResponse;
	}

	/**
	 * @param questionnaireResponse the questionnaireResponse to set
	 */
	public void setQuestionnaireResponse(List<QuestionnaireResponse> questionnaireResponse) {
		this.questionnaireResponse = questionnaireResponse;
	}
	
	/**
	 * @param questionnaireResponse
	 */
	public void addQuestionnaireResponse(QuestionnaireResponse questionnaireResponse) {
		this.questionnaireResponse.add(questionnaireResponse);
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

}
