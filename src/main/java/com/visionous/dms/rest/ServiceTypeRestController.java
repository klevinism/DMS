/**
 * 
 */
package com.visionous.dms.rest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.visionous.dms.pojo.Appointment;
import com.visionous.dms.pojo.Customer;
import com.visionous.dms.pojo.Personnel;
import com.visionous.dms.pojo.ServiceType;
import com.visionous.dms.repository.AccountRepository;
import com.visionous.dms.repository.AppointmentRepository;
import com.visionous.dms.repository.CustomerRepository;
import com.visionous.dms.repository.PersonnelRepository;
import com.visionous.dms.repository.RecordRepository;
import com.visionous.dms.repository.ServiceTypeRepository;
import com.visionous.dms.rest.response.ResponseBody;

/**
 * @author delimeta
 *
 */
@RestController
public class ServiceTypeRestController {
	private MessageSource messageSource;
	
	private AppointmentRepository appointmentRepository;
	private ServiceTypeRepository serviceTypeRepository;
	private PersonnelRepository personnelRepository;
	private CustomerRepository customerRepository;
	private AccountRepository accountRepository;
	private RecordRepository recordRepository;
	
	/**
	 * 
	 */
	public ServiceTypeRestController(AppointmentRepository appointmentRepository, PersonnelRepository personnelRepository, 
			CustomerRepository customerRepository, MessageSource messageSource, 
			RecordRepository recordRepository, AccountRepository accountRepostory, ServiceTypeRepository serviceTypeRepository) {
		this.appointmentRepository = appointmentRepository;
		this.personnelRepository = personnelRepository;
		this.customerRepository = customerRepository;
		this.messageSource = messageSource;
		this.recordRepository = recordRepository;
		this.accountRepository = accountRepostory;
		this.serviceTypeRepository = serviceTypeRepository;
	}
	
	@PostMapping("/api/serviceType/create")
    public ResponseEntity<?> createService(@RequestParam(name = "name", required = true) String serviceTypeName) { 

        String success = messageSource.getMessage("alert.success", null, LocaleContextHolder.getLocale());
        String serviceCreated = messageSource.getMessage("alert.serviceCreatedSuccessfully", null, LocaleContextHolder.getLocale());
        String serviceCreatedError = messageSource.getMessage("alert.errorGeneric", null, LocaleContextHolder.getLocale());

        ResponseBody<ServiceType> result = new ResponseBody<>();
        ServiceType service = new ServiceType(serviceTypeName);
        
        ServiceType savedService = serviceTypeRepository.saveAndFlush(service);
        if(savedService.getId() != null) {
        	result.setError(success);
        	result.setMessage(serviceCreated);
        	result.addResult(savedService);
        }else {
        	result.setError("error");
        	result.setMessage(serviceCreatedError);
        }
        return ResponseEntity.ok(result);
    }

	@PostMapping("/api/serviceType/edit")
    public ResponseEntity<?> editService(@RequestParam(name = "id", required = true) Long currentServiceId, @RequestParam(name = "newName", required = true) String newName) { 

        String success = messageSource.getMessage("alert.success", null, LocaleContextHolder.getLocale());
        String serviceEdited = messageSource.getMessage("alert.serviceEditedSuccessfully", null, LocaleContextHolder.getLocale());
        String serviceEditedError = messageSource.getMessage("alert.errorGeneric", null, LocaleContextHolder.getLocale());
        
        ResponseBody<ServiceType> result = new ResponseBody<>();
        
        Optional<ServiceType> oldService = serviceTypeRepository.findById(currentServiceId);
        
        if(oldService.isPresent()) {
        	oldService.get().setName(newName);
        	
        	ServiceType newService = serviceTypeRepository.saveAndFlush(oldService.get());
        	if(newService.getId() != null) {
            	result.setError(success);
            	result.setMessage(serviceEdited);
            	result.addResult(oldService.get());	
        	}else {
        		result.setError("error");
            	result.setMessage(serviceEditedError);
        	}
        }else {
        	result.setError("error");
        	result.setMessage(serviceEditedError);
        }
        
        return ResponseEntity.ok(result);
    }
	
	@PostMapping("/api/serviceType/delete")
    public ResponseEntity<?> deleteService(@RequestParam(name = "id", required = true) Long selectedServiceId) { 

        String success = messageSource.getMessage("alert.success", null, LocaleContextHolder.getLocale());
        String serviceEdited = messageSource.getMessage("alert.serviceDeletedSuccessfully", null, LocaleContextHolder.getLocale());
        String serviceEditedError = messageSource.getMessage("alert.errorGeneric", null, LocaleContextHolder.getLocale());

        ResponseBody<ServiceType> result = new ResponseBody<>();
        
        Optional<ServiceType> oldService = serviceTypeRepository.findById(selectedServiceId);
        
        if(oldService.isPresent()) {
        	serviceTypeRepository.delete(oldService.get());
			result.setError(success);
			result.setMessage(serviceEdited);
        }else {
        	result.setError("error");
        	result.setMessage(serviceEditedError);
        }
        
        return ResponseEntity.ok(result);
    }
}
