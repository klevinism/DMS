package com.visionous.dms.service;

import com.o2dent.lib.accounts.helpers.exceptions.EmailExistsException;
import com.o2dent.lib.accounts.helpers.exceptions.PhoneNumberExistsException;
import com.o2dent.lib.accounts.helpers.exceptions.UsernameExistsException;
import com.visionous.dms.pojo.Customer;
import com.visionous.dms.pojo.History;

import java.util.List;
import java.util.Optional;

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
	 *
	 * @param ids
	 * @return
	 */
	List<Customer> findAllByIdIn(List<Long> ids);
}
