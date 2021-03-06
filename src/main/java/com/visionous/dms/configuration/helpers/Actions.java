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
	CREATE("create"),
	DELETE("delete"); 
    
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
    
    public static Actions[] getAll(){
    	return new Actions[]{INFO,VIEW,EDIT,CREATE,DELETE};
    }
}
