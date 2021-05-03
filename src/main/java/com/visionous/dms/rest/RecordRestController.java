/**
 * 
 */
package com.visionous.dms.rest;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.visionous.dms.pojo.Record;
import com.visionous.dms.rest.response.ResponseBody;
import com.visionous.dms.service.RecordService;

/**
 * @author delimeta
 *
 */
@RestController
public class RecordRestController {
	
	private RecordService recordService;
	private MessageSource messageSource;

	/**
	 * 
	 */
	@Autowired
	public RecordRestController(RecordService recordService, MessageSource messageSource) {
		this.recordService = recordService;
		this.messageSource = messageSource;
	}
	
	@GetMapping("/api/record/get")
    public ResponseEntity<?> getRecords(@RequestParam(name = "serviceDate", required = false) Date serviceDate, 
    		@RequestParam(name = "serviceEndDate", required = false) Date serviceEndDate,
    		@RequestParam(name = "serviceTypeId", required = false) Long serviceTypeId,
    		@RequestParam(name = "personnelId", required = false) Long personnelId) {
		
        ResponseBody<Record> result = new ResponseBody<>();

        
    	if(serviceDate != null) {
    		List<Record> records = recordService.findAllByServicedateBetween(new Timestamp(serviceDate.getTime()).toLocalDateTime(), 
    				new Timestamp(serviceEndDate.getTime()).toLocalDateTime());
    		
	        String message = messageSource.getMessage("alert.success", null, LocaleContextHolder.getLocale());
			result.setError(message);
    		result.setResult(records);
    	}else {
			String messageError = messageSource.getMessage("alert.error", null, LocaleContextHolder.getLocale());
    		result.setError(messageError);
    	}
        
        
        return ResponseEntity.ok(result);
	}
}
