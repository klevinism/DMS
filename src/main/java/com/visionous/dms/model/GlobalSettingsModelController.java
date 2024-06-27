package com.visionous.dms.model;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.visionous.dms.pojo.IaoBusiness_GlobalSettings;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.configuration.helpers.FileManager;
import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.pojo.GlobalSettings;
import com.visionous.dms.pojo.ServiceType;
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
	
	
	/**
	 * 
	 */
	@Autowired
	public GlobalSettingsModelController(
			GlobalSettingsService globalSettingsService,
			ServiceTypeService serviceTypeService) {
				
		this.globalSettingsService = globalSettingsService;
		this.serviceTypeService = serviceTypeService;
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
						(IaoBusiness_GlobalSettings) super.getAllControllerParams().get("modelAttribute"),
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
	private void persistModelAttributes(IaoBusiness_GlobalSettings settings, String action) {
		IaoBusiness_GlobalSettings iaoBusinessGlobalSettings = settings;
		
		if(action.equals(Actions.DELETE.getValue())) {
		}else if(action.equals(Actions.EDIT.getValue()) ) {
			
			if(iaoBusinessGlobalSettings.getGlobalSettings().getId() != null) {
				if(super.getAllControllerParams().containsKey("businessImage")) {
					try {
						String imageName = null;
						if((imageName = uploadBusinessImage()) != null) {
							iaoBusinessGlobalSettings.getGlobalSettings().setBusinessImage(imageName);
						}else {
							iaoBusinessGlobalSettings.getGlobalSettings().setBusinessImage(AccountUtil.currentLoggedInBusinessSettings().getBusinessImage());
						}
					} catch (IOException e) {
							e.printStackTrace();
					}						
				}

				iaoBusinessGlobalSettings.getGlobalSettings().setBusinessId(AccountUtil.currentLoggedInBussines().getId());
				iaoBusinessGlobalSettings.getGlobalSettings().setSubscriptionHistorySet(AccountUtil.currentLoggedInBusinessSettings().getSubscriptionHistorySet());

				GlobalSettings newSetting = globalSettingsService.update(iaoBusinessGlobalSettings.getGlobalSettings());
				AccountUtil.currentLoggedInUser().setCurrentBusinessSettings(newSetting);
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
				Optional<GlobalSettings> globalSettings = globalSettingsService.findByBusinessId(AccountUtil.currentLoggedInBussines().getId());
				globalSettings.ifPresent(setting -> {
					IaoBusiness_GlobalSettings iaoBusinessGlobalSettings = new IaoBusiness_GlobalSettings();
					iaoBusinessGlobalSettings.setGlobalSettings(setting);
					super.addModelCollectionToView("iaoBusinessGlobalSettings", iaoBusinessGlobalSettings);
					List<ServiceType> serviceTypes = serviceTypeService.findAllByGlobalSettingsId(setting.getId());
					if(!serviceTypes.isEmpty()) {
						super.addModelCollectionToView("services", serviceTypes);
					}
				});
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

		super.addModelCollectionToView("currentRoles", AccountUtil.currentLoggedInUser().getAccount().getRoles());
		super.addModelCollectionToView("loggedInAccount", AccountUtil.currentLoggedInUser().getAccount());
		
		super.addModelCollectionToView("locale", AccountUtil.getCurrentLocaleLanguageAndCountry());
		
		super.addModelCollectionToView("logo", AccountUtil.currentLoggedInBusinessSettings().getBusinessImage());
		
		super.addModelCollectionToView("subscription", AccountUtil.currentLoggedInBusinessSettings().getActiveSubscription());

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
