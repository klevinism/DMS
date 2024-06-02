/**
 * 
 */
package com.visionous.dms.rest;

import com.o2dent.lib.accounts.entity.Account;
import com.o2dent.lib.accounts.persistence.AccountService;
import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.DateUtil;
import com.visionous.dms.configuration.helpers.DmsCore;
import com.visionous.dms.event.OnCustomerAppointmentBookEvent;
import com.visionous.dms.event.OnRegistrationCompleteEvent;
import com.visionous.dms.pojo.*;
import com.visionous.dms.rest.response.ResponseBody;
import com.visionous.dms.service.*;
import org.apache.http.HttpStatus;
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

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

	/**
	 * 
	 */
	@Autowired
	public AppointmentRestController(AppointmentService appointmentService, PersonnelService personnelService, 
			CustomerService customerService, MessageSource messageSource, 
			RecordService recordService, AccountService accountService,
			ServiceTypeService serviceTypeService, ApplicationEventPublisher eventPublisher) {
		
		this.appointmentService = appointmentService;
		this.personnelService = personnelService;
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
        
        List<Appointment> anyAppointment = appointmentService.findByAppointmentDate(new Timestamp(appointmentDate.getTime()).toLocalDateTime());
        
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
        	newAppointment.setAppointmentDate(new Timestamp(appointmentDate.getTime()).toLocalDateTime());
        	
        	if(serviceId != null) {
        		Optional<ServiceType> serviceSelected = serviceTypeService.findById(serviceId);
        		serviceSelected.ifPresent(selected -> {
        			newAppointment.setServiceType(selected);
        			newAppointment.setServiceTypeId(selected.getId());
        		});
        	}
        	
        	if(appointmentEndDate != null) {
        		newAppointment.setAppointmentEndDate(
        				new Timestamp(appointmentEndDate.getTime()).toLocalDateTime());
        	}else {
        		Date endingDate = DateUtil.addMinutes(appointmentDate, AccountUtil.currentLoggedInUser().getCurrentBusinessSettings().getAppointmentTimeSplit());
        		newAppointment.setAppointmentEndDate(
        				new Timestamp(endingDate.getTime()).toLocalDateTime());
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
        		newAppointment.setAppointmentDate(new Timestamp(appointmentDate.getTime()).toLocalDateTime());
        	}else {
        		newAppointment.setAppointmentDate(LocalDateTime.now());
        	}
        	
        	if(appointmentEndDate != null) {
        		newAppointment.setAppointmentEndDate(new Timestamp(appointmentEndDate.getTime()).toLocalDateTime());
        	}else {
        		Date endingDate = DateUtil.addMinutes(appointmentDate, 30);
        		newAppointment.setAppointmentEndDate(new Timestamp(endingDate.getTime()).toLocalDateTime());
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
		String vizits = messageSource.getMessage("Visits", null, LocaleContextHolder.getLocale());

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
							endingDate = DateUtil.addDays(start, daysToAdd);
						} 
						
						Integer records = recordService.countByPersonnelIdAndServicedateBetween(singlePersonnel.get().getId(), new Timestamp(startingDate.getTime()).toLocalDateTime(),
								new Timestamp(endingDate.getTime()).toLocalDateTime());

						start = DateUtil.addDays(start, daysToAdd);
						recordsForPersonnel.add(records);
	        		}
	        		listOfRecords.put(vizits, recordsForPersonnel);
	        		
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
	
	@PostMapping("/api/personnel/statistics/revenue")
    public ResponseEntity<?> personnelStatisticsRevenue(@RequestParam(name = "personnelId", required = true) Long personnelId,
    		@RequestParam(name = "startDate", required = true) String startDate,
    		@RequestParam(name = "endDate", required = true) String endDate){ 

        ResponseBody<Map<String,List<Integer>>> result = new ResponseBody<>();
		String revenue = messageSource.getMessage("Revenue", null, LocaleContextHolder.getLocale());

		Date start = null;
		Date end = null;
		
        Map<String,List<Integer>> listOfRevenue = new HashMap<>();
        		
		try {
			start = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
			end = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(start != null) {
			        		        		
    		int daysToAdd = DateUtil.calculateDaysToAddFromPeriod(start, end);

    		// getRecords for period startDate ~ endDate > add by daysToAdd
    		List<Integer> revenueForPersonnel = new ArrayList<>();
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
					endingDate = DateUtil.addDays(start, daysToAdd);
				} 
				
				Integer revenueForPeriod = recordService.sumOfPersonnelReceiptsAndBusinessId(personnelId, new Timestamp(startingDate.getTime()).toLocalDateTime(), 
						new Timestamp(endingDate.getTime()).toLocalDateTime());
				
				revenueForPersonnel.add(revenueForPeriod != null ? revenueForPeriod : 0);

				start = DateUtil.addDays(start, daysToAdd);
    		}
    		listOfRevenue.put(revenue, revenueForPersonnel);
        }
                		
        if(listOfRevenue.isEmpty()) {

			String messageError = messageSource.getMessage("alert.error", null, LocaleContextHolder.getLocale());
    		result.setError(messageError);
        }else {
    		result.addResult(listOfRevenue);
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
		String appointments = messageSource.getMessage("Appointments", null, LocaleContextHolder.getLocale());

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
						
						List<Appointment> appointmentsList = appointmentService.findByPersonnelIdAndAppointmentDateBetweenOrderByAppointmentDateAsc(singlePersonnel.get().getId(), new Timestamp(startingDate.getTime()).toLocalDateTime(), new Timestamp(endingDate.getTime()).toLocalDateTime());
						
						start = DateUtil.addDays(start, daysToAdd);
						recordsForPersonnel.add(appointmentsList.size());
	        		}
	        		listOfRecords.put(appointments, recordsForPersonnel);
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
	 *
	 * @param start
	 * @param daysToAdd
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
	 *
	 * @param start
	 * @param daysToAdd
	 * @return
	 */
	private Date setDayToEndingOfPeriod(Date start,int daysToAdd) {
		Date date = new Date(); 
		if(daysToAdd > 29 && daysToAdd<365) {
			date = DateUtil.setDayToEndOfMonth(start);
		}else if(daysToAdd >= 365) {
			date = DateUtil.setDayToEndOfYear(start);
		}else{
			date = DateUtil.addDays(start, daysToAdd);
		} 
		return date;
	}

	@GetMapping("/api/availableAppointments")
    public ResponseEntity<?> getSearchResultViaAjax(@RequestParam(name = "personnelId", required = true) Long personnelId) { 
        ResponseBody<Appointment> result = new ResponseBody<>();
        List<Account> accounts = accountService.findByBusinesses_IdAndIdIn(AccountUtil.currentLoggedInBussines().getId(), List.of(personnelId));
        List<Appointment> appointments = appointmentService.findByPersonnelIdIn(accounts.stream().map(a -> a.getId()).collect(Collectors.toList()));
        
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
        
        int minSplit = AccountUtil.currentLoggedInUser().getCurrentBusinessSettings().getAppointmentTimeSplit();

        Date today = new Date();
        Calendar todayCalendar = DateUtil.getCalendarFromDate(today);
        todayCalendar.set(Calendar.MINUTE, 0);
        todayCalendar.set(Calendar.SECOND, 0);
        todayCalendar.set(Calendar.MILLISECOND, 0);
        
        List<Account> allAccount = this.accountService.findAllByEnabledAndActiveAndRoles_Name(true, true, "PERSONNEL");
		List<Personnel> allPersonnel = this.personnelService.findByIdIn(allAccount.stream().map(a -> a.getId()).collect(Collectors.toList()));

    	Appointment autoAppointed = null;
    	boolean found = false;
    	int count = 10;
        while(autoAppointed == null) {
            Date newAppointmentDateTime = nextAvailableBusinessAppointmentTime(todayCalendar.getTime());
        	Date newAppointmentEndDateTime = DateUtil.addMinutes(newAppointmentDateTime, minSplit);

        	for(int x=0; x<allPersonnel.size(); x++) { 
    			List<Appointment> foundAppointment = this.appointmentService.findAllByPersonnelIdBetweenDateRange(allPersonnel.get(x).getId(), new Timestamp(newAppointmentDateTime.getTime()).toLocalDateTime(), new Timestamp(newAppointmentEndDateTime.getTime()).toLocalDateTime());

    			if(foundAppointment.isEmpty()) {
    				autoAppointed = new Appointment();
    				autoAppointed.setAppointmentDate(new Timestamp(newAppointmentDateTime.getTime()).toLocalDateTime());
    				autoAppointed.setAppointmentEndDate(new Timestamp(newAppointmentEndDateTime.getTime()).toLocalDateTime());
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
        int startDay = AccountUtil.currentLoggedInUser().getCurrentBusinessSettings().getBusinessStartDay();
        int endDay = AccountUtil.currentLoggedInUser().getCurrentBusinessSettings().getBusinessEndDay();
                
        Calendar calendar = DateUtil.getCalendarFromDate(date); 
        int todayDayNr = calendar.get(Calendar.DAY_OF_WEEK)-1;
        
        while(todayDayNr < startDay || todayDayNr > endDay) {
        	calendar.setTime(DateUtil.addDays(calendar.getTime(), 1));        	
        	todayDayNr = calendar.get(Calendar.DAY_OF_WEEK)-1;
        }
    	
        return calendar.getTime();
	}
	
	private Date nextAvailableBusinessAppointmentTime(Date date) {

        int minSplit = AccountUtil.currentLoggedInUser().getCurrentBusinessSettings().getAppointmentTimeSplit();

        Date today = date;        
        Date startTime = DateUtil.getStartWorkingHr(AccountUtil.currentLoggedInUser().getCurrentBusinessSettings().getBusinessStartTime(), today);
        Date endTime = DateUtil.getEndWorkingHr(AccountUtil.currentLoggedInUser().getCurrentBusinessSettings().getBusinessEndTime(), today);
                
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

		Optional<Account> account = accountService.findById(personnelId);

		if(account.isPresent()){
			List<Appointment> appointments = appointmentService.findAllByPersonnelIdInAndBetweenDateRange(List.of(account.get().getId()), new Timestamp(startRange.getTime()).toLocalDateTime(), new Timestamp(endRange.getTime()).toLocalDateTime());

			if (appointments.isEmpty()) {
				String messageNoUserFound = messageSource.getMessage("alert.noUpcomingAppointments", null, LocaleContextHolder.getLocale());
				result.setMessage(messageNoUserFound);
			} else {
				String messageSuccess = messageSource.getMessage("alert.success", null, LocaleContextHolder.getLocale());
				result.setMessage(messageSuccess);
			}
			result.setResult(appointments);
			return ResponseEntity.ok(result);

		}else{
			var response = new ResponseBody<>();
			response.setError("Cannot find personnel with id ["+ personnelId +"]");
			return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(response);
		}
    }
	
	@GetMapping("/api/allAppointmentsByDateRange")
    public ResponseEntity<?> getAllAppointmentsByDateRange(@RequestParam(name = "personnelId", required = false) Long personnelId,
    		@RequestParam(name = "start", required = true) Date startRange,
    		@RequestParam(name = "end", required = true) Date endRange) {
		
        ResponseBody<IaoAppointment> result = new ResponseBody<>();
        
        if(personnelId != null) {
        	return this.getAvailableAppointments(personnelId, startRange, endRange);
        }else {
			List<Account> personnelList = accountService.findAllByBusinessIdAndRoles_NameIn(AccountUtil.currentLoggedInBussines().getId(), List.of("ADMIN","PERSONNEL"));
			List<Appointment> appointments = appointmentService.findAllByPersonnelIdInAndBetweenDateRange(
					personnelList.stream().map(p->p.getId()).collect(Collectors.toList()),
					new Timestamp(startRange.getTime()).toLocalDateTime(),
					new Timestamp(endRange.getTime()).toLocalDateTime());

            if (appointments.isEmpty()) {
    			String messageNoUserFound = messageSource.getMessage("alert.noUpcomingAppointments", null, LocaleContextHolder.getLocale());
                result.setMessage(messageNoUserFound); 
            } else {
    			String messageSuccess = messageSource.getMessage("alert.success", null, LocaleContextHolder.getLocale());
                result.setMessage(messageSuccess);
            }

			List<IaoAppointment> iaoAppointments = appointments.stream().map(a -> {
				IaoAppointment iao = new IaoAppointment(a);

				Optional<Account> personnelAccount = accountService.findById(a.getPersonnelId());
				personnelAccount.ifPresent(acc -> iao.setPersonnelAccount(acc));

				Optional<Account> customerAccount = accountService.findById(a.getCustomerId());
				customerAccount.ifPresent(acc -> iao.setCustomerAccount(acc));

				return iao;
			}).collect(Collectors.toList());



            result.setResult(iaoAppointments);
            
            return ResponseEntity.ok(result);
        }
	}
	
	
	@GetMapping("/api/getAvailablePersonnel")
    public ResponseEntity<?> getAvailablePersonnel() {
		
        ResponseBody<Account> result = new ResponseBody<>();
        List<String> roles = new ArrayList<>();
        roles.add("PERSONNEL");
        roles.add("ADMIN");
        List<Account> accounts = accountService.findAllByAccountBusinessIdAndActiveAndEnabledAndRoles_NameIn(AccountUtil.currentLoggedInUser().getCurrentBusiness().getId(), true, true, roles);
		List<Personnel> personnel = personnelService.findByIdIn(accounts.stream().map(a->a.getId()).collect(Collectors.toList()));

        if(personnel.size() != 0) {
        	result.setResult(accounts);
        }

        return ResponseEntity.ok(result);
	}
	
	@GetMapping("/api/getAvailableCustomers")
    public ResponseEntity<?> getAvailableCustomer() {
		
        ResponseBody<Account> result = new ResponseBody<>();

		List<Account> accountList = accountService.findAllByBusinessIdAndRoles_Name(AccountUtil.currentLoggedInUser().getCurrentBusiness().getId(), "CUSTOMER");
        List<Customer> customer = customerService.findAllByIdIn(accountList.stream().map(a -> a.getId()).collect(Collectors.toList()));
        if(customer.size() != 0) {
        	result.setResult(accountList);
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
			Optional<Customer> customer = customerService.findById(account.getId());
			Optional<Personnel> personnel = personnelService.findById(account.getId());
			if((personnel.isPresent()) || (customer.isPresent())){
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
    public ResponseEntity<?> nextAppointments(@RequestParam(name = "accountId", required = true) Long accountId, 
    		@RequestParam(name = "startDate", required = true) Date startDate) {
		
		ResponseBody<Appointment> result = new ResponseBody<>();

		Optional<Account> acc = accountService.findById(accountId);
		acc.ifPresent(account->{
			Optional<Customer> customer = customerService.findById(account.getId());
			Optional<Personnel> personnel = personnelService.findById(account.getId());
			if((personnel.isPresent()) || (customer.isPresent())){
				Date start = startDate;
				Date end = DateUtil.addMinutes(start, 30);
				
				List<Appointment> all = appointmentService.findByPersonnelIdAndAppointmentDateBetweenOrderByAppointmentDateAsc(accountId, 
						new Timestamp(start.getTime()).toLocalDateTime(), new Timestamp(end.getTime()).toLocalDateTime());

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
	
	@PostMapping("/api/subscription")
    public ResponseEntity<?> subscription(@RequestParam(name = "status", required = false) String status) {
		ResponseBody<String> result = new ResponseBody<>();
		if(status != null && status.equals("expired")) {
			result.setError("error"); 
			result.setMessage("Subscription expired! from Mapping");
		}
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
