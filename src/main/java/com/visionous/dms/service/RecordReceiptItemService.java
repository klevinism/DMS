/**
 * 
 */
package com.visionous.dms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visionous.dms.pojo.RecordReceiptItem;
import com.visionous.dms.repository.RecordReceiptItemRepository;

/**
 * @author delimeta
 *
 */
@Service
public class RecordReceiptItemService implements IRecordReceiptItemService{
	private RecordReceiptItemRepository recordReceiptItemRepository;
	
	/**
	 * 
	 */
	@Autowired
	public RecordReceiptItemService(RecordReceiptItemRepository recordReceiptItemRepository) {
		this.recordReceiptItemRepository = recordReceiptItemRepository;
	}
	
	@Override
	public RecordReceiptItem create(RecordReceiptItem receiptItem) {
		return this.recordReceiptItemRepository.saveAndFlush(receiptItem);
	}
}
