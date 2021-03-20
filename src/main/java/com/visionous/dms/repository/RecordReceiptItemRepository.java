/**
 * 
 */
package com.visionous.dms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visionous.dms.pojo.RecordReceiptItem;

/**
 * @author delimeta
 *
 */
@Repository
public interface RecordReceiptItemRepository extends JpaRepository<RecordReceiptItem, Long> {
	
}