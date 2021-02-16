/**
 * 
 */
package com.visionous.dms.service;

import java.util.List;

import com.visionous.dms.pojo.GlobalSettings;

/**
 * @author delimeta
 *
 */
public interface IGlobalSettingsService {

	/**
	 * @return
	 */
	List<GlobalSettings> findAll();

	/**
	 * @param globalSettings
	 */
	GlobalSettings update(GlobalSettings globalSettings);

}
