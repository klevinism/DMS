/**
 * 
 */
package com.visionous.dms.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.visionous.dms.pojo.Record;

/**
 * @author delimeta
 *
 */
@Repository
public interface RecordRepository extends JpaRepository<Record, Long>{

	/**
	 * @param id
	 * @return
	 */
	Optional<Record> findByHistoryId(Long id);

	/**
	 * @param historyId
	 * @return
	 */
	List<Record> findByHistoryIdOrderByServicedateDesc(Long historyId);

	/**
	 * @param id 
	 * @return
	 */
	List<Record> findFirst10ByPersonnelIdOrderByServicedateDesc(Long personnelId);

	/**
	 * @param id
	 * @return
	 */
	List<Record> findFirst10ByPersonnelId(Long personnelId);

	/**
	 * @param id
	 * @return
	 */ 
	List<Record> findAllByPersonnelId(Long personnelId);

	/**
	 * @param personnelId
	 * @param beginDate
	 * @param endDate  
	 * @return
	 */
	Integer countByPersonnelIdAndServicedateBetween(Long personnelId, LocalDateTime beginDate, LocalDateTime endDate);

	/**
	 *
	 * @param personnelIds
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	Integer countByPersonnelIdInAndServicedateBetween(List<Long> personnelIds, LocalDateTime beginDate, LocalDateTime endDate);

	/**
	 * @param id
	 * @param startDate
	 * @param oneWeekBefore
	 * @return
	 */
	List<Record> findAllByPersonnelIdAndServicedateBetween(Long personnelId, LocalDateTime startDate, LocalDateTime endDate);

	/**
	 * @param currentDate
	 * @param endsDate
	 * @return
	 */
	List<Record> findAllByServicedateBetween(LocalDateTime startDate, LocalDateTime endDate);

	/**
	 * @return
	 */
	List<Record> findAllByOrderByServicedateDesc();

	/**
	 * @return
	 */
	List<Record> findAllByOrderByServicedateAsc();

	/**
	 * @param historyId
	 * @return
	 */
	List<Record> findTop5ByHistoryIdOrderByServicedateDesc(Long historyId);

	/**
	 * @param historyId
	 * @return
	 */
	Optional<Record> findFirstByHistoryIdOrderByServicedateDesc(Long historyId);

	/**
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	Integer countByServicedateBetween(LocalDateTime startDate, LocalDateTime endDate);

	/**
	 * @return
	 */
	@Query("SELECT sum(e.receipt.total) from Record e WHERE servicedate BETWEEN ?1 AND ?2")
	Integer sumOfAllReceipts(LocalDateTime startDate, LocalDateTime endDate);

	/**
	 * @param startDate
	 * @param endDate
	 * @param customerId
	 * @return
	 */
	List<Record> findAllByServicedateBetweenAndHistory_customerId(LocalDateTime startDate, LocalDateTime endDate, Long customerId);

	/**
	 * @param id
	 * @param startDate
	 * @param endDate
	 * @return sum of all receipts by personnel id and between dates 
	 */
	@Query("SELECT sum(e.receipt.total) from Record e WHERE e.personnelId = ?1 AND servicedate BETWEEN ?2 AND ?3")
	Integer findSumOfAllReceiptsByPersonnelId(Long id, LocalDateTime startDate, LocalDateTime endDate);

	/**
	 * @param localDateTime
	 * @param localDateTime2
	 * @param ids
	 * @return
	 */
	@Query("SELECT sum(e.receipt.total) from Record e WHERE e.personnel.id IN (?1) AND e.servicedate BETWEEN ?2 AND ?3 ")
	Integer sumOfAllReceiptsByPersonnelIdIn(List<Long> ids, LocalDateTime localDateTime, LocalDateTime localDateTime2);

	/**
	 *
	 * @param personnelId
	 * @param localDateTime
	 * @param localDateTime2
	 * @param payed
	 * @return
	 */
	@Query("SELECT sum(e.receipt.total) from Record e "
			+ "WHERE e.personnel.id = ?1 AND e.receipt.payed = ?4 AND e.servicedate BETWEEN ?2 AND ?3 ")
	Integer findSumOfAllReceiptsByPersonnelIdAndPayed(Long personnelId, LocalDateTime localDateTime,
			LocalDateTime localDateTime2, Boolean payed);


	/**
	 *
	 * @param personnelIds
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	List<Record> findAllByPersonnelIdInAndServicedateBetween(List<Long> personnelIds, LocalDateTime beginDate, LocalDateTime endDate);
}

