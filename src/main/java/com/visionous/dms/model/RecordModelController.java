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

import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.pojo.History;
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
	
	/**
	 * 
	 */
	@Autowired
	public RecordModelController(RecordRepository recordRepository) {
		this.recordRepository = recordRepository;
	}
	
	/**
	 *
	 */
	@Override
	public void run() {
		HttpServletRequest path = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

		super.addModelCollectionToView("currentPagePath", path.getRequestURI());
		super.addModelCollectionToView("currentPage", currentPage);
		
		if(!super.getAllControllerParams().containsKey("modal") &&
				super.getAllControllerParams().containsKey("action")) {
			buildRecordActionViewModel();
		}else {
			buildRecordViewModel();
			if(super.getAllControllerParams().containsKey("action")) {
				buildRecordActionViewModel();	
			}
		}
	}
	

	/**
	 * 
	 */
	private void buildRecordActionViewModel() {
		
		super.addModelCollectionToView("action", super.getAllControllerParams().get("action").toString().toLowerCase());
		
	}
	
	/**
	 * 
	 */
	private void buildRecordViewModel() {
//		String supervisorId = super.getAllControllerParams().get("id").toString();
		// TODO build record view model	
	}
}
