/**
 * 
 */
package com.visionous.dms.service;

import java.util.List;
import java.util.Optional;

import com.visionous.dms.exception.EmailExistsException;
import com.visionous.dms.exception.UsernameExistsException;
import com.visionous.dms.pojo.Customer;
import com.visionous.dms.pojo.History;

/**
 * @author delimeta
 *
 */
public interface ICustomerService {
	Customer create(Customer customer) throws EmailExistsException, UsernameExistsException;
	
	Optional<Customer> findById(Long id);
	
	List<Customer> findAll();
	
	Customer update(Customer oldCustomer, Customer newCustomer) throws EmailExistsException, UsernameExistsException;

	/**
	 * @param newCustomer
	 */
	void delete(Customer newCustomer);

	/**
	 * @param id
	 */
	void deleteById(Long id);

	/**
	 * @param customer
	 * @param newHistory
	 * @return
	 */
	Customer createHistoryForCustomer(Customer customer, History newHistory);

	/**
	 * @param customerId
	 * @param newHistory
	 * @return
	 */
	Customer createNewHistoryForCustomerId(Long customerId, History newHistory);

	/**
	 * @param customer
	 * @return
	 */
	Customer update(Customer customer);
}
