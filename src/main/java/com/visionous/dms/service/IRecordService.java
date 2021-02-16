/**
 * 
 */
package com.visionous.dms.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.visionous.dms.pojo.Customer;
import com.visionous.dms.pojo.Record;

/**
 * @author delimeta
 *
 */
public interface IRecordService {
	List<Record> findAllByHistoryId(Long historyId);
	
	Optional<Record> getFirstByHistoryIdOrderByServiceDate(Long historyId);

	/**
	 * @param historyId
	 * @return
	 */
	Record lastRecordOfHistoryId(Long historyId);
	
	List<Record> findLastRecordForAllCustomers(List<Customer> customers);
	
	List<Record> findTop5ByHistoryIdOrderByServicedateDesc(Long historyId);
	
	void deleteBatch(Set<Record> records);

	/**
	 * @param id
	 * @return
	 */
	List<Record> findFirst10ByPersonnelIdOrderByServicedateDesc(Long personnelId);

	/**
	 * @param personnelId
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	Integer countByPersonnelIdAndServicedateBetween(Long personnelId, Date beginDate, Date endDate);

	/**
	 * @param personnelId
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	Integer countAllByPersonnelIdAndServicedateBetween(Long personnelId, Date beginDate, Date endDate);

	/**
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	List<Record> findAllByServicedateBetween(Date beginDate, Date endDate);

	/**
	 * @param newRecord
	 * @return
	 */
	Record create(Record newRecord);

	/**
	 * @param recordId
	 * @return
	 */
	Optional<Record> findById(Long recordId);

	/**
	 * @return
	 */
	Iterable<Record> findAllOrderByServicedateDesc();
}
