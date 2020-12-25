package com.visionaus.dms.repository;
/**
 * @author delimeta
 *
 */
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.visionaus.dms.pojo.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
	
	/**
	 * @param username
	 * @return
	 */
	public Account findByUsername(String username);
	
}