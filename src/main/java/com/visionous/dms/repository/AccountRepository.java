package com.visionous.dms.repository;
/**
 * @author delimeta
 *
 */
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.visionous.dms.pojo.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
	
	/**
	 * @param username
	 * @return
	 */
	public Account findByUsername(String username);
	
}