/**
 * 
 */
package com.visionous.dms.model;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
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
import com.visionous.dms.pojo.Personnel;
import com.visionous.dms.pojo.Record;
import com.visionous.dms.pojo.Role;
import com.visionous.dms.repository.AccountRepository;
import com.visionous.dms.repository.AppointmentRepository;
import com.visionous.dms.repository.CustomerRepository;
import com.visionous.dms.repository.HistoryRepository;
import com.visionous.dms.repository.PersonnelRepository;
import com.visionous.dms.repository.RecordRepository;
import com.visionous.dms.repository.RoleRepository;

/**
 * @author delimeta
 *
 */
@Controller
public class HomeModelController extends ModelControllerImpl{
	
	private final Log logger = LogFactory.getLog(HomeModelController.class);

	private AccountRepository accountRepository;
	private RecordRepository recordRepository;
	private HistoryRepository historyRepository;
	private AppointmentRepository appointmentRepository;
	private PersonnelRepository personnelRepository;
	private CustomerRepository customerRepository;
	private GlobalSettings globalSettings;
	
	private RoleRepository roleRepository;
	
	private static String currentPage = LandingPages.HOME.value();


	/**
	 * @param personnelRepository
	 */
	@Autowired
	public HomeModelController(AccountRepository accountRepository, RecordRepository recordRepository, 
			AppointmentRepository appointmentRepository, HistoryRepository historyRepository,
			PersonnelRepository personnelRepository, RoleRepository roleRepository,
			CustomerRepository customerRepository, GlobalSettings globalSettings){
		this.accountRepository = accountRepository;
		this.recordRepository = recordRepository;
		this.appointmentRepository = appointmentRepository;
		this.historyRepository = historyRepository;
		this.personnelRepository = personnelRepository;
		this.customerRepository = customerRepository;
		this.roleRepository = roleRepository;
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
		Customer newCustomer = customerNewModel;
		
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
			System.out.println(this.globalSettings.getBusinessEmail()+ " EMAIL");

			int startDay = 1;
			int endDay = 5;
			String startTime = "08:00";
			String endTime = "18:00";
			int bookingSplit = 60;
			if(this.globalSettings != null) {
				String[] dayPeriod = this.globalSettings.getBusinessDays().split(",");
				String[] timePeriod = this.globalSettings.getBusinessTimes().split(",");
				
				bookingSplit = this.globalSettings.getAppointmentTimeSplit();
				
				startDay = Integer.parseInt(dayPeriod[0]);
				endDay = Integer.parseInt(dayPeriod[1]);
				
				startTime = timePeriod[0];
				endTime = timePeriod[1];
			}

			Date appointmentEndTime = DateUtil.getEndWorkingHr(endTime);

			Account acc = AccountUtil.currentLoggedInUser();
			Optional<Account> currentAccount = accountRepository.findByUsername(acc.getUsername());
			currentAccount.ifPresent(account -> {
				
				Period monthPeriodOfThisYear = DateUtil.getPeriodBetween(DateUtil.getBegginingOfYear(), new Date());
				System.out.println( DateUtil.getStartWorkingHr() +"-----"+ appointmentEndTime);
				List<Appointment> todaysAppointments = appointmentRepository.findByPersonnelIdAndAppointmentDateBetweenOrderByAppointmentDateAsc(account.getId(), DateUtil.getStartWorkingHr(), appointmentEndTime);
				List<Record> top10records = recordRepository.findFirst10ByPersonnelIdOrderByServicedateDesc(account.getId()); 
				List<Integer> nrOfRecordsForEachMonth = new ArrayList<>();
				for(int month=0 ; month <= monthPeriodOfThisYear.getMonths(); month++) {

					Date beginMonthDate = DateUtil.getCurrentDateByMonthAndDay(month, 1);
										
					Date endMonthDate = DateUtil.getCurrentDateByMonthAndDay(month, DateUtil.getCalendarFromDate(beginMonthDate).getActualMaximum(Calendar.DAY_OF_MONTH));

					Integer countBetweenDates = recordRepository.countByPersonnelIdAndServicedateBetween(account.getId(), beginMonthDate, endMonthDate);
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
					
					Optional<Role> rolePersonnel = roleRepository.findByName("PERSONNEL");
					List<Account> allPersonnel = new ArrayList<>();
					rolePersonnel.ifPresent(role-> allPersonnel.addAll(accountRepository.findAllByActiveAndEnabledAndRoles_Name(true,true, role.getName())));
					
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
							Integer records = recordRepository.countAllByPersonnelIdAndServicedateBetween(selectedPersonnel.getId(), startCalendar.getTime(), endCalendar.getTime());
							Integer appointments = appointmentRepository.countAllByPersonnelIdAndAppointmentDateBetween(selectedPersonnel.getId(), startCalendar.getTime(), endCalendar.getTime());
							dataForAppointment.add(appointments);
							dataForRange.add(records);
							startDate = DateUtil.setDays(startDate, 1);
						}
						
						super.addModelCollectionToView("allRecords", dataForRange);
						super.addModelCollectionToView("allAppointments", dataForAppointment);
					}
					
					Date endsDate = new Date();
					Date startsDate = DateUtil.setDayToBegginingOfYear(endsDate);
					
					List<Record> allRecordsThisYear = recordRepository.findAllByServicedateBetween(startsDate, endsDate);
					
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
					countBetweenDates = accountRepository.countByEnabledAndActiveAndCustomer_RegisterdateBetween(true,true, beginMonthDate, endMonthDate);
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
		
		Optional<Account> loggedInAccount = accountRepository.findByUsername(AccountUtil.currentLoggedInUser().getUsername());
		loggedInAccount.ifPresent(account -> {
			super.addModelCollectionToView("currentRoles", account.getRoles());
			super.addModelCollectionToView("loggedInAccount", account);
		});
		Locale locales = LocaleContextHolder.getLocale();
		super.addModelCollectionToView("locale", locales.getLanguage() + "_" + locales.getCountry());
		
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
