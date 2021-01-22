/**
 * 
 */
package com.visionous.dms.configuration.helpers;

import org.springframework.security.core.context.SecurityContextHolder;

import com.visionous.dms.configuration.AccountUserDetail;

/**
 * @author delimeta
 *
 */
public class AccountUtil {
	
	private AccountUtil() {
	}
	
	public static AccountUserDetail currentLoggedInUser(){
		AccountUserDetail loggedIn = null;
		
		try {
			loggedIn = (AccountUserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return loggedIn;
	}
}
