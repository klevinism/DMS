/**
 * 
 */
package com.visionous.dms.configuration.helpers;

import java.sql.Timestamp;
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
	
	public static Date getStartWorkingHr(String startHour) {
		Calendar cal = Calendar.getInstance();
		String[] hourMin = startHour.split(":");
		int hr = Integer.parseInt(hourMin[0]);
		int min = Integer.parseInt(hourMin[0]);
		
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, hr);
		cal.set(Calendar.MINUTE, min);
		
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
	
	public static Date getEndWorkingHr(String endHour) {
		Calendar cal = Calendar.getInstance();
		String[] hourMin = endHour.split(":");
		int hr = Integer.parseInt(hourMin[0]);
		int min = Integer.parseInt(hourMin[0]);
		
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, hr);
		cal.set(Calendar.MINUTE, min);
		
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
	
	public static Date getOneWeekBefore(Date date) {
		return getDaysBefore(date, 6);
	}

	public static Date getOneMonthBefore(Date date) {
		return getMonthsBefore(date, 1);
	}
	
	public static Date getDaysBefore(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - days);
		return cal.getTime();
	}
	
	public static Date getMonthsBefore(Date date, int months) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - months);
		return cal.getTime();
	}

	
	public static Date getBeginingOfMonth(Date startDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}
	
	public static Date getEndingOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}

	
	
	public static Date setDays(Date currentDate, int nrOfDays) {
		Calendar temp = DateUtil.getCalendarFromDate(currentDate);
		temp.add(Calendar.DAY_OF_MONTH, nrOfDays);
		return temp.getTime();
	}
	
	
	public static Date addHours(Date currentDate, int hoursNr) {
		Calendar temp = DateUtil.getCalendarFromDate(currentDate);
		temp.add(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY) + hoursNr);
		return temp.getTime();
	}
	
	public static Date addMinutes(Date currentDate, int minutesNr) {
		Calendar temp = DateUtil.getCalendarFromDate(currentDate);
		temp.add(Calendar.MINUTE, temp.get(Calendar.MINUTE) + minutesNr);
		return temp.getTime();
	}
	
	public static boolean isDayPeriod(Period period) {
		return (!period.isNegative() && period.getDays() <= 7 && period.getMonths() <= 0 && period.getYears() <=0) ? Boolean.TRUE : Boolean.FALSE;
	}
	
	
	public static boolean isWeekPeriod(Period period) {
		return (!period.isNegative() && period.getDays() >= 8 && period.getDays() <= 31 && period.getMonths() <= 0 && period.getYears() <=0) ? Boolean.TRUE : Boolean.FALSE;
	}
	
	public static boolean isMonthPeriod(Period period) {
		return (!period.isNegative() && (period.getMonths() >= 0) && period.getYears() <=0) ? Boolean.TRUE : Boolean.FALSE;
	}

	public static boolean isYearPeriod(Period period) {
		return (!period.isNegative() && period.getYears() >=0 ) ? Boolean.TRUE : Boolean.FALSE;
	}
	
	/**
	 * @param date
	 * @return
	 */
	public static Date setDayToBegginingOfMonth(Date date) {
		Calendar calendar = DateUtil.getCalendarFromDate(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 1);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	/**
	 * @param start
	 * @param end
	 * @return
	 */
	public static int calculateDaysToAddFromPeriod(Date start, Date end) {
		Period period = DateUtil.getPeriodBetween(start, end);
		int daysToAdd = 0;
		if(DateUtil.isDayPeriod(period)) {
			daysToAdd = 1;
		}else if(DateUtil.isWeekPeriod(period)) {
			daysToAdd = 7;
		}else if(DateUtil.isMonthPeriod(period)) {
			daysToAdd = DateUtil.getCalendarFromDate(start).getActualMaximum(Calendar.DAY_OF_MONTH);
		}else if(DateUtil.isYearPeriod(period)) {
			daysToAdd = 365;
		}else {
			daysToAdd = 1;
		}
		return daysToAdd;
	}

	/**
	 * @param endingDate
	 * @return
	 */
	public static Date setDayToEndOfMonth(Date date) {
		Calendar calendar = DateUtil.getCalendarFromDate(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	/**
	 * @param date
	 * @return
	 */
	public static Date setHoursToBegginingOfDay(Date date) {
		Calendar calendar = DateUtil.getCalendarFromDate(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}
	
	/**
	 * @param date
	 * @return
	 */
	public static Date setHoursToEndingOfDay(Date date) {
		Calendar calendar = DateUtil.getCalendarFromDate(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	/**
	 * @param start
	 * @return
	 */
	public static Date setDayToEndOfYear(Date date) {
		Calendar calendar = DateUtil.getCalendarFromDate(date);
		calendar.set(Calendar.MONTH, 11);
		calendar.set(Calendar.DAY_OF_MONTH, 31);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		return calendar.getTime();
	}

	/**
	 * @param start
	 * @return
	 */
	public static Date setDayToBegginingOfYear(Date date) {
		Calendar calendar = DateUtil.getCalendarFromDate(date);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		return calendar.getTime();
	}
	
    /**
     * @param expiryTimeInMinutes
     * @return
     */
    public static Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }
}
