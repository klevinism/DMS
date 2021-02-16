/**
 * 
 */
package com.visionous.dms.service;

import java.util.Optional;

import com.visionous.dms.pojo.Questionnaire;

/**
 * @author delimeta
 *
 */
public interface IQuestionnaireService {
	Optional<Questionnaire> findByCustomerId(Long customerId);

	/**
	 * @param questionnaire
	 */
	void delete(Questionnaire questionnaire);
}
