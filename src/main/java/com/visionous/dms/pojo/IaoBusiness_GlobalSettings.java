package com.visionous.dms.pojo;

import com.o2dent.lib.accounts.entity.Business;

public class IaoBusiness_GlobalSettings {
    private GlobalSettings globalSettings;
    private Business business;
    public IaoBusiness_GlobalSettings(){}
    public IaoBusiness_GlobalSettings(Business business, GlobalSettings globalSettings){
        this.business = business;
        this.globalSettings = globalSettings;
    }
    public GlobalSettings getGlobalSettings() {
        return globalSettings;
    }
    public void setGlobalSettings(GlobalSettings globalSettings) {
        this.globalSettings = globalSettings;
    }
    public Business getBusiness() {
        return business;
    }
    public void setBusiness(Business business) {
        this.business = business;
    }
}
