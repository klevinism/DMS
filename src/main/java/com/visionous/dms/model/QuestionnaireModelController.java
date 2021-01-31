/**
 * 
 */
package com.visionous.dms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.pojo.Account;
import com.visionous.dms.pojo.Customer;
import com.visionous.dms.pojo.Personnel;
import com.visionous.dms.pojo.Questionnaire;
import com.visionous.dms.pojo.QuestionnaireForm;
import com.visionous.dms.pojo.QuestionnaireResponse;
import com.visionous.dms.pojo.Role;
import com.visionous.dms.repository.AccountRepository;
import com.visionous.dms.repository.CustomerRepository;
import com.visionous.dms.repository.HistoryRepository;
import com.visionous.dms.repository.PersonnelRepository;
import com.visionous.dms.repository.QuestionnaireFormRepository;
import com.visionous.dms.repository.QuestionnaireRepository;
import com.visionous.dms.repository.QuestionnaireResponseRepository;
import com.visionous.dms.repository.RoleRepository;

/**
 * @author delimeta
 *
 */
@Controller
public class QuestionnaireModelController extends ModelControllerImpl{
	private final Log logger = LogFactory.getLog(QuestionnaireModelController.class);
	
	private PersonnelRepository personnelRepository;
	private RoleRepository roleRepository;
	private CustomerRepository customerRepository;
	private AccountRepository accountRepository;
	private QuestionnaireRepository questionnaireRepository;
	private QuestionnaireFormRepository questionnaireFormRepository;
	private QuestionnaireResponseRepository questionnaireResponseRepository;
	
	private static String currentPage = LandingPages.QUESTIONNAIRE.value();

	/**
	 * @param personnelRepository
	 */
	@Autowired
	public QuestionnaireModelController(PersonnelRepository personnelRepository, 
			CustomerRepository customerRepository, QuestionnaireRepository questionnaireRepository,
			QuestionnaireFormRepository questionnaireFormRepository, QuestionnaireResponseRepository questionnaireResponseRepository, 
			AccountRepository accountRepository) {
		
		this.personnelRepository = personnelRepository;
		this.questionnaireResponseRepository = questionnaireResponseRepository;
		this.customerRepository = customerRepository;
		this.questionnaireRepository = questionnaireRepository;
		this.questionnaireFormRepository = questionnaireFormRepository;
		this.accountRepository = accountRepository;
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
						(Questionnaire) super.getAllControllerParams().get("modelAttribute"), 
						super.getAllControllerParams().get("action").toString().toLowerCase()
						); 
			}
		}
		
		// Build view
		this.buildQuestionnaireViewModel(super.getAllControllerParams().get("viewType").toString().toLowerCase());
		
		// Build global view model for Personnel
		this.buildQuestionnaireGlobalViewModel();
	}

	/**
	 * 
	 */
	private void persistModelAttributes(Questionnaire questionnaireNewModel, String action) {
		Questionnaire questionnaireResponse = questionnaireNewModel;
		
		if(action.equals(Actions.DELETE.getValue())) {
			
			
		}else if(action.equals(Actions.EDIT.getValue()) ) {
			
			
		}else if(action.equals(Actions.CREATE.getValue())) {
			if(super.getAllControllerParams().get("modelAttribute") != null) {
				Questionnaire questionnaire = (Questionnaire)super.getAllControllerParams().get("modelAttribute");
				Long customerId = questionnaire.getCustomerId();
				
				Optional<Customer> customer = customerRepository.findById(customerId);
				customer.ifPresent(singleCustomer -> {
					questionnaire.setCustomer(singleCustomer);
		
					questionnaire.setAddedDate(new Date());
				});

				
				Questionnaire newQuestionnaire = questionnaireRepository.saveAndFlush(questionnaire);
				newQuestionnaire.setQuestionnaireResponse(questionnaire.getQuestionnaireResponse());
				newQuestionnaire.getQuestionnaireResponse().forEach(singleQuestionnaireResponse -> {

					singleQuestionnaireResponse.setId(null);
					singleQuestionnaireResponse.setResponseDate(new Date()); 
					singleQuestionnaireResponse.setQuestionId(singleQuestionnaireResponse.getQuestionForm().getId());
					
					singleQuestionnaireResponse.setQuestionnaire(newQuestionnaire);

					questionnaireFormRepository.flush();

					QuestionnaireResponse newQuestionnaireResponse = questionnaireResponseRepository.saveAndFlush(singleQuestionnaireResponse);

					System.out.println(newQuestionnaireResponse.getResponse() + " " + newQuestionnaireResponse.getQuestionId());
				});

			}
			
		}else if(action.equals(Actions.VIEW.getValue())) {
		}		

	}

	/**
	 * 
	 */
	private void buildQuestionnaireViewModel(String viewType) {
		super.addModelCollectionToView("viewType", viewType);
		
		if(viewType.equals(Actions.CREATE.getValue())) {
			if(super.getAllControllerParams().get("id") != null) {
				Long customerId = Long.valueOf(super.getAllControllerParams().get("id").toString());
				
				Optional<Customer> customer= customerRepository.findById(customerId);
				super.addModelCollectionToView("customer", customer.get());
				
				List<QuestionnaireForm> allQuestions = questionnaireFormRepository.findAll();
				super.addModelCollectionToView("allQuestions", allQuestions);
				
				Optional<Questionnaire> questionnaire = questionnaireRepository.findByCustomerId(customerId);
				if(questionnaire.isPresent()) {
					super.addModelCollectionToView("selected", questionnaire.get());
				}else {
					Questionnaire newQuestionnaire = new Questionnaire();
					newQuestionnaire.setCustomerId(customerId);
					newQuestionnaire.setCustomer(customer.get());
					
					List<QuestionnaireResponse> questionnaireResponses = new ArrayList<>();
					allQuestions.forEach(questionForm -> {
						QuestionnaireResponse newQuestionnaireResponse = new QuestionnaireResponse();
						newQuestionnaireResponse.setQuestionForm(questionForm);
						questionnaireResponses.add(newQuestionnaireResponse);
					});
					
					newQuestionnaire.setQuestionnaireResponse(questionnaireResponses);
					
					super.addModelCollectionToView("selected", newQuestionnaire);
				}

			}else {
				if(super.getAllControllerParams().get("modelAttribute") != null) {
					Questionnaire questionnaire = (Questionnaire) super.getAllControllerParams().get("modelAttribute");
					Long customerId = questionnaire.getCustomer().getId();

					Optional<Customer> customer= customerRepository.findById(customerId);
					super.addModelCollectionToView("customer", customer.get());

					List<QuestionnaireForm> allQuestions = questionnaireFormRepository.findAll();
					super.addModelCollectionToView("allQuestions", allQuestions);
					
					Optional<Questionnaire> questionnaires = questionnaireRepository.findByCustomerId(customer.get().getId());
					if(questionnaires.isPresent()) {
						super.addModelCollectionToView("selected", questionnaires.get());
					}else {
						Questionnaire newQuestionnaire = new Questionnaire();
						List<QuestionnaireResponse> questionnaireResponses = new ArrayList<>();
						
						allQuestions.forEach(questionForm -> {
							QuestionnaireResponse newQuestionnaireResponse = new QuestionnaireResponse();
							newQuestionnaireResponse.setQuestionForm(questionForm);
							questionnaireResponses.add(newQuestionnaireResponse);
						});
						newQuestionnaire.setQuestionnaireResponse(questionnaireResponses);
						newQuestionnaire.setCustomer(customer.get());
						super.addModelCollectionToView("selected", newQuestionnaire);
					}
				}
			}
			
		}else if(viewType.equals(Actions.DELETE.getValue()) || viewType.equals(Actions.EDIT.getValue())) {
			String personnelId = super.getAllControllerParams().get("id").toString();
			Optional<Personnel> personnel = personnelRepository.findById(Long.valueOf(personnelId));
			personnel.ifPresent(x -> super.addModelCollectionToView("selected", personnel.get()));

			Iterable<Role> allRoles = roleRepository.findAll();
			super.addModelCollectionToView("allRoles", allRoles);
			
		}else if(viewType.equals(Actions.VIEW.getValue())) {
			if(super.getAllControllerParams().get("id") != null) {
				String personnelId = super.getAllControllerParams().get("id").toString();
				Optional<Personnel> personnel = personnelRepository.findById(Long.valueOf(personnelId));
				personnel.ifPresent(x -> super.addModelCollectionToView("selected", personnel.get()));

				Iterable<Role> allRoles = roleRepository.findAll();
				super.addModelCollectionToView("allRoles", allRoles);
			}
		}
		
	}
	
	/**
	 * 
	 */
	private void buildQuestionnaireGlobalViewModel() {
		super.addModelCollectionToView("currentBreadcrumb", LandingPages.buildBreadCrumb(
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest())
		);
		super.addModelCollectionToView("currentPage", currentPage);
		super.addModelCollectionToView("currentRoles", AccountUtil.currentLoggedInUser().getRoles());
		

		Iterable<Personnel> personnels = personnelRepository.findAll();
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
	public <T> QuestionnaireModelController addModelAttributes(T object){
		super.addControllerParam("modelAttribute", object);
		return this;
	}

}
