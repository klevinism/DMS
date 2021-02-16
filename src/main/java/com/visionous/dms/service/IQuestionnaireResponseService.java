/**
 * 
 */
package com.visionous.dms.service;

import java.util.List;

import com.visionous.dms.pojo.QuestionnaireResponse;

/**
 * @author delimeta
 *
 */
public interface IQuestionnaireResponseService {
	List<QuestionnaireResponse> findAllByQuestionIdAndResponse(Long questionId, String response);
}
