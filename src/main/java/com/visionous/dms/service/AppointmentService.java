/**
 * 
 */
package com.visionous.dms.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visionous.dms.pojo.Appointment;
import com.visionous.dms.pojo.ServiceType;
import com.visionous.dms.repository.AppointmentRepository;

/**
 * @author delimeta
 *
 */
@Service
public class AppointmentService implements IAppointmentService{
	private AppointmentRepository appointmentRepository;
	
	/**
	 * 
	 */
	@Autowired
	public AppointmentService(AppointmentRepository appointmentRepository) {
		this.appointmentRepository = appointmentRepository;
	}


	@Override
	public void delete(Appointment appointment) {
		this.appointmentRepository.delete(appointment);
	}


	@Override
	public void deleteBatch(List<Appointment> appointments) {
		this.appointmentRepository.deleteInBatch(appointments);
	}


	/**
	 * @param appointmentDate
	 * @return
	 */
	@Override
	public List<Appointment> findByAppointmentDate(Date appointmentDate) {
		return this.appointmentRepository.findByAppointmentDate(appointmentDate);
	}


	/**
	 * @param newAppointment
	 * @return
	 */
	@Override
	public Appointment create(Appointment newAppointment) {
		return this.appointmentRepository.saveAndFlush(newAppointment);
	}


	/**
	 * @param id
	 * @param startingDate
	 * @param endingDate
	 * @return
	 */
	@Override
	public Integer countAllByPersonnelIdAndAppointmentDateBetween(Long id, Date startingDate, Date endingDate) {
		return this.appointmentRepository.countAllByPersonnelIdAndAppointmentDateBetween(id, startingDate, endingDate);
	}


	/**
	 * @param personnelId
	 * @return
	 */
	@Override
	public List<Appointment> findByPersonnelId(Long personnelId) {
		return this.appointmentRepository.findByPersonnelId(personnelId);
	}


	/**
	 * @param accountId
	 * @param start
	 * @param end
	 * @return
	 */
	@Override
	public List<Appointment> findByPersonnelIdAndAppointmentDateBetweenOrderByAppointmentDateAsc(Long accountId,
			Date start, Date end) {
		return this.appointmentRepository.findByPersonnelIdAndAppointmentDateBetweenOrderByAppointmentDateDesc(accountId, start, end);
	}


	/**
	 * @param appointmentId
	 * @return
	 */
	@Override
	public Optional<Appointment> findById(Long appointmentId) {
		return this.appointmentRepository.findById(appointmentId);
	}
	
	/**
	 * @param personnelId
	 * @param startRange
	 * @param endRange
	 * @return
	 */
	@Override
	public List<Appointment> findAllByPersonnelIdBetweenDateRange(Long personnelId, Date startRange, Date endRange) {
		return this.appointmentRepository.
				findAllByPersonnelIdAndAppointmentDateGreaterThanEqualAndAppointmentEndDateLessThanEqualOrderByAppointmentDateAsc(personnelId, startRange, endRange);
	}


	/**
	 * @param startRange
	 * @param endRange
	 * @return
	 */
	@Override
	public List<Appointment> findAllBetweenDateRange(Date startRange, Date endRange) {
		return this.appointmentRepository.
				findAllByAppointmentDateGreaterThanEqualAndAppointmentEndDateLessThanEqualOrderByAppointmentDateDesc(startRange, endRange);
	}

	@Override
	public List<Object[]> findTopAppointmentsByMostUsedServiceType() {
		return this.appointmentRepository.topAppointmentsByMostUsedServiceType();
	}
}
