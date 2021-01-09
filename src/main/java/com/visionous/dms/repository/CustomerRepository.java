/**
 * 
 */
package com.visionous.dms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visionous.dms.pojo.Customer;

/**
 * @author delimeta
 *
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	
}
