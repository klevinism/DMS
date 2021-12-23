/**
 * 
 */
package com.visionous.dms.service;

import java.util.List;
import java.util.Optional;

import com.visionous.dms.pojo.SubscriptionHistory;

/**
 * @author delimeta
 *
 */
public interface ISubscriptionHistoryService {

	/**
	 * @return
	 */
	List<SubscriptionHistory> findAllOrderedBySubscriptionEndDateDesc();

	/**
	 * @return
	 */
	Optional<SubscriptionHistory> findActiveSubscriptionByBusinessId(Long currentBusinessId);

	/**
	 * @param subscription
	 * @return 
	 */
	SubscriptionHistory update(SubscriptionHistory subscription);

}
