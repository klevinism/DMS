package com.visionous.dms.model;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

	/**
	 * @param personnelRepository
	 */
	@Autowired
	public PersonnelModelController(PersonnelRepository personnelRepository) {
		super();
		this.personnelRepository = personnelRepository;
	}
	
	/**
	 *
	 */
	@Override
	public void run() {
		buildPersonnelViewModel();
		
		//If view has action
		if(super.getAllControllerParams().containsKey("action")) {
			buildPersonnelActionViewModel();
		}
	}

	/**
	 * 
	 */
	private void buildPersonnelActionViewModel() {
		
		super.addModelCollectionToView("action", super.getAllControllerParams().get("action").toString().toLowerCase());
		
		String personnelId = super.getAllControllerParams().get("id").toString();
		Optional<Personnel> personnel = personnelRepository.findById(Long.valueOf(personnelId));
		personnel.ifPresent(x -> super.addModelCollectionToView("selectedAccount", personnel.get().getAccount()));
	}
	
	/**
	 * 
	 */
	private void buildPersonnelViewModel() {
		Iterable<Personnel> personnels = personnelRepository.findAll();
		super.addModelCollectionToView("personnelList", personnels);
	}
}
