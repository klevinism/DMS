/**
 * 
 */
package com.visionous.dms.repository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
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
	Integer countByPersonnelIdAndServicedateBetween(Long personnelId, Date beginDate, Date endDate);

	/**
	 * @param id
	 * @param startDate
	 * @param oneWeekBefore
	 * @return
	 */
	List<Record> findAllByPersonnelIdAndServicedateBetween(Long personnelId, Date startDate, Date endDate);

	/**
	 * @param currentDate
	 * @param endsDate
	 * @return
	 */
	List<Record> findAllByServicedateBetween(Date startDate, Date endDate);

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
}

