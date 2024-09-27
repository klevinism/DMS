package com.visionous.dms.repository;

import java.util.List;
import java.util.Optional;

import com.o2dent.lib.accounts.entity.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author delimeta
 *
 */
@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

	Optional<List<Business>> findBySubdomainUri(String subdomainUri);
}
