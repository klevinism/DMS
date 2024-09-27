/**
 * 
 */
package com.visionous.dms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import com.visionous.dms.pojo.Customer;
import com.visionous.dms.pojo.GlobalSettings;
import com.visionous.dms.pojo.Questionnaire;
import com.visionous.dms.pojo.QuestionnaireForm;
import com.visionous.dms.pojo.QuestionnaireResponse;
import com.visionous.dms.pojo.Subscription;
import com.visionous.dms.repository.QuestionnaireFormRepository;
import com.visionous.dms.service.CustomerService;
import com.visionous.dms.service.QuestionnaireService;

/**
 * @author delimeta
 *
 */
@Controller
public class QuestionnaireModelController extends ModelControllerImpl{
	private final Log logger = LogFactory.getLog(QuestionnaireModelController.class);
		
	private QuestionnaireFormRepository questionnaireFormRepository;
    
	private CustomerService customerService;
	private QuestionnaireService questionnaireService;

	
	private static String currentPage = LandingPages.QUESTIONNAIRE.value();

	/**
	 *
	 * @param questionnaireFormRepository
	 * @param questionnaireService
	 * @param customerService
	 */
	@Autowired
	public QuestionnaireModelController(QuestionnaireFormRepository questionnaireFormRepository,
			QuestionnaireService questionnaireService,
			CustomerService customerService) {
		
		this.questionnaireFormRepository = questionnaireFormRepository;
		
		this.customerService = customerService;
		this.questionnaireService = questionnaireService;
	}
	
	
	/**
	 *
	 */
	@Override
	public void run() {
			
		// If action occurred, persist object to db
		if(super.getAllControllerParams().containsKey("modelAttribute")) {
			if(super.getAllControllerParams().containsKey("action")) {
				if(super.hasResultBindingError()) {
					super.setControllerParam("viewType", super.getAllControllerParams().get("action").toString().toLowerCase());
				}else {
					persistModelAttributes(
						(Questionnaire) super.getAllControllerParams().get("modelAttribute"), 
						super.getAllControllerParams().get("action").toString().toLowerCase()
						);
				}
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
		Questionnaire questionnaire = questionnaireNewModel;
		
		if(action.equals(Actions.DELETE.getValue())) {
		}else if(action.equals(Actions.EDIT.getValue()) ) {
		}else if(action.equals(Actions.CREATE.getValue())) {
			if(questionnaire != null) {
				Optional<Customer> customer = customerService.findById(questionnaire.getCustomerId());
				customer.ifPresent(singleCustomer -> {
					questionnaire.setCustomer(singleCustomer);
					questionnaire.setAddedDate(new Date());
					questionnaireService.createQuestionnaireWithResponses(questionnaire);
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
		
			Long customerId = null;
			
			if(super.getAllControllerParams().get("id") != null) {
				 customerId = Long.valueOf(super.getAllControllerParams().get("id").toString());
			}else if(super.getAllControllerParams().get("modelAttribute") != null) {
				Questionnaire questionnaire = (Questionnaire) super.getAllControllerParams().get("modelAttribute");
				customerId = questionnaire.getCustomerId();
			}
			
			Optional<Questionnaire> questionnaire = questionnaireService.findByCustomerId(customerId);
			if(questionnaire.isPresent()) {
				super.addModelCollectionToView("questionnaire", questionnaire.get());
			}else {
				List<QuestionnaireForm> allQuestions = questionnaireFormRepository.findAll();
				super.addModelCollectionToView("allQuestions", allQuestions);
				
				if(!super.hasResultBindingError()) {
					Questionnaire newQuestionnaire = generateQuestionnaireFromQuestionsAndCustomerId(allQuestions, customerId);
					super.addModelCollectionToView("questionnaire", newQuestionnaire);
				}
			}
			
			if(super.hasResultBindingError()) {
				if(!super.getAllControllerParams().containsKey("modelAttribute")) {
					super.clearResultBindingErrors();
				}
			}
		}else if(viewType.equals(Actions.DELETE.getValue()) || viewType.equals(Actions.EDIT.getValue())) {
		}else if(viewType.equals(Actions.VIEW.getValue())) {
		}
	}
	
	/**
	 * @return
	 */
	private Questionnaire generateQuestionnaireFromQuestionsAndCustomerId(List<QuestionnaireForm> allQuestions, Long customerId) {
		List<QuestionnaireResponse> questionnaireResponses = new ArrayList<>();
		Questionnaire newQuestionnaire = new Questionnaire();
		
		newQuestionnaire.setCustomerId(customerId);
		
		allQuestions.forEach(questionForm -> {
			QuestionnaireResponse newQuestionnaireResponse = new QuestionnaireResponse();
			newQuestionnaireResponse.setQuestionForm(questionForm);
			questionnaireResponses.add(newQuestionnaireResponse);
		});
		
		newQuestionnaire.setQuestionnaireResponse(questionnaireResponses);
		return newQuestionnaire;
	}


	/**
	 * 
	 */
	private void buildQuestionnaireGlobalViewModel() {
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
	
	/**
	 *
	 */
	@Override
	public <T> QuestionnaireModelController addModelAttributes(T object){
		super.addControllerParam("modelAttribute", object);
		return this;
	}

}
