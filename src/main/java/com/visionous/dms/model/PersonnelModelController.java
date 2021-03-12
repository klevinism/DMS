package com.visionous.dms.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.configuration.helpers.DateUtil;
import com.visionous.dms.configuration.helpers.DmsCore;
import com.visionous.dms.configuration.helpers.FileManager;
import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.event.OnRegistrationCompleteEvent;
import com.visionous.dms.exception.EmailExistsException;
import com.visionous.dms.exception.UsernameExistsException;
import com.visionous.dms.pojo.Account;
import com.visionous.dms.pojo.GlobalSettings;
import com.visionous.dms.pojo.Personnel;
import com.visionous.dms.pojo.Role;
import com.visionous.dms.service.PersonnelService;
import com.visionous.dms.service.RecordService;
import com.visionous.dms.service.RoleService;

/**
 * @author delimeta
 *
 */
@Controller
public class PersonnelModelController extends ModelControllerImpl{
	
	private final Log logger = LogFactory.getLog(PersonnelModelController.class);
	
	private RoleService roleService;
	private RecordService recordService;
    
    private ApplicationEventPublisher eventPublisher;
    private PersonnelService personnelService;
    private GlobalSettings globalSettings;
    private MessageSource messages;
    
	private static String currentPage = LandingPages.PERSONNEL.value();

	/**
	 * @param personnelRepository
	 */
	@Autowired
	public PersonnelModelController(RoleService roleService, ApplicationEventPublisher eventPublisher, 
			RecordService recordService, MessageSource messages, 
			GlobalSettings globalSettings, PersonnelService personnelService) {
	
		this.roleService = roleService;
		this.eventPublisher = eventPublisher;
		this.recordService = recordService;
		this.messages = messages;
		this.globalSettings = globalSettings;
		this.personnelService = personnelService;
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
							(Personnel) super.getAllControllerParams().get("modelAttribute"), 
							super.getAllControllerParams().get("action").toString().toLowerCase()
					);
				}
			}
		}

		// Build view
		this.buildPersonnelViewModel(super.getAllControllerParams().get("viewType").toString().toLowerCase());
		
		// Build global view model for Personnel
		this.buildPersonnelGlobalViewModel();
	}

	/**
	 * 
	 */
	private void persistModelAttributes(Personnel personnelNewModel, String action) {
		Personnel newPersonnel = personnelNewModel;

		String messageEmailExists = messages.getMessage("alert.emailExists", null, LocaleContextHolder.getLocale());
        String messageUsernameExists = messages.getMessage("alert.usernameExists", null, LocaleContextHolder.getLocale());
		
		
		if(action.equals(Actions.DELETE.getValue())) {
			personnelService.disableById(newPersonnel.getId());
		}else if(action.equals(Actions.EDIT.getValue()) ) {
			
		}else if(action.equals(Actions.CREATE.getValue())) {

			if(newPersonnel.getAccount().getRoles().get(0).getName().equals("PERSONNEL")) {
				
				String passPlain = newPersonnel.getAccount().getPassword();

				try {
					String imageName = null;
					if((imageName = this.upladProfileImage()) != null) {
						newPersonnel.getAccount().setImage(imageName);
					}

					Personnel createdPersonnel = personnelService.create(newPersonnel);
					createdPersonnel.getAccount().setPassword(passPlain);
					
					this.publishNewAccountEvent(createdPersonnel.getAccount());
					
				} catch (IOException e) {
					logger.error(e.getMessage());
				} catch (EmailExistsException e) {					
					super.getBindingResult().addError(
							new FieldError("account", "account.email", newPersonnel.getAccount().getEmail(), false, null, null, messageEmailExists)
						);

			        super.removeControllerParam("viewType");
					super.addControllerParam("viewType", Actions.CREATE.getValue());
			        logger.error(messageEmailExists);
				} catch (UsernameExistsException e) {
					super.getBindingResult().addError(
							new FieldError("account", "account.username", newPersonnel.getAccount().getEmail(), false, null, null, messageUsernameExists)
						);

				    super.removeControllerParam("viewType");
					super.addControllerParam("viewType", Actions.CREATE.getValue());
					logger.error(messageUsernameExists);
				}
			}
		}else if(action.equals(Actions.VIEW.getValue())) {
		}		

	}
	
	private void publishNewAccountEvent(Account account) {
		eventPublisher.publishEvent(
        		new OnRegistrationCompleteEvent(account, LocaleContextHolder.getLocale(), DmsCore.appMainPath())
    		);
	}
	
	
	/**
	 * Returns the name of the saved profile image, 
	 * or null if uploaded file has no name
	 * 
	 * @return imageFileName String
	 * @throws IOException
	 */
	private String upladProfileImage() throws IOException {
		if(super.getAllControllerParams().containsKey("profileimage")) {
			MultipartFile uploadedFile = (MultipartFile) super.getAllControllerParams().get("profileimage");
			if(uploadedFile.getOriginalFilename() != null && !uploadedFile.isEmpty()){
				return FileManager.write(uploadedFile, "/tmp/personnel/profile/");
			}
		}
		return null;
	}
	/**
	 * 
	 */
	private void buildPersonnelViewModel(String viewType) {
		super.addModelCollectionToView("viewType", viewType);
		
		if(viewType.equals(Actions.CREATE.getValue())) {

			if(!super.hasResultBindingError()) {
				Personnel newPersonnel = new Personnel();
				newPersonnel.setAccount(new Account());
				super.addModelCollectionToView("personnel", newPersonnel);
			}
			
			Iterable<Role> allRoles = roleService.findAll();
			super.addModelCollectionToView("allRoles", allRoles);
			super.addModelCollectionToView("isPersonnelCreation", true);
			
		}else if(viewType.equals(Actions.DELETE.getValue()) || viewType.equals(Actions.EDIT.getValue())) {
			String personnelId = super.getAllControllerParams().get("id").toString();
			Optional<Personnel> personnel = personnelService.findById(Long.valueOf(personnelId));
			personnel.ifPresent(x -> super.addModelCollectionToView("selected", personnel.get()));

			Iterable<Role> allRoles = roleService.findAll();
			super.addModelCollectionToView("allRoles", allRoles);
			
		}else if(viewType.equals(Actions.VIEW.getValue())) {
			if(super.getAllControllerParams().get("id") != null) {
				String personnelId = super.getAllControllerParams().get("id").toString();
				Optional<Personnel> personnel = personnelService.findById(Long.valueOf(personnelId));
				personnel.ifPresent(x -> super.addModelCollectionToView("selected", personnel.get()));

				Iterable<Role> allRoles = roleService.findAll();
				super.addModelCollectionToView("allRoles", allRoles);
			}
		}
	}
	
	/**
	 * 
	 */
	private void buildPersonnelGlobalViewModel() {
		super.addModelCollectionToView("currentBreadcrumb", LandingPages.buildBreadCrumb(
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest())
		);
		super.addModelCollectionToView("currentPage", currentPage);
		super.addModelCollectionToView("currentRoles", AccountUtil.currentLoggedInUser().getRoles());
		
		Iterable<Personnel> personnels = personnelService.findAllByRoleName("PERSONNEL");
		super.addModelCollectionToView("personnelList", personnels);
		

		Date today = new Date();
		Date lastMonthEnd = DateUtil.getEndingOfMonth(DateUtil.getOneMonthBefore(today));
		Date lastMonthBegin = DateUtil.getBeginingOfMonth(DateUtil.getOneMonthBefore(today));
		Date lastLastMonthEnd = DateUtil.getEndingOfMonth(DateUtil.getMonthsBefore(today, 2));
		Date lastLastMonthBegin = DateUtil.getBeginingOfMonth(DateUtil.getMonthsBefore(today, 2));
		
		List<Double> monthlyVisitPerc = new ArrayList<>();
		personnels.forEach(personnel->{
			int cntLastMonth = 0;
			double perc = 0.0;
			cntLastMonth = recordService.countByPersonnelIdAndServicedateBetween(personnel.getId(), lastMonthBegin, lastMonthEnd);
			int cntLastLastMonth = 0;
			cntLastLastMonth = recordService.countByPersonnelIdAndServicedateBetween(personnel.getId(), lastLastMonthBegin, lastLastMonthEnd);
			int diff = cntLastMonth - cntLastLastMonth;
			try {
				if(cntLastMonth != 0) {
					double divByLastMonth = diff/cntLastMonth;
					perc = divByLastMonth*100;
				}
				if(cntLastMonth == 0) {
					perc = diff*100;
				}
			}catch(ArithmeticException e) {
				e.printStackTrace();
			}

			monthlyVisitPerc.add(perc);
			super.addModelCollectionToView("monthlyGrowth", monthlyVisitPerc);
		});

		super.addModelCollectionToView("currentRoles", AccountUtil.currentLoggedInUser().getAccount().getRoles());
		super.addModelCollectionToView("loggedInAccount", AccountUtil.currentLoggedInUser().getAccount());
	
		super.addModelCollectionToView("locale", AccountUtil.getCurrentLocaleLanguageAndCountry());
		
		super.addModelCollectionToView("logo", globalSettings.getBusinessImage());
	}
	
	@Override
	public <T> PersonnelModelController addModelAttributes(T object){
		super.addControllerParam("modelAttribute", object);
		return this;
	}

}
