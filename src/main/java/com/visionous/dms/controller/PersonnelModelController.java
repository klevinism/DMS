/**
 * 
 */
package com.visionous.dms.controller;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.pojo.Account;
import com.visionous.dms.repository.AccountRepository;

/**
 * @author delimeta
 *
 */
@Service
public class PersonnelModelController extends ModelController{
	
	private final Log logger = LogFactory.getLog(PersonnelModelController.class);
	
	private AccountRepository accountRepository;

	/**
	 * @param accountRepository
	 */
	@Autowired
	public PersonnelModelController(AccountRepository accountRepository) {
		super();
		this.accountRepository = accountRepository;
	}
	
	/**
	 *
	 */
	@Override
	public void run() {
		
		// add LoggedInUsers
		// add userList
		Iterable<Account> accounts = accountRepository.findAll();
		super.addModelCollectionToView("accountList", accounts);
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
		Optional<Account> accountById = accountRepository.findById(Long.valueOf(idControllerParamValue));
		
		if(accountById.isPresent()) {
			Account selectedAccount = accountById.get();
			super.addModelCollectionToView("selectedAccount", selectedAccount);
		}
	}

	/**
	 * 
	 */
	private void mapModelToActionView(String actionViewType) {
		Actions action = Actions.valueOf(actionViewType);
		
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
		Optional<Account> accountById = accountRepository.findById(Long.valueOf(idControllerParamValue));
			
		if(accountById.isPresent()) {
			Account selectedAccount = accountById.get();
			super.addModelCollectionToView("selectedAccount", selectedAccount);
		}
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
