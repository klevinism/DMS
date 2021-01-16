package com.visionous.dms.configuration.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author delimeta
 *
 */
public enum LandingPages {
	DEFAULT("/"),
    LOGIN("/login"),
    LOGOUT("/logout"), 
    INDEX("/index"),
    DASHBOARD("/dashboard"),
    ADMIN("/admin"),
    REGISTER("/register"),
    PERSONNEL("/personnel"), 
    CUSTOMER("/customer"), 
    HISTORY("/history"), 
    RECORD("/record"), 
    ACCOUNT("/account"), 
    QUESTIONNAIRE("/questionnaire") ; 
    
    private final String landingPage;
    
    /**
     * @param landingPage
     */
    private LandingPages(String landingPage) {
        this.landingPage = landingPage;
    }
    
    /**
     * @return landingPage
     */
    public String value() {
        return landingPage;
    }
    
    public static List<HashMap<String, String>> buildBreadCrumb(HttpServletRequest request){
    	String path = "home"+request.getRequestURI();
		StringBuffer fullPath = request.getRequestURL();
		
		String domainPath = fullPath.substring(0, fullPath.indexOf(request.getRequestURI()));
		
		String[] breadcrumbArray = path.split("/");
		List<HashMap<String, String>> breadcrumbList = new ArrayList<>();
		StringBuilder singleBreadcrumbUrl = null;
		HashMap<String, String> singleBreadCrumbMap = null;


		for(int cnt = 0; cnt < breadcrumbArray.length; cnt++) {
			
			singleBreadcrumbUrl =  new StringBuilder("");
			singleBreadCrumbMap = new HashMap<>();
			
			for(int y=0; y<= cnt; y++) {			
				if(!breadcrumbArray[y].equals("home")) {
					singleBreadcrumbUrl.append(breadcrumbArray[y]);
				}else {
					singleBreadcrumbUrl.append(domainPath);
				}
				singleBreadcrumbUrl.append("/");
			}
		
			singleBreadCrumbMap.put(breadcrumbArray[cnt], singleBreadcrumbUrl.toString());
			breadcrumbList.add(singleBreadCrumbMap);

		}
		

    	return breadcrumbList;
    }
}
