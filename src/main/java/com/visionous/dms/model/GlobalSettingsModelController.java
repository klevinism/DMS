/**
 * 
 */
package com.visionous.dms.model;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.configuration.helpers.FileManager;
import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.pojo.GlobalSettings;
import com.visionous.dms.pojo.ServiceTypes;
import com.visionous.dms.service.GlobalSettingsService;
import com.visionous.dms.service.ServiceTypeService;

/**
 * @author delimeta
 *
 */

@Controller
public class GlobalSettingsModelController extends ModelControllerImpl{
	
	private final Log logger = LogFactory.getLog(GlobalSettingsModelController.class);

	private static String currentPage = LandingPages.GLOBALSETTINGS.value();

	private GlobalSettingsService globalSettingsService;
	private ServiceTypeService serviceTypeService;
	
	// Bean
	private GlobalSettings globalSettings;
	private ApplicationContext ctx;
	
	/**
	 * 
	 */
	@Autowired
	public GlobalSettingsModelController(
			GlobalSettingsService globalSettingsService,
			ServiceTypeService serviceTypeService,
			GlobalSettings globalSettings, 
			ApplicationContext ctx) {
				
		this.globalSettingsService = globalSettingsService;
		this.serviceTypeService = serviceTypeService;
		this.globalSettings = globalSettings;
		this.ctx = ctx;
	}
	
	/**
	 *
	 */
	@Override
	public void run() {
		// If action occurred, persist object to db
		if(super.getAllControllerParams().containsKey("modelAttribute")) {
			if(super.getAllControllerParams().containsKey("action")) {
				if(super.hasResultBindingError()) {
					super.setControllerParam("viewType", super.getAllControllerParams().get("action").toString().toLowerCase());
				}else {
					persistModelAttributes(
						(GlobalSettings) super.getAllControllerParams().get("modelAttribute"), 
						super.getAllControllerParams().get("action").toString().toLowerCase()
						);					
				}
			}
		}
		
		// Build view
		this.buildGlobalSettingsViewModel(super.getAllControllerParams().get("viewType").toString().toLowerCase());
		
		// Build global view model for Customer
		this.buildGlobalSettingsGlobalViewModel();
	}
	/**
	 * 
	 */
	private void persistModelAttributes(GlobalSettings settings, String action) {
		GlobalSettings globalSetting = settings;
		
		if(action.equals(Actions.DELETE.getValue())) {
		}else if(action.equals(Actions.EDIT.getValue()) ) {
			
			if(globalSetting.getId() != null) {
				if(super.getAllControllerParams().containsKey("businessImage")) {
					try {
						String imageName = null;
						if((imageName = uploadBusinessImage()) != null) {
							globalSetting.setBusinessImage(imageName);
						}else {
							globalSetting.setBusinessImage(globalSettings.getBusinessImage());
						}
					} catch (IOException e) {
							e.printStackTrace();
					}						
				}

				GlobalSettings newSetting = globalSettingsService.update(globalSetting);			
				ctx.getBean(GlobalSettings.class).setCurrentSetting(newSetting);
				 
				ctx.getBean(JavaMailSenderImpl.class).setUsername(newSetting.getBusinessEmail());
				ctx.getBean(JavaMailSenderImpl.class).setPassword(newSetting.getBusinessPassword());
			}
			
		}else if(action.equals(Actions.CREATE.getValue())) {

		}else if(action.equals(Actions.VIEW.getValue())) {
		}		
	}
	
	private String uploadBusinessImage() throws IOException {
		return FileManager.uploadImage((MultipartFile) super.getAllControllerParams().get("businessImage"), FileManager.BUSINESS_LOGO_IMAGE_PATH);
	}
	
	/**
	 * 
	 */
	private void buildGlobalSettingsViewModel(String viewType) {
		super.addModelCollectionToView("viewType", viewType);
		
		if(viewType.equals(Actions.CREATE.getValue())) {
			
		}else if(viewType.equals(Actions.DELETE.getValue())){
			
		}else if (viewType.equals(Actions.EDIT.getValue())) {
			if(!super.hasResultBindingError()) {
				List<GlobalSettings> globalSetting = globalSettingsService.findAll();
				super.addModelCollectionToView("globalSettings", globalSetting.get(0));
			}
			
			ServiceTypes serviceTypes = new ServiceTypes(serviceTypeService.findAll());
			if(!serviceTypes.getServices().isEmpty()) {
				super.addModelCollectionToView("services", serviceTypes);
			}
		}else if(viewType.equals(Actions.VIEW.getValue())) {
			
		}
	}
	
	/**
	 * 
	 */
	private void buildGlobalSettingsGlobalViewModel() {
		super.addModelCollectionToView("currentBreadcrumb", LandingPages.buildBreadCrumb(
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest())
		);

		super.addModelCollectionToView("currentPage", currentPage);

		super.addModelCollectionToView("currentRoles", AccountUtil.currentLoggedInUser().getRoles());
		super.addModelCollectionToView("loggedInAccount", AccountUtil.currentLoggedInUser());
		
		super.addModelCollectionToView("locale", AccountUtil.getCurrentLocaleLanguageAndCountry());
		
		super.addModelCollectionToView("logo", globalSettings.getBusinessImage());
	}
	
	/**
	 * @return CustomerModelController
	 */
	@Override
	public <T> GlobalSettingsModelController addModelAttributes(T object){
		super.addControllerParam("modelAttribute", object);
		return this;
	}
}
