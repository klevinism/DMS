package com.visionaus.dms;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.visionous.dms.pojo.Customer;
import com.visionous.dms.repository.CustomerRepository;
/**
 * 
 */
@DataJpaTest
public class CustomerRepositoryIntegrationTest {

    @Autowired
    private CustomerRepository repository;

    @Test
    public void whenFindByPublicationDate_thenArticles1And2Returned() {
    	System.out.println("ahha");
        Iterable<Customer> result = repository.findAll();
        
        result.forEach(customer -> {
        	System.out.println(customer);
        });
    }
}

