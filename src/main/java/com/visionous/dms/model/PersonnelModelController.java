package com.visionous.dms.model;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.Actions;
import com.visionous.dms.configuration.helpers.LandingPages;
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

	private static String currentPage = LandingPages.PERSONNEL.value();

	/**
	 * @param personnelRepository
	 */
	@Autowired
	public PersonnelModelController(PersonnelRepository personnelRepository, RoleRepository roleRepository,
			AccountRepository accountRepository,
			HistoryRepository historyRepository) {
		this.personnelRepository = personnelRepository;
		this.roleRepository = roleRepository;
		this.accountRepository = accountRepository;
	}
	
	
	/**
	 *
	 */
	@Override
	public void run() {
			
		// If action occurred, persist object to db
		if(super.getAllControllerParams().containsKey("modelAttribute")) {
			if(super.getAllControllerParams().containsKey("action")) {
				persistModelAttributes(
						(Personnel) super.getAllControllerParams().get("modelAttribute"), 
						super.getAllControllerParams().get("action").toString().toLowerCase()
						);
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
				newPersonnel.setAccount(account);
				account.setPersonnel(newPersonnel);
				accountRepository.saveAndFlush(account);
			});
			
		}else if(action.equals(Actions.CREATE.getValue())) {
			if(super.getAllControllerParams().get("roles") != null) {
				String[] roleids = (String[]) super.getAllControllerParams().get("roles");
				
				newPersonnel.getAccount().setCustomer(null);
				newPersonnel.getAccount().setPersonnel(null);
				
				for(String roleName: roleids) {
					roleRepository.findByName(roleName).ifPresent(role -> {newPersonnel.getAccount().addRole(role);});
				}
				
				Account newAccount = accountRepository.saveAndFlush(newPersonnel.getAccount());
				newPersonnel.setAccount(newAccount);				
				personnelRepository.saveAndFlush(newPersonnel);
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
			Personnel newPersonnel = new Personnel();
			newPersonnel.setAccount(new Account());
			super.addModelCollectionToView("selected", newPersonnel);
			
			Iterable<Role> allRoles = roleRepository.findAll();
			super.addModelCollectionToView("allRoles", allRoles);
			
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
		

		Iterable<Personnel> personnels = personnelRepository.findAll();
		super.addModelCollectionToView("personnelList", personnels);
	}
	
	@Override
	public <T> PersonnelModelController addModelAttributes(T object){
		super.addControllerParam("modelAttribute", object);
		return this;
	}

}
