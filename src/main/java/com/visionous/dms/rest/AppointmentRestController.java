/**
 * 
 */
package com.visionous.dms.rest;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.visionous.dms.pojo.Appointment;
import com.visionous.dms.pojo.Customer;
import com.visionous.dms.pojo.Personnel;
import com.visionous.dms.repository.AppointmentRepository;
import com.visionous.dms.repository.CustomerRepository;
import com.visionous.dms.repository.PersonnelRepository;
import com.visionous.dms.rest.response.ResponseBody;

/**
 * @author delimeta
 *
 */
@RestController
public class AppointmentRestController {
	private MessageSource messageSource;
	
	private AppointmentRepository appointmentRepository;
	private PersonnelRepository personnelRepository;
	private CustomerRepository customerRepository;
	
	/**
	 * 
	 */
	@Autowired
	public AppointmentRestController(AppointmentRepository appointmentRepository, PersonnelRepository personnelRepository, 
			CustomerRepository customerRepository,
			MessageSource messageSource) {
		this.appointmentRepository = appointmentRepository;
		this.personnelRepository = personnelRepository;
		this.customerRepository = customerRepository;
		this.messageSource = messageSource;
	}
	
	@PostMapping("/api/book")
    public ResponseEntity<?> bookAppointment(@RequestParam(name = "customerId", required = true) Long customerId,
    		@RequestParam(name = "personnelId", required = true) Long personnelId,
    		@RequestParam(name = "appointmentDate", required = true) Date appointmentDate) { 

        ResponseBody<Appointment> result = new ResponseBody<>();
        
        List<Appointment> anyAppointment = appointmentRepository.findByAppointmentDate(appointmentDate);
        
        String noAppointmentSet = messageSource.getMessage("alert.noAppointmentSet", null, LocaleContextHolder.getLocale());
        String success = messageSource.getMessage("alert.success", null, LocaleContextHolder.getLocale());
        String appointmentset = messageSource.getMessage("alert.appointmentSet", null, LocaleContextHolder.getLocale());
        String errorCreatingAppointment = messageSource.getMessage("alert.errorCreatingAppointment", null, LocaleContextHolder.getLocale());
        		
        if(!anyAppointment.isEmpty()) {
        	result.setError("error");
        	result.setMessage(noAppointmentSet + " " + new SimpleDateFormat("dd-MM-yy hh:mm").format(appointmentDate));
        }else {
        	Appointment newAppointment = new Appointment();
        	Optional<Personnel> personnel = personnelRepository.findById(personnelId);
        	Optional<Customer> customer = customerRepository.findById(customerId);
        	
        	if(personnel.isPresent() && customer.isPresent()) {
        		newAppointment.setPersonnel(personnel.get());
        		newAppointment.setCustomer(customer.get());
        	}
        	newAppointment.setAddeddate(new Date());
        	newAppointment.setAppointmentDate(appointmentDate); 
        	
        	Appointment created = appointmentRepository.saveAndFlush(newAppointment);
        	if(created.getId() != null) {
        		result.addResult(created);
        		result.setError(success);
        		result.setMessage(appointmentset);
        	}else {
        		result.setError("error");
        		result.setMessage(errorCreatingAppointment);
        	}
        }

        return ResponseEntity.ok(result);
    }
	
	@GetMapping("/api/availableAppointments")
    public ResponseEntity<?> getSearchResultViaAjax(@RequestParam(name = "personnelId", required = false) Long personnelId) { 
        ResponseBody<Appointment> result = new ResponseBody<>();
        
        List<Appointment> appointments = appointmentRepository.findByPersonnelId(personnelId);
        
        List<Appointment> userss = new ArrayList<>();
        if (userss.isEmpty()) {
            result.setMessage("no user found!"); 
        } else {
            result.setMessage("success");
        }
        result.setResult(appointments); 

        return ResponseEntity.ok(result);

    }
	
	@GetMapping("/api/getAvailablePersonnel")
    public ResponseEntity<?> getAvailablePersonnel() {
		
        ResponseBody<Personnel> result = new ResponseBody<>();
        
        List<Personnel> personnel = personnelRepository.findAll();
        if(personnel.size() != 0) {
        	result.setResult(personnel);
        }

        return ResponseEntity.ok(result);
	}
	
}
