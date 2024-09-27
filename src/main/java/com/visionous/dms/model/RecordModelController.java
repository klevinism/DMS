/**
 * 
 */
package com.visionous.dms.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.o2dent.lib.accounts.entity.Account;
import com.o2dent.lib.accounts.persistence.AccountService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.configuration.helpers.FileManager;
import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.pojo.Customer;
import com.visionous.dms.pojo.GlobalSettings;
import com.visionous.dms.pojo.History;
import com.visionous.dms.pojo.Personnel;
import com.visionous.dms.pojo.Record;
import com.visionous.dms.pojo.ServiceType;
import com.visionous.dms.pojo.Subscription;
import com.visionous.dms.pojo.Teeth;
import com.visionous.dms.service.QuestionnaireResponseService;
import com.visionous.dms.service.RecordReceiptItemService;
import com.visionous.dms.service.RecordReceiptService;
import com.visionous.dms.service.CustomerService;
import com.visionous.dms.service.HistoryService;
import com.visionous.dms.service.PersonnelService;
import com.visionous.dms.service.RecordService;
import com.visionous.dms.service.ServiceTypeService;
import com.visionous.dms.service.TeethService;

/**
 * @author delimeta
 *
 */
@Controller
public class RecordModelController extends ModelControllerImpl {
	private final Log logger = LogFactory.getLog(RecordModelController.class);
	
	private static String currentPage = LandingPages.RECORD.value();

	private QuestionnaireResponseService questionnaireResponseService;
	private ServiceTypeService serviceTypeService;
	private PersonnelService personnelService;
	private CustomerService customerService;
	private AccountService accountService;
	private HistoryService historyService;
	private RecordService recordService;
	private RecordReceiptService recordReceiptService;
	private RecordReceiptItemService recordReceiptItemService;

	private TeethService teethService;


	/**
	 * 
	 */
	@Autowired
	public RecordModelController(QuestionnaireResponseService questionnaireResponseService, 
			ServiceTypeService serviceTypeService, PersonnelService personnelService,
			CustomerService customerService, HistoryService historyService, RecordService recordService, TeethService teethService,
			RecordReceiptService recordReceiptService, RecordReceiptItemService recordReceiptItemService,
								 AccountService accountService) {
		
		this.questionnaireResponseService = questionnaireResponseService;
		this.serviceTypeService = serviceTypeService;
		this.personnelService = personnelService;
		this.customerService = customerService;
		this.historyService = historyService;
		this.recordService = recordService;
		this.recordReceiptItemService = recordReceiptItemService;
		this.recordReceiptService = recordReceiptService;
		this.teethService = teethService;
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

		if(action.equals(Actions.EDIT.getValue()) ) {
		}else if(action.equals(Actions.CREATE.getValue())) {
			Optional<Personnel> personnel = personnelService.findById(newRecord.getPersonnelId());
			Optional<History> history = historyService.findById(newRecord.getHistoryId());
			
			if(!newRecord.getVisitedTeeth().isEmpty()) {
				
				List<Teeth> visitedTeeth = newRecord.getVisitedTeeth().stream()
						.map(visitedTooth -> {
								Optional<Teeth> tooth = teethService.findByName(visitedTooth.getName());
								return tooth.isPresent() ? tooth.get() : null;
							})
						.filter(Objects::nonNull)
						.collect(Collectors.toList());
				
				Optional<ServiceType> serviceType = serviceTypeService.findById(newRecord.getServiceType().getId());
				
				if(personnel.isPresent() && history.isPresent() 
						&& serviceType.isPresent() && !visitedTeeth.isEmpty()) {
					
					if(super.getAllControllerParams().containsKey("files")) {
						String allRecordAttachmentNames = uploadAllRecordImages(
								(MultipartFile[]) super.getAllControllerParams().get("files"));
						newRecord.setAttachments(allRecordAttachmentNames);
					}
					newRecord.setHistory(history.get());
					newRecord.setPersonnel(personnel.get());
					newRecord.setServiceType(serviceType.get());
					newRecord.setVisitedTeeth(visitedTeeth);
					
					super.addModelCollectionToView("selectedRecord", recordService.create(newRecord));
					
					history.get().addRecord(newRecord);
					historyService.create(history.get());
				}
			}
		}
	}
	
	private String uploadAllRecordImages(MultipartFile[] filesArray) {
		StringBuilder attachmentsFileNames = new StringBuilder();

		for(MultipartFile file : filesArray) {
			try {
				String imageName = null;
				if((imageName = FileManager.uploadImage(file, "/tmp/records/")) != null) {
					attachmentsFileNames.append(imageName);
					
					if(!filesArray[filesArray.length-1].equals(file)) 
						attachmentsFileNames.append(",");
				}
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		return attachmentsFileNames.toString();
	}
	
	/**
	 * 
	 */
	private void buildRecordViewModel(String viewType) {
		super.addModelCollectionToView("viewType", viewType);
		
		if(viewType.equals(Actions.CREATE.getValue())) {
			if(super.getAllControllerParams().get("historyId") != null) {
				Long customerId = (Long) super.getAllControllerParams().get("customerId");
				Long historyId = (Long) super.getAllControllerParams().get("historyId");
				
				Optional<Customer> recordCustomer = customerService.findById(customerId);
				recordCustomer.ifPresent(customer -> {
					Optional<History> customerHistory = historyService.findById(historyId);
					
					customerHistory.ifPresent(history-> {
						super.addModelCollectionToView("selectedHistory", history);
						Optional<Personnel> personnel = personnelService.findById(AccountUtil.currentLoggedInUser().getAccount().getId());
						personnel.ifPresent(personnelAcc -> {
							Record record = new Record(history, personnelAcc);
							List<Teeth> toothList = new ArrayList<>();
							toothList.add(new Teeth());
							record.setVisitedTeeth(toothList);
							super.addModelCollectionToView("selected", record);
							Optional<Account> customerAccount = accountService.findById(customer.getId());
							customerAccount.ifPresent(acc -> {
								super.addModelCollectionToView("account", acc);
							});
						});
					});
					
					super.addModelCollectionToView("services", serviceTypeService.findAllByGlobalSettingsId(AccountUtil.currentLoggedInBusinessSettings().getId()));
				});
			}
			
		}else if(viewType.equals(Actions.VIEW.getValue())) {	
			if(super.getAllControllerParams().containsKey("id")) {
				Long recordId = (Long) super.getAllControllerParams().get("id");
				Optional<Record> selectedRecord = recordService.findById(recordId);
				selectedRecord.ifPresent(record->{
					if(record.getAttachments() != null && record.getAttachments().length() > 0) {
						super.addModelCollectionToView("attachmentList", 
								record.getAttachments().split(","));
					}
					
					super.addModelCollectionToView("selectedRecord", record);
				});

			}

			Long customerId = (Long) super.getAllControllerParams().get("customerId");
			
			Optional<History> history = historyService.findByCustomerId(customerId);
			history.ifPresent(selectedHistory -> super.addModelCollectionToView("selected", selectedHistory));
			
			Optional<Customer> customer = customerService.findById(customerId);
			customer.ifPresent(selectedCustomer -> super.addModelCollectionToView("currentCustomer", selectedCustomer));

			Optional<Account> customerAccount = accountService.findById(customerId);
			customerAccount.ifPresent( acc -> super.addModelCollectionToView("account", acc));
		}
		
		super.addModelCollectionToView("listTeeth", teethService.findAll());
		
		if(super.getAllControllerParams().get("historyId") != null) {
			Long customerId = (Long) super.getAllControllerParams().get("customerId");
			Optional<Customer> recordCustomer = customerService.findById(customerId);
			recordCustomer.ifPresent(customer -> {
				if(customer.hasQuestionnaire()) { 
					super.addModelCollectionToView("anamezeAllergies", 
							questionnaireResponseService.findAllByQuestionIdAndResponse(customer.getQuestionnaire().getId(), "yes"));
				}
			});
		}
	}
	
	/**
	 * 
	 */
	private void buildRecordGlobalViewModel() {
		super.addModelCollectionToView("currentBreadcrumb", LandingPages.buildBreadCrumb(
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest())
		);
		super.addModelCollectionToView("currentPage", currentPage);
		super.addModelCollectionToView("recordList", recordService.findAll());

		super.addModelCollectionToView("currentRoles", AccountUtil.currentLoggedInUser().getAccount().getRoles());
		super.addModelCollectionToView("loggedInAccount", AccountUtil.currentLoggedInUser().getAccount());
		
		super.addModelCollectionToView("locale", AccountUtil.getCurrentLocaleLanguageAndCountry());
		
		super.addModelCollectionToView("logo", AccountUtil.currentLoggedInBusinessSettings().getBusinessImage());
		
		super.addModelCollectionToView("subscription", AccountUtil.currentLoggedInBusinessSettings().getActiveSubscription());

	}
	
	@Override
	public <T> RecordModelController addModelAttributes(T object){
		super.addControllerParam("modelAttribute", object);
		return this;
	}

}
