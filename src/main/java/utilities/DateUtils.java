package utilities;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DateUtils {
	protected static Log log = null;

	protected DateUtils() {
		log = LogFactory.getLog(getClass());
	}

	/**
	 * Getting milliseconds of a current time
	 *
	 * @return a number of milliseconds
	 */
	public static long getMilliseconds() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		return timestamp.getTime();
	}

	public static boolean isDateBetweenPeriod(String strDate, String strDateFrom, String strDateTo, String format) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		Date date = simpleDateFormat.parse(strDate);
		Date dateFrom = simpleDateFormat.parse(strDateFrom);
		Date dateTo = simpleDateFormat.parse(strDateTo);

		return ((date.after(dateFrom) || date.equals(dateFrom)) && (date.before(dateTo) || date.equals(dateTo)));
	}

	public static boolean areDatesBetweenPeriod(List<String> lstDates, String strDateFrom, String strDateTo, String format) throws ParseException {
		for (String date : lstDates) {
			if (!isDateBetweenPeriod(date, strDateFrom, strDateTo, format)) {
				log.debug(String.format("DEBUG: This error ocurs at %s <= %s < %s", strDateFrom, date, strDateTo));
				return false;
			}

		}
		return true;
	}
	
	public static String getDateFollowingGMT(String gmt, String format) {
		Date currentTime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(TimeZone.getTimeZone(gmt));
		return sdf.format(currentTime);
	}
	
	public static String getDateBeforeCurrentDate(int numberBeforeCurrentDate, String format) {
		return DateUtils.getDateBeforeCurrentDate(numberBeforeCurrentDate, "GMT-4", format);
	}
	
	public static String getDateBeforeCurrentDate(int numberBeforeCurrentDate, String gmt, String format) {
		if(numberBeforeCurrentDate > 0) {
			numberBeforeCurrentDate = numberBeforeCurrentDate * -1;
		}
		
		Calendar c = Calendar.getInstance();
		c = (Calendar) c.clone();
		c.add(Calendar.DATE, numberBeforeCurrentDate);
		Date date = c.getTime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone(gmt));
		
		return simpleDateFormat.format(date);
	}
	
	public static String getDateAffterCurrentDate(int numberAffterCurrentDate, String format) {
		return DateUtils.getDateBeforeCurrentDate(numberAffterCurrentDate, "GMT-4", format);
	}
	
	public static String getDateAffterCurrentDate(int numberAffterCurrentDate, String gmt, String format) {
		if(numberAffterCurrentDate < 0) {
			numberAffterCurrentDate = numberAffterCurrentDate * -1;
		}
		
		Calendar c = Calendar.getInstance();
		c = (Calendar) c.clone();
		c.add(Calendar.DATE, numberAffterCurrentDate);
		Date date = c.getTime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone(gmt));
		
		return simpleDateFormat.format(date);
	}
	
	public static String getFirstDateOfLastWeek(String format) {
		return getFirstDayOfLastWeek(format, "GMT-04", true);
	}
	
	public static String getFirstDayOfLastWeek(String format, String gmt, boolean isSunday) {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone(gmt));
		c = (Calendar) c.clone();
		Date date = c.getTime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone(gmt));
		String currentDateGMT = simpleDateFormat.format(date);
		Date dateGMT4 = DateUtils.convertToDate(currentDateGMT, format);
		String currentDateGMT1 = dateGMT4.toString();
		
		if(currentDateGMT1.contains("Sun") && isSunday) {
			c.add(Calendar.WEEK_OF_YEAR,  -2);
			c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 1);
		} else {
			// last week
			c.add(Calendar.WEEK_OF_YEAR, -1);
			
			// first day is monday
			c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 1);
		}
		Date monDay2WeekAgo = c.getTime();
		return simpleDateFormat.format(monDay2WeekAgo);
	}
	
	public static int getYear(String gmt) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.setTimeZone(TimeZone.getTimeZone(gmt));
		LocalDateTime now = LocalDateTime.now();
		return now.getYear();
	}
	
	public static int getMonth(String gmt) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.setTimeZone(TimeZone.getTimeZone(gmt));
		LocalDateTime now = LocalDateTime.now();
		return now.getMonthValue();
	}
	
	public static int getDay(String gmt) {
		LocalDateTime now = LocalDateTime.now();
		return now.getDayOfMonth();
	}
	
	public static String getMonthYear(String gmt, int previousMonth, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.setTimeZone(TimeZone.getTimeZone(gmt));
		
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, previousMonth);
		return sdf.format(c.getTime());
	}
	
	public static String formatDate(String date, String oldFormat, String newFormat) {
		if (date.isEmpty() || oldFormat.isEmpty() || newFormat.isEmpty()) {
			log.error("Debug: date or oldFormat or newFormat is empty");
			return "";
		}
		Date d = convertToDate(date, oldFormat);
		SimpleDateFormat formatter = new SimpleDateFormat(newFormat);
		
		return formatter.format(d);
	}
	
	public static String convertMillisToDateTime(String timeMillis, String format) {
		Date date = new Date(Long.parseLong(timeMillis));
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
		return sdf.format(date);
	}
	
	public static String getTimeStamp(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date());
	}
	
	public static boolean isDateTimeSortedInDescending(List<String> lst, String format) {
		for(int i = 0; i < lst.size() -1; i++) {
			String s1 = lst.get(i);
			String s2 = lst.get(i + 1);
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			try {
				if (!Objects.isNull(s1) && !s1.isEmpty() && !Objects.isNull(s2) && !s2.isEmpty()) {
					Date date1 = parseToDate(sdf, s1);
					Date date2 = parseToDate(sdf, s2);
					if(date1.compareTo(date2) < 0) {
						return false;
					}
				}
			} catch (ParseException e) {
				log.error("Exception: ParseException occurs at isDate isDateTimeSortedInDescending " + e.getMessage());
				return false;
			}
		}
		return true;
	}
	
	public static boolean isDateTimeSortedInAccenDing(List<String> lst, String format) {
		for(int i = 0; i < lst.size() -1; i++) {
			String s1 = lst.get(i);
			String s2 = lst.get(i + 1);
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			try {
				if (!Objects.isNull(s1) && !s1.isEmpty() && !Objects.isNull(s2) && !s2.isEmpty()) {
					Date date1 = parseToDate(sdf, s1);
					Date date2 = parseToDate(sdf, s2);
					if(date1.compareTo(date2) > 0) {
						return false;
					}
				}
			} catch (ParseException e) {
				log.error("Exception: ParseException occurs at isDate isDateTimeSortedInAccending " + e.getMessage());
				return false;
			}
		}
		return true;
	}
	
	private static Date parseToDate(SimpleDateFormat sdf, String stringDate) throws ParseException {
		if (stringDate.trim().equals("-")) {
			return sdf.parse("01/01/0001 01:01:01");
		}
		return sdf.parse(stringDate);
	}
	
	public static String convertAPIDateToString(String dateAPI) {
		Date d = new Date(Long.parseLong(dateAPI));
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("GMT-4"));
		return formatter.format(d);
	}
	
	public static int getLastDayOfMonth(int month, int year) {
		String date = "1/" +month+ "/" + year;
		LocalDate lastDayOfMonth = LocalDate.parse(date, DateTimeFormatter.ofPattern("d/M/yyyy"))
				.with(TemporalAdjusters.lastDayOfMonth());
		return lastDayOfMonth.getDayOfMonth();
	}
	
	public static Date getLastDateByDayOfWeek(String gmt, int dayOfWeek) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.setTimeZone(TimeZone.getTimeZone(gmt));
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, dayOfWeek);
		return c.getTime();
	}
	
	public static Date convertToDate(String date, String format) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			return formatter.parse(date.replaceAll("(?<=\\d)(st|nd|rd|th)", ""));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
};