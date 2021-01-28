/**
 * 
 */
package com.visionous.dms.model;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.visionous.dms.configuration.AccountUserDetail;
import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.pojo.Account;
import com.visionous.dms.pojo.Customer;
import com.visionous.dms.pojo.History;
import com.visionous.dms.pojo.Personnel;
import com.visionous.dms.pojo.Record;
import com.visionous.dms.pojo.Teeth;
import com.visionous.dms.repository.AccountRepository;
import com.visionous.dms.repository.CustomerRepository;
import com.visionous.dms.repository.HistoryRepository;
import com.visionous.dms.repository.PersonnelRepository;
import com.visionous.dms.repository.RecordRepository;
import com.visionous.dms.repository.TeethRepository;

/**
 * @author delimeta
 *
 */
@Controller
public class HistoryModelController extends ModelControllerImpl{
	private final Log logger = LogFactory.getLog(HistoryModelController.class);
	private static String currentPage = LandingPages.HISTORY.value();

	private HistoryRepository historyRepository;
	private CustomerRepository customerRepository;
	private PersonnelRepository personnelRepository;
	private AccountRepository accountRepository;
	private TeethRepository teethRepository;
	private RecordRepository recordRepository;
	
	@Autowired
	private HistoryModelController(HistoryRepository historyRepository, RecordRepository recordRepository,
			CustomerRepository customerRepository, PersonnelRepository personnelRepository,
			AccountRepository accountRepository, TeethRepository teethRepository) {
		
		this.historyRepository = historyRepository;
		this.customerRepository = customerRepository;
		this.personnelRepository = personnelRepository;
		this.accountRepository = accountRepository;
		this.teethRepository = teethRepository;
		this.recordRepository = recordRepository;
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
					(History) super.getAllControllerParams().get("modelAttribute"), 
					super.getAllControllerParams().get("action").toString().toLowerCase()
				);
			}
		}
		
		// Build view
		this.buildHistoryViewModel(super.getAllControllerParams().get("viewType").toString().toLowerCase());
		
		// Build global view model for Personnel
		this.buildHistoryGlobalViewModel();
	}
	
	/**
	 * 
	 */
	private void persistModelAttributes(History historyNewModel, String action) {
		History newHistory = historyNewModel;

		if(action.equals(Actions.DELETE.getValue())) {
			
		}else if(action.equals(Actions.EDIT.getValue()) ) {
			
		}else if(action.equals(Actions.CREATE.getValue())) {

			if(super.getAllControllerParams().get("id") != null) {
				
				Optional<Customer> customer = customerRepository.findById(Long.valueOf(super.getAllControllerParams().get("id").toString()));

				customer.ifPresent(currentCustomer -> {
					newHistory.setCustomer(currentCustomer);
					newHistory.setRecords(null);
					newHistory.setStartdate(new Date());
					
					AccountUserDetail currentAccountDetails =  AccountUtil.currentLoggedInUser();	
					Optional<Account> loggedInAccount = accountRepository.findByUsername(currentAccountDetails.getUsername());
					
					loggedInAccount.ifPresent( account -> {
						System.out.println(account.toString());
						Optional<Personnel> supervisor = personnelRepository.findById(account.getId());
						
						newHistory.setSupervisor(supervisor.get());				

					});
					History createdNewHistory = historyRepository.saveAndFlush(newHistory);
					currentCustomer.setCustomerHistory(createdNewHistory);
					Customer editCustomer = customerRepository.saveAndFlush(currentCustomer);

				});
			}
		}else if(action.equals(Actions.VIEW.getValue())) {
		}		

	}

	/**
	 * 
	 */
	private void buildHistoryViewModel(String viewType) {
		super.addModelCollectionToView("viewType", viewType);
		
		if(viewType.equals(Actions.CREATE.getValue())) {
			
			Optional<Customer> customer = customerRepository.findById(Long.valueOf(super.getAllControllerParams().get("id").toString()));
			
			super.addModelCollectionToView("historyId", customer.get().getCustomerHistory().getId());
			super.addModelCollectionToView("selected", customer);
			
		}else if(viewType.equals(Actions.DELETE.getValue()) || viewType.equals(Actions.EDIT.getValue())) {

		}else if(viewType.equals(Actions.VIEW.getValue())) {
			Long customerId = (Long) super.getAllControllerParams().get("customerId");
			Optional<Customer> customer = customerRepository.findById(customerId);
			customer.ifPresent(single -> {
				super.addModelCollectionToView("selected", single);
				if(single.getCustomerHistory() != null) {
					Long historyId = single.getCustomerHistory().getId();
					List<Record> customerRecords = recordRepository.findTop5ByHistoryIdOrderByServicedateDesc(historyId);
					if(!customerRecords.isEmpty()){
						super.addModelCollectionToView("customerRecords", customerRecords);
					}
				}
			});
			
			List<Teeth> teeths = teethRepository.findAll();
			super.addModelCollectionToView("listTeeth", teeths);		

		}
	}

	/**
	 * 
	 */
	private void buildHistoryGlobalViewModel() {
		super.addModelCollectionToView("currentBreadcrumb", LandingPages.buildBreadCrumb(
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest())
		);
		super.addModelCollectionToView("currentPage", currentPage);
		super.addModelCollectionToView("currentRoles", AccountUtil.currentLoggedInUser().getRoles());
		
		Iterable<History> personnels = historyRepository.findAll();
		super.addModelCollectionToView("personnelList", personnels);
		
		Optional<Account> loggedInAccount = accountRepository.findByUsername(AccountUtil.currentLoggedInUser().getUsername());
		loggedInAccount.ifPresent(account -> {
			super.addModelCollectionToView("currentRoles", account.getRoles());
			super.addModelCollectionToView("loggedInAccount", account);
		});
		Locale locales = LocaleContextHolder.getLocale();
		super.addModelCollectionToView("locale", locales.getLanguage() + "_" + locales.getCountry());
	}
	
	/**
	 *
	 */
	@Override
	public <T> HistoryModelController addModelAttributes(T object){
		super.addControllerParam("modelAttribute", object);
		return this;
	}

}
