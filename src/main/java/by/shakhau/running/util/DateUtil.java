package by.shakhau.running.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    private static final long SECOND_BY_MILLISECOND = 1000;
    private static final long MITUTE_BY_MILLISECOND = SECOND_BY_MILLISECOND * 60;
    private static final long HOUR_BY_MILLISECOND = MITUTE_BY_MILLISECOND * 60;
    private static final long DAY_BY_MILLISECOND = HOUR_BY_MILLISECOND * 24;
    private static final long WEAK_BY_MILLISECOND = DAY_BY_MILLISECOND * 7;

    public static int dayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek;
    }

    public static int fromMonday(int dayOfWeak) {
        int resultDay = dayOfWeak - 1;
        if (resultDay < 1) {
            resultDay = 7;
        }
        return resultDay;
    }

    public static long weakDifference(Date fromDate, Date toDate) {
        return (toDate.getTime() - fromDate.getTime()) / WEAK_BY_MILLISECOND;
    }

    public static Date addDays(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }

    public static Date resetTimeDown(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        return c.getTime();
    }

    public static String dateToString(Date date, String dateFormat) {
        DateFormat format = new SimpleDateFormat(dateFormat);
        return format.format(date);
    }
}
