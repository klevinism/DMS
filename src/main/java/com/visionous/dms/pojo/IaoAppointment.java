package com.visionous.dms.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.o2dent.lib.accounts.entity.Account;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

public class IaoAppointment {

    private Long id;
    private Long customerId;
    private Long personnelId;
    private Long serviceTypeId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime appointmentDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime appointmentEndDate;
    @DateTimeFormat (pattern="dd-MM-YYYY")
    private Date addeddate;
    private Customer customer;
    private Personnel personnel;
    private Account customerAccount;
    private Account personnelAccount;
    private ServiceType serviceType;

    public IaoAppointment(Long id, Long customerId, Long personnelId, Long serviceTypeId,
                          LocalDateTime appointmentDate, LocalDateTime appointmentEndDate,
                          Date addeddate, Customer customer, Personnel personnel, Account customerAccount,
                          Account personnelAccount, ServiceType serviceType) {
        this.id = id;
        this.customerId = customerId;
        this.personnelId = personnelId;
        this.serviceTypeId = serviceTypeId;
        this.appointmentDate = appointmentDate;
        this.appointmentEndDate = appointmentEndDate;
        this.addeddate = addeddate;
        this.customer = customer;
        this.personnel = personnel;
        this.customerAccount = customerAccount;
        this.personnelAccount = personnelAccount;
        this.serviceType = serviceType;
    }

    public IaoAppointment(Appointment appointment) {
        this.id = appointment.getId();
        this.customerId = appointment.getCustomerId();
        this.personnelId = appointment.getPersonnelId();
        this.serviceTypeId = appointment.getServiceTypeId();
        this.appointmentDate = appointment.getAppointmentDate();
        this.appointmentEndDate = appointment.getAppointmentEndDate();
        this.addeddate = appointment.getAddeddate();
        this.customer = appointment.getCustomer();
        this.personnel = appointment.getPersonnel();
        this.customerAccount = null;
        this.personnelAccount = null;
        this.serviceType = appointment.getServiceType();
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the customerId
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the personnelId
     */
    public Long getPersonnelId() {
        return personnelId;
    }

    /**
     * @param personnelId the personnelId to set
     */
    public void setPersonnelId(Long personnelId) {
        this.personnelId = personnelId;
    }

    /**
     * @return the serviceTypeId
     */
    public Long getServiceTypeId() {
        return serviceTypeId;
    }

    /**
     * @param serviceTypeId the serviceTypeId to set
     */
    public void setServiceTypeId(Long serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    /**
     * @return the appointmentDate
     */
    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    /**
     *
     * @param appointmetDate
     */
    public void setAppointmentDate(LocalDateTime appointmetDate) {
        this.appointmentDate = appointmetDate;
    }

    /**
     * @return the addeddate
     */
    public Date getAddeddate() {
        return addeddate;
    }

    /**
     * @param addeddate the addeddate to set
     */
    public void setAddeddate(Date addeddate) {
        this.addeddate = addeddate;
    }

    /**
     * @return the appointmentEndDate
     */
    public LocalDateTime getAppointmentEndDate() {
        return appointmentEndDate;
    }

    /**
     * @param appointmentEndDate the appointmentEndDate to set
     */
    public void setAppointmentEndDate(LocalDateTime appointmentEndDate) {
        this.appointmentEndDate = appointmentEndDate;
    }

    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the personnel
     */
    public Personnel getPersonnel() {
        return personnel;
    }

    /**
     * @param personnel the personnel to set
     */
    public void setPersonnel(Personnel personnel) {
        this.personnel = personnel;
    }

    /**
     * @return the serviceType
     */
    public ServiceType getServiceType() {
        return serviceType;
    }

    /**
     * @param serviceType the serviceType to set
     */
    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public Account getCustomerAccount() {
        return customerAccount;
    }

    public void setCustomerAccount(Account customerAccount) {
        this.customerAccount = customerAccount;
    }

    public Account getPersonnelAccount() {
        return personnelAccount;
    }

    public void setPersonnelAccount(Account personnelAccount) {
        this.personnelAccount = personnelAccount;
    }

    @Override
    public String toString() {
        return "Appointment [id=" + id + ", customerId=" + customerId + ", personnelId=" + personnelId
                + ", appointmentDate=" + appointmentDate + ", addeddate=" + addeddate + "]";
    }
}
