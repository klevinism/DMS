/**
 * 
 */
package com.visionous.dms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visionous.dms.pojo.QuestionnaireResponse;

/**
 * @author delimeta
 *
 */
@Repository
public interface QuestionnaireResponseRepository extends JpaRepository<QuestionnaireResponse, Long> {

}
