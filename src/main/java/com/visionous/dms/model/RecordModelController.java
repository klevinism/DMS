/**
 * 
 */
package com.visionous.dms.model;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.pojo.Account;
import com.visionous.dms.pojo.Customer;
import com.visionous.dms.pojo.History;
import com.visionous.dms.pojo.Personnel;
import com.visionous.dms.pojo.Record;
import com.visionous.dms.pojo.Role;
import com.visionous.dms.repository.CustomerRepository;
import com.visionous.dms.repository.HistoryRepository;
import com.visionous.dms.repository.RecordRepository;

/**
 * @author delimeta
 *
 */
@Service
public class RecordModelController extends ModelController {
	private final Log logger = LogFactory.getLog(RecordModelController.class);
	
	private static String currentPage = LandingPages.RECORD.value();

	private RecordRepository recordRepository;
	private CustomerRepository customerRepository;
	private HistoryRepository historyRepository;
	
	/**
	 * 
	 */
	@Autowired
	public RecordModelController(RecordRepository recordRepository, CustomerRepository customerRepository, HistoryRepository historyRepository) {
		this.recordRepository = recordRepository;
		this.customerRepository = customerRepository;
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
						(Record) super.getAllControllerParams().get("modelAttribute"), 
						super.getAllControllerParams().get("action").toString().toLowerCase()
						);
			}
		}
		
		// Build view
		this.buildRecordViewModel(super.getAllControllerParams().get("viewType").toString().toLowerCase());
		
		// Build global view model for Personnel
		this.buildRecordGlobalViewModel();
	}
	
	/**
	 * 
	 */
	private void persistModelAttributes(Record recordNewModel, String action) {
		Record newRecord = recordNewModel;
		
		if(action.equals(Actions.DELETE.getValue())) {
			
		}else if(action.equals(Actions.EDIT.getValue()) ) {
		}else if(action.equals(Actions.CREATE.getValue())) {
		}else if(action.equals(Actions.VIEW.getValue())) {
		}		

	}

	/**
	 * 
	 */
	private void buildRecordViewModel(String viewType) {
		super.addModelCollectionToView("viewType", viewType);
		
		if(viewType.equals(Actions.CREATE.getValue())) {
			if(super.getAllControllerParams().get("historyId") != null) {
				Long customerId = Long.valueOf(super.getAllControllerParams().get("customerId").toString());
				Long historyId = Long.valueOf(super.getAllControllerParams().get("historyId").toString());
				Optional<Customer> recordCustomer = customerRepository.findById(customerId);
				recordCustomer.ifPresent(customer -> {
					Optional<History> historyCustomer = historyRepository.findById(historyId);
					historyCustomer.ifPresent(history-> {
						
					});
				});
				
			}
			
			
		}else if(viewType.equals(Actions.DELETE.getValue()) || viewType.equals(Actions.EDIT.getValue())) {
			
		}else if(viewType.equals(Actions.VIEW.getValue())) {
			 
		}
		
	}
	
	/**
	 * 
	 */
	private void buildRecordGlobalViewModel() {
		HttpServletRequest path = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		super.addModelCollectionToView("currentPagePath", path.getRequestURI());
		super.addModelCollectionToView("currentPage", currentPage);
		super.addModelCollectionToView("currentRoles", AccountUtil.currentLoggedInUser().getRoles());
		

		Iterable<Record> personnels = recordRepository.findAll();
		super.addModelCollectionToView("recordList", personnels);
	}
	
	@Override
	public <T> RecordModelController addModelAttributes(T object){
		super.addControllerParam("modelAttribute", object);
		return this;
	}

}
