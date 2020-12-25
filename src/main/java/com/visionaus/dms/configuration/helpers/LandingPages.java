package com.visionaus.dms.configuration.helpers;
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
    REGISTER("/register"); 
    
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
    public String getValue() {
        return landingPage;
    }
}
