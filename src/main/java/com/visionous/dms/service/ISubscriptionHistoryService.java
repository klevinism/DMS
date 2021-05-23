/**
 * 
 */
package com.visionous.dms.service;

import java.util.List;

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

}
