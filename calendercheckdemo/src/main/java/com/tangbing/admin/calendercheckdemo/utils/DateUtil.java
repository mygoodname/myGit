package com.tangbing.admin.calendercheckdemo.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by tangbing on 2019/3/20.
 * Describe :
 */


public class DateUtil {

    //获取今天是星期几
    public static int getCurrentDayOfWeek() {
        Calendar calendar = Calendar.getInstance();//日历对象
        calendar.setTime(new Date());//设置当前日期
        int i = calendar.get(Calendar.DAY_OF_WEEK);
        return i;
    }
    //获取本月第一天是星期几
    public static int getFirstDayOfMouth() {
        Calendar calendar = Calendar.getInstance();//日历对象
        calendar.set(Calendar.DAY_OF_MONTH, 1);
//        calendar.setTime(new Date());//设置当前日期
        int i = calendar.get(Calendar.DAY_OF_WEEK);
        return i;
    }
    //获取本月第一天是星期几
    public static int getLastDayOfMouth() {
        Calendar calendar = Calendar.getInstance();//日历对象
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
//        calendar.setTime(new Date());//设置当前日期
        int i = calendar.get(Calendar.DAY_OF_WEEK);
        return i;
    }

    //获取前一天的日期
    public static Date getDateForLastDay(int amount) {
        Calendar ca = Calendar.getInstance();//得到一个Calendar的实例
        ca.setTime(new Date()); //设置时间为当前时间
        ca.add(Calendar.DATE, -amount);
        Date lastDay = ca.getTime(); //结果
        //前一年ca.add(Calendar.YEAR, -1);
        //求前一月ca.add(Calendar.MONTH, -1)，
        //前一天ca.add(Calendar.DATE, -1)
        //lastDay.getDay()周几  lastDay.getDate()日期
        return lastDay;
    }

    //获取后一天的日期
    public static Date getDateForNextDay(int amount) {
        Calendar ca = Calendar.getInstance();//得到一个Calendar的实例
        ca.setTime(new Date()); //设置时间为当前时间
        ca.add(Calendar.DATE, +amount);
        Date lastDay = ca.getTime(); //结果
        return lastDay;
    }
    public static String getDateOfMouth(int m){
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        if(m<=cal.getActualMaximum(Calendar.DAY_OF_MONTH)){
            cal.set(Calendar.DAY_OF_MONTH,m);
            return dateFormater.format(cal.getTime());
        }else return "";
    }
    public static int getCurrentMouthDays(){
        Calendar cal = Calendar.getInstance();
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
    public static String getCurrentDate(){
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        return dateFormater.format(cal.getTime());
    }
}