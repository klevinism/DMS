/**
 * 
 */
package com.visionous.dms.model;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.visionous.dms.configuration.helpers.LandingPages;
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
	
	private static String currentPage = LandingPages.CUSTOMER.value();

	
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
		HttpServletRequest path = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

		super.addModelCollectionToView("currentPagePath", path.getRequestURI());
		super.addModelCollectionToView("currentPage", currentPage);
		
		if(!super.getAllControllerParams().containsKey("modal") &&
				super.getAllControllerParams().containsKey("action")) {
			buildCustomerActionViewModel();
		}else {
			buildCustomerViewModel();
			
			if(super.getAllControllerParams().containsKey("action")) {
				buildCustomerActionViewModel();	
			}
		}
	}

	/**
	 * 
	 */
	private void buildCustomerActionViewModel() {
		
		super.addModelCollectionToView("action", super.getAllControllerParams().get("action").toString().toLowerCase());
		
		String customerId = super.getAllControllerParams().get("id").toString();
		Optional<Customer> customer = customerRepository.findById(Long.valueOf(customerId));
		customer.ifPresent(x -> super.addModelCollectionToView("selected", customer.get()));
	}
	
	/**
	 * 
	 */
	private void buildCustomerViewModel() {
		Iterable<Customer> customers = customerRepository.findAll();
		super.addModelCollectionToView("customers", customers);
	}
}
