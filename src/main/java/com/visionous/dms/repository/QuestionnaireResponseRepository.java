/**
 * 
 */
package com.visionous.dms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visionous.dms.pojo.QuestionnaireResponse;

/**
 * @author delimeta
 *
 */
@Repository
public interface QuestionnaireResponseRepository extends JpaRepository<QuestionnaireResponse, Long> {

	/**
	 * @param id
	 * @param string
	 * @return
	 */
	List<QuestionnaireResponse> findAllByQuestionnaireIdAndResponse(Long id, String response);

}
