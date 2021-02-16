/**
 * 
 */
package com.visionous.dms.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.visionous.dms.exception.EmailExistsException;
import com.visionous.dms.exception.UsernameExistsException;
import com.visionous.dms.pojo.Account;

/**
 * @author delimeta
 *
 */
public interface IAccountService {
	/**
	 * @param newAccount
	 * @return
	 * @throws EmailExistsException
	 * @throws UsernameExistsException
	 */
	Account create(Account newAccount) throws EmailExistsException, UsernameExistsException;
	
	/**
	 * @param id
	 * @return
	 */
	Optional<Account> findById(Long id);
	
	/**
	 * @param newAccount
	 * @return
	 * @throws UsernameExistsException 
	 * @throws EmailExistsException 
	 */
	Account update(Account newAccount) throws EmailExistsException, UsernameExistsException;
	
	/**
	 * @param account
	 */
	void delete(Account account);
	
	/**
	 * @param email
	 * @return
	 */
	boolean emailExists(String email);
	
	/**
	 * @param username
	 * @return
	 */
	boolean usernameExists(String username);

	/**
	 * @param username
	 * @return
	 */
	Optional<Account> findByUsernameOrEmail(String username);

	/**
	 * Find all accounts
	 * @return account List
	 */
	List<Account> findAll();

	/**
	 * Plain method to solely create an account, without additional 
	 * exception checks
	 * 
	 * @param account
	 * @return created/updated Account
	 */
	Account createPlain(Account account);

	/**
	 * @param enabled
	 * @param active
	 * @param role_name
	 * @return
	 */
	List<Account> findAllByActiveAndEnabledAndRoles_Name(boolean enabled, boolean active, String role_name);

	/**
	 * @param enabled
	 * @param active
	 * @param beginMonthDate
	 * @param endMonthDate
	 * @return
	 */
	Integer countByEnabledAndActiveAndCustomer_RegisterdateBetween(boolean enabled, boolean active, Date beginMonthDate,
			Date endMonthDate);

}
