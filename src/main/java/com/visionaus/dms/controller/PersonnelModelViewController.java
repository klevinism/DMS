/**
 * 
 */
package com.visionaus.dms.controller;

import java.util.List;

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
public class PersonnelModelViewController extends ModelViewController implements ViewControllerImpl{
	
	private AccountRepository accountRepository;

	/**
	 * @param accountRepository
	 */
	@Autowired
	public PersonnelModelViewController(AccountRepository accountRepository) {
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
		super.addCollectionToView("accountList", accounts);
		super.addCollectionToView("user", AccountUtil.currentLoggedInUser());
	}
	
}
