/**
 * 
 */
package com.visionous.dms.model;

import java.util.Optional;

import com.o2dent.lib.accounts.entity.Account;
import com.o2dent.lib.accounts.persistence.AccountService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.pojo.Customer;
import com.visionous.dms.pojo.GlobalSettings;
import com.visionous.dms.pojo.Record;
import com.visionous.dms.pojo.RecordReceipt;
import com.visionous.dms.pojo.RecordReceiptItem;
import com.visionous.dms.pojo.Subscription;
import com.visionous.dms.service.CustomerService;
import com.visionous.dms.service.HistoryService;
import com.visionous.dms.service.QuestionnaireResponseService;
import com.visionous.dms.service.RecordReceiptItemService;
import com.visionous.dms.service.RecordReceiptService;
import com.visionous.dms.service.RecordService;

/**
 * @author delimeta
 *
 */
@Controller
public class RecordReceiptModelController extends ModelControllerImpl{
	private final Log logger = LogFactory.getLog(RecordModelController.class);
	private static String currentPage = LandingPages.RECORDRECEIPT.value();
	private RecordReceiptService recordReceiptService;
	private RecordReceiptItemService recordReceiptItemService;
	private CustomerService customerService;
	private HistoryService historyService;
	private RecordService recordService;
	private AccountService accountService;
	private QuestionnaireResponseService questionnaireResponseService;

	/**
	 * 
	 */
	@Autowired
	public RecordReceiptModelController(RecordReceiptService recordReceiptService,
			RecordReceiptItemService recordReceiptItemService,
			CustomerService customerService, HistoryService historyService, 
			QuestionnaireResponseService questionnaireResponseService,
			RecordService recordService, AccountService accountService) {
		
		this.questionnaireResponseService = questionnaireResponseService;
		this.recordReceiptItemService = recordReceiptItemService;
		this.recordReceiptService = recordReceiptService;
		this.customerService = customerService;
		this.historyService = historyService;
		this.recordService = recordService;
		this.accountService = accountService;
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
					(RecordReceipt) super.getAllControllerParams().get("modelAttribute"), 
					super.getAllControllerParams().get("action").toString().toLowerCase()
				);
			}
		}
		
		// Build view
		this.buildRecordReceiptViewModel(super.getAllControllerParams().get("viewType").toString().toLowerCase());
		
		// Build global view model for Personnel
		this.buildRecordReceiptGlobalViewModel();
	}
	
	/**
	 * 
	 */
	private void persistModelAttributes(RecordReceipt recordNewModel, String action) {
		RecordReceipt newRecord = recordNewModel;

		if(action.equals(Actions.EDIT.getValue()) ) {
			Optional<RecordReceipt> old = this.recordReceiptService.findById(newRecord.getId());
			old.ifPresent(receipt -> {
				receipt.setPayed(newRecord.isPayed());
				this.recordReceiptService.create(receipt);
			});
		}else if(action.equals(Actions.CREATE.getValue())) {
		}
	}
	
	/**
	 * 
	 */
	private void buildRecordReceiptViewModel(String viewType) {
		super.addModelCollectionToView("viewType", viewType);
			
		Long recordId = (Long) super.getAllControllerParams().get("recordId");
		Long historyId = (Long) super.getAllControllerParams().get("historyId");
		Long customerId = (Long) super.getAllControllerParams().get("customerId");
		
		if(viewType.equals(Actions.CREATE.getValue())) {
			
			Optional<Record> selectedRecord = this.recordService.findById(recordId);
			selectedRecord.ifPresent(currentRecord -> {
				
				if(currentRecord.getReceipt() == null ) {
					RecordReceipt recordReceipt = new RecordReceipt();
					recordReceipt.setPayed(false);
					recordReceipt.setVat(0.0);
					recordReceipt.setTotal(Double.valueOf(currentRecord.getServiceType().getPrice()));
					
					RecordReceipt newRecordReceipt = this.recordReceiptService.create(recordReceipt);
					
					RecordReceiptItem recordReceiptItem = new RecordReceiptItem(newRecordReceipt, currentRecord.getServiceType());
					recordReceiptItem.setAmount(1);
					
					RecordReceiptItem newRecordReceiptItem = this.recordReceiptItemService.create(recordReceiptItem);
					newRecordReceipt.addReceiptItem(newRecordReceiptItem);
					
					newRecordReceipt.setRecord(currentRecord);
					currentRecord.setReceipt(newRecordReceipt);
					
					Record newRecord = this.recordService.create(currentRecord);
					super.addModelCollectionToView("selectedReceipt", newRecord.getReceipt());

					Optional<Account> personnel = accountService.findById(newRecord.getPersonnelId());
					personnel.ifPresent(acc -> {
						super.addModelCollectionToView("performer", acc);
					});
					Optional<Account> customer = accountService.findById(newRecord.getHistory().getCustomerId());
					customer.ifPresent(acc -> {
						super.addModelCollectionToView("account", acc);
					});
				}else {
					super.addModelCollectionToView("selectedReceipt", currentRecord.getReceipt());
				}
			});
		}else if(viewType.equals(Actions.VIEW.getValue())) {
			
			Long receiptId = (Long) super.getAllControllerParams().get("receiptId");
			
			Optional<RecordReceipt> selectedReceipt = this.recordReceiptService.findById(receiptId);
			selectedReceipt.ifPresent(receipt -> {
				super.addModelCollectionToView("selectedReceipt", receipt);

				Optional<Account> personnel = accountService.findById(receipt.getRecord().getPersonnelId());
				personnel.ifPresent(acc -> {
					super.addModelCollectionToView("performer", acc);
				});

				Optional<Account> customer = accountService.findById(receipt.getRecord().getHistory().getCustomerId());
				customer.ifPresent(acc -> {
					super.addModelCollectionToView("account", acc);
				});
			});
			
			
		}
		Optional<Customer> recordCustomer = customerService.findById(customerId);
		recordCustomer.ifPresent(customer -> {
			if(customer.hasQuestionnaire()) { 
				super.addModelCollectionToView("anamezeAllergies", 
						questionnaireResponseService.findAllByQuestionIdAndResponse(customer.getQuestionnaire().getId(), "yes"));
			}
		});
		
		super.addModelCollectionToView("global", AccountUtil.currentLoggedInBusinessSettings());
	}
	
	/**
	 * 
	 */
	private void buildRecordReceiptGlobalViewModel() {
		super.addModelCollectionToView("currentBreadcrumb", LandingPages.buildBreadCrumb(
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest())
		);
		super.addModelCollectionToView("currentPage", currentPage);

		super.addModelCollectionToView("currentRoles", AccountUtil.currentLoggedInUser().getAccount().getRoles());
		super.addModelCollectionToView("loggedInAccount", AccountUtil.currentLoggedInUser().getAccount());
		
		super.addModelCollectionToView("locale", AccountUtil.getCurrentLocaleLanguageAndCountry());
		
		super.addModelCollectionToView("logo", AccountUtil.currentLoggedInBusinessSettings().getBusinessImage());
		
		super.addModelCollectionToView("subscription", AccountUtil.currentLoggedInBusinessSettings().getActiveSubscription());

	}
	
	@Override
	public <T> RecordReceiptModelController addModelAttributes(T object){
		super.addControllerParam("modelAttribute", object);
		return this;
	}
}
