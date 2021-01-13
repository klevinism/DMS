/**
 * 
 */
package com.visionous.dms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visionous.dms.pojo.Questionnaire;

/**
 * @author delimeta
 *
 */
@Repository
public interface QuestionnaireRepository  extends JpaRepository<Questionnaire, Long> {

	/**
	 * @param customerId
	 * @return
	 */
	Optional<Questionnaire> findByCustomerId(Long customerId);

}
