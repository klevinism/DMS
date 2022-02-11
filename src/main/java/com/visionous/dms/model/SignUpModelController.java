package com.visionous.dms.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.pojo.IaoAccount;
import com.visionous.dms.service.AccountService;

@Controller
public class SignUpModelController extends ModelControllerImpl {

	private final Log logger = LogFactory.getLog(SignUpModelController.class);

	private static String currentPage = LandingPages.REGISTER.value();
	
	private AccountService accountService;
	
	// Bean
	private ApplicationContext ctx;
	
	/**
	 * 
	 */
	@Autowired
	public SignUpModelController( ApplicationContext ctx, AccountService accountService) {
		this.accountService = accountService;
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
						(IaoAccount) super.getAllControllerParams().get("modelAttribute"), 
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
	private void persistModelAttributes(IaoAccount iaoaccount, String action) {
		IaoAccount iaoAccount = iaoaccount;
		
		if(action.equals(Actions.DELETE.getValue())) {
		} else if(action.equals(Actions.EDIT.getValue()) ) {
			
//			if(globalSetting.getId() != null) {
//				if(super.getAllControllerParams().containsKey("businessImage")) {
//					try {
//						String imageName = null;
//						if((imageName = uploadBusinessImage()) != null) {
//							globalSetting.setBusinessImage(imageName);
//						}else {
//							globalSetting.setBusinessImage(AccountUtil.currentLoggedInBussines().getGlobalSettings().getBusinessImage());
//						}
//					} catch (IOException e) {
//							e.printStackTrace();
//					}						
//				}
//
//				GlobalSettings newSetting = globalSettingsService.update(globalSetting);
//			}
			
		} else if(action.equals(Actions.CREATE.getValue())) {
		} else if(action.equals(Actions.VIEW.getValue())) {
		}		
	}
	
	
	/**
	 * 
	 */
	private void buildGlobalSettingsViewModel(String viewType) {
		super.addModelCollectionToView("viewType", viewType);
		
		if(viewType.equals(Actions.CREATE.getValue())) {
			super.addModelCollectionToView("register", new IaoAccount());
		}else if(viewType.equals(Actions.DELETE.getValue())){
			
		}else if (viewType.equals(Actions.EDIT.getValue())) {
			if(!super.hasResultBindingError()) {
				
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
		
		super.addModelCollectionToView("locale", AccountUtil.getCurrentLocaleLanguageAndCountry());
		
	}
	
	/**
	 * @return CustomerModelController
	 */
	@Override
	public <T> SignUpModelController addModelAttributes(T object){
		super.addControllerParam("modelAttribute", object);
		return this;
	}
	
}
