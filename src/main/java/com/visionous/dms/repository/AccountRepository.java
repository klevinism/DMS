package com.visionous.dms.repository;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.visionous.dms.pojo.Account;
import com.visionous.dms.pojo.Personnel;

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
}