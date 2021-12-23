/**
 * 
 */
package com.visionous.dms.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visionous.dms.exception.EmailExistsException;
import com.visionous.dms.exception.UsernameExistsException;
import com.visionous.dms.pojo.Account;
import com.visionous.dms.pojo.Customer;
import com.visionous.dms.pojo.History;
import com.visionous.dms.repository.CustomerRepository;

/**
 * @author delimeta
 *
 */
@Service
public class CustomerService implements ICustomerService{

	private AccountService accountService;
	private HistoryService historyService;
	private AppointmentService appointmentService;
	private QuestionnaireService questionnaireService;	
	private RecordService recordService;
	
	private CustomerRepository customerRepository;
	
	/**
	 * 
	 */
	@Autowired
	public CustomerService(CustomerRepository customerRepository, AccountService accountService,
			HistoryService historyService, QuestionnaireService questionnaireService,
			AppointmentService appointmentService, RecordService recordService) {
		
		this.accountService = accountService;
		this.customerRepository = customerRepository;
		this.historyService = historyService;
		this.questionnaireService = questionnaireService;
		this.appointmentService = appointmentService;
		this.recordService = recordService;
	}
	
	@Override
	public Customer create(Customer newCustomer) throws EmailExistsException, UsernameExistsException {
		newCustomer.getAccount().setCustomer(null);

		if(newCustomer.getRegisterdate() == null) {
			newCustomer.setRegisterdate(new Date(System.currentTimeMillis()));
		}
		
		newCustomer.getAccount().setActive(true);
		newCustomer.getAccount().setEnabled(true);

		Account newAccount = accountService.create(newCustomer.getAccount());
		
		newCustomer.setAccount(newAccount);
		
		return this.update(newCustomer);
	}

	@Override
	public Optional<Customer> findById(Long id) {
		return customerRepository.findById(id);
	}

	@Override
	public Customer update(Customer oldCustomer, Customer newCustomer) throws EmailExistsException, UsernameExistsException{
		
		if(newCustomer.getRegisterdate() == null) {
			newCustomer.setRegisterdate(new Date(System.currentTimeMillis()));
		}
		
		oldCustomer.getAccount().setCustomer(newCustomer);
		
		Account updated = accountService.update(oldCustomer.getAccount());
		if(updated != null) {
			return updated.getCustomer();
		}
		
		return null;
	}
	
	/**
	 * @param customer Customer
	 */
	@Override
	public Customer update(Customer customer) {
		return this.customerRepository.saveAndFlush(customer);
	}

	@Override
	public List<Customer> findAll() {
		return this.customerRepository.findAll();
	}

	/**
	 * @param newCustomer
	 */
	@Override
	public void delete(Customer customer) {
		if(customer.getQuestionnaire() != null) {
			questionnaireService.delete(customer.getQuestionnaire());
			customer.setQuestionnaire(null);
		}
		
		if(customer.getCustomerHistory() != null) {
			if(!customer.getCustomerHistory().getRecords().isEmpty()) {
				recordService.deleteBatch(customer.getCustomerHistory().getRecords());
				customer.getCustomerHistory().setRecords(null);
			}

			historyService.delete(customer.getCustomerHistory());
			customer.setCustomerHistory(null);
		}
		
		if(customer.getAppointment() != null) {
			appointmentService.deleteBatch(customer.getAppointment());
			customer.setAppointment(null);
		}

		accountService.delete(customer.getAccount());
	}
	
	/**
	 *
	 * @param customer Customer
	 * @param history History
	 */
	@Override
	public Customer createHistoryForCustomer(Customer customer, History history) {
		history.setCustomer(customer);
		customer.setCustomerHistory(historyService.createNewHistory(history));
		return this.update(customer);
	}
	
	/**
	 * @param customerId Long
	 * @param newHistory History
	 */
	@Override
	public Customer createNewHistoryForCustomerId(Long customerId, History newHistory) {
		Optional<Customer> selectedCustomer = this.findById(customerId);
		if(selectedCustomer.isPresent() && !selectedCustomer.get().hasCustomerHistory()) {
			return this.createHistoryForCustomer(selectedCustomer.get(), newHistory);
		}
		return null;
	}

	/**
	 * @param id
	 */
	@Override
	public void deleteById(Long id) {
		Optional<Customer> customer = findById(id);
		customer.ifPresent(selected -> delete(selected));
	}

	/**
	 * @param id
	 * @return
	 */
	@Override
	public List<Customer> findAllByAccount_Businesses_Id(Long id) {
		return this.customerRepository.findAllByAccount_Businesses_Id(id);
	}

	/**
	 * @param customerId
	 * @param currentBusinessId
	 * @return
	 */
	@Override
	public Optional<Customer> findByIdAndAccount_Businesses_Id(Long customerId, Long currentBusinessId) {
		return this.customerRepository.findByIdAndAccount_Businesses_Id(customerId, currentBusinessId);
	}

	/**
	 * @param id
	 * @param currentBusinessId
	 */
	@Override
	public void deleteByIdAndAccount_Businesses_Id(Long id, long currentBusinessId) {
		this.customerRepository.deleteByIdAndAccount_Businesses_Id(id, currentBusinessId);
	}

}
