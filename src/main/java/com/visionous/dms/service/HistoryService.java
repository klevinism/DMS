/**
 * 
 */
package com.visionous.dms.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visionous.dms.pojo.History;
import com.visionous.dms.repository.HistoryRepository;

/**
 * @author delimeta
 *
 */
@Service
public class HistoryService implements IHistoryService{
	private HistoryRepository historyRepository;
	
	/**
	 * 
	 */
	@Autowired
	public HistoryService(HistoryRepository historyRepository) {
		this.historyRepository = historyRepository;
	}

	/**
	 * @param history History
	 */
	@Override
	public void delete(History history) {
		this.historyRepository.delete(history);
	}

	/**
	 * @param history
	 * @return
	 */
	public History create(History history){
		return this.historyRepository.saveAndFlush(history);
	}
	
	/**
	 * @param newHistory
	 */
	@Override
	public History createNewHistory(History history) {
		history.setRecords(null);
		history.setStartdate(new Date());
		return this.create(history);
	}
	
	/**
	 * @param history
	 */
	@Override
	public History update(History history) {
		return this.historyRepository.saveAndFlush(history);
	}

	/**
	 * @return historyList List<History>
	 */
	@Override
	public Iterable<History> findAll() {
		return this.historyRepository.findAll();
	}

	/**
	 * @param historyId
	 * @return
	 */
	@Override
	public Optional<History> findById(Long historyId) {
		return this.historyRepository.findById(historyId);
	}

	/**
	 * @param customerId
	 * @return
	 */
	@Override
	public Optional<History> findByCustomerId(Long customerId) {
		return this.historyRepository.findByCustomerId(customerId);
	}
}
