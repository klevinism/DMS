/**
 * 
 */
package com.visionous.dms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visionous.dms.pojo.GlobalSettings;
import com.visionous.dms.repository.GlobalSettingsRepository;

/**
 * @author delimeta
 *
 */
@Service
public class GlobalSettingsService implements IGlobalSettingsService{
	
	private GlobalSettingsRepository globalSettingsRepository;
	
	/**
	 * 
	 */
	@Autowired
	public GlobalSettingsService(GlobalSettingsRepository globalSettingsRepository) {
		this.globalSettingsRepository = globalSettingsRepository;
	}

	/**
	 * @return
	 */
	@Override
	public List<GlobalSettings> findAll() {
		return this.globalSettingsRepository.findAll();
	}
	
	/**
	 * @return
	 */
	@Override
	public GlobalSettings update(GlobalSettings globalSettings) {
		return this.globalSettingsRepository.saveAndFlush(globalSettings);
	}
	
	
}
