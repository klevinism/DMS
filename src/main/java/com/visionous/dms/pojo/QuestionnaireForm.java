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
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;

import com.visionous.dms.configuration.helpers.DmsCore;

/**
 * @author delimeta
 *
 */
@Entity
@Table(name="questionnaire_form")
public class QuestionnaireForm implements Serializable{

	private static final long serialVersionUID = DmsCore.SERIAL_VERSION_UID;
	
	@Id
	private Long id;
	
	private String question;
	
	@Column(name="multiple_choice")
	private boolean multipleChoice;
	
	@Column(name="addeddate")
	@DateTimeFormat (pattern="dd-MM-YYYY")
	private Date addedDate;
	
	@Valid
	@OneToMany(mappedBy = "questionForm",cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
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
	 * @return the question
	 */
	public String getQuestion() {
		return question;
	}


	/**
	 * @param question the question to set
	 */
	public void setQuestion(String question) {
		this.question = question;
	}


	/**
	 * @return the multipleChoice
	 */
	public boolean isMultipleChoice() {
		return multipleChoice;
	}


	/**
	 * @param multipleChoice the multipleChoice to set
	 */
	public void setMultipleChoice(boolean multipleChoice) {
		this.multipleChoice = multipleChoice;
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

	
}
