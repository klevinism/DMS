/**
 * 
 */
package com.visionous.dms.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author delimeta
 *
 */
public class ServiceTypes {
	
	private List<ServiceType> services;
	
	/**
	 * 
	 */
	public ServiceTypes() {
		this.services = new ArrayList<>();
		// TODO Auto-generated constructor stub
	}

	public void addAllServices(List<ServiceType> serviceType) {
		this.services.addAll(serviceType);
	}
	
	/**
	 * @return the services
	 */
	public List<ServiceType> getServices() {
		return services;
	}

	/**
	 * @param services the services to set
	 */
	public void setServices(List<ServiceType> services) {
		this.services = services;
	}
	
	
}
