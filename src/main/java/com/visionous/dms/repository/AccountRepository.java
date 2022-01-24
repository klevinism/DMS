package com.visionous.dms.repository;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.visionous.dms.pojo.Account;

/**
 * @author delimeta
 *
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	
	/**
	 * @param username
	 * @return
	 */
	public Optional<Account> findByUsername(String username);
	
	/**
	 * @param id
	 * @return Long
	 */
	void deleteById(Long id);
	
	/** 
	 * @param name
	 * @return Long
	 */
	Long deleteByName(String name);

	/**
	 * Custom JPA query
	 * Finds {@link Account} by Username or Email
	 * @param username
	 * @param username2 
	 * @return Optional<{@link Account}>
	 */
	@Query("select t from Account t where t.username = ?1 or t.email = ?2")
	public Optional<Account> findByUsernameOrEmail(String username, String email);

	/**
	 * @param b
	 * @param c
	 * @param name
	 * @return
	 */
	public List<Account> findAllByActiveAndEnabledAndRoles_Name(boolean b, boolean c, String name);

	/**
	 * @param b
	 * @param c
	 * @param beginMonthDate
	 * @param endMonthDate
	 * @return
	 */
	public Integer countByEnabledAndActiveAndCustomer_RegisterdateBetween(boolean b, boolean c, Date beginMonthDate,
			Date endMonthDate);

	/**
	 * @param email
	 * @return
	 */
	public Optional<Account> findByEmail(String email);

	/**
	 * @param name
	 * @param surname
	 * @param birthday
	 * @return
	 */
	public Optional<Account> findByNameIgnoreCaseAndSurnameIgnoreCaseAndBirthday(String name, String surname,
			Date birthday);


	/**
	 * @param id
	 * @param currentBusinessId
	 * @return
	 */
	public Optional<Account> findByIdAndBusinesses_Id(Long id, long currentBusinessId);

	/**
	 * @param currentBusinessId
	 * @param b
	 * @param b2
	 * @param name
	 * @return
	 */
	public List<Account> findAllByBusinesses_IdAndActiveAndEnabledAndRoles_Name(Long currentBusinessId, boolean enabled,
			boolean active, String name);


	/**
	 * @param currentBusinessId
	 * @param enabled
	 * @param active
	 * @param beginMonthDate
	 * @param endMonthDate
	 * @return
	 */
	public Integer countByBusinesses_IdAndEnabledAndActiveAndCustomer_RegisterdateBetween(Long currentBusinessId,
			boolean enabled, boolean active, Date beginMonthDate, Date endMonthDate);

	/**
	 * @param id
	 * @param b
	 * @param c
	 * @param roles
	 * @return
	 */
	public List<Account> findAllByBusinesses_IdAndActiveAndEnabledAndRoles_NameIn(Long id, boolean b, boolean c,
			List<String> roles);
	
	/**
	 * @param phone Long phone number
	 * @return
	 */
	public Optional<Account> findByPhone(Long phone);
	
}