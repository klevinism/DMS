/**
 * 
 */
package com.visionous.dms.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visionous.dms.pojo.Customer;

/**
 * @author delimeta
 *
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	/**
	 * @param currentDate
	 * @param endsDate
	 * @return
	 */
	List<Customer> findAllByRegisterdateBetween(Date start, Date end);

	/**
	 * @param id
	 * @return
	 */
	List<Customer> findAllByAccount_Businesses_Id(Long id);

	/**
	 * @param customerId
	 * @param currentBusinessId
	 * @return
	 */
	Optional<Customer> findByIdAndAccount_Businesses_Id(Long customerId, long currentBusinessId);

	/**
	 * @param id
	 * @param currentBusinessId
	 */
	void deleteByIdAndAccount_Businesses_Id(Long id, long currentBusinessId);

	/**
	 * @param id
	 * @param currentbusinessId
	 * @return
	 */
	Optional<Customer> findByIdAndAccount_Businesses_Id(Long id, Long currentbusinessId);

	
}
