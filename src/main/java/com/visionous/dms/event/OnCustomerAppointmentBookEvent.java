/**
 * 
 */
package com.visionous.dms.event;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import com.visionous.dms.configuration.helpers.DmsCore;
import com.visionous.dms.pojo.Appointment;

/**
 * @author delimeta
 *
 */
public class OnCustomerAppointmentBookEvent extends ApplicationEvent{

    /**
	 * 
	 */
	private static final long serialVersionUID = DmsCore.SERIAL_VERSION_UID;
	
	private String appUrl;
    private Locale locale;
    private Appointment appointment;

    public OnCustomerAppointmentBookEvent(Appointment appointment, Locale locale, String appUrl) {
        super(appointment);
        
        this.appointment = appointment;
        this.locale = locale;
        this.appUrl = appUrl;
    }

	/**
	 * @return the appUrl
	 */
	public String getAppUrl() {
		return appUrl;
	}

	/**
	 * @param appUrl the appUrl to set
	 */
	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * @return the appointment
	 */
	public Appointment getAppointment() {
		return appointment;
	}

	/**
	 * @param appointment the appointment to set
	 */
	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}
}
