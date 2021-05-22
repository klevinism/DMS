/**
 * 
 */
package com.visionous.dms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visionous.dms.exception.EmailExistsException;
import com.visionous.dms.exception.UsernameExistsException;
import com.visionous.dms.pojo.Account;
import com.visionous.dms.pojo.Personnel;
import com.visionous.dms.repository.PersonnelRepository;

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
	public Personnel create(Personnel newPersonnel) throws EmailExistsException, UsernameExistsException {
		newPersonnel.getAccount().setCustomer(null);
		
		Account newAccount = accountService.create(newPersonnel.getAccount());
		
		newPersonnel.setAccount(newAccount);
		
		return personnelRepository.saveAndFlush(newPersonnel);
	}

	@Override
	public Optional<Personnel> findById(Long id) {
		return this.personnelRepository.findById(id);
	}

	@Override
	public Personnel update(Personnel oldPersonnel, Personnel newPersonnel)
			throws EmailExistsException, UsernameExistsException {
		oldPersonnel.getAccount().setPersonnel(newPersonnel);
		
		Account updated = accountService.update(oldPersonnel.getAccount());
		if(updated != null) {
			return updated.getPersonnel();
		}
		return null;
	}
	
	@Override
	public List<Personnel> findAllByRoleName(String roleName){
		return this.personnelRepository.findAllByAccount_Roles_Name(roleName);
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

	
	/**
	 * Disable personnel
	 * 
	 * @param personnel Personnel
	 */
	@Override
	public Personnel disable(Personnel personnel) {
		personnel.getAccount().setEnabled(false);
		return this.personnelRepository.saveAndFlush(personnel);
	}

	/**
	 * @param id
	 */
	@Override
	public Personnel disableById(Long id) {
		Optional<Personnel> selectedPersonnel = this.findById(id);
		if(selectedPersonnel.isPresent()) {
			return this.disable(selectedPersonnel.get());
		}
		return null;
	}

	/**
	 * @param b
	 * @param c
	 * @param string
	 * @return
	 */
	@Override
	public List<Personnel> findAllByAccount_EnabledAndAccount_ActiveAndAccount_Roles_Name(boolean enabled, boolean active,
			String role_name) {
		return this.personnelRepository.findAllByAccount_EnabledAndAccount_ActiveAndAccount_Roles_Name(enabled, active, role_name);
	}

	/**
	 * @param b
	 * @param string
	 * @return
	 */
	@Override
	public List<Personnel> findAllByAccount_EnabledAndAccount_Roles_Name(boolean enabled, String role_name) {
		return this.personnelRepository.findAllByAccount_EnabledAndAccount_Roles_Name(enabled, role_name);
	}
}
