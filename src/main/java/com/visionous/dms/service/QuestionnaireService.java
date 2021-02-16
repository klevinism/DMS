/**
 * 
 */
package com.visionous.dms.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visionous.dms.pojo.Questionnaire;
import com.visionous.dms.pojo.QuestionnaireResponse;
import com.visionous.dms.repository.QuestionnaireRepository;

/**
 * @author delimeta
 *
 */
@Service
public class QuestionnaireService implements IQuestionnaireService{
	private QuestionnaireRepository questionnaireRepository;
	private QuestionnaireResponseService questionnaireResponseService;
	/**
	 * 
	 */
	@Autowired
	public QuestionnaireService(QuestionnaireRepository questionnaireRepository, 
			QuestionnaireResponseService questionnaireResponseService) {
		this.questionnaireRepository = questionnaireRepository;
		this.questionnaireResponseService = questionnaireResponseService;
	}

	@Override
	public Optional<Questionnaire> findByCustomerId(Long customerId) {
		return this.questionnaireRepository.findByCustomerId(customerId);
	}

	/**
	 * @param questionnaire
	 */
	@Override
	public void delete(Questionnaire questionnaire) {
		this.questionnaireRepository.delete(questionnaire);
	}

	/**
	 * @param questionnaire
	 */
	public Questionnaire create(Questionnaire questionnaire) {
		return this.questionnaireRepository.saveAndFlush(questionnaire);
	}

	/**
	 * @param questionnaire
	 */
	public Questionnaire createQuestionnaireWithResponses(Questionnaire questionnaire) {
		// Create empty questionnaire, without responses
		Questionnaire newQuestionnaire = this.create(questionnaire);
		
		
		
		// Map new responses to newQuestionnaire		
		List<QuestionnaireResponse> responses = questionnaire.getQuestionnaireResponse().stream().map(response -> {
			response.setId(null);
			response.setResponseDate(new Date());
			response.setQuestionId(response.getQuestionForm().getId());
			response.setQuestionnaire(newQuestionnaire);
			return response;
		}).collect(Collectors.toList());
	
		newQuestionnaire.setQuestionnaireResponse(
				this.questionnaireResponseService.createBatch(responses)
				);
		
		return newQuestionnaire;
	}
	
	
}
