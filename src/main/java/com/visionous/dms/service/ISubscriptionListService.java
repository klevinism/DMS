/**
 * 
 */
package com.visionous.dms.service;

import java.util.List;

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

}
