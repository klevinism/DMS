/**
 * 
 */
package com.visionous.dms.service;

import java.util.Optional;

import com.visionous.dms.pojo.RecordReceipt;

/**
 * @author delimeta
 *
 */
public interface IRecordReceiptService {

	/**
	 * @param receipt
	 * @return
	 */
	RecordReceipt create(RecordReceipt receipt);

	/**
	 * @param receiptId
	 * @return
	 */
	Optional<RecordReceipt> findById(Long receiptId);

}
