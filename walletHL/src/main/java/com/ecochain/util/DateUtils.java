package com.ecochain.util;

import org.joda.time.DateTime;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * 日期工具类。<br>
 *
 * @author zhou jintong
 * @version 1.0
 */
public class DateUtils {

    /**
     * 按照指定的格式返回日期字符串. 默认 "yyyy-MM-dd"
     *
     * @param date
     * @param pattern
     */
    public static String formatDate(Date date, String pattern) {
        if (date == null) return "";
        if (pattern == null) pattern = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return (sdf.format(date));
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String formatDateTime(Date date) {
        return (formatDate(date, "yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 当前时间 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String formatDateTime() {
        return (formatDate(now(), "yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 当前时间 yyyyMMddHHmmss
     *
     * @return
     */
    public static String formatDateTime2() {
        return (formatDate(now(), "yyyyMMddHHmmss"));
    }

    /**
     * yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        return (formatDate(date, "yyyy-MM-dd"));
    }

    /**
     * 当前日期 yyyy-MM-dd
     *
     * @return
     */
    public static String formatDate() {
        return (formatDate(now(), "yyyy-MM-dd"));
    }

    /**
     * 当前日期 yyyyMMdd
     *
     * @return
     */
    public static String formatDate2() {
        return (formatDate(now(), "yyyyMMdd"));
    }

    /**
     * yyyyMMdd
     *
     * @return
     */
    public static String formatDate2(Date date) {
        return (formatDate(date, "yyyyMMdd"));
    }

    /**
     * HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String formatTime(Date date) {
        return (formatDate(date, "HH:mm:ss"));
    }

    /**
     * 当前时间 HH:mm:ss
     *
     * @return
     */
    public static String formatTime() {
        return (formatDate(now(), "HH:mm:ss"));
    }

    /**
     * 当前时间 HHmmss
     *
     * @return
     */
    public static String formatTime2() {
        return (formatDate(now(), "HHmmss"));
    }

    /**
     * 当前时间 Date类型
     *
     * @return
     */
    public static Date now() {
        return (new Date());
    }

    /**
     * yyyy-MM-dd HH:mm:ss 转Date
     *
     * @param datetime
     * @return
     */
    public static Date parseDateTime(String datetime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if ((datetime == null) || (datetime.equals(""))) {
            return null;
        } else {
            try {
                return formatter.parse(datetime);
            } catch (ParseException e) {
                return parseDate(datetime);
            }
        }
    }

    /**
     * format 转Date
     *
     * @param date
     * @return
     */
    public static Date parseDate(String date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);

        if ((date == null) || (date.equals(""))) {
            return null;
        } else {
            try {
                return formatter.parse(date);
            } catch (ParseException e) {
                return null;
            }
        }
    }

    /**
     * yyyy-MM-dd 转Date
     *
     * @param date
     * @return
     */
    public static Date parseDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        if ((date == null) || (date.equals(""))) {
            return null;
        } else {
            try {
                return formatter.parse(date);
            } catch (ParseException e) {
                return null;
            }
        }
    }

    public static Date parseDate(Date datetime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        if (datetime == null) {
            return null;
        } else {
            try {
                return formatter.parse(formatter.format(datetime));
            } catch (ParseException e) {
                return null;
            }
        }
    }

    public static String formatDate(Object o) {
        if (o == null) return "";
        if (o.getClass() == String.class)
            return formatDate((String) o);
        else if (o.getClass() == Date.class)
            return formatDate((Date) o);
        else if (o.getClass() == Timestamp.class) {
            return formatDate(new Date(((Timestamp) o).getTime()));
        } else
            return o.toString();
    }

    public static String formatDateTime(Object o) {
        if (o.getClass() == String.class)
            return formatDateTime((String) o);
        else if (o.getClass() == Date.class)
            return formatDateTime((Date) o);
        else if (o.getClass() == Timestamp.class) {
            return formatDateTime(new Date(((Timestamp) o).getTime()));
        } else
            return o.toString();
    }

    /**
     * 给时间加上或减去指定毫秒，秒，分，时，天、月或年等，返回变动后的时间
     *
     * @param date   要加减前的时间，如果不传，则为当前日期
     * @param field  时间域，有Calendar.MILLISECOND,Calendar.SECOND,Calendar.MINUTE,<br>
     *               Calendar.HOUR,Calendar.DATE, Calendar.MONTH,Calendar.YEAR
     * @param amount 按指定时间域加减的时间数量，正数为加，负数为减。
     * @return 变动后的时间
     */
    public static Date add(Date date, int field, int amount) {
        if (date == null) {
            date = new Date();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(field, amount);

        return cal.getTime();
    }

    public static Date addMilliSecond(Date date, int amount) {
        return add(date, Calendar.MILLISECOND, amount);
    }

    public static Date addSecond(Date date, int amount) {
        return add(date, Calendar.SECOND, amount);
    }

    public static Date addMiunte(Date date, int amount) {
        return add(date, Calendar.MINUTE, amount);
    }

    public static Date addHour(Date date, int amount) {
        return add(date, Calendar.HOUR, amount);
    }

    public static Date addDay(Date date, int amount) {
        return add(date, Calendar.DATE, amount);
    }

    public static Date addMonth(Date date, int amount) {
        return add(date, Calendar.MONTH, amount);
    }

    public static Date addYear(Date date, int amount) {
        return add(date, Calendar.YEAR, amount);
    }

    /**
     * @return 当前日期 yyyy-MM-dd
     */
    public static Date getDate() {
        return parseDate(formatDate());
    }

    public static Date getDateTime() {
        return parseDateTime(formatDateTime());
    }

    public static boolean between(Date date, int offset, TimeUnit unit) {
        return System.currentTimeMillis() - date.getTime() <= unit.toMillis(offset);
    }

    /**
     * 间隔日时间
     *
     * @param date
     * @param interval
     * @return
     */
    public static Date getDate(Date date, int interval) {
        if (date == null) return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) + interval);
        return cal.getTime();
    }

    /**
     * 间隔月时间
     *
     * @param date
     * @param interval
     * @return
     */
    public static Date getDateByMonth(Date date, int interval) {
        if (date == null) return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + interval);
        return cal.getTime();
    }

    /**
     * 间隔分钟时间
     *
     * @param date
     * @param interval
     * @return
     */
    public static Date getDateByMinute(Date date, int interval) {
        if (date == null) return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + interval);
        return cal.getTime();
    }

    /**
     * date1>date2 返回1，否则返回-1
     * @param date1
     * @param date2
     * @return
     */
    public static int compare(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        return cal1.compareTo(cal2);
    }

    /**
     * @param date1
     * @param date2
     * @return
     */
    public static int getDaysBetween(Date date1, Date date2) {
        Calendar d1 = Calendar.getInstance();
        Calendar d2 = Calendar.getInstance();
        d1.setTime(date1);
        d2.setTime(date2);

        if (d1.after(d2)) {
            Calendar swap = d1;
            d1 = d2;
            d2 = swap;
        }
        int days = d2.get(Calendar.DAY_OF_YEAR)
                - d1.get(Calendar.DAY_OF_YEAR);
        int y2 = d2.get(Calendar.YEAR);

        if (d1.get(Calendar.YEAR) != y2) {
            d1 = (Calendar) d1.clone();
            do {
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != y2);
        }
        return days;
    }

    public static int getDiffDays(Date startdate, Date enddate) {
        int days = (int) ((enddate.getTime() - startdate.getTime()) / (24 * 3600 * 1000));
        return days;
    }

    /**
     * 根据日期获取年月日数组
     *
     * @param date 不能为null,该方法不做date为null判断,为null时不知返回什么信息
     * @return int[]{年，月，日}数组长度固定
     * exp :[2016, 05, 19]
     */
    public static String[] getYearAndMonthAndDay(Date date) {
        String strDate = DateUtils.formatDate(date, "yyyy-MM-dd");
        return new String[]{strDate.substring(0, 4), strDate.substring(5, 7), strDate.substring(8, 10)};
    }

    /**
     * 根据日期获取年
     *
     * @param date 不能为null,该方法不做date为null判断,为null时不知返回什么信息
     * @return
     */
    public static String getYear(Date date) {

        DateTime dt = new DateTime(date.getTime());

        int year = dt.year().get();

        return String.valueOf(year);
    }

    /**
     * 根据日期获取月
     *
     * @param date 不能为null,该方法不做date为null判断,为null时不知返回什么信息
     * @return
     */
    public static String getMonth(Date date) {

        DateTime dt = new DateTime(date.getTime());

        int month = dt.monthOfYear().get();

        //TODO 暂时土办法
        if (month < 10)
            return "0" + String.valueOf(month);
        else
            return String.valueOf(month);
    }

    /**
     * 根据日期获取日
     *
     * @param date 不能为null,该方法不做date为null判断,为null时不知返回什么信息
     * @return
     */
    public static String getDay(Date date) {
        DateTime dt = new DateTime(date.getTime());

        int day = dt.dayOfMonth().get();

        return String.valueOf(day);
    }

    public static boolean sameYear(long thisTime, long thatTime) {
        Calendar thisCalendar = Calendar.getInstance();
        thisCalendar.setTimeInMillis(thisTime);

        Calendar thatCalendar = Calendar.getInstance();
        thatCalendar.setTimeInMillis(thatTime);
        return thisCalendar.get(Calendar.YEAR) == thatCalendar.get(Calendar.YEAR);
    }

    public static boolean sameMonth(long thisTime, long thatTime) {
        Calendar thisCalendar = Calendar.getInstance();
        thisCalendar.setTimeInMillis(thisTime);

        Calendar thatCalendar = Calendar.getInstance();
        thatCalendar.setTimeInMillis(thatTime);
        return thisCalendar.get(Calendar.YEAR) == thatCalendar.get(Calendar.YEAR) && thisCalendar.get(Calendar.MONTH) == thatCalendar.get(Calendar.MONTH);
    }

    public static boolean sameDate(long thisTime, long thatTime) {
        Calendar thisCalendar = Calendar.getInstance();
        thisCalendar.setTimeInMillis(thisTime);

        Calendar thatCalendar = Calendar.getInstance();
        thatCalendar.setTimeInMillis(thatTime);
        return thisCalendar.get(Calendar.YEAR) == thatCalendar.get(Calendar.YEAR) && thisCalendar.get(Calendar.MONTH) == thatCalendar.get(Calendar.MONTH) && thisCalendar.get(Calendar.DATE) == thatCalendar.get(Calendar.DATE);
    }

    /**
     * 增加或者减少年份
     *
     * @param date 当前时间
     * @param year 年份，正数为加，负数为减
     */
    public static Date toYear(Date date, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, year);
        return calendar.getTime();
    }

    /**
     * 根据date获取返回每个月的第一天的开始时间。
     * 若果date为空则返回当前月的第一天
     * 2017-01-01 00:00:00
     * @return Date
     */
    public static Date getMonthStartTime(Date date){
        Calendar calendar = Calendar.getInstance();
        if (date != null)
            calendar.setTime(date);

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        //将小时至0
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至0
        calendar.set(Calendar.MINUTE, 0);
        //将秒至0
        calendar.set(Calendar.SECOND,0);
        //将毫秒至0
        calendar.set(Calendar.MILLISECOND, 0);
        //获得当前月第一天
        Date stratDate = calendar.getTime();

        return stratDate;
    }

    /**
     * 根据date获取返回每个月的最后一天的结束时间。
     * 若果date为空则返回当前月的最后一天
     *
     * 2017-01-31 23:59:59
     * @return Date
     */
    public static Date getMonthEndTime(Date date){
        Calendar calendar = Calendar.getInstance();

        if (date != null)
            calendar.setTime(date);

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        //将小时至0
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至0
        calendar.set(Calendar.MINUTE, 0);
        //将秒至0
        calendar.set(Calendar.SECOND,0);
        //将毫秒至0
        calendar.set(Calendar.MILLISECOND, 0);

        //将当前月加1；
        calendar.add(Calendar.MONTH, 1);
        //在当前月的下一月基础上减去1毫秒
        calendar.add(Calendar.MILLISECOND, -1);
        //获得当前月最后一天
        Date endDate = calendar.getTime();
        return endDate;
    }

    public static Date[] getMonthStartEnd(Date d1, int months) {

        DateTime dt = new DateTime(d1.getTime());

        DateTime dt1 = new DateTime(d1.getTime());

        if (months < 0)
            dt = dt1.minusMonths(Math.abs(months));
        else if (months > 0)
            dt = dt1.plusMonths(Math.abs(months));

        int year = dt.year().get();
        int month = dt.monthOfYear().get();
        int start = dt.dayOfMonth().getMinimumValue();
        int end = dt.dayOfMonth().getMaximumValue();

        String format = "%s-%s-%s";
        String startDate = String.format(format, year, month, start);
        String endDate = String.format(format, year, month, end);

        return Stream.of(DateUtils.parseDate(startDate),DateUtils.parseDate(endDate)).toArray(Date[]::new);

    }

    public static Date getWorkingDate() {
        return new Date();
    }

    public static Date getDateByCurrDateAndDays(Date currDate, int days){
        String outDate;
        try {
            String pattern = "yyyy-MM-dd";
            Calendar theCa = Calendar.getInstance();
            theCa.setTime(currDate);
            theCa.add(theCa.DATE, -days);
            Date date = theCa.getTime();
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            outDate = format.format(date);
//            System.out.println(outDate);
            return format.parse(outDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date[] getDayTerm(Date date) {
        String startDate = DateUtils.formatDate(date) + " 00:00:00";
        String endDate = DateUtils.formatDate(date) + " 23:59:59";

        return Stream.of(DateUtils.parseDateTime(startDate),DateUtils.parseDateTime(endDate)).toArray(Date[]::new);

    }

    public static Date[] getDayTerm(Date d1,Date d2) {
        String startDate = DateUtils.formatDate(d1) + " 00:00:00";
        String endDate = DateUtils.formatDate(d2) + " 23:59:59";

        return Stream.of(DateUtils.parseDateTime(startDate),DateUtils.parseDateTime(endDate)).toArray(Date[]::new);

    }

//    public static void main(String[] args) {
////        Date[] dates = getDayTerm(DateUtil.parseDate("2017-02-16"));
////        System.out.println(dates[0]);
////        System.out.println(dates[1]);
////        System.out.println(getMonthStartTime(dates[0]));
////        System.out.println(getMonthEndTime(new Date()));
////        System.out.println(getMonthStartTime(null));
////        System.out.println(getMonthEndTime(null));
//
//        Date d = parseDate("2017-02", "yyyy-MM");
//        Date d1 = getDateByMonth(d, -1);
//        System.out.println(getYear(d1));
//        System.out.println(getMonth(d1));
//    }


//    public static void main(String[] args) {
//        Date d1 = getDate();
//        System.out.println(formatDate(getDate(d1, 180), "yyyy-MM-dd"));
//        System.out.println(formatDate(getDateByMonth(d1, 3), "yyyy-MM-dd"));
//
//        Date date1 = DateUtil.parseDate("2016-03-01");
//        Date date2 = DateUtil.parseDate("2016-03-03");
//        Date date3 = DateUtil.parseDate("2015-03-02");
//        Date date4 = DateUtil.parseDate("2016-08-05");
//        Date date5 = DateUtil.parseDate("2016-11-05");
//        System.out.println(compare(date1, date2));
//        System.out.println(getDaysBetween(date1, date2));
//        System.out.println(getDaysBetween(date1, date2));
//        System.out.println(getDaysBetween(date1, date3));
//        System.out.println(getDaysBetween(date1, date4));
//        System.out.println(getDaysBetween(date1, date5));
//        System.out.println(Arrays.toString(getYearAndMonthAndDay(d1)));
//        System.out.println(getYear(d1) + getMonth(d1) +getDay(d1));
//
//
//        System.out.println("The first day of month=="+getMonthStartTime());
//        System.out.println("The last day of month=="+getMonthEndTime());
//        System.out.println("每个月第一天18点之前"+addHour(getMonthStartTime(), 18));
//        System.out.println("当前时间与每一个第一天18点之前" + compare(new Date(),getMonthStartTime() ));
//
//
//        Date[] dates = getMonthStartEnd(new Date(), 1);
//        System.out.println(dates[0]);
//        System.out.println(dates[1]);
//
//    }
}