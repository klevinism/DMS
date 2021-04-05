/**
 * 
 */
package com.visionous.dms.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.visionous.dms.configuration.helpers.DateUtil;
import com.visionous.dms.configuration.helpers.DmsCore;
import com.visionous.dms.event.OnCustomerAppointmentBookEvent;
import com.visionous.dms.event.OnRegistrationCompleteEvent;
import com.visionous.dms.pojo.Account;
import com.visionous.dms.pojo.Appointment;
import com.visionous.dms.pojo.Customer;
import com.visionous.dms.pojo.GlobalSettings;
import com.visionous.dms.pojo.Personnel;
import com.visionous.dms.pojo.ServiceType;
import com.visionous.dms.rest.response.ResponseBody;
import com.visionous.dms.service.AccountService;
import com.visionous.dms.service.AppointmentService;
import com.visionous.dms.service.CustomerService;
import com.visionous.dms.service.PersonnelService;
import com.visionous.dms.service.RecordService;
import com.visionous.dms.service.ServiceTypeService;

/**
 * @author delimeta
 *
 */
@RestController
public class AppointmentRestController {
		
	private AppointmentService appointmentService;
	private PersonnelService personnelService;
	
	private ServiceTypeService serviceTypeService;
	private CustomerService customerService;
	private AccountService accountService;
	private RecordService recordService;
	
	private ApplicationEventPublisher eventPublisher;
	private MessageSource messageSource;

	private GlobalSettings globalSettings;
	/**
	 * 
	 */
	@Autowired
	public AppointmentRestController(AppointmentService appointmentService, PersonnelService personnelService, 
			CustomerService customerService, MessageSource messageSource, 
			RecordService recordService, AccountService accountService, GlobalSettings globalSettings,
			ServiceTypeService serviceTypeService, ApplicationEventPublisher eventPublisher) {
		
		this.appointmentService = appointmentService;
		this.personnelService = personnelService;
		this.globalSettings = globalSettings;
		this.serviceTypeService = serviceTypeService;
		
		this.customerService = customerService;
		this.recordService = recordService;
		
		this.accountService = accountService;
		
		this.eventPublisher = eventPublisher;
		this.messageSource = messageSource;
	}
	
	@PostMapping("/api/book")
    public ResponseEntity<?> bookAppointment(@RequestParam(name = "customerId", required = true) Long customerId,
    		@RequestParam(name = "personnelId", required = true) Long personnelId,
    		@RequestParam(name = "appointmentDate", required = true) Date appointmentDate,
    		@RequestParam(name = "serviceId", required = false) Long serviceId,
    		@RequestParam(name = "appointmentEndDate", required = false) Date appointmentEndDate) { 

        ResponseBody<Appointment> result = new ResponseBody<>();
        
        List<Appointment> anyAppointment = appointmentService.findByAppointmentDate(appointmentDate);
        
        String noAppointmentSet = messageSource.getMessage("alert.noAppointmentSet", null, LocaleContextHolder.getLocale());
        String success = messageSource.getMessage("alert.success", null, LocaleContextHolder.getLocale());
        String appointmentset = messageSource.getMessage("alert.appointmentSet", null, LocaleContextHolder.getLocale());
        String errorCreatingAppointment = messageSource.getMessage("alert.errorCreatingAppointment", null, LocaleContextHolder.getLocale());
        		
        if(!anyAppointment.isEmpty()) {
        	result.setError("error");
        	result.setMessage(noAppointmentSet + " " + new SimpleDateFormat("dd-MM-yy hh:mm").format(appointmentDate));
        }else {
        	Appointment newAppointment = new Appointment();
        	Optional<Personnel> personnel = personnelService.findById(personnelId);
        	Optional<Customer> customer = customerService.findById(customerId);
        	
        	if(personnel.isPresent() && customer.isPresent()) {
        		newAppointment.setPersonnel(personnel.get());
        		newAppointment.setCustomer(customer.get());
        	}
        	newAppointment.setAddeddate(new Date());
        	newAppointment.setAppointmentDate(appointmentDate);
        	
        	if(serviceId != null) {
        		Optional<ServiceType> serviceSelected = serviceTypeService.findById(serviceId);
        		serviceSelected.ifPresent(selected -> {
        			newAppointment.setServiceType(selected);
        			newAppointment.setServiceTypeId(selected.getId());
        		});
        	}
        	
        	if(appointmentEndDate != null) {
        		newAppointment.setAppointmentEndDate(appointmentEndDate);
        	}else {
        		newAppointment.setAppointmentEndDate(DateUtil.addMinutes(appointmentDate, globalSettings.getAppointmentTimeSplit()));
        	}
        	
        	Appointment created = appointmentService.create(newAppointment);
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
	
	/**
	 * @param appointmentId
	 * @return
	 */
	@GetMapping("/api/appointment/sendVerification")
	public ResponseEntity<?> sendAppointmentVerification(@RequestParam(name = "id", required = true) Long appointmentId){
        ResponseBody<Appointment> result = new ResponseBody<>();
        
        String error = messageSource.getMessage("alert.error", null, LocaleContextHolder.getLocale());
        result.setError(error);
        
		Optional<Appointment> appointmentFound = appointmentService.findById(appointmentId);
		appointmentFound.ifPresent(appointment -> {
			eventPublisher.publishEvent(
	        		new OnCustomerAppointmentBookEvent(appointment, LocaleContextHolder.getLocale(), DmsCore.appMainPath())
	    		);
	        String success = messageSource.getMessage("alert.success", null, LocaleContextHolder.getLocale());
	        result.setError(success);
		});
		
		return ResponseEntity.ok(result);
	}
	
	@PostMapping("/api/appointment/edit")
    public ResponseEntity<?> editAppointment(@RequestParam(name = "id", required = true) Long appointmentId,
    		@RequestParam(name = "serviceId", required = false) Long serviceId,
    		@RequestParam(name = "customerId", required = false) Long customerId,
    		@RequestParam(name = "personnelId", required = false) Long personnelId,
    		@RequestParam(name = "appointmentDate", required = false) Date appointmentDate,
    		@RequestParam(name = "appointmentEndDate", required = false) Date appointmentEndDate) { 

        ResponseBody<Appointment> result = new ResponseBody<>();
        
        Optional<Appointment> selectedAppointment = appointmentService.findById(appointmentId);
        
        String noAppointmentWithId = messageSource.getMessage("alert.noAppointmentWithId", null, LocaleContextHolder.getLocale());
        String success = messageSource.getMessage("alert.success", null, LocaleContextHolder.getLocale());
        String appointmentset = messageSource.getMessage("alert.appointmentSet", null, LocaleContextHolder.getLocale());
        String errorCreatingAppointment = messageSource.getMessage("alert.errorCreatingAppointment", null, LocaleContextHolder.getLocale());
        		
        if(!selectedAppointment.isPresent()) {
        	result.setError("error");
        	result.setMessage(noAppointmentWithId + " " + appointmentId);
        }else {
        	Appointment newAppointment = selectedAppointment.get();
        	if(appointmentDate != null) {
        		newAppointment.setAppointmentDate(appointmentDate);
        	}else {
        		newAppointment.setAppointmentDate(new Date());
        	}
        	
        	if(appointmentEndDate != null) {
        		newAppointment.setAppointmentEndDate(appointmentEndDate);
        	}else {
        		newAppointment.setAppointmentEndDate(DateUtil.addMinutes(appointmentDate, 30));
        	}
        	
        	newAppointment.setServiceType(null);
        	if(serviceId != null) {
        		Optional<ServiceType> serviceSelected = serviceTypeService.findById(serviceId);
        		serviceSelected.ifPresent(selected -> {
        			newAppointment.setServiceType(selected);
        			newAppointment.setServiceTypeId(selected.getId());
        		});
        	}
        	
        	if(customerId != null) {
        		Optional<Customer> customerSelected = customerService.findById(customerId);
        		customerSelected.ifPresent(customer -> {
        			newAppointment.setCustomer(customer);
        			newAppointment.setCustomerId(customer.getId());
        		});
        	}
        	
        	if(personnelId != null) {
        		Optional<Personnel> personnelSelected = personnelService.findById(personnelId);
        		personnelSelected.ifPresent(customer -> {
        			newAppointment.setPersonnel(customer);
        			newAppointment.setPersonnelId(customer.getId());
        		});
        	}
        	
        	newAppointment.setAddeddate(new Date());
        	
        	Appointment created = appointmentService.create(newAppointment);
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
	
	@PostMapping("/api/appointment/delete")
    public ResponseEntity<?> deleteAppointment(@RequestParam(name = "id", required = true) Long appointmentId) { 

        ResponseBody<Appointment> result = new ResponseBody<>();
        
        Optional<Appointment> selectedAppointment = appointmentService.findById(appointmentId);
        
        String noAppointmentWithId = messageSource.getMessage("alert.noAppointmentWithId", null, LocaleContextHolder.getLocale());
        String success = messageSource.getMessage("alert.success", null, LocaleContextHolder.getLocale());
        String appointmentset = messageSource.getMessage("alert.appointmentSet", null, LocaleContextHolder.getLocale());

        if(!selectedAppointment.isPresent()) {
        	result.setError("error");
        	result.setMessage(noAppointmentWithId + " " + appointmentId);
        }else {
    		appointmentService.delete(selectedAppointment.get());
    		result.setError(success);
    		result.setMessage(appointmentset);
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
        		Optional<Personnel> singlePersonnel = personnelService.findById(personnelIds[cnt]);
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
						
						Integer records = recordService.countByPersonnelIdAndServicedateBetween(singlePersonnel.get().getId(), startingDate, endingDate);

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
        		Optional<Personnel> singlePersonnel = personnelService.findById(personnelIds[cnt]);
        		singlePersonnel.ifPresent(personnel -> personnels.add(personnel));
        		
        		try {
        			start = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
        			end = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
        		} catch (ParseException e) {
        			e.printStackTrace();
        		}
        		        		
				if(start != null && singlePersonnel.isPresent()) {
	        		int daysToAdd = DateUtil.calculateDaysToAddFromPeriod(start, end);
	        		
	        		// getRecords for period startDate ~ endDate > add by daysToAdd
	        		List<Integer> recordsForPersonnel = new ArrayList<>();
	        		while (start.before(end)) {
						Date startingDate = setDayToBegginingOfPeriod(start, daysToAdd);
						Date endingDate = setDayToEndingOfPeriod(start, daysToAdd);
						
						Integer records = appointmentService.countAllByPersonnelIdAndAppointmentDateBetween(singlePersonnel.get().getId(), startingDate, endingDate);
						
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
    public ResponseEntity<?> getSearchResultViaAjax(@RequestParam(name = "personnelId", required = true) Long personnelId) { 
        ResponseBody<Appointment> result = new ResponseBody<>();
        
        List<Appointment> appointments = appointmentService.findByPersonnelId(personnelId);
        
        if (appointments.isEmpty()) {
			String messageNoUserFound = messageSource.getMessage("alert.noUserFound", null, LocaleContextHolder.getLocale());
            result.setMessage(messageNoUserFound); 
        } else {
			String messageSuccess = messageSource.getMessage("alert.success", null, LocaleContextHolder.getLocale());
            result.setMessage(messageSuccess);
        }
        result.setResult(appointments); 
        
        return ResponseEntity.ok(result);

    }
	
	@GetMapping("/api/appointment/automatic")
    public ResponseEntity<?> getAutomaticAppointment() { 
        ResponseBody<Appointment> result = new ResponseBody<>();
        
        int minSplit = globalSettings.getAppointmentTimeSplit();

        Date today = new Date();
        Calendar todayCalendar = DateUtil.getCalendarFromDate(today);
        todayCalendar.set(Calendar.MINUTE, 0);
        todayCalendar.set(Calendar.SECOND, 0);
        todayCalendar.set(Calendar.MILLISECOND, 0);
        
        List<Personnel> allPersonnel = this.personnelService.findAllByAccount_EnabledAndAccount_ActiveAndAccount_Roles_Name(true, true, "PERSONNEL");

    	Appointment autoAppointed = null;
    	boolean found = false;
    	int count = 10;
        while(autoAppointed == null) {
            Date newAppointmentDateTime = nextAvailableBusinessAppointmentTime(todayCalendar.getTime());
        	Date newAppointmentEndDateTime = DateUtil.addMinutes(newAppointmentDateTime, minSplit);

        	for(int x=0; x<allPersonnel.size(); x++) { 
    			List<Appointment> foundAppointment = this.appointmentService.findAllByPersonnelIdBetweenDateRange(allPersonnel.get(x).getId(), newAppointmentDateTime, newAppointmentEndDateTime);

    			if(foundAppointment.isEmpty()) {
    				autoAppointed = new Appointment();
    				autoAppointed.setAppointmentDate(newAppointmentDateTime);
    				autoAppointed.setAppointmentEndDate(newAppointmentEndDateTime);
    				autoAppointed.setPersonnelId(allPersonnel.get(x).getId());
    				autoAppointed.setPersonnel(allPersonnel.get(x));
    				found = true;
    				break;
    			}
            }
        	if(found) {
        		break;
        	}else {
        		todayCalendar.setTime(DateUtil.addMinutes(todayCalendar.getTime(), minSplit));
        	}
        	
        	if(count == 0) {
        		break;
        	}
        	
        	count--;
        }
        
        if(autoAppointed != null) {
        	String messageSuccess = messageSource.getMessage("alert.success", null, LocaleContextHolder.getLocale());
    		result.setError(messageSuccess);
    		result.addResult(autoAppointed);	
        }else {
			String messageError = messageSource.getMessage("alert.error", null, LocaleContextHolder.getLocale());
        	result.setError(messageError);

	        String message = messageSource.getMessage("alert.errorOccurred", null, LocaleContextHolder.getLocale());
			result.setMessage(message);
        }
        	
        return ResponseEntity.ok(result);

    }
	
	private Date searchForNewDay(Date date) {
        int startDay = globalSettings.getBusinessStartDay();
        int endDay = globalSettings.getBusinessEndDay();
                
        Calendar calendar = DateUtil.getCalendarFromDate(date); 
        int todayDayNr = calendar.get(Calendar.DAY_OF_WEEK)-1;
        
        while(todayDayNr < startDay || todayDayNr > endDay) {
        	calendar.setTime(DateUtil.addDays(calendar.getTime(), 1));        	
        	todayDayNr = calendar.get(Calendar.DAY_OF_WEEK)-1;
        }
    	
        return calendar.getTime();
	}
	
	private Date nextAvailableBusinessAppointmentTime(Date date) {

        int minSplit = globalSettings.getAppointmentTimeSplit();

        Date today = date;        
        Date startTime = DateUtil.getStartWorkingHr(globalSettings.getBusinessStartTime(), today);
        Date endTime = DateUtil.getEndWorkingHr(globalSettings.getBusinessEndTime(), today);
                
		while(today.before(startTime) || today.after(endTime)) {

			today = DateUtil.addMinutes(today, minSplit);
			int tHr  = DateUtil.getCalendarFromDate(today).get(Calendar.HOUR);
			int tMin  = DateUtil.getCalendarFromDate(today).get(Calendar.MINUTE);
			int sHr  = DateUtil.getCalendarFromDate(startTime).get(Calendar.HOUR);
			int sMin  = DateUtil.getCalendarFromDate(startTime).get(Calendar.MINUTE);

			
			if((tHr == sHr) && (tMin == sMin)) {
				break;
			}
		}
		
		Date newDate = searchForNewDay(today);
		
		if(DateUtil.getPeriodBetween(today, newDate).getDays() > 0) {
			int hrs = DateUtil.getCalendarFromDate(startTime).get(Calendar.HOUR_OF_DAY);
			int mins = DateUtil.getCalendarFromDate(startTime).get(Calendar.MINUTE);
			
			newDate = DateUtil.setHoursToDate(newDate, hrs);
			newDate = DateUtil.setMinutesToDate(newDate, mins);
			today = newDate;
		}
		
		return today;
	}
	
	@GetMapping("/api/availableAppointmentsByDateRange")
    public ResponseEntity<?> getAvailableAppointments(@RequestParam(name = "personnelId", required = true) Long personnelId,
    		@RequestParam(name = "start", required = true) Date startRange,
    		@RequestParam(name = "end", required = true) Date endRange) {
		
        ResponseBody<Appointment> result = new ResponseBody<>();
        List<Appointment> appointments = appointmentService.findAllByPersonnelIdBetweenDateRange(personnelId, startRange, endRange);

        if (appointments.isEmpty()) {
			String messageNoUserFound = messageSource.getMessage("alert.noUpcomingAppointments", null, LocaleContextHolder.getLocale());
            result.setMessage(messageNoUserFound); 
        } else {
			String messageSuccess = messageSource.getMessage("alert.success", null, LocaleContextHolder.getLocale());
            result.setMessage(messageSuccess);
        }
        result.setResult(appointments); 
        
        return ResponseEntity.ok(result);

    }
	
	@GetMapping("/api/allAppointmentsByDateRange")
    public ResponseEntity<?> getAllAppointmentsByDateRange(@RequestParam(name = "personnelId", required = false) Long personnelId,
    		@RequestParam(name = "start", required = true) Date startRange,
    		@RequestParam(name = "end", required = true) Date endRange) {
		
        ResponseBody<Appointment> result = new ResponseBody<>();
        
        if(personnelId != null) {
        	return this.getAvailableAppointments(personnelId, startRange, endRange);
        }else {
        	List<Appointment> appointments = appointmentService.findAllBetweenDateRange(startRange, endRange);

            if (appointments.isEmpty()) {
    			String messageNoUserFound = messageSource.getMessage("alert.noUpcomingAppointments", null, LocaleContextHolder.getLocale());
                result.setMessage(messageNoUserFound); 
            } else {
    			String messageSuccess = messageSource.getMessage("alert.success", null, LocaleContextHolder.getLocale());
                result.setMessage(messageSuccess);
            }
            result.setResult(appointments); 
            
            return ResponseEntity.ok(result);
        }
	}
	
	
	@GetMapping("/api/getAvailablePersonnel")
    public ResponseEntity<?> getAvailablePersonnel() {
		
        ResponseBody<Personnel> result = new ResponseBody<>();

        List<Personnel> personnel = personnelService.findAllByAccount_EnabledAndAccount_ActiveAndAccount_Roles_Name(true, true, "PERSONNEL");
        if(personnel.size() != 0) {
        	result.setResult(personnel);
        }

        return ResponseEntity.ok(result);
	}
	
	@GetMapping("/api/getAvailableCustomers")
    public ResponseEntity<?> getAvailableCustomer() {
		
        ResponseBody<Customer> result = new ResponseBody<>();

        List<Customer> customer = customerService.findAll();
        if(customer.size() != 0) {
        	result.setResult(customer);
        }

        return ResponseEntity.ok(result);
	}
	
	@PostMapping("/api/account/changePassword")
    public ResponseEntity<?> changePassword(@RequestParam(name = "id", required = true) Long accountId, 
    		@RequestParam(name = "oldPassword", required = true) String oldPassword, 
    		@RequestParam(name = "newPassword", required = true) String newPassword) {
		
		ResponseBody<Personnel> result = new ResponseBody<>();

		Optional<Account> acc = accountService.findById(accountId);
		acc.ifPresent(account->{
			if((account.getPersonnel() != null) || (account.getCustomer() != null)){
				BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
				if(encoder.matches(oldPassword, account.getPassword())) {
					String pass = new BCryptPasswordEncoder().encode(newPassword);					
					account.setPassword(pass);
					accountService.createPlain(account);
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

		Optional<Account> acc = accountService.findById(accountId);
		acc.ifPresent(account->{
			
			if((account.getPersonnel() != null) || (account.getCustomer() != null)){
				
				Date start = startDate;
				Date end = DateUtil.addMinutes(start, 30);
				
				List<Appointment> all = appointmentService.findByPersonnelIdAndAppointmentDateBetweenOrderByAppointmentDateAsc(accountId, start, end);
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

		Optional<Account> acc = accountService.findById(personnelId);
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
