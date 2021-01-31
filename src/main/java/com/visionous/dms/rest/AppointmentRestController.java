/**
 * 
 */
package com.visionous.dms.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.visionous.dms.configuration.helpers.DateUtil;
import com.visionous.dms.event.OnRegistrationCompleteEvent;
import com.visionous.dms.pojo.Account;
import com.visionous.dms.pojo.Appointment;
import com.visionous.dms.pojo.Customer;
import com.visionous.dms.pojo.Personnel;
import com.visionous.dms.repository.AccountRepository;
import com.visionous.dms.repository.AppointmentRepository;
import com.visionous.dms.repository.CustomerRepository;
import com.visionous.dms.repository.PersonnelRepository;
import com.visionous.dms.repository.RecordRepository;
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
	private AccountRepository accountRepository;
	private RecordRepository recordRepository;
	private ApplicationEventPublisher eventPublisher;

	
	/**
	 * 
	 */
	@Autowired
	public AppointmentRestController(AppointmentRepository appointmentRepository, PersonnelRepository personnelRepository, 
			CustomerRepository customerRepository, MessageSource messageSource, 
			RecordRepository recordRepository, AccountRepository accountRepostory, ApplicationEventPublisher eventPublisher) {
		this.appointmentRepository = appointmentRepository;
		this.personnelRepository = personnelRepository;
		this.customerRepository = customerRepository;
		this.messageSource = messageSource;
		this.recordRepository = recordRepository;
		this.accountRepository = accountRepostory;
		this.eventPublisher = eventPublisher;
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
	
	@PostMapping("/api/personnel/statistics")
    public ResponseEntity<?> personnelStatistics(@RequestParam(name = "personnelIds[]", required = false) Long[] personnelIds,
    		@RequestParam(name = "startDate", required = true) String startDate,
    		@RequestParam(name = "endDate", required = true) String endDate){ 

        ResponseBody<Map<String,List<Integer>>> result = new ResponseBody<>();
        
        Date start = null;
		Date end = null;
		
        List<Personnel> personnels = new ArrayList<>();
        Map<String,List<Integer>> listOfRecords = new HashMap<>();
        
        if(personnelIds.length > 0) {
        	for(int cnt=0; cnt<personnelIds.length; cnt++) {
        		Optional<Personnel> singlePersonnel = personnelRepository.findById(personnelIds[cnt]);
        		singlePersonnel.ifPresent(personnel -> personnels.add(personnel));
        		
        		try {
        			start = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
        			end = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
        		} catch (ParseException e) {
        			e.printStackTrace();
        		}
        		
        		
				if(start != null) {
					        		        		
	        		int daysToAdd = DateUtil.calculateDaysToAddFromPeriod(start, end);

	        		// getRecords for period startDate ~ endDate > add by daysToAdd
	        		List<Integer> recordsForPersonnel = new ArrayList<>();
	        		while (start.before(end)) {
						Date startingDate = new Date();
						Date endingDate = new Date();
						if(daysToAdd > 29 && daysToAdd<365) {
							startingDate = DateUtil.setDayToBegginingOfMonth(start);
							endingDate = DateUtil.setDayToEndOfMonth(start);
						}else if(daysToAdd >= 365) {
							startingDate = DateUtil.setDayToBegginingOfYear(start);
							endingDate = DateUtil.setDayToEndOfYear(start);
	        			}else{
							startingDate = DateUtil.setHoursToBegginingOfDay(start);
							endingDate = DateUtil.setDays(start, daysToAdd);
						} 
						
						Integer records = recordRepository.countAllByPersonnelIdAndServicedateBetween(singlePersonnel.get().getId(), startingDate, endingDate);

						start = DateUtil.setDays(start, daysToAdd);
						recordsForPersonnel.add(records);
	        		}
	        		listOfRecords.put(singlePersonnel.get().getAccount().getName(), recordsForPersonnel);
	        		
				}
        	}
        }
                		
        if(listOfRecords.isEmpty()) {

			String messageError = messageSource.getMessage("alert.error", null, LocaleContextHolder.getLocale());
    		result.setError(messageError);
        }else {
    		result.addResult(listOfRecords);
			String messageSuccess = messageSource.getMessage("alert.success", null, LocaleContextHolder.getLocale());
    		result.setError(messageSuccess);
    		result.setMessage(messageSuccess);
        }

        return ResponseEntity.ok(result);
    }
	
	@PostMapping("/api/personnel/appointment/statistics")
    public ResponseEntity<?> personnelAppointmentStatistics(@RequestParam(name = "personnelIds[]", required = false) Long[] personnelIds,
    		@RequestParam(name = "startDate", required = true) String startDate,
    		@RequestParam(name = "endDate", required = true) String endDate){ 

        ResponseBody<Map<String,List<Integer>>> result = new ResponseBody<>();
        
        Date start = null;
		Date end = null;
		
        List<Personnel> personnels = new ArrayList<>();
        Map<String,List<Integer>> listOfRecords = new HashMap<>();
        
        if(personnelIds.length > 0) {
        	for(int cnt=0; cnt<personnelIds.length; cnt++) {
        		Optional<Personnel> singlePersonnel = personnelRepository.findById(personnelIds[cnt]);
        		singlePersonnel.ifPresent(personnel -> personnels.add(personnel));
        		
        		try {
        			start = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
        			end = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
        		} catch (ParseException e) {
        			e.printStackTrace();
        		}
        		        		
				if(start != null) {
	        		int daysToAdd = DateUtil.calculateDaysToAddFromPeriod(start, end);
	        		
	        		// getRecords for period startDate ~ endDate > add by daysToAdd
	        		List<Integer> recordsForPersonnel = new ArrayList<>();
	        		while (start.before(end)) {
						Date startingDate = setDayToBegginingOfPeriod(start, daysToAdd);
						Date endingDate = setDayToEndingOfPeriod(start, daysToAdd);
						
						Integer records = appointmentRepository.countAllByPersonnelIdAndAppointmentDateBetween(singlePersonnel.get().getId(), startingDate, endingDate);
						
						start = DateUtil.setDays(start, daysToAdd);
						recordsForPersonnel.add(records);
	        		}
	        		listOfRecords.put(singlePersonnel.get().getAccount().getName(), recordsForPersonnel);
				}
        	}
        }
                		
        if(listOfRecords.isEmpty()) {
			String messageError = messageSource.getMessage("alert.error", null, LocaleContextHolder.getLocale());
        	result.setError(messageError);
        }else {
    		result.addResult(listOfRecords);
			String messageSuccess = messageSource.getMessage("alert.success", null, LocaleContextHolder.getLocale());
    		result.setError(messageSuccess);
    		result.setMessage(messageSuccess);
        }

        return ResponseEntity.ok(result);
    }
	
	/**
	 * @param Date startDate
	 * @param int daysToAdd
	 * @return
	 */
	private Date setDayToBegginingOfPeriod(Date start,int daysToAdd) {
		Date date = new Date();
		if(daysToAdd > 29 && daysToAdd<365) {
			date = DateUtil.setDayToBegginingOfMonth(start);
		}else if(daysToAdd >= 365) {
			date = DateUtil.setDayToBegginingOfYear(start);
		}else{
			date = DateUtil.setHoursToBegginingOfDay(start);
		} 
		return date;
	}
	
	/**
	 * @param Date startDate
	 * @param int daysToAdd
	 * @return
	 */
	private Date setDayToEndingOfPeriod(Date start,int daysToAdd) {
		Date date = new Date(); 
		if(daysToAdd > 29 && daysToAdd<365) {
			date = DateUtil.setDayToEndOfMonth(start);
		}else if(daysToAdd >= 365) {
			date = DateUtil.setDayToEndOfYear(start);
		}else{
			date = DateUtil.setDays(start, daysToAdd);
		} 
		return date;
	}

	@GetMapping("/api/availableAppointments")
    public ResponseEntity<?> getSearchResultViaAjax(@RequestParam(name = "personnelId", required = false) Long personnelId) { 
        ResponseBody<Appointment> result = new ResponseBody<>();
        
        List<Appointment> appointments = appointmentRepository.findByPersonnelId(personnelId);
        
        List<Appointment> userss = new ArrayList<>();
        if (userss.isEmpty()) {
			String messageNoUserFound = messageSource.getMessage("alert.noUserFound", null, LocaleContextHolder.getLocale());
            result.setMessage(messageNoUserFound); 
        } else {
			String messageSuccess = messageSource.getMessage("alert.success", null, LocaleContextHolder.getLocale());
            result.setMessage(messageSuccess);
        }
        result.setResult(appointments); 
        
        return ResponseEntity.ok(result);

    }
	
	@GetMapping("/api/getAvailablePersonnel")
    public ResponseEntity<?> getAvailablePersonnel() {
		
        ResponseBody<Personnel> result = new ResponseBody<>();

        List<Personnel> personnel = personnelRepository.findAllByAccount_EnabledAndAccount_ActiveAndAccount_Roles_Name(true, true, "PERSONNEL");
        if(personnel.size() != 0) {
        	result.setResult(personnel);
        }

        return ResponseEntity.ok(result);
	}
	
	@PostMapping("/api/account/changePassword")
    public ResponseEntity<?> changePassword(@RequestParam(name = "id", required = true) Long accountId, 
    		@RequestParam(name = "oldPassword", required = true) String oldPassword, 
    		@RequestParam(name = "newPassword", required = true) String newPassword) {
		
		ResponseBody<Personnel> result = new ResponseBody<>();

		Optional<Account> acc = accountRepository.findById(accountId);
		acc.ifPresent(account->{
			if((account.getPersonnel() != null) || (account.getCustomer() != null)){
				BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
				if(encoder.matches(oldPassword, account.getPassword())) {
					String pass = new BCryptPasswordEncoder().encode(newPassword);					
					account.setPassword(pass);
					accountRepository.saveAndFlush(account);
					String messageSuccess = messageSource.getMessage("alert.success", null, LocaleContextHolder.getLocale());
					result.setError(messageSuccess);
				}else {
			        
			        String messageError = messageSource.getMessage("alert.error", null, LocaleContextHolder.getLocale());
			        result.setError(messageError);
			        String messagePassNotSame = messageSource.getMessage("alert.passNotSame", null, LocaleContextHolder.getLocale());
					result.setMessage(messagePassNotSame);
				}
			}
		});

        return ResponseEntity.ok(result);
	}
	
	@PostMapping("/api/notifications/nextAppointments")
    public ResponseEntity<?> changePassword(@RequestParam(name = "accountId", required = true) Long accountId, 
    		@RequestParam(name = "startDate", required = true) Date startDate) {
		
		ResponseBody<Appointment> result = new ResponseBody<>();

		Optional<Account> acc = accountRepository.findById(accountId);
		acc.ifPresent(account->{
			
			if((account.getPersonnel() != null) || (account.getCustomer() != null)){
				
				Date start = startDate;
				Date end = DateUtil.addMinutes(start, 30);
				
				List<Appointment> all = appointmentRepository.findByPersonnelIdAndAppointmentDateBetweenOrderByAppointmentDateAsc(accountId, start, end);
				all.stream().forEach(appointment -> appointment.getPersonnel().getAccount().setPassword(""));

				if(all.isEmpty()) {
			        String messageError = messageSource.getMessage("alert.error", null, LocaleContextHolder.getLocale());
					result.setError(messageError);
			        String message = messageSource.getMessage("alert.noUpcomingAppointments", null, LocaleContextHolder.getLocale());
					result.setMessage(message);	
				}else {
			        String message = messageSource.getMessage("alert.success", null, LocaleContextHolder.getLocale());
					result.setError(message);
			        String uHave = messageSource.getMessage("YouHave", null, LocaleContextHolder.getLocale());
			        String upcomingAppointments = messageSource.getMessage("UpcomingAppointment", null, LocaleContextHolder.getLocale());

					result.setMessage(uHave+" "+ all.size() + " " +upcomingAppointments);
					result.setResult(all);
				}
			}else {
		        String messageError = messageSource.getMessage("alert.error", null, LocaleContextHolder.getLocale());
				result.setError(messageError);
		        String message = messageSource.getMessage("alert.errorOccurred", null, LocaleContextHolder.getLocale());
				result.setMessage(message);
			}
		});
		
        return ResponseEntity.ok(result);
	}
	
	@PostMapping("/api/personnel/sendConfirmation")
    public ResponseEntity<?> sendConfirmation(@RequestParam(name = "id", required = true) Long personnelId) {
		ResponseBody<Personnel> result = new ResponseBody<>();

		Optional<Account> acc = accountRepository.findById(personnelId);
		acc.ifPresent(account->{
	        String appUrl = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getContextPath();
	        try {
	        	eventPublisher.publishEvent(new OnRegistrationCompleteEvent(account, LocaleContextHolder.getLocale(), appUrl));
	        	String messageSuccess = messageSource.getMessage("alert.success", null, LocaleContextHolder.getLocale());
				result.setError(messageSuccess);
	            String confirmationEmailMsg = messageSource.getMessage("alert.confirmationEmailSent", null, LocaleContextHolder.getLocale());
	        	result.setMessage(confirmationEmailMsg);
	        }catch(Exception e) {
		        String messageError = messageSource.getMessage("alert.error", null, LocaleContextHolder.getLocale());
				result.setError(messageError);
	        	result.setMessage(e.getMessage());
	        }
		});
        return ResponseEntity.ok(result);
	}
	
}
