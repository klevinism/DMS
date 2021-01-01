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
import com.visionous.dms.pojo.Personnel;
import com.visionous.dms.repository.PersonnelRepository;

/**
 * @author delimeta
 *
 */
@Service
public class PersonnelModelController extends ModelController{
	
	private final Log logger = LogFactory.getLog(PersonnelModelController.class);
	
	private PersonnelRepository personnelRepository;
	
	private static String currentPage = LandingPages.PERSONNEL.value();

	/**
	 * @param personnelRepository
	 */
	@Autowired
	public PersonnelModelController(PersonnelRepository personnelRepository) {
		this.personnelRepository = personnelRepository;
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
			buildPersonnelActionViewModel();
		}else {
			buildPersonnelViewModel();
			
			if(super.getAllControllerParams().containsKey("action")) {
				buildPersonnelActionViewModel();	
			}
		}
		
	}

	/**
	 * 
	 */
	private void buildPersonnelActionViewModel() {
		super.addModelCollectionToView("action",super.getAllControllerParams().get("action").toString().toLowerCase());
		
		String personnelId = super.getAllControllerParams().get("id").toString();
		Optional<Personnel> personnel = personnelRepository.findById(Long.valueOf(personnelId));
		personnel.ifPresent(x -> super.addModelCollectionToView("selected", personnel.get().getAccount()));
	}
	
	/**
	 * 
	 */
	private void buildPersonnelViewModel() {
		Iterable<Personnel> personnels = personnelRepository.findAll();
		super.addModelCollectionToView("personnelList", personnels);
	}
}
