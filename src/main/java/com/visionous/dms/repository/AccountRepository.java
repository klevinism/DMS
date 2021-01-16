package com.visionous.dms.repository;
import java.util.Optional;

import javax.transaction.Transactional;
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
	
}