/**
 * 
 */
package com.visionous.dms.model;

import java.text.DateFormatSymbols;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
import com.visionous.dms.pojo.Record;
import com.visionous.dms.repository.AccountRepository;
import com.visionous.dms.repository.AppointmentRepository;
import com.visionous.dms.repository.HistoryRepository;
import com.visionous.dms.repository.RecordRepository;
import com.visionous.dms.repository.RoleRepository;

/**
 * @author delimeta
 *
 */
@Component
public class HomeModelController extends ModelControllerImpl{
	
	private final Log logger = LogFactory.getLog(HomeModelController.class);

	private AccountRepository accountRepository;
	private RecordRepository recordRepository;
	private HistoryRepository historyRepository;
	private AppointmentRepository appointmentRepository;
	
	private static String currentPage = LandingPages.HOME.value();


	/**
	 * @param personnelRepository
	 */
	@Autowired
	public HomeModelController(AccountRepository accountRepository, RecordRepository recordRepository, AppointmentRepository appointmentRepository, HistoryRepository historyRepository) {
		this.accountRepository = accountRepository;
		this.recordRepository = recordRepository;
		this.appointmentRepository = appointmentRepository;
		this.historyRepository = historyRepository;
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
			Account acc = AccountUtil.currentLoggedInUser();
			Optional<Account> currentAccount = accountRepository.findByUsername(acc.getUsername());
			currentAccount.ifPresent(account -> {
				
				Period monthPeriodOfThisYear = DateUtil.getPeriodBetween(DateUtil.getBegginingOfYear(), new Date());
				
				List<Appointment> todaysAppointments = appointmentRepository.findByPersonnelIdAndAppointmentDateBetweenOrderByAppointmentDateAsc(account.getId(), DateUtil.getStartWorkingHr(), DateUtil.getEndWorkingHr());
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
		super.addModelCollectionToView("currentRoles", AccountUtil.currentLoggedInUser().getRoles());
		
		
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
