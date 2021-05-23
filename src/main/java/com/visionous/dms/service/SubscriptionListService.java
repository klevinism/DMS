/**
 * 
 */
package com.visionous.dms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visionous.dms.pojo.Subscription;
import com.visionous.dms.repository.SubscriptionListRepository;

/**
 * @author delimeta
 *
 */
@Service
public class SubscriptionListService implements ISubscriptionListService {

	private SubscriptionListRepository subscriptionListRepository;
	
	@Autowired
	public SubscriptionListService(SubscriptionListRepository subscriptionListRepository) {
		this.subscriptionListRepository = subscriptionListRepository;
	}

	/**
	 * @return
	 */
	@Override
	public List<Subscription> findAll() {
		return this.subscriptionListRepository.findAll();
	}

	/**
	 * @return
	 */
	public List<Subscription> findAllOrderedByIdAsc() { 
		return this.subscriptionListRepository.findAllByOrderByIdAsc();
	}
	
	
}
