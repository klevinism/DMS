/**
 * 
 */
package com.visionous.dms.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.o2dent.lib.accounts.helpers.exceptions.EmailExistsException;
import com.o2dent.lib.accounts.helpers.exceptions.PhoneNumberExistsException;
import com.o2dent.lib.accounts.helpers.exceptions.UsernameExistsException;
import com.visionous.dms.pojo.Personnel;

/**
 * @author delimeta
 *
 */
public interface IPersonnelService {
	
	Personnel create(Personnel personnel) throws EmailExistsException, UsernameExistsException, PhoneNumberExistsException;
	
	Optional<Personnel> findById(Long id);
	
	/**
	 * @param personnel
	 */
	void delete(Personnel personnel);

	/**
	 *
	 * @param id
	 */
	void deleteById(Long id);

	/**
	 * @param newPersonnel
	 * @return
	 */
	Personnel update(Personnel newPersonnel);

}
