/**
 * 
 */
package com.visionous.dms.repository;

import org.springframework.data.repository.CrudRepository;

import com.visionous.dms.pojo.Customer;

/**
 * @author delimeta
 *
 */
public interface CustomerRepository extends CrudRepository<Customer, Long> {
	
}
