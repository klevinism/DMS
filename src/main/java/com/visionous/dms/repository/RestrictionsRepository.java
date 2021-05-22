/**
 * 
 */
package com.visionous.dms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visionous.dms.pojo.Restrictions;

/**
 * @author delimeta
 *
 */
@Repository
public interface RestrictionsRepository extends JpaRepository<Restrictions, Long> {

}
