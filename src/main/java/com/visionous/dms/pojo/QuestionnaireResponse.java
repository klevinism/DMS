/**
 * 
 */
package com.visionous.dms.pojo;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

import com.visionous.dms.configuration.helpers.DmsCoreVersion;

/**
 * @author delimeta
 *
 */
@Entity
public class QuestionnaireResponse implements Serializable{

	private static final long serialVersionUID = DmsCoreVersion.SERIAL_VERSION_UID;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QUESTIONNAIRE_RESPONSE_SEQ")
    @SequenceGenerator(sequenceName = "questionnaire_response_seq", allocationSize = 1, name = "QUESTIONNAIRE_RESPONSE_SEQ")
	private Long id;
	
	@Column(name="questionid", insertable = false, updatable = false)
	private Long questionId;
	
	@NotEmpty(message = "{alert.fieldEmpty}")
	private String response;
	
	@Column(name="questionnaireid", insertable = false, updatable = false)
	private Long questionnaireId;

	@Column(name="responsedate")
	@DateTimeFormat (pattern="dd-MM-YYYY")
	private Date responseDate;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name="questionid")
	private QuestionnaireForm questionForm;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name="questionnaireid")
	private Questionnaire questionnaire;
		
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
	 * @return the questionId
	 */
	public Long getQuestionId() {
		return questionId;
	}

	/**
	 * @param questionId the questionId to set
	 */
	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	/**
	 * @return the response
	 */
	public String getResponse() {
		return response;
	}

	/**
	 * @param response the response to set
	 */
	public void setResponse(String response) {
		this.response = response;
	}

	/**
	 * @return the questionnaireId
	 */
	public Long getQuestionnaireId() {
		return questionnaireId;
	}

	/**
	 * @param questionnaireId the questionnaireId to set
	 */
	public void setQuestionnaireId(Long questionnaireId) {
		this.questionnaireId = questionnaireId;
	}

	/**
	 * @return the responseDate
	 */
	public Date getResponseDate() {
		return responseDate;
	}

	/**
	 * @param responseDate the responseDate to set
	 */
	public void setResponseDate(Date responseDate) {
		this.responseDate = responseDate;
	}
	
	
	/**
	 * @return the question
	 */
	public QuestionnaireForm getQuestionForm() {
		return questionForm;
	}

	/**
	 * @param question the question to set
	 */
	public void setQuestionForm(QuestionnaireForm questionForm) {
		this.questionForm = questionForm;
	}

	/**
	 * @return the questionnaire
	 */
	public Questionnaire getQuestionnaire() {
		return questionnaire;
	}

	/**
	 * @param questionnaire the questionnaire to set
	 */
	public void setQuestionnaire(Questionnaire questionnaire) {
		this.questionnaire = questionnaire;
	}
	
}
