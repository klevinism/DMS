package com.visionous.dms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visionous.dms.pojo.Business;

/**
 * @author delimeta
 *
 */
@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
	
}
