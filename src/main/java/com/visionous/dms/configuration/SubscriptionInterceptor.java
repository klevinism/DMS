/**
 * 
 */
package com.visionous.dms.configuration;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.configuration.helpers.LandingPages;
import com.visionous.dms.service.SubscriptionHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author delimeta
 *
 */
@Component
public class SubscriptionInterceptor implements HandlerInterceptor {
	private SubscriptionHistoryService subscriptionHistoryService;

	public SubscriptionInterceptor(SubscriptionHistoryService subscriptionHistoryService){
		this.subscriptionHistoryService = subscriptionHistoryService;
	}
	
    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
      String path=request.getRequestURI();
      
      if(!path.contains(LandingPages.LOGIN.value()) 
    		  && !path.contains(LandingPages.BUSINESS.value()) ) {
	  	  
    	  if(request.getMethod().equals("POST") && path.contains("/api") && request.getParameter("status") == null
    			  && !subscriptionHistoryService.findActiveSubscriptionByBusinessId(AccountUtil.currentLoggedInUser().getCurrentBusiness().getId()).isPresent()) {
	  		  path = "/api/subscription?status=expired";  		  
	  		  request.getRequestDispatcher(path).forward(request,response); //redirect to /api/subscription to not get 404 or 500 error, to not invoke actual restricted post rpc 
	  		  return false;
	  	  }else {
	    	  if(request.getMethod().equals("GET") && path.contains("create") && !subscriptionHistoryService.findActiveSubscriptionByBusinessId(AccountUtil.currentLoggedInUser().getCurrentBusiness().getId()).isPresent()) {
	    		  if(AccountUtil.currentLoggedInUser().getAccount().getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN"))) {
	    			  path = "/admin/subscription?status=expired";
	    		  }else {
	    			  path = "/subscription?status=expired";  
	    		  }
		  		  request.getRequestDispatcher(path).forward(request,response); //redirect MVC to /subscription to not get 404 or 500 error 
	    		  return false;
	    	  }
	      }
      }
      
      
  	  return true;
    }

}
