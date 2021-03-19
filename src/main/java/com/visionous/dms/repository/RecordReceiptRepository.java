/**
 * 
 */
package com.visionous.dms.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.visionous.dms.pojo.RecordReceipt;

/**
 * @author delimeta
 *
 */
@Repository
public interface RecordReceiptRepository extends JpaRepository<RecordReceipt, Long> {
	
}
