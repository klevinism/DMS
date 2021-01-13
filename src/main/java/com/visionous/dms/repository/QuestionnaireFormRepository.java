/**
 * 
 */
package com.visionous.dms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visionous.dms.pojo.QuestionnaireForm;

/**
 * @author delimeta
 *
 */
@Repository
public interface QuestionnaireFormRepository  extends JpaRepository<QuestionnaireForm, Long> {

}
