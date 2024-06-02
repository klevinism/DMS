/**
 * 
 */
package com.visionous.dms.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.visionous.dms.pojo.Appointment;

/**
 * @author delimeta
 *
 */
public interface IAppointmentService {
	void delete(Appointment appointment);
	
	void deleteBatch(List<Appointment> appointments);

	void deleteBatch(Set<Appointment> appointments);


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

	List<Appointment> findAllByPersonnelIdInAndBetweenDateRange(List<Long> personnelIds, LocalDateTime startRange, LocalDateTime endRange);

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
	 *
	 * @param ids
	 * @return
	 */
    List<Appointment> findByPersonnelIdIn(List<Long> ids);
}
