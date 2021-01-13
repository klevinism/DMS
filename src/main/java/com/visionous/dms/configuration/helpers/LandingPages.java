package com.visionous.dms.configuration.helpers;
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
}
