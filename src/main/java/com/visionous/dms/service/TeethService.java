/**
 * 
 */
package com.visionous.dms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visionous.dms.pojo.Teeth;
import com.visionous.dms.repository.TeethRepository;

/**
 * @author delimeta
 *
 */
@Service
public class TeethService implements ITeethService{
	
	private TeethRepository teethRepository;
	
	/**
	 * 
	 */
	@Autowired
	public TeethService(TeethRepository teethRepository) {
		this.teethRepository = teethRepository;
	}

	/**
	 * @param name
	 * @return
	 */
	@Override
	public Optional<Teeth> findByName(String name) {
		return this.teethRepository.findByName(name);
	}

	/**
	 * @return
	 */
	@Override
	public List<Teeth> findAll() {
		return this.teethRepository.findAll();
	}

	
}
