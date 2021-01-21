/**
 * 
 */
package com.visionous.dms.configuration.helpers;

import java.time.Period;
import java.util.Calendar;
import java.util.Date;

/**
 * @author delimeta
 *
 */
public class DateUtil {
	public static Period getPeriodBetween(Date begin, Date end) {
		Period diff = Period.between(
	            new java.sql.Date(begin.getTime()).toLocalDate(),
	            new java.sql.Date(end.getTime()).toLocalDate());
		return diff;
	}
	
	public static Date getStartWorkingHr() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		return cal.getTime();
	}
	
	public static Date getEndWorkingHr() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY,18);
		cal.set(Calendar.MINUTE,30);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0); 
		return cal.getTime();
	}
	
	public static Date getEndOfYear() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.MONTH, 11);
		cal.set(Calendar.DAY_OF_MONTH, 31);
		return cal.getTime();
	}
	
	public static Date getBegginingOfYear() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}
	
	public static Date getCurrentDateByMonthAndDay(int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, day);
		return cal.getTime();
	}
	
	public static Calendar getCalendarFromDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}
}
