/**
 * 
 */
package com.visionous.dms.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.visionous.dms.pojo.Appointment;

/**
 * @author delimeta
 *
 */
public interface IAppointmentService {
	void delete(Appointment appointment);
	
	void deleteBatch(List<Appointment> appointments);

	/**
	 * @param appointmentDate
	 * @return
	 */
	List<Appointment> findByAppointmentDate(LocalDateTime appointmentDate);

	/**
	 * @param newAppointment
	 * @return
	 */
	Appointment create(Appointment newAppointment);

	/**
	 * @param personnelId
	 * @return
	 */
	List<Appointment> findByPersonnelId(Long personnelId);

	/**
	 * @param id
	 * @param startingDate
	 * @param endingDate
	 * @return
	 */
	Integer countAllByPersonnelIdAndAppointmentDateBetween(Long id, LocalDateTime startingDate, LocalDateTime endingDate);

	/**
	 * @param accountId
	 * @param start
	 * @param end
	 * @return
	 */
	List<Appointment> findByPersonnelIdAndAppointmentDateBetweenOrderByAppointmentDateAsc(Long accountId, LocalDateTime start,
			LocalDateTime end);

	/**
	 * @param appointmentId
	 * @return
	 */
	Optional<Appointment> findById(Long appointmentId);

	/**
	 * @param personnelId
	 * @param startRange
	 * @param endRange
	 * @return
	 */
	List<Appointment> findAllByPersonnelIdBetweenDateRange(Long personnelId, LocalDateTime startRange, LocalDateTime endRange);

	/**
	 * @param startRange
	 * @param endRange
	 * @return
	 */
	List<Appointment> findAllBetweenDateRange(LocalDateTime startRange, LocalDateTime endRange);
	
	
	/**
	 * @return
	 */
	List<Object[]> findTopAppointmentsByMostUsedServiceType();

	/**
	 * @param id
	 * @param localDateTime
	 * @param localDateTime2
	 * @return
	 */
	List<Appointment> findAllByBusinessIdAndBetweenDateRange(Long id, LocalDateTime localDateTime,
			LocalDateTime localDateTime2);


}
