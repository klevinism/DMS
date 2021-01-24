/**
 * 
 */
package com.visionous.dms.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visionous.dms.pojo.Customer;

/**
 * @author delimeta
 *
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	/**
	 * @param currentDate
	 * @param endsDate
	 * @return
	 */
	List<Customer> findAllByRegisterdateBetween(Date start, Date end);

	
}
