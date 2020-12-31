/**
 * 
 */
package com.visionous.dms.model;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visionous.dms.pojo.Customer;
import com.visionous.dms.repository.CustomerRepository;

/**
 * @author delimeta
 *
 */
@Service
public class CustomerModelController extends ModelController{
	
	private final Log logger = LogFactory.getLog(CustomerModelController.class);

	private CustomerRepository customerRepository;
	
	/**
	 * @param customerRepository
	 */
	@Autowired
	public CustomerModelController(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}
	
	
	/**
	 *
	 */
	@Override
	public void run() {
		buildCustomerViewModel();
		
		//If view has action
		if(super.getAllControllerParams().containsKey("action")) {
			logger.info(" ACTION ");
			buildCustomerActionViewModel();
		}
	}

	/**
	 * 
	 */
	private void buildCustomerActionViewModel() {
		
		super.addModelCollectionToView("action", super.getAllControllerParams().get("action").toString().toLowerCase());
		
		String customerId = super.getAllControllerParams().get("id").toString();
		Optional<Customer> customer = customerRepository.findById(Long.valueOf(customerId));
		customer.ifPresent(x -> super.addModelCollectionToView("selectedCustomer", customer.get()));
	}
	
	/**
	 * 
	 */
	private void buildCustomerViewModel() {
		Iterable<Customer> customers = customerRepository.findAll();
		super.addModelCollectionToView("customers", customers);
	}
}
