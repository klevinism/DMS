/**
 * 
 */
package com.visionous.dms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visionous.dms.pojo.Role;
import com.visionous.dms.repository.RoleRepository;

/**
 * @author delimeta
 *
 */
@Service
public class RoleService implements IRoleService{
	private RoleRepository roleRepository;
	
	/**
	 * 
	 */
	@Autowired
	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	/**
	 * @param name
	 * @return
	 */
	@Override
	public Optional<Role> findByName(String name) {
		return this.roleRepository.findByName(name);
	}

	/**
	 * @return
	 */
	@Override
	public List<Role> findAll() {
		return this.roleRepository.findAll();
	}
	
	
}
