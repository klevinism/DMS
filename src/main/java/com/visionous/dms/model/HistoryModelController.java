/**
 * 
 */
package com.visionous.dms.model;

import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.pojo.Customer;
import com.visionous.dms.pojo.History;
import com.visionous.dms.pojo.Record;
import com.visionous.dms.repository.HistoryRepository;
import com.visionous.dms.repository.RecordRepository;

/**
 * @author delimeta
 *
 */
@Service
public class HistoryModelController extends ModelController{
	private final Log logger = LogFactory.getLog(HistoryModelController.class);
	private static String currentPage = LandingPages.HISTORY.value();

	private HistoryRepository historyRepository;
	private RecordRepository recordRepository;
	
	@Autowired
	private HistoryModelController(HistoryRepository historyRepository, RecordRepository recordRepository) {
		this.historyRepository = historyRepository;
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
			buildHistoryActionViewModel();
		}else {
			buildHistoryViewModel();
			if(super.getAllControllerParams().containsKey("action")) {
				buildHistoryActionViewModel();	
			}
		}
	}
	

	/**
	 * 
	 */
	private void buildHistoryActionViewModel() {
		
		super.addModelCollectionToView("action", super.getAllControllerParams().get("action").toString().toLowerCase());
		
	}
	
	/**
	 * 
	 */
	private void buildHistoryViewModel() {
		String supervisorId = super.getAllControllerParams().get("id").toString();
		Optional<History> history = historyRepository.findBySupervisorId(Long.valueOf(supervisorId));
		history.ifPresent(x -> {
			super.addModelCollectionToView("selectedHistory", history.get());
		});	
	}

}
