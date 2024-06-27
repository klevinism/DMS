/**
 * 
 */
package com.visionous.dms.configuration;

import com.o2dent.lib.accounts.persistence.AccountService;
import com.o2dent.lib.accounts.persistence.BusinessService;
import com.o2dent.security.bundle.authentication.access.O2AccountInfo;
import com.o2dent.security.bundle.authentication.access.O2OidcAccountService;

import com.visionous.dms.pojo.GlobalSettings;
import com.visionous.dms.service.GlobalSettingsService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * @author delimeta
 *
 * This class is used to find the user in db after oAuth2 athentication.
 * Since introduction of decoupling for Account, Business and
 * Roles we need to attach global settings to the business of the logged_in account.
 */
@Service
public class AuthenticationOidcService extends O2OidcAccountService {
	private final AccountService accountService;
	private final GlobalSettingsService globalSettingsService;
	private final BusinessService businessService;

	public AuthenticationOidcService(AccountService accountService, BusinessService businessService,
									 GlobalSettingsService globalSettingsService) {
		super(accountService);
		this.accountService = accountService;
		this.globalSettingsService = globalSettingsService;
		this.businessService = businessService;
	}

	@Override
	public O2AccountInfo loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
		O2AccountInfo accountLoaded = super.loadUser(userRequest);
		return this.attachGlobalSettingsToLoggedInAccount(accountLoaded);
	}

	private O2AccountInfo attachGlobalSettingsToLoggedInAccount(O2AccountInfo accountInfo) {
		if(Objects.nonNull(accountInfo) && Objects.nonNull(accountInfo.getCurrentBusiness())){
			if(Objects.isNull(accountInfo.getCurrentBusinessSettings())){
				Optional<GlobalSettings> globalSettings = globalSettingsService.findByBusinessId(accountInfo.getCurrentBusiness().getId());
				globalSettings.ifPresent(settings -> accountInfo.setCurrentBusinessSettings(settings));
			}
		}
		return accountInfo;
	}
}
