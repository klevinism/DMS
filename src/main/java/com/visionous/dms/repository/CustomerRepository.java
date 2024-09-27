package com.visionous.dms.repository;

import com.o2dent.lib.accounts.entity.Account;
import com.visionous.dms.pojo.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author delimeta
 *
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	/**
	 *
	 * @param start
	 * @param end
	 * @return
	 */
	List<Customer> findAllByRegisterdateBetween(Date start, Date end);

	/**
	 *
	 * @param selectedAccounts
	 * @return
	 */
	List<Customer> findAllByIdIn(List<Long> ids);
}
