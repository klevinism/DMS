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
		return (AccountUserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
