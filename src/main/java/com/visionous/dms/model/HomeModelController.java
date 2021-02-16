/**
 * 
 */
package com.visionous.dms.model;

import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.configuration.helpers.DateUtil;
import com.visionous.dms.pojo.Account;
import com.visionous.dms.pojo.Appointment;
import com.visionous.dms.pojo.Customer;
import com.visionous.dms.pojo.GlobalSettings;
import com.visionous.dms.pojo.Record;
import com.visionous.dms.pojo.Role;
import com.visionous.dms.service.AccountService;
import com.visionous.dms.service.AppointmentService;
import com.visionous.dms.service.RecordService;
import com.visionous.dms.service.RoleService;

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
	private GlobalSettings globalSettings;
	
	private RoleService roleService;
	
	private static String currentPage = LandingPages.HOME.value();


	/**
	 * @param personnelRepository
	 */
	@Autowired
	public HomeModelController(AccountService accountService, RecordService recordService, 
			AppointmentService appointmentService, RoleService roleService,
			GlobalSettings globalSettings){
		this.accountService = accountService;
		this.recordService = recordService;
		this.appointmentService = appointmentService;
		this.roleService = roleService;
		this.globalSettings = globalSettings;
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

			String endTime = this.globalSettings.getBusinessEndTime();

			Date appointmentEndTime = DateUtil.getEndWorkingHr(endTime);

			Account acc = AccountUtil.currentLoggedInUser();
			Optional<Account> currentAccount = accountService.findByUsernameOrEmail(acc.getUsername());
			currentAccount.ifPresent(account -> {
				
				Period monthPeriodOfThisYear = DateUtil.getPeriodBetween(DateUtil.getBegginingOfYear(), new Date());
				System.out.println( DateUtil.getStartWorkingHr() +"-----"+ appointmentEndTime);
				List<Appointment> todaysAppointments = appointmentService.findByPersonnelIdAndAppointmentDateBetweenOrderByAppointmentDateAsc(account.getId(), DateUtil.getStartWorkingHr(), appointmentEndTime);
				List<Record> top10records = recordService.findFirst10ByPersonnelIdOrderByServicedateDesc(account.getId()); 
				List<Integer> nrOfRecordsForEachMonth = new ArrayList<>();
				 for(int month=0 ; month <= monthPeriodOfThisYear.getMonths(); month++) {

					Date beginMonthDate = DateUtil.getCurrentDateByMonthAndDay(month, 1);
										
					Date endMonthDate = DateUtil.getCurrentDateByMonthAndDay(month, DateUtil.getCalendarFromDate(beginMonthDate).getActualMaximum(Calendar.DAY_OF_MONTH));

					Integer countBetweenDates = recordService.countByPersonnelIdAndServicedateBetween(account.getId(), beginMonthDate, endMonthDate);
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
					
					Optional<Role> rolePersonnel = roleService.findByName("PERSONNEL");
					List<Account> allPersonnel = new ArrayList<>();
					rolePersonnel.ifPresent(role-> allPersonnel.addAll(accountService.findAllByActiveAndEnabledAndRoles_Name(true,true, role.getName())));
					
					if(!allPersonnel.isEmpty()) {
						super.addModelCollectionToView("allPersonnel", allPersonnel);
						
						Account selectedPersonnel = allPersonnel.get(0);
						
						Date currentDate = new Date();
						Date startDate = DateUtil.getOneWeekBefore(new Date());
						
						List<Integer> dataForRange = new ArrayList<>();
						List<Integer> dataForAppointment = new ArrayList<>();
						super.addModelCollectionToView("dateRangeInit", new SimpleDateFormat("dd/MM/yyyy").format(startDate.getTime()) + " - " + new SimpleDateFormat("dd/MM/yyyy").format(currentDate.getTime()));

						while (startDate.before(currentDate)) { 
							Date end = startDate;
							Calendar endCalendar = DateUtil.getCalendarFromDate(end);
							endCalendar.set(Calendar.HOUR_OF_DAY, 23);
							Calendar startCalendar = DateUtil.getCalendarFromDate(startDate);
							startCalendar.set(Calendar.HOUR_OF_DAY, 0);
							Integer records = recordService.countAllByPersonnelIdAndServicedateBetween(selectedPersonnel.getId(), startCalendar.getTime(), endCalendar.getTime());
							Integer appointments = appointmentService.countAllByPersonnelIdAndAppointmentDateBetween(selectedPersonnel.getId(), startCalendar.getTime(), endCalendar.getTime());
							dataForAppointment.add(appointments);
							dataForRange.add(records);
							startDate = DateUtil.setDays(startDate, 1);
						}
						
						super.addModelCollectionToView("allRecords", dataForRange);
						super.addModelCollectionToView("allAppointments", dataForAppointment);
					}
					
					Date endsDate = new Date();
					Date startsDate = DateUtil.setDayToBegginingOfYear(endsDate);
					
					List<Record> allRecordsThisYear = recordService.findAllByServicedateBetween(startsDate, endsDate);
					
					Map<String, Integer> allNewAndOldCustomers = new HashMap<>();
					Integer allNewCustomers= new Integer(0); 
					Integer allOldCustomers= new Integer(0);				
					
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
				}
				
				Period periodOfThisYear = DateUtil.getPeriodBetween(DateUtil.getBegginingOfYear(), new Date());
				
				List<Integer> allNewCustomersForEachMonth = new ArrayList<>();
				for(int month=0 ; month <= periodOfThisYear.getMonths(); month++) {
					
					Date beginMonthDate = DateUtil.getCurrentDateByMonthAndDay(month, 1);
										
					Date endMonthDate = DateUtil.getCurrentDateByMonthAndDay(month, DateUtil.getCalendarFromDate(beginMonthDate).getActualMaximum(Calendar.DAY_OF_MONTH));
					Integer countBetweenDates = 0;
					countBetweenDates = accountService.countByEnabledAndActiveAndCustomer_RegisterdateBetween(true,true, beginMonthDate, endMonthDate);
					allNewCustomersForEachMonth.add(countBetweenDates);
				}
				
				if(!allNewCustomersForEachMonth.isEmpty()) {
					super.addModelCollectionToView("allNewCustomers", allNewCustomersForEachMonth);
				}
					
				
			});
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
		
		Optional<Account> loggedInAccount = accountService.findByUsernameOrEmail(AccountUtil.currentLoggedInUser().getUsername());
		loggedInAccount.ifPresent(account -> {
			super.addModelCollectionToView("currentRoles", account.getRoles());
			super.addModelCollectionToView("loggedInAccount", account);
		});
		
		super.addModelCollectionToView("locale", AccountUtil.getCurrentLocaleLanguageAndCountry());
		
		super.addModelCollectionToView("logo", globalSettings.getBusinessImage());

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
