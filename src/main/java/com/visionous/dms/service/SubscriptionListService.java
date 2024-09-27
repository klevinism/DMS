/**
 * 
 */
package com.visionous.dms.service;

import java.util.List;
import java.util.Optional;

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
	@Override
	public List<Subscription> findAllOrderedByIdAsc() { 
		return this.subscriptionListRepository.findAllByOrderByIdAsc();
	}

	@Override
	public List<Subscription> findAllByBusinessIdOrderedByIdAsc(Long businessId) {
		return this.subscriptionListRepository.findAllBySubscriptionHistory_BusinessIdOrderByIdAsc(businessId);
	}

	@Override
	public Optional<Subscription> findByName(String name) {
		// TODO Auto-generated method stub
		return this.subscriptionListRepository.findByName(name);
	}
	
	
}
