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

	/**
	 * @param name
	 * @param surname
	 * @param birthday
	 * @return
	 */
	Optional<Account> findByNameAndSurnameAndBirthday(String name, String surname, Date birthday);

	/**
	 * @param email
	 * @return
	 */
	Optional<Account> findByEmail(String email);

	/**
	 * @param phoneNr
	 * @return
	 */
	Optional<Account> findByPhoneNr(Long phoneNr);

	/**
	 * @param id
	 * @param currentBusinessId
	 * @return
	 */
	Optional<Account> findByIdAndBusinesses_Id(Long id, long currentBusinessId);


	/**
	 * @param currentBusinessId
	 * @param active
	 * @param enabled
	 * @param name
	 * @return
	 */
	List<Account> findAllByAccountBusinessIdAndActiveAndEnabledAndRoles_Name(Long currentBusinessId, boolean active,
			boolean enabled, String name);

}
