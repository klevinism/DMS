/**
 * 
 */
package com.visionaus.dms.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visionaus.dms.configuration.helpers.AccountUtil;
import com.visionaus.dms.pojo.Account;
import com.visionaus.dms.repository.AccountRepository;

/**
 * @author delimeta
 *
 */
@Service
public class PersonnelModelController extends ModelController{
	
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
	 *	Return PersonnelModelViewController
	 */
	@Override
	public void run() {
		// add LoggedInUsers
		// add userList
		Iterable<Account> accounts = accountRepository.findAll();
		super.addModelCollectionToView("accountList", accounts);
		super.addModelCollectionToView("user", AccountUtil.currentLoggedInUser());
		
		//Find Selected user in case of Edit
		if(super.getAllControllerParams().containsKey("id")) {
			
			
			String idControllerParamValue = super.getAllControllerParams().get("id").toString();
			Optional<Account> accountById = accountRepository.findById(Long.valueOf(idControllerParamValue));
			if(accountById.isPresent()) {
				Account selectedAccount = accountById.get();
				super.addModelCollectionToView("selectedAccount", selectedAccount);
			}
		}
	}

}
