/**
 * 
 */
package com.visionous.dms.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.pojo.ServiceType;
import com.visionous.dms.rest.response.ResponseBody;
import com.visionous.dms.service.ServiceTypeService;

/**
 * @author delimeta
 *
 */
@RestController
public class ServiceTypeRestController {
	private MessageSource messageSource;
	private ServiceTypeService serviceTypeService;
	
	/**
	 * 
	 */
	public ServiceTypeRestController(MessageSource messageSource, ServiceTypeService serviceTypeService) {
		this.messageSource = messageSource;
		this.serviceTypeService = serviceTypeService;
	}
	
	@PostMapping("/api/serviceType/create")
    public ResponseEntity<?> createService(@RequestParam(name = "name", required = true) String serviceTypeName,
    		@RequestParam(name = "price", required = true) int serviceTypePrice) { 

        String success = messageSource.getMessage("alert.success", null, LocaleContextHolder.getLocale());
        String serviceCreated = messageSource.getMessage("alert.serviceCreatedSuccessfully", null, LocaleContextHolder.getLocale());
        String serviceCreatedError = messageSource.getMessage("alert.errorGeneric", null, LocaleContextHolder.getLocale());

        ResponseBody<ServiceType> result = new ResponseBody<>();
        ServiceType service = new ServiceType(serviceTypeName);
        service.setGlobalSettings(AccountUtil.currentLoggedInBussines().getGlobalSettings());
        service.setPrice(serviceTypePrice);
        
        ServiceType savedService = serviceTypeService.create(service);
        if(savedService.getId() != null) {
        	result.setError(success);
        	result.setMessage(serviceCreated);
        	result.addResult(savedService);
        }else {
        	result.setError("error");
        	result.setMessage(serviceCreatedError);
        }
        return ResponseEntity.ok(result);
    }

	@PostMapping("/api/serviceType/edit")
    public ResponseEntity<?> editService(@RequestParam(name = "id", required = true) Long currentServiceId, 
    		@RequestParam(name = "newName", required = true) String newName,
    		@RequestParam(name = "price", required = true) int serviceTypePrice) { 

        String success = messageSource.getMessage("alert.success", null, LocaleContextHolder.getLocale());
        String serviceEdited = messageSource.getMessage("alert.serviceEditedSuccessfully", null, LocaleContextHolder.getLocale());
        String serviceEditedError = messageSource.getMessage("alert.errorGeneric", null, LocaleContextHolder.getLocale());
        
        ResponseBody<ServiceType> result = new ResponseBody<>();
        
        Optional<ServiceType> oldService = serviceTypeService.findByIdAndGlobalSettingsId(currentServiceId, AccountUtil.currentLoggedInBussines().getGlobalSettings().getId());
        
        if(oldService.isPresent()) {
        	oldService.get().setName(newName);
        	oldService.get().setPrice(serviceTypePrice);
        	
        	ServiceType newService = serviceTypeService.update(oldService.get());
        	if(newService.getId() != null) {
            	result.setError(success);
            	result.setMessage(serviceEdited);
            	result.addResult(oldService.get());	
        	}else {
        		result.setError("error");
            	result.setMessage(serviceEditedError);
        	}
        }else {
        	result.setError("error");
        	result.setMessage(serviceEditedError);
        }
        
        return ResponseEntity.ok(result);
    }
	
	@PostMapping("/api/serviceType/delete")
    public ResponseEntity<?> deleteService(@RequestParam(name = "id", required = true) Long selectedServiceId) { 

        String success = messageSource.getMessage("alert.success", null, LocaleContextHolder.getLocale());
        String serviceEdited = messageSource.getMessage("alert.serviceDeletedSuccessfully", null, LocaleContextHolder.getLocale());
        String serviceEditedError = messageSource.getMessage("alert.errorGeneric", null, LocaleContextHolder.getLocale());

        ResponseBody<ServiceType> result = new ResponseBody<>();
        
        Optional<ServiceType> oldService = serviceTypeService.findById(selectedServiceId);
        
        if(oldService.isPresent()) {
        	serviceTypeService.delete(oldService.get());
			result.setError(success);
			result.setMessage(serviceEdited);
        }else {
        	result.setError("error");
        	result.setMessage(serviceEditedError);
        }
        
        return ResponseEntity.ok(result);
    }
	
	@GetMapping("/api/serviceType/all")
    public ResponseEntity<?> getAllServiceTypes() { 
        String success = messageSource.getMessage("alert.success", null, LocaleContextHolder.getLocale());
        String error = messageSource.getMessage("alert.errorGeneric", null, LocaleContextHolder.getLocale());

        ResponseBody<ServiceType> result = new ResponseBody<>();
        
        List<ServiceType> allServices = serviceTypeService.findAllByGlobalSettingsId(AccountUtil.currentLoggedInBussines().getGlobalSettings().getId());
        
        if(!allServices.isEmpty()) {
			result.setError(success);
			result.setMessage(success);
			result.setResult(allServices);
        }else {
        	result.setError("error");
        	result.setMessage(error);
        }
        
        return ResponseEntity.ok(result);
    }
	
	@GetMapping("/api/serviceType/get")
    public ResponseEntity<?> getAllServiceTypes(@RequestParam(name = "id", required = true) Long selectedServiceId) { 
        String success = messageSource.getMessage("alert.success", null, LocaleContextHolder.getLocale());
        String error = messageSource.getMessage("alert.errorGeneric", null, LocaleContextHolder.getLocale());

        ResponseBody<ServiceType> result = new ResponseBody<>();
        Optional<ServiceType> selectedService = serviceTypeService.findByIdAndGlobalSettingsId(selectedServiceId, AccountUtil.currentLoggedInBussines().getGlobalSettings().getId());
        
        if(selectedService.isPresent()) {
			result.setError(success);
			result.addResult(selectedService.get());
        }else {
        	result.setError("error");
        	result.setMessage(error);
        }
        
        return ResponseEntity.ok(result);
    }
}
