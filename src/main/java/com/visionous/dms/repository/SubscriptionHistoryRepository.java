/**
 * 
 */
package com.visionous.dms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visionous.dms.pojo.SubscriptionHistory;

/**
 * @author delimeta
 *
 */
@Repository
public interface SubscriptionHistoryRepository extends JpaRepository<SubscriptionHistory, Long>{

	/**
	 * @return
	 */
	List<SubscriptionHistory> findAllByOrderBySubscriptionEndDateDesc();

	/**
	 * @param b
	 * @return
	 */
	Optional<SubscriptionHistory> findOneByActive(boolean b);

	/**
	 * @param b
	 * @param currentBusinessId
	 * @return
	 */
	Optional<SubscriptionHistory> findOneByActiveAndBusinessId(boolean b, Long currentBusinessId);

	/**
	 * @param businessId
	 * @return
	 */
	List<SubscriptionHistory> findAllByBusinessIdOrderBySubscriptionEndDateDesc(Long businessId);

}
