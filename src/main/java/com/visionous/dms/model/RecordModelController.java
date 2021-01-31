/**
 * 
 */
package com.visionous.dms.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.visionous.dms.configuration.AccountUserDetail;
import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.configuration.helpers.FileManager;
import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.pojo.Account;
import com.visionous.dms.pojo.Customer;
import com.visionous.dms.pojo.History;
import com.visionous.dms.pojo.Personnel;
import com.visionous.dms.pojo.Questionnaire;
import com.visionous.dms.pojo.QuestionnaireResponse;
import com.visionous.dms.pojo.Record;
import com.visionous.dms.pojo.Role;
import com.visionous.dms.pojo.ServiceType;
import com.visionous.dms.pojo.Teeth;
import com.visionous.dms.repository.AccountRepository;
import com.visionous.dms.repository.CustomerRepository;
import com.visionous.dms.repository.HistoryRepository;
import com.visionous.dms.repository.PersonnelRepository;
import com.visionous.dms.repository.QuestionnaireRepository;
import com.visionous.dms.repository.QuestionnaireResponseRepository;
import com.visionous.dms.repository.RecordRepository;
import com.visionous.dms.repository.ServiceTypeRepository;
import com.visionous.dms.repository.TeethRepository;

/**
 * @author delimeta
 *
 */
@Controller
public class RecordModelController extends ModelControllerImpl {
	private final Log logger = LogFactory.getLog(RecordModelController.class);
	
	private static String currentPage = LandingPages.RECORD.value();

	private RecordRepository recordRepository;
	private CustomerRepository customerRepository;
	private HistoryRepository historyRepository;
	private ServiceTypeRepository serviceTypeRepository;
	private TeethRepository teethRepository;
	private AccountRepository accountRepository;
	private PersonnelRepository personnelRepository;
	private QuestionnaireResponseRepository questionnaireResponseRepository;
	private QuestionnaireRepository questionnaireRepository;

	/**
	 * 
	 */
	@Autowired
	public RecordModelController(RecordRepository recordRepository, CustomerRepository customerRepository, 
			HistoryRepository historyRepository, ServiceTypeRepository serviceTypeRepository,
			TeethRepository teethRepository, AccountRepository accountRepository,
			PersonnelRepository personnelRepository, QuestionnaireResponseRepository questionnaireResponseRepository,
			QuestionnaireRepository questionnaireRepository) {
		this.recordRepository = recordRepository;
		this.customerRepository = customerRepository;
		this.historyRepository = historyRepository;
		this.serviceTypeRepository = serviceTypeRepository;
		this.teethRepository = teethRepository;
		this.accountRepository = accountRepository;
		this.personnelRepository = personnelRepository;
		this.questionnaireResponseRepository = questionnaireResponseRepository;
		this.questionnaireRepository = questionnaireRepository;
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
			Long personnelId = newRecord.getPersonnelId();
			Long historyId = newRecord.getHistoryId();
			
			Optional<Personnel> personnel = personnelRepository.findById(personnelId);
			Optional<History> history = historyRepository.findById(historyId);
			Optional<ServiceType> serviceType = serviceTypeRepository.findByName(newRecord.getServiceType().getName());
			Optional<Teeth> tooth = teethRepository.findByName(newRecord.getTooth().getName());
			MultipartFile[] uploadedFiles =null;
			if(super.getAllControllerParams().get("files") != null) {
				uploadedFiles = (MultipartFile[]) super.getAllControllerParams().get("files");
			}
			
			if(personnel.isPresent() && history.isPresent() 
					&& serviceType.isPresent() 
					&& tooth.isPresent()) {
				
				if(uploadedFiles != null){
					StringBuilder attachments = new StringBuilder();
					for(MultipartFile file : uploadedFiles) {
						try {
						    String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss-"));
							String path = FileManager.write(file, "/tmp/records/"); 
						    String fileName = date + file.getOriginalFilename();
							attachments.append(fileName+","); 
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					newRecord.setAttachments(attachments.toString());
				}
				
				newRecord.setHistory(history.get());
				newRecord.setPersonnel(personnel.get());
				newRecord.setServiceType(serviceType.get());
				newRecord.setTooth(tooth.get());
				newRecord.setServicedate(new Date());
				
				history.get().addRecord(newRecord);
				
				Record createdRecord = recordRepository.saveAndFlush(newRecord);
				History updateHistory = historyRepository.saveAndFlush(history.get()); 
			
			}
			
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
				
				List<Teeth> allTeeth = teethRepository.findAll();
				super.addModelCollectionToView("listTeeth", allTeeth);
				
				AccountUserDetail currentAccountDetails =  AccountUtil.currentLoggedInUser();	
				Optional<Account> loggedInAccount = accountRepository.findByUsername(currentAccountDetails.getUsername());
				
				Optional<Customer> recordCustomer = customerRepository.findById(customerId);
				recordCustomer.ifPresent(x -> {
					Optional<Questionnaire> questionnaire = questionnaireRepository.findByCustomerId(x.getId());
					questionnaire.ifPresent(singlequestionnaire -> {					
						List<QuestionnaireResponse> questionnaireResponses = questionnaireResponseRepository.findAllByQuestionnaireIdAndResponse(singlequestionnaire.getId(), "yes");
						super.addModelCollectionToView("anamezeAllergies", questionnaireResponses);
					});
				});
				recordCustomer.ifPresent(customer -> {
					Optional<History> historyCustomer = historyRepository.findById(historyId);
					
					
					historyCustomer.ifPresent(history-> {
						super.addModelCollectionToView("selectedHistory", history);
						
						Record record = new Record();
						record.setHistory(history);
						record.setHistoryId(history.getId());
						record.setPersonnelId(loggedInAccount.get().getId());
						
						super.addModelCollectionToView("selected", record);
						
					}); 
					
					List<ServiceType> serviceTypes = serviceTypeRepository.findAll();
					super.addModelCollectionToView("services", serviceTypes);
					
				});
			}
			
		}else if(viewType.equals(Actions.DELETE.getValue()) || viewType.equals(Actions.EDIT.getValue())) {
			
		}else if(viewType.equals(Actions.VIEW.getValue())) {
				Long customerId = Long.valueOf(super.getAllControllerParams().get("customerId").toString());
				
				if(super.getAllControllerParams().containsKey("id")) {
					Long recordId = (Long) super.getAllControllerParams().get("id");
					Optional<Record> selectedRecord = recordRepository.findById(recordId);
					selectedRecord.ifPresent(record->{
						
						super.addModelCollectionToView("selectedRecord", record);
						
						if(record.getAttachments() != null) {
							String attachment = record.getAttachments();
							String[] attachments = attachment.split(",");
							if(attachments.length >0 ) {
								super.addModelCollectionToView("attachmentList", attachments);
							}
						}					
						
					});

					List<Teeth> allTeeth = teethRepository.findAll();
					super.addModelCollectionToView("listTeeth", allTeeth);
				}
				
				Optional<History> history = historyRepository.findByCustomerId(customerId);
				super.addModelCollectionToView("selected", history.get());
				
				Optional<Customer> customer = customerRepository.findById(customerId);
				super.addModelCollectionToView("currentCustomer", customer.get());
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
		super.addModelCollectionToView("currentRoles", AccountUtil.currentLoggedInUser().getRoles());

		Iterable<Record> records = recordRepository.findAllByOrderByServicedateDesc();
		super.addModelCollectionToView("recordList", records);

		Optional<Account> loggedInAccount = accountRepository.findByUsername(AccountUtil.currentLoggedInUser().getUsername());
		loggedInAccount.ifPresent(account -> {
			super.addModelCollectionToView("currentRoles", account.getRoles());
			super.addModelCollectionToView("loggedInAccount", account);
		});
		
		Locale locales = LocaleContextHolder.getLocale();
		super.addModelCollectionToView("locale", locales.getLanguage() + "_" + locales.getCountry());
	}
	
	@Override
	public <T> RecordModelController addModelAttributes(T object){
		super.addControllerParam("modelAttribute", object);
		return this;
	}

}
