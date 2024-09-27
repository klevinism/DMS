/**
 * 
 */
package com.visionous.dms.service;

import com.o2dent.lib.accounts.entity.Account;
import com.o2dent.lib.accounts.helpers.exceptions.EmailExistsException;
import com.o2dent.lib.accounts.helpers.exceptions.PhoneNumberExistsException;
import com.o2dent.lib.accounts.helpers.exceptions.UsernameExistsException;
import com.o2dent.lib.accounts.persistence.AccountService;
import com.visionous.dms.pojo.Customer;
import com.visionous.dms.pojo.History;
import com.visionous.dms.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
	public Customer create(Customer newCustomer) throws EmailExistsException, UsernameExistsException, PhoneNumberExistsException {

		if(newCustomer.getRegisterdate() == null) {
			newCustomer.setRegisterdate(new Date(System.currentTimeMillis()));
		}
		
		return this.update(newCustomer);
	}

	@Override
	public Optional<Customer> findById(Long id) {
		return customerRepository.findById(id);
	}

	@Override
	public Customer update(Customer oldCustomer, Customer newCustomer) throws EmailExistsException, UsernameExistsException, PhoneNumberExistsException{
		
		if(newCustomer.getRegisterdate() == null) {
			newCustomer.setRegisterdate(new Date(System.currentTimeMillis()));
		}
		
		oldCustomer.setCustomerHistory(newCustomer.getCustomerHistory());
		oldCustomer.setRegisterdate(newCustomer.getRegisterdate());
		oldCustomer.setAppointment(newCustomer.getAppointment());
		oldCustomer.setQuestionnaire(newCustomer.getQuestionnaire());
		
		return this.update(oldCustomer);
	}
	
	/**
	 * @param customer Customer
	 */
	@Override
	public Customer update(Customer customer) {
		return this.customerRepository.saveAndFlush(customer);
	}

	/**
	 *
	 * @param ids
	 * @return
	 */
	@Override
	public List<Customer> findAllByIdIn(List<Long> ids) {
		return this.customerRepository.findAllByIdIn(ids);
	}

	@Override
	public List<Customer> findAll() {
		return this.customerRepository.findAll();
	}

	/**
	 * @param customer
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

		accountService.disableById(customer.getId());
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

	public List<Customer> findAllByRegisterDateBetween(Date start, Date end){
		return this.customerRepository.findAllByRegisterdateBetween(start, end);
	}

}
