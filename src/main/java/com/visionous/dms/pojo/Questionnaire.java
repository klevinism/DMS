/**
 * 
 */
package com.visionous.dms.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;

import org.springframework.format.annotation.DateTimeFormat;

import com.visionous.dms.configuration.helpers.DmsCoreVersion;

/**
 * @author delimeta
 *
 */
@Entity
public class Questionnaire implements Serializable{

	private static final long serialVersionUID = DmsCoreVersion.SERIAL_VERSION_UID;
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QUESTIONNAIRE_SEQ")
    @SequenceGenerator(sequenceName = "questionnaire_seq", allocationSize = 1, name = "QUESTIONNAIRE_SEQ")
	private Long id;
	
	@Column(name="customerid", insertable = false, updatable =false)
	private Long customerId;

	@Column(name="addeddate")
	@DateTimeFormat (pattern="dd-MM-YYYY")
	private Date addedDate;
	
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name="customerid")
	private Customer customer;

	@OneToMany(mappedBy = "questionnaire", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
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
