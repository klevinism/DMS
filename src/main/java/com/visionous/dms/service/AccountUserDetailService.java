package com.visionous.dms.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.visionous.dms.configuration.AccountUserDetail;
import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.pojo.Account;
import com.visionous.dms.pojo.Role;
import com.visionous.dms.pojo.Subscription;
import com.visionous.dms.pojo.SubscriptionHistory;


/**
 * @author delimeta
 *
 */
@Service("userDetailsService")
public class AccountUserDetailService implements UserDetailsService{
	private final Log logger = LogFactory.getLog(AccountUserDetailService.class);
	
	private AccountService accountService;
	
	private BusinessService businessService;
	
	private ApplicationContext context;
	
	private SubscriptionHistoryService subscriptionHistoryService;
	
	
	@Autowired
	private AccountUserDetailService(AccountService accountService, ApplicationContext context, 
			SubscriptionHistoryService subscriptionHistoryService, BusinessService businessService) {
		this.accountService = accountService;
		this.context = context;
		this.subscriptionHistoryService = subscriptionHistoryService;
		this.businessService = businessService; 
	}
	
	/**
	 * @param username
	 */
	@Override
	public UserDetails loadUserByUsername(String username){
		List<GrantedAuthority> authorities = new ArrayList<>();
		AccountUserDetail accountUserDetail = null;
		Optional<Account> acc = accountService.findByUsernameOrEmail(username); 
		
		if(acc.isPresent()) {
			authorities.addAll(buildUserAuthority(acc.get().getRoles()));
			accountUserDetail = buildUserForAuthentication(acc.get(), authorities);
			
			
			// TEMPorary implementation, current Business Id should come from another place not here.
			// TODO fix it!
			if(!acc.get().getBusinesses().isEmpty()) {
				if(acc.get().getBusinesses().size() == new AtomicInteger(1).get()) {
					accountUserDetail.setCurrentBusiness(acc.get().getBusinesses().stream().findFirst().get());
				}
			}
			
			// Update Subscription() bean after subscription expiration checks.
//			updateBeanSubscription(); 
		}
		
		return accountUserDetail;
	}
	
	
//	public void updateBeanSubscription() {
//    	Optional<SubscriptionHistory> activeSubscription = this.subscriptionHistoryService.findActiveSubscriptionByBusinessId(AccountUtil.currentLoggedInUser().getCurrentBusiness().getId());
//		if(activeSubscription.isPresent()) {
//			if(activeSubscription.get().getSubscriptionEndDate().isBefore(LocalDateTime.now())) {
//				activeSubscription.get().setActive(false);
//				this.subscriptionHistoryService.update(activeSubscription.get());
//				
//				context.getBean(Subscription.class).setSubscription(new Subscription());
//			}else {
//				context.getBean(Subscription.class).setSubscription(activeSubscription.get().getSubscription());
//			}
//		}else {
//			context.getBean(Subscription.class).setSubscription(new Subscription());
//		}
//	}
	
    /** 
     * @param user 
     * @param authorities
     * @return Returns {@link Account} instead of {@link User} 
     */
    private AccountUserDetail buildUserForAuthentication(Account user,
        List<GrantedAuthority> authorities) {
    	return new AccountUserDetail(user, true, true, true, authorities);
    }

    /**
     * Builds custom Set of {@link GrantedAuthority}
     * @param list
     * @return Set of {@link GrantedAuthority}
     */
    private List<GrantedAuthority> buildUserAuthority(List<Role> list) {
    		
        List<GrantedAuthority> setAuths = new ArrayList<>();
        for (Role userRole : list) {
            setAuths.add(new SimpleGrantedAuthority(userRole.getName()));
        }

        return setAuths;
    }
}
