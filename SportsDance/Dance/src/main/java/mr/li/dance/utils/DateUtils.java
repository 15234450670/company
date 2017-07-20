package mr.li.dance.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    private int mCurrYear, mCurrMonth, mCurrDay;
    private int mSelYear, mSelMonth, mSelDay;

    public DateUtils() {
        setCurrentYearMonth();
    }

    public DateUtils(int year, int month, int day) {
        mCurrYear = year;
        mCurrMonth = month;
        mCurrDay = day;

        setSelectYearMonth(mCurrYear, mCurrMonth, mCurrDay);
    }

    /**
     * 通过年份和月份
     *
     * @param month
     * @return
     */
    public int getMonthDays(int year, int month) {
        month++;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
                    return 29;
                } else {
                    return 28;
                }
            default:
                return -1;
        }
    }

    public String getDayWeek() {
        return getDayWeek(mSelYear,mSelMonth,mSelDay);
    }
    public String getDayWeek(int year, int month,int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day - 1);
        Log.d("DateView", "DateView:First:" + calendar.getFirstDayOfWeek());
        int weekIndex = calendar.get(Calendar.DAY_OF_WEEK);
        String weekName = "一";
        switch (weekIndex) {
            case 1:
                weekName = "一";
                break;
            case 2:
                weekName = "二";
                break;
            case 3:
                weekName = "三";
                break;
            case 4:
                weekName = "四";
                break;
            case 5:
                weekName = "五";
                break;
            case 6:
                weekName = "六";
                break;
            case 7:
                weekName = "日";
                break;
        }
        return weekName;
    }
    private void setSelectYearMonth(int year, int month, int day) {
        mSelYear = year;
        mSelMonth = month;
        mSelDay = day;
    }

    private void setCurrentYearMonth() {
        Calendar calendar = Calendar.getInstance();
        mCurrYear = calendar.get(Calendar.YEAR);
        mCurrMonth = calendar.get(Calendar.MONTH);
        mCurrDay = calendar.get(Calendar.DATE);
        setSelectYearMonth(mCurrYear,mCurrMonth,mCurrDay);
    }

    /**
     * 左点击，日历向后翻页
     */
    public void leftClick() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(mSelYear, mSelMonth, mSelDay);
        calendar.add(Calendar.DATE, -1);
        mSelYear = calendar.get(Calendar.YEAR);
        mSelMonth = calendar.get(Calendar.MONTH);
        mSelDay = calendar.get(Calendar.DATE);
    }

    /**
     * 右点击，日历向前翻页
     */
    public void rightClick() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(mSelYear, mSelMonth, mSelDay);
        calendar.add(Calendar.DATE, +1);
        mSelYear = calendar.get(Calendar.YEAR);
        mSelMonth = calendar.get(Calendar.MONTH);
        mSelDay = calendar.get(Calendar.DAY_OF_MONTH);
    }
    public String getDDMMYYStr(){
        return getDDMMYYStr(mSelYear,mSelMonth,mSelDay);
    }
    public String getDDMMYYStr(int year,int month,int daty) {
        String monthName = "一";
        switch (month) {
            case 1:
                monthName = "一";
                break;
            case 2:
                monthName = "二";
                break;
            case 3:
                monthName = "三";
                break;
            case 4:
                monthName = "四";
                break;
            case 5:
                monthName = "五";
                break;
            case 6:
                monthName = "六";
                break;
            case 7:
                monthName = "七";
                break;
            case 8:
                monthName = "八";
                break;
            case 9:
                monthName = "九";
                break;
            case 10:
                monthName = "十";
                break;
            case 11:
                monthName = "十一";
                break;
            case 12:
                monthName = "十二";
                break;
        }
        return daty + " " + monthName + "月" + year;
    }
    public String getYYMMDDStr() {
        return mSelYear + "-" + (mSelMonth + 1) + "-" + mSelDay;
    }

    public int getmSelYear() {
        return mSelYear;
    }

    public int getmSelMonth() {
        return mSelMonth + 1;
    }

    public int getmSelDay() {
        return mSelDay;
    }

    public String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(mSelYear,mSelMonth,mSelDay);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = calendar.getTime();
        return sdf.format(date);
    }

    public String getStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(mSelYear,mSelMonth,mSelDay);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00");
        Date date = calendar.getTime();
        return sdf.format(date);
    }

    public String getSpotsStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(mSelYear,mSelMonth,mSelDay);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = calendar.getTime();
        return sdf.format(date);
    }
    public String getAdvanceDayTime(int advanceCount) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(mSelYear, mSelMonth, mSelDay);
        calendar.add(Calendar.DATE, (-1)*advanceCount);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = calendar.getTime();
        return sdf.format(date);
    }

    public String getEndTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(mSelYear,mSelMonth,mSelDay);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 23:59");
        Date date = calendar.getTime();
        return sdf.format(date);
    }

    public void setmSelYear(int mSelYear) {
        this.mSelYear = mSelYear;
    }

    public void setmSelMonth(int mSelMonth) {
        this.mSelMonth = mSelMonth;
    }

    public void setmSelDay(int mSelDay) {
        this.mSelDay = mSelDay;
    }
}
