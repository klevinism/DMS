/**
 * 
 */
package com.visionous.dms.service;

import com.o2dent.lib.accounts.helpers.exceptions.EmailExistsException;
import com.o2dent.lib.accounts.helpers.exceptions.PhoneNumberExistsException;
import com.o2dent.lib.accounts.helpers.exceptions.UsernameExistsException;
import com.o2dent.lib.accounts.persistence.AccountService;
import com.visionous.dms.pojo.Personnel;
import com.visionous.dms.repository.PersonnelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author delimeta
 *
 */
@Service
public class PersonnelService implements IPersonnelService{

	private PersonnelRepository personnelRepository;
	
	private AccountService accountService;
	
	/**
	 * 
	 */
	@Autowired
	public PersonnelService(PersonnelRepository personnelRepository, AccountService accountService) {
		this.personnelRepository = personnelRepository;
		this.accountService = accountService;
	}
	
	@Override
	public Personnel create(Personnel newPersonnel) throws EmailExistsException, UsernameExistsException, PhoneNumberExistsException {
		
		return personnelRepository.saveAndFlush(newPersonnel);
	}
	
	@Override
	public Personnel update(Personnel newPersonnel) {
		return personnelRepository.saveAndFlush(newPersonnel);
	}

	@Override
	public Optional<Personnel> findById(Long id) {
		return this.personnelRepository.findById(id);
	}

	public List<Personnel> findByIdIn(List<Long> ids) {
		return this.personnelRepository.findByIdIn(ids);
	}

	/**
	 * @param id
	 */
	@Override
	public void deleteById(Long id) {
		Optional<Personnel> selectedPersonnel = this.findById(id);
		selectedPersonnel.ifPresent(personnel -> {
			this.delete(personnel);
		});
	}

	/**
	 * Deletes personnel 
	 * 
	 * @param personnel Personnel
	 */
	@Override
	public void delete(Personnel personnel) {
		this.personnelRepository.delete(personnel);
	}
	
}
