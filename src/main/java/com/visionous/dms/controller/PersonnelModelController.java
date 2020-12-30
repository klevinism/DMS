package com.visionous.dms.controller;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.Actions;
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
	 * @param accountRepository
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
		
		// add LoggedInUsers
		// add userList
		Iterable<Personnel> personnels = personnelRepository.findAll();
		super.addModelCollectionToView("personnelList", personnels);
		super.addModelCollectionToView("currentLoggedInUser", AccountUtil.currentLoggedInUser());
		
		//If view is on modal
		if(super.getAllControllerParams().containsKey("modal")) {
			mapModelToModalActionView();
			mapModelToActionView(super.getAllControllerParams().get("action").toString());

		}
		
		// If an action is given to a single Personnel model handle it
	}

	/**
	 * 
	 */
	private void mapModelToModalActionView() {
		super.addModelCollectionToView("action", super.getAllControllerParams().get("action").toString().toLowerCase());
		String idControllerParamValue = super.getAllControllerParams().get("id").toString();
		Optional<Personnel> personnel = personnelRepository.findById(Long.valueOf(idControllerParamValue));
		personnel.ifPresent(x -> {
			super.addModelCollectionToView("selectedAccount", personnel.get().getAccount());
		});
	}

	/**
	 * 
	 */
	private void mapModelToActionView(String actionType) {
		Actions action = Actions.valueOf(actionType);
		
		switch(action) {
			case INFO : break;
			case VIEW : break;
			case EDIT : editPersonnelModel(); break;
			case CREATE : break;
			case DELETE : deletePersonnelModel(); break;
			default: break;
		}
		
	}

	/**
	 * 
	 */
	private void editPersonnelModel() {
		String idControllerParamValue = super.getAllControllerParams().get("id").toString();
		Optional<Personnel> personnel = personnelRepository.findById(Long.valueOf(idControllerParamValue));
		personnel.ifPresent(x -> {
			super.addModelCollectionToView("selectedAccount", personnel.get().getAccount());
		});
	}
	

	/**
	 * 
	 */
	private void deletePersonnelModel() {
		String idControllerParamValue = super.getAllControllerParams().get("id").toString();
		
		//TODO Delete Personnel
		//Optional<Account> accountById = accountRepository.findById(Long.valueOf(idControllerParamValue));
	}
	
}
