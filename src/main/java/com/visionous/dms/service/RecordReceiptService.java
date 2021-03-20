/**
 * 
 */
package com.visionous.dms.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visionous.dms.pojo.Record;
import com.visionous.dms.pojo.RecordReceipt;
import com.visionous.dms.repository.RecordReceiptRepository;

/**
 * @author delimeta
 *
 */
@Service
public class RecordReceiptService implements IRecordReceiptService{
	
	private RecordReceiptRepository recordReceiptRepository;
	
	/**
	 * 
	 */
	@Autowired
	public RecordReceiptService(RecordReceiptRepository recordReceiptRepository) {
		this.recordReceiptRepository = recordReceiptRepository;
	}
	
	
	@Override
	public RecordReceipt create(RecordReceipt receipt) {
		return this.recordReceiptRepository.saveAndFlush(receipt);
	}


	/**
	 * @param receiptId
	 * @return
	 */
	@Override
	public Optional<RecordReceipt> findById(Long receiptId) {
		return this.recordReceiptRepository.findById(receiptId);
	}
}
