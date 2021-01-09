package com.visionous.dms.repository;
import java.util.Optional;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.visionous.dms.pojo.Account;

/**
 * @author delimeta
 *
 */
@Repository
@Transactional
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
	
}