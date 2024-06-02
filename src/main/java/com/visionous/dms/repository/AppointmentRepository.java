/**
 * 
 */
package com.visionous.dms.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.visionous.dms.pojo.Appointment;

/**
 * @author delimeta
 *
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>{

	/**
	 * @param id
	 * @return
	 */
	Set<Appointment> findAllByPersonnelId(Long id);

	/**
	 * @param personnelId
	 * @return
	 */
	List<Appointment> findByPersonnelId(Long personnelId);

	/**
	 * @param appointmentDate
	 * @return
	 */
	List<Appointment> findByAppointmentDate(LocalDateTime appointmentDate);

	/**
	 * @param id
	 * @param startWorkHours
	 * @param endWorkHours
	 * @return
	 */
	List<Appointment> findByPersonnelIdAndAppointmentDateBetweenOrderByAppointmentDateDesc(Long id, LocalDateTime startWorkHours,
			LocalDateTime endWorkHours);

	/**
	 * @param id
	 * @param startWorkHours
	 * @param endWorkHours
	 * @return
	 */
	List<Appointment> findByPersonnelIdAndAppointmentDateBetweenOrderByAppointmentDateAsc(Long id, LocalDateTime startWorkHours,
			LocalDateTime endWorkHours);

	/**
	 * @param id
	 * @param time
	 * @param time2
	 * @return
	 */
	Integer countAllByPersonnelIdAndAppointmentDateBetween(Long personnelId, LocalDateTime start, LocalDateTime end);
	
	/**
	 * @param personnelId
	 * @param startRange
	 * @param endRange
	 * @return
	 */
	List<Appointment> findAllByPersonnelIdAndAppointmentDateGreaterThanEqualAndAppointmentEndDateLessThanEqualOrderByAppointmentDateAsc(
			Long personnelId, LocalDateTime startRange, LocalDateTime endRange);

	/**
	 * @param startRange
	 * @param endRange
	 * @return
	 */
	List<Appointment> findAllByAppointmentDateGreaterThanEqualAndAppointmentEndDateLessThanEqualOrderByAppointmentDateDesc(
			LocalDateTime startRange, LocalDateTime endRange);

	/**
	 * @return
	 */
	@Query("select t.serviceType.id, t.serviceType.name from Appointment t group by t.serviceType.id, t.serviceType.name order by count(t.serviceType.id) desc")
	List<Object[]> topAppointmentsByMostUsedServiceType();

	/**
	 *
	 * @param ids
	 * @return
	 */
	@Query("select t.serviceType.id, t.serviceType.name from Appointment t join t.personnel p where p.id in (?1) group by t.serviceType.id, t.serviceType.name order by count(t.serviceType.id) desc")
	List<Object[]> findTopAppointmentsByMostUsedServiceTypeAndPersonnelIdIn(List<Long> ids);

	/**
	 *
	 * @param ids
	 * @return
	 */
	List<Appointment> findAllByPersonnelIdIn(List<Long> ids);

	/**
	 *
	 * @param personnelIds
	 * @param startRange
	 * @param endRange
	 * @return
	 */
	List<Appointment> findAllByPersonnelIdInAndAppointmentDateGreaterThanEqualAndAppointmentEndDateLessThanEqualOrderByAppointmentDateAsc(List<Long> personnelIds, LocalDateTime startRange, LocalDateTime endRange);

	/**
	 *
	 * @param customerIds
	 * @param localDateTime
	 * @param localDateTime2
	 * @return
	 */
	List<Appointment> findAllByCustomerIdInAndAppointmentDateBetweenOrderByAppointmentDateAsc(List<Long> customerIds, LocalDateTime localDateTime, LocalDateTime localDateTime2);

}
