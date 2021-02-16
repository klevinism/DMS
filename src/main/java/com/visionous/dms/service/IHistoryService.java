/**
 * 
 */
package com.visionous.dms.service;

import java.util.Optional;

import com.visionous.dms.pojo.History;

/**
 * @author delimeta
 *
 */
public interface IHistoryService {
	void delete(History history);

	/**
	 * @param currentCustomer
	 * @return 
	 */
	History createNewHistory(History history);

	/**
	 * @param history
	 * @return
	 */
	History update(History history);

	/**
	 * @return
	 */
	Iterable<History> findAll();

	/**
	 * @param historyId
	 * @return
	 */
	Optional<History> findById(Long historyId);

	/**
	 * @param customerId
	 * @return
	 */
	Optional<History> findByCustomerId(Long customerId);
}
