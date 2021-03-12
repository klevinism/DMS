/**
 * 
 */
package com.visionous.dms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visionous.dms.pojo.Customer;
import com.visionous.dms.pojo.Record;
import com.visionous.dms.repository.RecordRepository;

/**
 * @author delimeta
 *
 */
@Service
public class RecordService implements IRecordService{
	
	private RecordRepository recordRepository;
	
	/**
	 * 
	 */
	@Autowired
	public RecordService(RecordRepository recordRepository) {
		this.recordRepository = recordRepository;
	}
	
	@Override
	public List<Record> findAllByHistoryId(Long historyId) {
		return this.recordRepository.findByHistoryIdOrderByServicedateDesc(historyId);
	}

	@Override
	public Optional<Record> getFirstByHistoryIdOrderByServiceDate(Long historyId) {
		return this.recordRepository.findFirstByHistoryIdOrderByServicedateDesc(historyId);
	}

	
	@Override
	public Record lastRecordOfHistoryId(Long historyId){
		Record lastRecord = null;
		Optional<Record> customerRecords = recordRepository.findFirstByHistoryIdOrderByServicedateDesc(historyId);
		if(customerRecords.isPresent()) {
			lastRecord = customerRecords.get();
		}
		return lastRecord;
	}
	
	@Override
	public List<Record> findLastRecordForAllCustomers(List<Customer> customers){
		List<Record> lastCustomerRecords = new ArrayList<>();
		customers.forEach(customer -> {
			Record nullRecord = null;
			if(customer.getCustomerHistory() != null && !customer.getCustomerHistory().getRecords().isEmpty()) {
				nullRecord = lastRecordOfHistoryId(customer.getCustomerHistory().getId());
			}
			lastCustomerRecords.add(nullRecord);
		});
		return lastCustomerRecords;
	}

	@Override
	public List<Record> findTop5ByHistoryIdOrderByServicedateDesc(Long historyId) {
		List<Record> customerRecords = recordRepository.findTop5ByHistoryIdOrderByServicedateDesc(historyId);
		if(!customerRecords.isEmpty()){
			return customerRecords;
		}
		return null;
	}

	@Override
	public void deleteBatch(Set<Record> set) {
		this.recordRepository.deleteInBatch(set);	
	}

	/**
	 * @param personnelId
	 * @return Last 10 records of a personnel
	 */
	@Override
	public List<Record> findFirst10ByPersonnelIdOrderByServicedateDesc(Long personnelId) {
		return this.recordRepository.findFirst10ByPersonnelIdOrderByServicedateDesc(personnelId);
	}

	/**
	 * @param personnelId
	 * @param beginDate
	 * @param endDate
	 * @return record count of personnel serviced between dates
	 */
	@Override
	public Integer countByPersonnelIdAndServicedateBetween(Long personnelId, Date beginDate, Date endDate) {
		return this.recordRepository.countByPersonnelIdAndServicedateBetween(personnelId, beginDate, endDate);
	}

	/**
	 * @param personnelId
	 * @param beginDate
	 * @param endDate
	 * @return all records of personnel serviced between dates
	 */
	@Override
	public Integer countAllByPersonnelIdAndServicedateBetween(Long personnelId, Date beginDate, Date endDate) {
		return this.recordRepository.countByPersonnelIdAndServicedateBetween(personnelId, beginDate, endDate);
	}

	/**
	 * @param beginDate
	 * @param endDate
	 * @return all records between service dates
	 */
	@Override
	public List<Record> findAllByServicedateBetween(Date beginDate, Date endDate) {
		return this.recordRepository.findAllByServicedateBetween(beginDate, endDate);
	}

	/**
	 * @param newRecord
	 * @return
	 */
	@Override
	public Record create(Record newRecord) {
		if(newRecord.getServicedate() == null) {
			newRecord.setServicedate(new Date());
		}
		return this.recordRepository.saveAndFlush(newRecord);
	}

	/**
	 * @param recordId
	 * @return
	 */
	@Override
	public Optional<Record> findById(Long recordId) {
		return this.recordRepository.findById(recordId);
	}

	/**
	 * @return
	 */
	@Override
	public Iterable<Record> findAllOrderByServicedateDesc() {
		return this.recordRepository.findAllByOrderByServicedateDesc();
	}
}
