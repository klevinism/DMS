/**
 * 
 */
package com.visionous.dms.configuration.helpers;

import org.springframework.security.core.context.SecurityContextHolder;

import com.visionous.dms.pojo.Account;

/**
 * @author delimeta
 *
 */
public class AccountUtil {
	
	private AccountUtil() {
	}
	
	public static Account currentLoggedInUser(){
		return (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
