package com.visionous.dms.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.data.mapping.PreferredConstructor.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.visionous.dms.pojo.Business;
import com.visionous.dms.rest.response.ResponseBody;
import com.visionous.dms.service.BusinessService;

@RestController
@RequestMapping("/api/business")
public class BusinessRestController {

	private BusinessService businessService;
	
	@Autowired
	public BusinessRestController(BusinessService businessService) {
		this.businessService = businessService;
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
        
        if(Objects.isNull(result.getError())) {
        	Business newBusiness = businessService.create(business);
        	result.addResult(newBusiness);
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
