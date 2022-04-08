/**
 * 
 */
package com.visionous.dms.configuration;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.visionous.dms.configuration.helpers.AccountUtil;
import com.visionous.dms.rest.response.ResponseBody;
import com.visionous.dms.service.SubscriptionHistoryService;

/**
 * @author delimeta
 *
 */
@RestControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice {
	private SubscriptionHistoryService subscriptionHistoryService;
	private MessageSource messageSource;

	/**
	 * 
	 */
	@Autowired
	public ResponseAdvice(SubscriptionHistoryService subscriptionHistoryService, MessageSource messageSource) {
		this.subscriptionHistoryService = subscriptionHistoryService;
		this.messageSource = messageSource;
	}
	@Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return returnType.getParameterType().isAssignableFrom(ResponseEntity.class);
    }

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

		String subscriptionExpired=messageSource.getMessage("SubscriptionExpired", null, LocaleContextHolder.getLocale());
		String featureExcluded = messageSource.getMessage("FeatureNotAllowedSubscriptionExpired", null, LocaleContextHolder.getLocale());
		String subscriptionExpiredPersonnel = messageSource.getMessage("SubscriptionExpiredPersonnel", null, LocaleContextHolder.getLocale());
		String subscriptionExpiredAdmin = messageSource.getMessage("SubscriptionExpiredAdmin", null, LocaleContextHolder.getLocale());
		
		ResponseBody<String> bodyReplacing = new ResponseBody<>();
		if(AccountUtil.currentLoggedInUser() != null 
				&& Objects.nonNull(AccountUtil.currentLoggedInUser().getCurrentBusiness())
				&& request.getMethodValue().equals("POST") 
				&& !subscriptionHistoryService.findActiveSubscriptionByBusinessId(
						AccountUtil.currentLoggedInUser()
						.getCurrentBusiness()
						.getId()).isPresent()) {
			
			bodyReplacing.setError("error");
			
			if(AccountUtil.currentLoggedInUser() != null ) {
				bodyReplacing.setMessage(subscriptionExpired + " " + featureExcluded + " " + (AccountUtil.currentLoggedInUser().getRoles().stream().anyMatch(role -> role.getName().equals("PERSONNEL")) ? subscriptionExpiredPersonnel : subscriptionExpiredAdmin));
			}
			
			return bodyReplacing;
		}else {
			return body;
		}
	}
}