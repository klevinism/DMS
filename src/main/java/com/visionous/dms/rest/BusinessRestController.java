package com.visionous.dms.rest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import jakarta.validation.Valid;

import com.o2dent.lib.accounts.entity.Account;
import com.o2dent.lib.accounts.entity.Business;
import com.o2dent.lib.accounts.helpers.exceptions.SubdomainExistsException;
import com.o2dent.lib.accounts.persistence.AccountService;
import com.o2dent.lib.accounts.persistence.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.pojo.GlobalSettings;
import com.visionous.dms.pojo.Personnel;
import com.visionous.dms.pojo.SubscriptionHistory;
import com.visionous.dms.rest.response.ResponseBody;
import com.visionous.dms.service.GlobalSettingsService;
import com.visionous.dms.service.PersonnelService;
import com.visionous.dms.service.SubscriptionHistoryService;
import com.visionous.dms.service.SubscriptionListService;

@RestController
@RequestMapping("/api/business")
public class BusinessRestController {

	private BusinessService businessService;
	private SubscriptionListService subscriptionListService;
	private SubscriptionHistoryService subscriptionHistoryService;
	private GlobalSettingsService globalSettingsService;
	private PersonnelService personnelService;
	private AccountService accountService;
	
	@Autowired
	public BusinessRestController(BusinessService businessService, SubscriptionListService subscriptionListService,
			SubscriptionHistoryService subscriptionHistoryService, GlobalSettingsService globalSettingsService, 
			PersonnelService personnelService, AccountService accountService) {
		this.businessService = businessService;
		this.subscriptionListService = subscriptionListService;
		this.subscriptionHistoryService = subscriptionHistoryService;
		this.globalSettingsService = globalSettingsService;
		this.personnelService = personnelService;
		this.accountService = accountService;
	}
	
	
	@GetMapping("/get")
	public ResponseEntity<?> findBusinessById(
			@RequestParam(name = "id", required = true) Long businessId) {
		
        ResponseBody<Business> result = new ResponseBody<>();

        Optional<Business> foundBusiness = businessService.findById(businessId);
        
        foundBusiness.ifPresent(business -> {
        	result.addResult(business);
        });
        
        if(result.getResult().isEmpty()) {
        	result.setError("No Business found with id: " + businessId);
        	result.setMessage("No Business found with id: " + businessId);
        }
		
		return ResponseEntity.ok(result);
	}
	
	@PostMapping("/create")
	public ResponseEntity<?> createBusiness(@Valid @RequestBody Business business, BindingResult bindingResult) throws MethodArgumentNotValidException {
		
        ResponseBody<Business> result = new ResponseBody<>();
        
        Pattern txt_pattern = Pattern.compile("^[a-z]+$");
        Pattern txtAndNr_pattern = Pattern.compile("^[a-zA-Z0-9]+$");

        if(Objects.nonNull(business.getId())) {
        	businessService.findById(business.getId()).ifPresent(singleBusiness -> {
        		result.setError("A business with this id already exists");
        	});
        }
        
        
        /*
         * Gonna check
         * 
         * Business:
         * -Name | (a-zA-Z0-9) text and nr only
         * -BusinessUrl | validate Url,
         * -Subdomain_uri | validate text only!
         * 
         */
        
        if(Objects.nonNull(business.getName()) 
        		&& !txtAndNr_pattern.matcher(business.getName()).find()) {
        	result.setError("Enter a valid business name:" + business.getName());
        }
        
        if(Objects.nonNull(business.getSubdomainUri()) 
        		&& !txt_pattern.matcher(business.getSubdomainUri()).find()) {
        	result.setError("Enter a valid subdomain name:" + business.getSubdomainUri());
        }
        
        if(Objects.isNull(business.getAccounts()) || business.getAccounts().isEmpty()) {
        	business.setAccounts(new HashSet<Account>());
        }
        
    	business.getAccounts().add(AccountUtil.currentLoggedInUser().getAccount());
        
        if(Objects.isNull(result.getError())) {
			try {
			
	        	business.setEnabled(true);

		        Business newBusiness = businessService.create(business);

		        Optional<Account> newAccount = accountService.findById(AccountUtil.currentLoggedInUser().getAccount().getId());
		        newAccount.ifPresent(account -> {
			        Personnel newPersonnel = new Personnel();
			        newPersonnel.setType("ADMIN");
			        personnelService.update(newPersonnel);
					AccountUtil.currentLoggedInUser().setPersonnel(true);
		        });
				

		        GlobalSettings globalSettingsDraft = new GlobalSettings();
		        globalSettingsDraft.setAppointmentTimeSplit(30);
		        globalSettingsDraft.setBusinessDays("1,6");
		        globalSettingsDraft.setBusinessEmail("business@email.com");
		        globalSettingsDraft.setBusinessName("MyDentalClinic");
		        globalSettingsDraft.setBusinessTimes("07:00,18:00");
		        
		        if(Objects.isNull(globalSettingsDraft.getSubscriptionHistorySet())) {
		        	globalSettingsDraft.setSubscriptionHistorySet(new HashSet<>());
		        }
		        
		        globalSettingsDraft.setBusinessId(business.getId());

		        GlobalSettings newGlobalSettings = this.globalSettingsService.update(globalSettingsDraft);
	        	
		        SubscriptionHistory subscriptionHistoryDraft = new SubscriptionHistory();
		        subscriptionHistoryDraft.setActive(true);
		        subscriptionHistoryDraft.setAddeddate(LocalDateTime.now());
		        subscriptionHistoryDraft.setBusinessId(business.getId());
		        subscriptionHistoryDraft.setSubscriptionStartDate(LocalDateTime.now());
		        subscriptionHistoryDraft.setSubscriptionEndDate(LocalDateTime.now().plusDays(31));
		        
		        subscriptionListService.findByName("Free").ifPresent(freeSubscription -> {
			        subscriptionHistoryDraft.setSubscription(freeSubscription);
		        });
		        
		        subscriptionHistoryDraft.setGlobalSettings(newGlobalSettings);
		        newGlobalSettings.getSubscriptionHistorySet().add(subscriptionHistoryDraft);
		        
		        SubscriptionHistory subscriptionHistory = this.subscriptionHistoryService.update(subscriptionHistoryDraft);

				AccountUtil.setCurrentLoggedInBusiness(business);
				AccountUtil.currentLoggedInUser().setCurrentBusinessSettings(newGlobalSettings);

		        result.addResult(newBusiness);
		        
			} catch (SubdomainExistsException e) {
				result.setError("Subdomain exists [subdomain]:" + business.getSubdomainUri());
				e.printStackTrace();
			}
			
        }
        
		return ResponseEntity.ok(result);
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(
	  MethodArgumentNotValidException ex) {
	    Map<String, String> errors = new HashMap<>();
	    ex.getBindingResult().getAllErrors().forEach((error) -> {
	        String fieldName = ((FieldError) error).getField();
	        String errorMessage = error.getDefaultMessage();
	        errors.put(fieldName, errorMessage);
	    });
	    return errors;
	}
	
}
