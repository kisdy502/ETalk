package cn.sdt.libniocommon.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SDT13411 on 2017/10/25.
 */

public class DateUtil {
    public final static String FORMAT_MMDD = "MMDD";
    public final static String FORMAT_MMDDHH = "MMDDHH";
    public final static String FORMAT_YYYYMMDD = "yyyy-MM-dd";
    public final static String FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public final static String FORMAT_HHMMSS = "HH:mm:ss";

    /**
     * 将时间转换为时间戳
     */
    public static String dateToTimestamp(String dateStr) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(dateStr);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /**
     * 将时间转换为时间戳
     */
    public static String dateToTimestamp(String dateStr, String format) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = simpleDateFormat.parse(dateStr);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /**
     * 将时间戳转换为时间
     */
    public static String timestampToDateString(String s, String format) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 将时间戳转换为时间
     */
    public static String timestampToDateString(String s) {
        return timestampToDateString(s, "yyyy-MM-dd HH:mm:ss");
    }

    public static String timestampToDateString(long stamp, String format) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = new Date(stamp);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 将时间戳转换为时间
     */
    public static String timestampToDateString(long stamp) {
        return timestampToDateString(stamp, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date timestampToDate(long stamp) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(stamp);
        return date;
    }


    /**
     * 判断跨天的方法
     *
     * @param starttime
     * @param currentime
     * @return
     */
    public static boolean isCrossDay(long starttime, long currentime) {

        Date startDate = DateUtil.timestampToDate(starttime);
        Date currentDate = DateUtil.timestampToDate(currentime);

        if (startDate.getDay() == currentDate.getDay() && startDate.getMonth() == currentDate.getMonth() &&
                startDate.getYear() == currentDate.getYear()) {
            //年月日形同，没有跨天
            Log.d("DateUtil", starttime + "没有和" + currentime + "跨天");
            return false;
        } else {
            // 跨天了
            Log.d("DateUtil", starttime + "和" + currentime + "跨天了");
            return true;
        }
    }
}
