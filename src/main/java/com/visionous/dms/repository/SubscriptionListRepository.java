/**
 * 
 */
package com.visionous.dms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visionous.dms.pojo.Subscription;

/**
 * @author delimeta
 *
 */
@Repository
public interface SubscriptionListRepository extends JpaRepository<Subscription, Long> {
	
	public List<Subscription> findAllByOrderByIdAsc();

	public List<Subscription> findAllBySubscriptionHistory_BusinessIdOrderByIdAsc(Long businessId);

	public Optional<Subscription> findByName(String name);
}
