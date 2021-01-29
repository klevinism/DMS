package com.visionous.dms.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.configuration.helpers.DateUtil;
import com.visionous.dms.configuration.helpers.FileManager;
import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.event.OnRegistrationCompleteEvent;
import com.visionous.dms.pojo.Account;
import com.visionous.dms.pojo.Personnel;
import com.visionous.dms.pojo.Role;
import com.visionous.dms.repository.AccountRepository;
import com.visionous.dms.repository.HistoryRepository;
import com.visionous.dms.repository.PersonnelRepository;
import com.visionous.dms.repository.RoleRepository;

/**
 * @author delimeta
 *
 */
@Controller
public class PersonnelModelController extends ModelControllerImpl{
	
	private final Log logger = LogFactory.getLog(PersonnelModelController.class);
	
	private PersonnelRepository personnelRepository;
	private RoleRepository roleRepository;
	private AccountRepository accountRepository;
	private ApplicationEventPublisher eventPublisher;
	private static String currentPage = LandingPages.PERSONNEL.value();

	/**
	 * @param personnelRepository
	 */
	@Autowired
	public PersonnelModelController(PersonnelRepository personnelRepository, RoleRepository roleRepository,
			AccountRepository accountRepository, HistoryRepository historyRepository,
			ApplicationEventPublisher eventPublisher) {
		this.personnelRepository = personnelRepository;
		this.roleRepository = roleRepository;
		this.accountRepository = accountRepository;
		this.eventPublisher = eventPublisher;
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
		System.out.println(super.getAllControllerParams().get("viewType") + " <ACITON");

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
		
		if(action.equals(Actions.DELETE.getValue())) {
			Optional<Account> acc = accountRepository.findById(newPersonnel.getId());
			acc.ifPresent(account->{
				account.setPersonnel(null);
				account.setCustomer(null);
				account.setRoles(null);
				accountRepository.delete(account);
			});
			
		}else if(action.equals(Actions.EDIT.getValue()) ) {
			Optional<Account> acc = accountRepository.findById(newPersonnel.getId());
			acc.ifPresent(account -> {
				
				if(super.getAllControllerParams().containsKey("profileimage")) {
					MultipartFile uploadedFile = (MultipartFile) super.getAllControllerParams().get("files");
					if(uploadedFile != null && (uploadedFile.getOriginalFilename()!= null || uploadedFile.getOriginalFilename() != "")){
						StringBuilder attachmentPath = new StringBuilder();
						try {
							String path = FileManager.write(uploadedFile, "/tmp/personnel/profile/");
						    String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss-"));
						    String fileName = date + uploadedFile.getOriginalFilename();
							attachmentPath.append(fileName);
						} catch (IOException e) {
							e.printStackTrace();
						}
						account.setImage(attachmentPath.toString());
					}
				}

				Date birthday = account.getBirthday();
				Date today = new Date();
				Period period = DateUtil.getPeriodBetween(birthday, today);
				account.setAge(period.getYears());
				
				newPersonnel.setAccount(account);
				account.setPersonnel(newPersonnel);
				accountRepository.saveAndFlush(account);
			});
			
		}else if(action.equals(Actions.CREATE.getValue())) {
			
			if(newPersonnel.getAccount().getRoles().get(0).getName().equals("PERSONNEL")) {
				newPersonnel.getAccount().setCustomer(null);
				newPersonnel.getAccount().setPersonnel(null);
				String passPlain = newPersonnel.getAccount().getPassword();
				newPersonnel.getAccount().setPassword(new BCryptPasswordEncoder().encode(passPlain));

				if(super.getAllControllerParams().get("profileimage") != null) {
					MultipartFile uploadedFile = (MultipartFile) super.getAllControllerParams().get("profileimage");
					
					if(!uploadedFile.isEmpty() && (uploadedFile.getOriginalFilename()!= null || !uploadedFile.getOriginalFilename().equals(""))){
						StringBuilder attachmentPath = new StringBuilder();
						try {
							String path = FileManager.write(uploadedFile, "/tmp/personnel/profile/");
						    String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss-"));
						    String fileName = date + uploadedFile.getOriginalFilename();
							attachmentPath.append(fileName);
						} catch (IOException e) {
							e.printStackTrace();
						}
						newPersonnel.getAccount().setImage(attachmentPath.toString());
					}
				} 
				Date birthday = newPersonnel.getAccount().getBirthday();
				Date today = new Date();
				Period period = DateUtil.getPeriodBetween(birthday, today);
				newPersonnel.getAccount().setAge(period.getYears());

				Account newAccount = accountRepository.saveAndFlush(newPersonnel.getAccount());
				newPersonnel.setAccount(newAccount);
				Personnel createdPersonnel = personnelRepository.saveAndFlush(newPersonnel);
				
		        String appUrl = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getContextPath();
				if(createdPersonnel != null) {
					Account publishedAccount = createdPersonnel.getAccount();
					publishedAccount.setPassword(passPlain);
			        eventPublisher.publishEvent(
			        		new OnRegistrationCompleteEvent(publishedAccount,LocaleContextHolder.getLocale(), appUrl)
			        		);
				}
				
			}
		}else if(action.equals(Actions.VIEW.getValue())) {
		}		

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
			
			Iterable<Role> allRoles = roleRepository.findAll();
			super.addModelCollectionToView("allRoles", allRoles);
			super.addModelCollectionToView("isPersonnelCreation", true);

		}else if(viewType.equals(Actions.DELETE.getValue()) || viewType.equals(Actions.EDIT.getValue())) {
			String personnelId = super.getAllControllerParams().get("id").toString();
			Optional<Personnel> personnel = personnelRepository.findById(Long.valueOf(personnelId));
			personnel.ifPresent(x -> super.addModelCollectionToView("selected", personnel.get()));

			Iterable<Role> allRoles = roleRepository.findAll();
			super.addModelCollectionToView("allRoles", allRoles);
			
		}else if(viewType.equals(Actions.VIEW.getValue())) {
			if(super.getAllControllerParams().get("id") != null) {
				String personnelId = super.getAllControllerParams().get("id").toString();
				Optional<Personnel> personnel = personnelRepository.findById(Long.valueOf(personnelId));
				personnel.ifPresent(x -> super.addModelCollectionToView("selected", personnel.get()));

				Iterable<Role> allRoles = roleRepository.findAll();
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
		
		Iterable<Personnel> personnels = personnelRepository.findAllByAccount_Roles_Name("PERSONNEL");
		super.addModelCollectionToView("personnelList", personnels);

		Optional<Account> loggedInAccount = accountRepository.findByUsername(AccountUtil.currentLoggedInUser().getUsername());
		loggedInAccount.ifPresent(account -> {
			super.addModelCollectionToView("currentRoles", account.getRoles());
			super.addModelCollectionToView("loggedInAccount", account);
		});
		
		Locale locales = LocaleContextHolder.getLocale();
		super.addModelCollectionToView("locale", locales.getLanguage() + "_" + locales.getCountry());
	}
	
	@Override
	public <T> PersonnelModelController addModelAttributes(T object){
		super.addControllerParam("modelAttribute", object);
		return this;
	}

}
