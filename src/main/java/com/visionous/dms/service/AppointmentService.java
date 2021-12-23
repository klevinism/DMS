/**
 * 
 */
package com.visionous.dms.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visionous.dms.pojo.Appointment;
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
	public List<Appointment> findByAppointmentDate(LocalDateTime appointmentDate) {
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
	public Integer countAllByPersonnelIdAndAppointmentDateBetween(Long id, LocalDateTime startingDate, LocalDateTime endingDate) {
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
			LocalDateTime start, LocalDateTime end) {
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
	public List<Appointment> findAllByPersonnelIdBetweenDateRange(Long personnelId, LocalDateTime startRange, LocalDateTime endRange) {
		return this.appointmentRepository.
				findAllByPersonnelIdAndAppointmentDateGreaterThanEqualAndAppointmentEndDateLessThanEqualOrderByAppointmentDateAsc(personnelId, startRange, endRange);
	}


	/**
	 * @param startRange
	 * @param endRange
	 * @return
	 */
	@Override
	public List<Appointment> findAllBetweenDateRange(LocalDateTime startRange, LocalDateTime endRange) {
		return this.appointmentRepository.
				findAllByAppointmentDateGreaterThanEqualAndAppointmentEndDateLessThanEqualOrderByAppointmentDateDesc(startRange, endRange);
	}

	/**
	 *
	 */
	@Override
	public List<Object[]> findTopAppointmentsByMostUsedServiceType() {
		return this.appointmentRepository.topAppointmentsByMostUsedServiceType();
	}


	/**
	 * @param currentBusinessId
	 * @param localDateTime
	 * @param localDateTime2
	 * @return
	 */
	@Override
	public List<Appointment> findAllByBusinessIdAndBetweenDateRange(Long currentBusinessId, LocalDateTime localDateTime,
			LocalDateTime localDateTime2) {
		return this.appointmentRepository.findAllByCustomer_Account_Businesses_IdAndAppointmentDateBetweenOrderByAppointmentDateAsc(currentBusinessId, localDateTime, localDateTime2);
	}


	/**
	 * @param personnelId
	 * @return
	 */
	public List<Appointment> findByBusinessIdAndPersonnelId(Long currentBusinessId, Long personnelId) {
		return this.appointmentRepository.findAllByPersonnel_Account_Businesses_IdAndPersonnelId(currentBusinessId, personnelId);
	}


	/**
	 * @param id
	 * @param personnelId
	 * @param localDateTime
	 * @param localDateTime2
	 * @return
	 */
	public List<Appointment> findAllByCustomerBusinessIdAndPersonnelIdAndBetweenDateRange(Long currentBusienssId, Long personnelId,
			LocalDateTime localDateTime, LocalDateTime localDateTime2) {
		return this.appointmentRepository
				.findAllByCustomer_Account_Businesses_IdAndPersonnelIdAndAppointmentDateGreaterThanEqualAndAppointmentEndDateLessThanEqualOrderByAppointmentDateAsc(currentBusienssId, personnelId, localDateTime, localDateTime2);
	}

}
