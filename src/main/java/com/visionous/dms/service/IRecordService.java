/**
 * 
 */
package com.visionous.dms.service;

import java.time.LocalDateTime;
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
	 * @param personnelId
	 * @return
	 */
	List<Record> findFirst10ByPersonnelIdOrderByServicedateDesc(Long personnelId);

	/**
	 * @param personnelId
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	Integer countByPersonnelIdAndServicedateBetween(Long personnelId, LocalDateTime beginDate, LocalDateTime endDate);

	/**
	 * @param personnelIds
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	Integer countAllByPersonnelIdInAndServicedateBetween(List<Long> personnelIds, LocalDateTime beginDate, LocalDateTime endDate);

	List<Record> findAllByServicedateBetween(LocalDateTime beginDate, LocalDateTime endDate);

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

	/**
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	Integer countByServicedateBetween(LocalDateTime startDate, LocalDateTime endDate);

	/**
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	Integer sumOfReceipts(LocalDateTime startDate, LocalDateTime endDate);

	/**
	 * @param id
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	Integer sumOfPersonnelReceipts(Long id, LocalDateTime startDate, LocalDateTime endDate);

	/**
	 * @param personnelId
	 * @param beginDate
	 * @param endDate
	 * @return all records of personnel serviced between dates
	 */
	List<Record> findAllByPersonnelIdAndServicedateBetween(Long personnelId, LocalDateTime beginDate,
			LocalDateTime endDate);
}
