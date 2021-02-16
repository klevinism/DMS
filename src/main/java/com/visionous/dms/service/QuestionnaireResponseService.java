/**
 * 
 */
package com.visionous.dms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visionous.dms.pojo.QuestionnaireResponse;
import com.visionous.dms.repository.QuestionnaireResponseRepository;

/**
 * @author delimeta
 *
 */
@Service
public class QuestionnaireResponseService implements IQuestionnaireResponseService{

	private QuestionnaireResponseRepository questionnaireResponseRepository;
	
	/**
	 * 
	 */
	@Autowired
	public QuestionnaireResponseService(QuestionnaireResponseRepository questionnaireResponseRepository) {
		this.questionnaireResponseRepository = questionnaireResponseRepository;
	}

	@Override
	public List<QuestionnaireResponse> findAllByQuestionIdAndResponse(Long questionId, String response) {
		return this.questionnaireResponseRepository.findAllByQuestionnaireIdAndResponse(questionId, response);
	}

	/**
	 * @param questionnaireResponse
	 * @return
	 */
	public List<QuestionnaireResponse> createBatch(List<QuestionnaireResponse> questionnaireResponse) {
		return this.questionnaireResponseRepository.saveAll(questionnaireResponse);
	}
	
	
	
}
