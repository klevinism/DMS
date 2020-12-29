/**
 * 
 */
package com.visionous.dms.configuration.helpers;

/**
 * @author delimeta
 *
 */
public enum Actions {
	INFO("info"),
	VIEW("view"),
	EDIT("edit"),
	CREATE("CREATE"),
	DELETE("DELETE"); 
    
    private final String action;
    
    /**
     * @param landingPage
     */
    private Actions(String action) {
        this.action = action;
    }
    
    /**
     * @return landingPage
     */
    public String getValue() {
        return action;
    }
}
