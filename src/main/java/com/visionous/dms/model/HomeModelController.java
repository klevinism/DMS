package com.visionous.dms.model;

import com.o2dent.lib.accounts.entity.Account;
import com.o2dent.lib.accounts.persistence.AccountService;
import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.configuration.helpers.DateUtil;
import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.pojo.Appointment;
import com.visionous.dms.pojo.Customer;
import com.visionous.dms.pojo.Record;
import com.visionous.dms.service.AppointmentService;
import com.visionous.dms.service.CustomerService;
import com.visionous.dms.service.PersonnelService;
import com.visionous.dms.service.RecordService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author delimeta
 *
 */
@Controller
public class HomeModelController extends ModelControllerImpl{
	
	private final Log logger = LogFactory.getLog(HomeModelController.class);

	private AccountService accountService;
	private RecordService recordService;
	private AppointmentService appointmentService;
    private MessageSource messages;
	private CustomerService customerService;
	private PersonnelService personnelService;



	private static String currentPage = LandingPages.HOME.value();


	/**
	 *
	 * @param accountService
	 * @param recordService
	 * @param appointmentService
	 * @param messages
	 */
	@Autowired
	public HomeModelController(AccountService accountService, RecordService recordService,
							   AppointmentService appointmentService, MessageSource messages,
							   CustomerService customerService, PersonnelService personnelService){
		this.accountService = accountService;
		this.recordService = recordService;
		this.appointmentService = appointmentService;
		this.messages = messages;
		this.customerService = customerService;
		this.personnelService = personnelService;
	}
	
	/**
	 *
	 */
	@Override
	public void run() {
		// If action occurred, persist object to db
		if(super.getAllControllerParams().containsKey("modelAttribute")) {
			if(super.getAllControllerParams().containsKey("action")) {
				persistModelAttributes(
					(Customer) super.getAllControllerParams().get("modelAttribute"), 
					super.getAllControllerParams().get("action").toString().toLowerCase()
					);
			}
		}
		
		// Build view
		this.buildHomeViewModel(super.getAllControllerParams().get("viewType").toString().toLowerCase());
		
		// Build global view model for Customer
		this.buildHomeGlobalViewModel();
	}
	
	/**
	 * 
	 */
	private void persistModelAttributes(Customer customerNewModel, String action) {

		if(action.equals(Actions.DELETE.getValue())) {
		}else if(action.equals(Actions.EDIT.getValue()) ) {
		}else if(action.equals(Actions.CREATE.getValue())) {		
		}else if(action.equals(Actions.VIEW.getValue())) {
		}		
	
	}
	
	/**
	 * 
	 */
	private void buildHomeViewModel(String viewType) {
		super.addModelCollectionToView("viewType", viewType);
		
		if(viewType.equals(Actions.CREATE.getValue())) {
		}else if(viewType.equals(Actions.DELETE.getValue()) || viewType.equals(Actions.EDIT.getValue())) {
		}else if(viewType.equals(Actions.VIEW.getValue())) {

			String endTime = AccountUtil.currentLoggedInUser().getCurrentBusinessSettings().getBusinessEndTime();

			Date appointmentEndTime = DateUtil.getEndWorkingHr(endTime);

			Account account = AccountUtil.currentLoggedInUser();

			Period monthPeriodOfThisYear = DateUtil.getPeriodBetween(DateUtil.getBegginingOfYear(), new Date());
			
			List<Appointment> todaysAppointments = appointmentService.findByPersonnelIdAndAppointmentDateBetweenOrderByAppointmentDateAsc(account.getId(),
					new Timestamp(DateUtil.getStartWorkingHr().getTime()).toLocalDateTime(), 
					new Timestamp(appointmentEndTime.getTime()).toLocalDateTime());
			
			List<Record> top10records = recordService.findFirst10ByPersonnelIdOrderByServicedateDesc(account.getId()); 
			List<Integer> nrOfRecordsForEachMonth = new ArrayList<>();
			 for(int month=0 ; month <= monthPeriodOfThisYear.getMonths(); month++) {

				Date beginMonthDate = DateUtil.getCurrentDateByMonthAndDay(month, 1);
									
				Date endMonthDate = DateUtil.getCurrentDateByMonthAndDay(month, DateUtil.getCalendarFromDate(beginMonthDate).getActualMaximum(Calendar.DAY_OF_MONTH));
				
				Integer countBetweenDates = recordService.countByPersonnelIdAndServicedateBetween(account.getId(), 
						new Timestamp(beginMonthDate.getTime()).toLocalDateTime(), new Timestamp(endMonthDate.getTime()).toLocalDateTime());
				nrOfRecordsForEachMonth.add(countBetweenDates);
			}
			
			if(!nrOfRecordsForEachMonth.isEmpty()) {
				super.addModelCollectionToView("nrOfVisitsList", nrOfRecordsForEachMonth);
			}
			
			if(!todaysAppointments.isEmpty()) {
				super.addModelCollectionToView("appointmentList", todaysAppointments);
			}
			
			if(!top10records.isEmpty()) {
				super.addModelCollectionToView("lastVisitList", top10records); 
			}
			
			
			if(account.getRoles().get(0).getName().equals("ADMIN")) {
				
				List<String> roles = new ArrayList<>();
				roles.add("PERSONNEL");
				roles.add("ADMIN");
				
				List<Account> allPersonnel = accountService.findAllByAccountBusinessIdAndActiveAndEnabledAndRoles_NameIn(AccountUtil.currentLoggedInBussines().getId(), true, true, roles);
				
				if(!allPersonnel.isEmpty()) {
					super.addModelCollectionToView("allPersonnel", allPersonnel);
					
					Account selectedPersonnel = allPersonnel.get(0);
					
					Date currentDate = new Date();
					Date startDate = DateUtil.getOneWeekBefore(new Date());
					
					List<Integer> dataForRange = new ArrayList<>();
					List<Integer> dataForAppointment = new ArrayList<>();
					List<Integer> dataForRevenue = new ArrayList<>();
					super.addModelCollectionToView("dateRangeInit", new SimpleDateFormat("dd/MM/yyyy").format(startDate.getTime()) + " - " + new SimpleDateFormat("dd/MM/yyyy").format(currentDate.getTime()));

					while (startDate.before(currentDate)) { 
						Date end = startDate;
						Calendar endCalendar = DateUtil.getCalendarFromDate(end);
						endCalendar.set(Calendar.HOUR_OF_DAY, 23);
						Calendar startCalendar = DateUtil.getCalendarFromDate(startDate);
						startCalendar.set(Calendar.HOUR_OF_DAY, 0);
						
						Integer records = recordService.countByPersonnelIdAndServicedateBetween(selectedPersonnel.getId(),
								new Timestamp(startCalendar.getTime().getTime()).toLocalDateTime(), new Timestamp(endCalendar.getTime().getTime()).toLocalDateTime());
						
						Integer appointments = appointmentService.findByPersonnelIdAndAppointmentDateBetweenOrderByAppointmentDateAsc(selectedPersonnel.getId(),
								new Timestamp(startCalendar.getTime().getTime()).toLocalDateTime(), new Timestamp(endCalendar.getTime().getTime()).toLocalDateTime()).size();
						
						Integer singlePersonnelRevenue = recordService.sumOfPersonnelReceiptsAndBusinessId(selectedPersonnel.getId(),
								new Timestamp(startCalendar.getTime().getTime()).toLocalDateTime(), new Timestamp(endCalendar.getTime().getTime()).toLocalDateTime());
						
						dataForAppointment.add(appointments != null ? appointments : 0);
						dataForRange.add(records != null ? records : 0);
						dataForRevenue.add(singlePersonnelRevenue != null ? singlePersonnelRevenue : 0);
						
						startDate = DateUtil.addDays(startDate, 1);
					}
					
					super.addModelCollectionToView("allRecords", dataForRange);
					super.addModelCollectionToView("allAppointments", dataForAppointment);
					super.addModelCollectionToView("allRevenues", dataForRevenue);

				}
				
				Date endsDate = new Date();
				Date startsDate = DateUtil.setDayToBegginingOfYear(endsDate);

				List<Account> personnelList = accountService.findAllByBusinessIdAndRoles_NameIn(
						AccountUtil.currentLoggedInBussines().getId(),
						List.of("ADMIN", "PERSONNEL"));

				List<Record> allRecordsThisYear = recordService.findAllByPersonnelIdInAndServicedateBetween(
						personnelList.stream().map(acc -> acc.getId()).collect(Collectors.toList()),
						new Timestamp(startsDate.getTime()).toLocalDateTime(), 
						new Timestamp(endsDate.getTime()).toLocalDateTime());
				
				Map<String, Integer> allNewAndOldCustomers = new HashMap<>();
				Integer allNewCustomers= new AtomicInteger(0).get(); 
				Integer allOldCustomers= new AtomicInteger(0).get();				
				
				allNewAndOldCustomers.put("oldcustomers", allOldCustomers);
				allNewAndOldCustomers.put("newcustomers", allNewCustomers);
				
				if(!allRecordsThisYear.isEmpty()) {
					allRecordsThisYear.forEach(record -> {
						if(record.getHistory().getCustomer().getRegisterdate().before(startsDate)) {
							allNewAndOldCustomers.replace("oldcustomers", allNewAndOldCustomers.get("oldcustomers") + 1);
						}else {
							allNewAndOldCustomers.replace("newcustomers", allNewAndOldCustomers.get("newcustomers") + 1);
						}
					});
				}

				super.addModelCollectionToView("allCustomers", allNewAndOldCustomers);
				
				List<Date> lastWorkingDays = new ArrayList<>();
				List<Integer> workingDays = AccountUtil.currentLoggedInUser().getCurrentBusinessSettings().getWorkingBusinessDays();
				
				Calendar currentCalendar = DateUtil.getCalendarFromDate(DateUtil.subtractDays(new Date(), 1));
				
				int maxDaysOfWeek = currentCalendar.getActualMaximum(Calendar.DAY_OF_WEEK);

				while(lastWorkingDays.size() != maxDaysOfWeek) {
					if(workingDays.contains((DateUtil.getNrDayOfWeek(currentCalendar.getTime())-1))) {
						lastWorkingDays.add(currentCalendar.getTime());
					}else {
					}
					currentCalendar.setTime(DateUtil.subtractDays(currentCalendar.getTime(), 1));
				}
				
				List<String> lastWorkingDates = new ArrayList<>();
				
				List<Integer> nrOfVisitsForWorkingDays = new ArrayList<>();
				List<Integer> nrOfRevenueForWorkingDays = new ArrayList<>();
				List<Integer> nrOfNewCustomersForWorkingDays = new ArrayList<>();
				List<Integer> nrOfAppointmentsNoShows = new ArrayList<>();
				String yesterday = messages.getMessage("Yesterday", null, LocaleContextHolder.getLocale());

				lastWorkingDays.forEach(date -> {
					Date startDate = DateUtil.getBeginingOfDay(date);
					Date endDate = DateUtil.getEndOfDay(date);
					
					if(DateUtil.getPeriodBetween(startDate, new Date()).getDays() == 1) {
						lastWorkingDates.add(yesterday);
						super.addModelCollectionToView("isYesterday", true);
					}else {
						lastWorkingDates.add(new SimpleDateFormat(" dd MMMM ").format(startDate));
					}
					
					nrOfVisitsForWorkingDays.add(recordService.countByPersonnelIdAndServicedateBetween(AccountUtil.currentLoggedInBussines().getId(), new Timestamp(startDate.getTime()).toLocalDateTime(), new Timestamp(endDate.getTime()).toLocalDateTime()));
					
					Integer currentRevenue = recordService
							.sumOfReceiptsByPersonnelIdInAndServiceDate(
									personnelList.stream().map(p -> p.getId()).collect(Collectors.toList()),
									new Timestamp(startDate.getTime()).toLocalDateTime(), 
									new Timestamp(endDate.getTime()).toLocalDateTime()); 
					
					nrOfRevenueForWorkingDays.add(currentRevenue == null ? 0 : currentRevenue);


					List<Customer> customerList = customerService.findAllByRegisterDateBetween(startDate, endDate);
 					List<Account> customerAccounts = accountService.findByBusinesses_IdAndIdIn(AccountUtil.currentLoggedInBussines().getId(), customerList.stream().map(c -> c.getId()).collect(Collectors.toList()));
					List<Long> customerIds = customerAccounts.stream().map(c -> c.getId()).collect(Collectors.toList());
					nrOfNewCustomersForWorkingDays.add(
								accountService.countByBusinesses_IdAndEnabledAndActiveAndIdIn(AccountUtil.currentLoggedInBussines().getId(), true, true, customerIds));

					List<Appointment> allAppointmentsBtwDates = appointmentService.findAllByPersonnelIdInAndBetweenDateRange(
							personnelList.stream().map(p -> p.getId()).collect(Collectors.toList()),
							new Timestamp(startDate.getTime()).toLocalDateTime(), 
							new Timestamp(endDate.getTime()).toLocalDateTime());
					
					List<Appointment> noShowAppointmentsForDay = allAppointmentsBtwDates.stream().filter(appointment -> 
					recordService.findAllByServicedateBetweenAndCustomerId(
							appointment.getAppointmentDate().withHour(0).withMinute(0),
							appointment.getAppointmentEndDate().withHour(23).withMinute(0),
							appointment.getCustomerId()).isEmpty()
						).collect(Collectors.toList());
					
					nrOfAppointmentsNoShows.add(noShowAppointmentsForDay.size());
				});
				
				Collections.reverse(lastWorkingDates);
				
				Collections.reverse(nrOfVisitsForWorkingDays);
				Collections.reverse(nrOfRevenueForWorkingDays);
				Collections.reverse(nrOfNewCustomersForWorkingDays);
				Collections.reverse(nrOfAppointmentsNoShows);
				
				int inc = nrOfVisitsForWorkingDays.get(nrOfVisitsForWorkingDays.size()-1) - nrOfVisitsForWorkingDays.get(0);
				double visitGrowth;
				if(inc == 0) {
					visitGrowth = 0;
				}else {
					if(!nrOfVisitsForWorkingDays.get(nrOfVisitsForWorkingDays.size()-1).equals(0)) {
						visitGrowth = (inc/nrOfVisitsForWorkingDays.get(nrOfVisitsForWorkingDays.size()-1) )*100;
					}else {
						visitGrowth = -100;
					}
				}
				
				int startNr = nrOfRevenueForWorkingDays.get(nrOfRevenueForWorkingDays.size()-1);
				int endNr = nrOfRevenueForWorkingDays.get(0);
				int incRevenue = startNr - endNr;
				double revenueGrowth;
				if(incRevenue == 0) {
					revenueGrowth = 0;
				}else {
					if(startNr != 0) {
						revenueGrowth = (incRevenue/startNr)*100;
					}else {
						revenueGrowth = -100;
					}
				}
				
				int startNewCustomerNr = nrOfNewCustomersForWorkingDays.get(nrOfNewCustomersForWorkingDays.size() - 1);
				int endNewCustomerNr = nrOfNewCustomersForWorkingDays.get(0);
				int incNewCustomers = startNewCustomerNr - endNewCustomerNr;
				double newCustomerGrowth;
				if(incNewCustomers == 0) {
					newCustomerGrowth = 0;
				}else {
					if(startNewCustomerNr != 0) {
						newCustomerGrowth = (incNewCustomers/startNewCustomerNr)*100;
					}else {
						newCustomerGrowth = -100;
					}
				}
				
				int startNoShowsNr = nrOfAppointmentsNoShows.get(nrOfAppointmentsNoShows.size() - 1);
				int endNoShowsNr = nrOfAppointmentsNoShows.get(0);
				int incNoShows = startNoShowsNr - endNoShowsNr;
				double noShowGrowth;
				if(incNoShows == 0) {
					noShowGrowth = 0;
				}else {
					if(startNoShowsNr != 0) {
						noShowGrowth = (incNoShows/startNoShowsNr)*100;
					}else {
						noShowGrowth = -100;
					}
				}
				
				super.addModelCollectionToView("lastWorkingDates", lastWorkingDates);

				super.addModelCollectionToView("nrOfAppointmentsNoShows", nrOfAppointmentsNoShows);
				super.addModelCollectionToView("noShowGrowth", noShowGrowth);
				
				super.addModelCollectionToView("nrOfNewCustomersForWorkingDays", nrOfNewCustomersForWorkingDays);
				super.addModelCollectionToView("newCustomerGrowth", newCustomerGrowth);
				
				super.addModelCollectionToView("nrOfRevenueForWorkingDays", nrOfRevenueForWorkingDays);
				super.addModelCollectionToView("revenueGrowth", revenueGrowth);

				super.addModelCollectionToView("nrOfVisitsForWorkingDays", nrOfVisitsForWorkingDays);
				super.addModelCollectionToView("visitsGrowth", visitGrowth);
			}

			Period periodOfThisYear = DateUtil.getPeriodBetween(DateUtil.getBegginingOfYear(), new Date());
			
			Integer sumNewCustomersForEachMonth = 0;
			Integer sumVisitsForEachMonth = 0;
			Integer sumRevenueForEachMonth = 0;
			Integer sumNoShowsForEachMonth = 0;

			List<Integer> allVisitsForEachMonth = new ArrayList<>();
			List<Integer> allRevenueForEachMonth = new ArrayList<>();
			List<Integer> allNoShowsForEachMonth = new ArrayList<>();
			List<Integer> allNewCustomersForEachMonth = new ArrayList<>();
			for(int month=0 ; month <= periodOfThisYear.getMonths(); month++) {
				
				Date beginMonthDate = DateUtil.setHoursToBegginingOfDay(DateUtil.getCurrentDateByMonthAndDay(month, 1));
				
				Date endMonthDate = DateUtil.setHoursToEndingOfDay(
						DateUtil.getCurrentDateByMonthAndDay(
								month, 
								DateUtil.getCalendarFromDate(beginMonthDate).getActualMaximum(Calendar.DAY_OF_MONTH)
						)
				);
				List<Account> personnelList = accountService.findAllByBusinessIdAndRoles_NameIn(AccountUtil.currentLoggedInBussines().getId(), List.of("ADMIN", "PERSONNEL"));

				List<Customer> customerList = customerService.findAllByRegisterDateBetween(beginMonthDate, endMonthDate);
				List<Account> accountList = accountService.findByBusinesses_IdAndIdIn(AccountUtil.currentLoggedInBussines().getId(), customerList.stream().map(c -> c.getId()).collect(Collectors.toList()));
				List<Long> customerIds = accountList.stream().map(c -> c.getId()).collect(Collectors.toList());
				
				Integer countBetweenDates = accountService.countByBusinesses_IdAndEnabledAndActiveAndIdIn(AccountUtil.currentLoggedInBussines().getId(), true, true, customerIds);
				
				Integer totalVisitBtwDates = recordService.countAllByPersonnelIdInAndServicedateBetween(
						personnelList.stream().map(p -> p.getId()).collect(Collectors.toList()),
						new Timestamp(beginMonthDate.getTime()).toLocalDateTime(), new Timestamp(endMonthDate.getTime()).toLocalDateTime());
				
				Integer totalRevenueBtwDates = recordService.sumOfReceiptsByPersonnelIdInAndServiceDate(
						personnelList.stream().map(p -> p.getId()).collect(Collectors.toList()),
						new Timestamp(beginMonthDate.getTime()).toLocalDateTime(),
						new Timestamp(endMonthDate.getTime()).toLocalDateTime());

				List<Appointment> allAppointments= appointmentService.findAllByPersonnelIdInAndBetweenDateRange(
						personnelList.stream().map(p->p.getId()).collect(Collectors.toList()),
						new Timestamp(beginMonthDate.getTime()).toLocalDateTime(), 
						new Timestamp(endMonthDate.getTime()).toLocalDateTime());
				
				List<Appointment> noShowAppointmentsBtwDates = allAppointments.stream().filter(appointment -> 
				recordService.findAllByServicedateBetweenAndCustomerId(
						appointment.getAppointmentDate().withHour(0).withMinute(0),
						appointment.getAppointmentEndDate().withHour(23).withMinute(0),
						appointment.getCustomerId()).isEmpty()
				).collect(Collectors.toList());
				
				
				allNewCustomersForEachMonth.add(countBetweenDates);
				allVisitsForEachMonth.add(totalVisitBtwDates);
				allRevenueForEachMonth.add(totalRevenueBtwDates != null ? totalRevenueBtwDates : 0);
				allNoShowsForEachMonth.add(noShowAppointmentsBtwDates.size());
				
				sumNewCustomersForEachMonth += countBetweenDates;
				sumVisitsForEachMonth += totalVisitBtwDates;
				sumRevenueForEachMonth += totalRevenueBtwDates != null ? totalRevenueBtwDates : 0;
				sumNoShowsForEachMonth += noShowAppointmentsBtwDates.size();
			}
			
			if(!allNewCustomersForEachMonth.isEmpty()) {
				super.addModelCollectionToView("allNewCustomers", allNewCustomersForEachMonth);
				super.addModelCollectionToView("sumNewCustomers", sumNewCustomersForEachMonth);
			}

			if(!allVisitsForEachMonth.isEmpty()) {
				super.addModelCollectionToView("allVisitsThisYear", allVisitsForEachMonth);
				super.addModelCollectionToView("sumVisitsThisYear", sumVisitsForEachMonth);
			}
			
			if(!allRevenueForEachMonth.isEmpty()) {
				super.addModelCollectionToView("allRevenueThisYear", allRevenueForEachMonth);
				super.addModelCollectionToView("sumRevenueThisYear", sumRevenueForEachMonth);
			}
			
			if(!allNoShowsForEachMonth.isEmpty()) {
				super.addModelCollectionToView("allNoShowsThisYear", allNoShowsForEachMonth);
				super.addModelCollectionToView("sumNoShowsThisYear", sumNoShowsForEachMonth);
			}
			
			super.addModelCollectionToView("isMorning", DateUtil.getCalendarFromDate(new Date()).get(Calendar.HOUR_OF_DAY) <= 10 ? true : false);
				
		}
	}
	
	/**
	 * 
	 */
	private void buildHomeGlobalViewModel() {
		super.addModelCollectionToView("currentBreadcrumb", LandingPages.buildBreadCrumb(
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest())
		);
		 
		super.addModelCollectionToView("currentPage", currentPage);
		
		super.addModelCollectionToView("currentRoles", AccountUtil.currentLoggedInUser().getAccount().getRoles());
		super.addModelCollectionToView("loggedInAccount", AccountUtil.currentLoggedInUser().getAccount());
		
		super.addModelCollectionToView("currentUser", AccountUtil.currentLoggedInUser());
		
		super.addModelCollectionToView("locale", AccountUtil.getCurrentLocaleLanguageAndCountry());
		
		super.addModelCollectionToView("logo", AccountUtil.currentLoggedInUser().getCurrentBusinessSettings().getBusinessImage());
		super.addModelCollectionToView("settings", AccountUtil.currentLoggedInUser().getCurrentBusinessSettings());
		
		super.addModelCollectionToView("subscription", AccountUtil.currentLoggedInUser().getCurrentBusinessSettings().getActiveSubscription());
	}
	
	/**
	 * @return CustomerModelController
	 */
	@Override
	public <T> HomeModelController addModelAttributes(T object){
		super.addControllerParam("modelAttribute", object);
		return this;
	}
}
