/**
 * 
 */
package com.visionous.dms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visionous.dms.pojo.GlobalSettings;

/**
 * @author delimeta
 *
 */
@Repository
public interface GlobalSettingsRepository extends JpaRepository<GlobalSettings, Long>{

	Optional<GlobalSettings> findByBusinessId(Long id);

}
