/**
 * 
 */
package com.visionous.dms.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.exception.EmailExistsException;
import com.visionous.dms.exception.UsernameExistsException;
import com.visionous.dms.pojo.Account;
import com.visionous.dms.repository.AccountRepository;

/**
 * @author delimeta
 *
 */
@Service
public class AccountService implements IAccountService{

	private AccountRepository accountRepository;
	
	/**
	 * 
	 */
	@Autowired
	public AccountService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}
	
	/**
	 *
	 */
	@Override
	public Account create(Account newAccount) throws EmailExistsException, UsernameExistsException {
		if(emailExists(newAccount.getEmail())) {
			throw new EmailExistsException();
		}else if(usernameExists(newAccount.getUsername())) {
			throw new UsernameExistsException();
		}
		
		newAccount.setCustomer(null);
		newAccount.setPersonnel(null);
		newAccount.setPassword(new BCryptPasswordEncoder().encode(newAccount.getPassword()));

		return createPlain(newAccount);
	}

	/**
	 *
	 */
	@Override
	public Optional<Account> findById(Long id) {
		return accountRepository.findById(id);
	}
	
	@Override
	public Account createPlain(Account account) {
		account.setAge(
				AccountUtil.calculateAgeFromBirthday(account.getBirthday())
			);
		
		return accountRepository.saveAndFlush(account);
	}
	
	/**
	 *
	 */
	@Override
	public Account update(Account newAccount) throws EmailExistsException, UsernameExistsException {
		if(emailExists(newAccount.getEmail())) {
			throw new EmailExistsException();
		}else if(usernameExists(newAccount.getUsername())) {
			throw new UsernameExistsException();
		}
		
		return createPlain(newAccount);
	}
	
	/**
	 *
	 */
	@Override
	public boolean emailExists(String email) {
		return accountRepository.findByEmail(email).isPresent();
	}

	/**
	 *
	 */
	@Override
	public boolean usernameExists(String username) {
		return accountRepository.findByUsername(username).isPresent();
	}

	/**
	 *
	 */
	@Override
	public void delete(Account account) {
		account.setCustomer(null);
		account.setPersonnel(null);
		accountRepository.delete(account);
	}

	/**
	 * @param username
	 * @return
	 */
	@Override
	public Optional<Account> findByUsernameOrEmail(String username) {
		return this.accountRepository.findByUsernameOrEmail(username, username);
	}

	/**
	 * Finds all accounts
	 * @return account List
	 */
	@Override
	public List<Account> findAll() {
		return this.accountRepository.findAll();
	}

	/**
	 * @param b
	 * @param active
	 * @param name
	 * @return
	 */
	@Override
	public List<Account> findAllByActiveAndEnabledAndRoles_Name(boolean active, boolean enabled, String role_name) {
		return this.accountRepository.findAllByActiveAndEnabledAndRoles_Name(active, enabled, role_name);
	}

	/**
	 * @param enabled
	 * @param active
	 * @param beginMonthDate
	 * @param endMonthDate
	 * @return
	 */
	@Override
	public Integer countByEnabledAndActiveAndCustomer_RegisterdateBetween(boolean enabled, boolean active, Date beginMonthDate,
			Date endMonthDate) {
		return this.accountRepository.countByEnabledAndActiveAndCustomer_RegisterdateBetween(enabled, active, beginMonthDate, endMonthDate);
	}

	/**
	 * @param name
	 * @param surname
	 * @param birthday
	 * @return
	 */
	@Override
	public Optional<Account> findByNameAndSurnameAndBirthday(String name, String surname, Date birthday) {
		return this.accountRepository.findByNameIgnoreCaseAndSurnameIgnoreCaseAndBirthday(name, surname, birthday);
	}

	/**
	 * @param email
	 * @return
	 */
	@Override
	public Optional<Account> findByEmail(String email) {
		return this.accountRepository.findByEmail(email);
	}

	/**
	 * @param phoneNr
	 * @return
	 */
	@Override
	public Optional<Account> findByPhoneNr(Long phoneNr) {
		return this.accountRepository.findAllByPhone(phoneNr);
	}

	/**
	 * @param id
	 * @param currentBusinessId
	 * @return
	 */
	@Override
	public Optional<Account> findByIdAndBusinesses_Id(Long id, long currentBusinessId) {
		return this.accountRepository.findByIdAndBusinesses_Id(id, currentBusinessId);
	}

	/**
	 * @param currentBusinessId
	 * @param active
	 * @param enabled
	 * @param name
	 * @return
	 */
	@Override
	public List<Account> findAllByAccountBusinessIdAndActiveAndEnabledAndRoles_Name(Long currentBusinessId, boolean active,
			boolean enabled, String name) {
		return this.accountRepository.findAllByBusinesses_IdAndActiveAndEnabledAndRoles_Name(currentBusinessId, active, enabled, name);
	}

	/**
	 * @param currentBusinessId
	 * @param enabled
	 * @param active
	 * @param beginMonthDate
	 * @param endMonthDate
	 * @return
	 */
	public Integer countByBusinesses_IdAndEnabledAndActiveAndCustomer_RegisterdateBetween(Long currentBusinessId, boolean enabled, boolean active,
			Date beginMonthDate, Date endMonthDate) {
		return this.accountRepository.countByBusinesses_IdAndEnabledAndActiveAndCustomer_RegisterdateBetween(currentBusinessId, enabled, active, beginMonthDate, endMonthDate);
	}

	/**
	 * @param id
	 * @param b
	 * @param c
	 * @param roles
	 * @return
	 */
	public List<Account> findAllByAccountBusinessIdAndActiveAndEnabledAndRoles_NameIn(Long id, boolean b, boolean c,
			List<String> roles) {
		return this.accountRepository.findAllByBusinesses_IdAndActiveAndEnabledAndRoles_NameIn(id, b, c, roles);
	}
	
}
