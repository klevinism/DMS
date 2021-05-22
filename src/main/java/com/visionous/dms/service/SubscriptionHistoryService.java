/**
 * 
 */
package com.visionous.dms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visionous.dms.pojo.SubscriptionHistory;
import com.visionous.dms.repository.SubscriptionHistoryRepository;

/**
 * @author delimeta
 *
 */
@Service
public class SubscriptionHistoryService implements ISubscriptionHistoryService {

	private SubscriptionHistoryRepository subscriptionHistoryRepository;
	
	/**
	 * 
	 */
	@Autowired
	public SubscriptionHistoryService(SubscriptionHistoryRepository subscriptionHistoryRepository) {
		this.subscriptionHistoryRepository = subscriptionHistoryRepository;
	}

	/**
	 * @return
	 */
	@Override
	public List<SubscriptionHistory> findAllOrderedBySubscriptionEndDateDesc() {
		return this.subscriptionHistoryRepository.findAllByOrderBySubscriptionEndDateDesc();
	}

	/**
	 * @return
	 */
	public Optional<SubscriptionHistory> findActiveSubscription() { 
		return this.subscriptionHistoryRepository.findOneByActive(true);
	}

	/**
	 * @param subscription
	 * @return 
	 */
	public SubscriptionHistory update(SubscriptionHistory subscription) {
		return this.subscriptionHistoryRepository.saveAndFlush(subscription);
	}
}
