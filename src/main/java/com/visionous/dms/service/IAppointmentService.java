/**
 * 
 */
package com.visionous.dms.service;

import java.util.Date;
import java.util.List;

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
	List<Appointment> findByAppointmentDate(Date appointmentDate);

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
	Integer countAllByPersonnelIdAndAppointmentDateBetween(Long id, Date startingDate, Date endingDate);

	/**
	 * @param accountId
	 * @param start
	 * @param end
	 * @return
	 */
	List<Appointment> findByPersonnelIdAndAppointmentDateBetweenOrderByAppointmentDateAsc(Long accountId, Date start,
			Date end);
}
