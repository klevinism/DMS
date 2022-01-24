/**
 * 
 */
package com.visionous.dms.service;

import java.util.List;
import java.util.Optional;

import com.visionous.dms.exception.EmailExistsException;
import com.visionous.dms.exception.PhoneNumberExistsException;
import com.visionous.dms.exception.UsernameExistsException;
import com.visionous.dms.pojo.Customer;
import com.visionous.dms.pojo.History;

/**
 * @author delimeta
 *
 */
public interface ICustomerService {
	/**
	 * @param customer
	 * @return
	 * @throws EmailExistsException
	 * @throws UsernameExistsException
	 */
	Customer create(Customer customer) throws EmailExistsException, UsernameExistsException, PhoneNumberExistsException;
	
	/**
	 * @param id
	 * @return
	 */
	Optional<Customer> findById(Long id);
	
	/**
	 * @return
	 */
	List<Customer> findAll();
	
	/**
	 * @param oldCustomer
	 * @param newCustomer
	 * @return
	 * @throws EmailExistsException
	 * @throws UsernameExistsException
	 */
	Customer update(Customer oldCustomer, Customer newCustomer) throws EmailExistsException, UsernameExistsException, PhoneNumberExistsException;

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

	/**
	 * @param id
	 * @return
	 */
	Object findAllByAccount_Businesses_Id(Long id);

	/**
	 * @param customerId
	 * @param currentBusinessId
	 * @return
	 */
	Optional<Customer> findByIdAndAccount_Businesses_Id(Long customerId, Long currentBusinessId);

	/**
	 * @param id
	 * @param currentBusinessId
	 */
	void deleteByIdAndAccount_Businesses_Id(Long id, long currentBusinessId);

}
