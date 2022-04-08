/**
 * 
 */
package com.visionous.dms.service;

import java.util.List;
import java.util.Optional;

import com.visionous.dms.pojo.Subscription;

/**
 * @author delimeta
 *
 */
public interface ISubscriptionListService {

	/**
	 * @return
	 */
	List<Subscription> findAll();

	/**
	 * @return
	 */
	List<Subscription> findAllOrderedByIdAsc();

	List<Subscription> findAllByBusinessIdOrderedByIdAsc(Long businessId);

	Optional<Subscription> findByName(String name);

}
