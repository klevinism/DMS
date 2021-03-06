/**
 * 
 */
package com.visionous.dms.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.visionous.dms.pojo.Appointment;
import com.visionous.dms.pojo.ServiceType;

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
	List<Appointment> findByAppointmentDate(Date appointmentDate);

	/**
	 * @param id
	 * @param startWorkHours
	 * @param endWorkHours
	 * @return
	 */
	List<Appointment> findByPersonnelIdAndAppointmentDateBetweenOrderByAppointmentDateDesc(Long id, Date startWorkHours,
			Date endWorkHours);

	/**
	 * @param id
	 * @param startWorkHours
	 * @param endWorkHours
	 * @return
	 */
	List<Appointment> findByPersonnelIdAndAppointmentDateBetweenOrderByAppointmentDateAsc(Long id, Date startWorkHours,
			Date endWorkHours);

	/**
	 * @param id
	 * @param time
	 * @param time2
	 * @return
	 */
	Integer countAllByPersonnelIdAndAppointmentDateBetween(Long personnelId, Date start, Date end);
	
	/**
	 * @param personnelId
	 * @param startRange
	 * @param endRange
	 * @return
	 */
	List<Appointment> findAllByPersonnelIdAndAppointmentDateGreaterThanEqualAndAppointmentEndDateLessThanEqualOrderByAppointmentDateAsc(
			Long personnelId, Date startRange, Date endRange);

	/**
	 * @param startRange
	 * @param endRange
	 * @return
	 */
	List<Appointment> findAllByAppointmentDateGreaterThanEqualAndAppointmentEndDateLessThanEqualOrderByAppointmentDateDesc(
			Date startRange, Date endRange);

	/**
	 * @return
	 */
	@Query("select t.serviceType.id, t.serviceType.name from Appointment t group by t.serviceType.id, t.serviceType.name order by count(t.serviceType.id) desc")
	List<Object[]> topAppointmentsByMostUsedServiceType();
}
