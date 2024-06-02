package com.visionous.dms.pojo;

import com.o2dent.lib.accounts.entity.Account;
public class IaoAccount_Customer{
    private Account account;
    private Customer customer;
    public IaoAccount_Customer(Account account, Customer customer){
        this.account = account;
        this.customer = customer;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }
}
