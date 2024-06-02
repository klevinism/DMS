package com.visionous.dms.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.o2dent.lib.accounts.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visionous.dms.pojo.Verification;

/**
 * @author delimeta
 *
 */
@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long>{

	/**
	 * @return
	 */
	Verification findByToken(String token);

	/**
	 * @param account
	 * @return
	 */
	Optional<Verification> findByAccount(Account account);

	/**
	 * @param id
	 * @return
	 */
	List<Verification> findByAccount_id(Long id);

	/**
	 * @param id
	 * @param now
	 * @return
	 */
	Optional<Verification> findByAccount_idAndExpiryDateAfter(Long id, LocalDateTime now);

}
