/**
 * 
 */
package com.visionous.dms.model;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.configuration.helpers.FileManager;
import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.pojo.Account;
import com.visionous.dms.pojo.GlobalSettings;
import com.visionous.dms.repository.AccountRepository;
import com.visionous.dms.repository.GlobalSettingsRepository;

/**
 * @author delimeta
 *
 */

@Controller
public class GlobalSettingsModelController extends ModelControllerImpl{
	
	private final Log logger = LogFactory.getLog(CustomerModelController.class);

	private static String currentPage = LandingPages.GLOBALSETTINGS.value();

	private AccountRepository accountRepository;
	private GlobalSettingsRepository globalSettingsRepository;
	private GlobalSettings globalSettings;
	private ApplicationContext ctx;
	
	/**
	 * 
	 */
	@Autowired
	public GlobalSettingsModelController(AccountRepository accountRepository, GlobalSettingsRepository globalSettingsRepository,
			ApplicationContext ctx, GlobalSettings globalSettings) {
		this.accountRepository = accountRepository;
		this.globalSettingsRepository = globalSettingsRepository;
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
				
				if(super.getAllControllerParams().get("businessImage") != null) {
					
					MultipartFile uploadedFile = (MultipartFile) super.getAllControllerParams().get("businessImage");
					System.out.println(uploadedFile.toString());
					System.out.println(new FileSystemResource("").getFile().getAbsolutePath()+"/tmp/business/logo/");
					if(!uploadedFile.isEmpty() && (uploadedFile.getOriginalFilename()!= null || !uploadedFile.isEmpty())){
							StringBuilder attachmentPath = new StringBuilder();
							try {
								String path = FileManager.write(uploadedFile, "/tmp/business/logo/");
							    String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss-"));
							    String fileName = date + uploadedFile.getOriginalFilename();
								attachmentPath.append(fileName);
							} catch (IOException e) {
								e.printStackTrace();
							} 
							globalSetting.setBusinessImage(attachmentPath.toString());
					}else {
						globalSetting.setBusinessImage(globalSettings.getBusinessImage());
					}
				}
				
				GlobalSettings old = ctx.getBean(GlobalSettings.class);
				GlobalSettings newSetting = globalSettingsRepository.saveAndFlush(globalSetting);
				
				ctx.getBean(GlobalSettings.class).setAppointmentTimeSplit(newSetting.getAppointmentTimeSplit());
				ctx.getBean(GlobalSettings.class).setBusinessDays(newSetting.getBusinessDays());
				ctx.getBean(GlobalSettings.class).setBusinessEmail(newSetting.getBusinessEmail());
				ctx.getBean(GlobalSettings.class).setBusinessPassword(newSetting.getBusinessPassword());
				ctx.getBean(GlobalSettings.class).setBusinessImage(newSetting.getBusinessImage());
				ctx.getBean(GlobalSettings.class).setBusinessName(newSetting.getBusinessName());
				ctx.getBean(GlobalSettings.class).setBusinessTimes(newSetting.getBusinessTimes());
				
				 
				ctx.getBean(JavaMailSenderImpl.class).setUsername(newSetting.getBusinessEmail());
				ctx.getBean(JavaMailSenderImpl.class).setPassword(newSetting.getBusinessPassword());
				
			}
		}else if(action.equals(Actions.CREATE.getValue())) {

		}else if(action.equals(Actions.VIEW.getValue())) {
		}		

	}
	
	/**
	 * 
	 */
	private void buildGlobalSettingsViewModel(String viewType) {
		super.addModelCollectionToView("viewType", viewType);
		
		if(viewType.equals(Actions.CREATE.getValue())) {
			
		}else if(viewType.equals(Actions.DELETE.getValue()) || viewType.equals(Actions.EDIT.getValue())) {
			if(!super.hasResultBindingError()) {
				List<GlobalSettings> globalSettings = globalSettingsRepository.findAll();
				super.addModelCollectionToView("globalSettings", globalSettings.get(0));
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

		Optional<Account> loggedInAccount = accountRepository.findByUsername(AccountUtil.currentLoggedInUser().getUsername());
		loggedInAccount.ifPresent(account -> {
			super.addModelCollectionToView("currentRoles", account.getRoles());
			super.addModelCollectionToView("loggedInAccount", account);
		});
		
		Locale locales = LocaleContextHolder.getLocale();
		super.addModelCollectionToView("locale", locales.getLanguage() + "_" + locales.getCountry());
		
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
