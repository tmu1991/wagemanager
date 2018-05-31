package com.wz.wagemanager.tools;

import com.wz.wagemanager.exception.HandThrowException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTimeUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {


    private final static List<String> datePattern = Arrays.asList ("yyyy-MM","yyyy年MM月","yyyy/MM","yyyyMM");

    public static Date getDate(String dateStr){
        SimpleDateFormat dateFormat;
        for(String pattern:datePattern){
            try{
                dateFormat=new SimpleDateFormat (pattern);
                return dateFormat.parse (dateStr);
            }catch (Exception e){

            }
        }
        throw new HandThrowException ("日期格式错误");
    }

    public static int getYear(Date date) {
        Calendar cl=Calendar.getInstance ();
        cl.setTime (date);
        return cl.get (Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        Calendar cl=Calendar.getInstance ();
        cl.setTime (date);
        return cl.get (Calendar.MONTH)+1;
    }

    public static void main (String[] args) {
        Date date = getDate ("2017-12");
        System.out.println (getYear (date));
        System.out.println (getMonth (date));
    }

    public static Boolean compareDate(Date DATE1, Date DATE2) {
        return DateUtils.isSameDay(DATE1,DATE2);
    }

    public static String toDateString(Integer year,Integer month){
        if(year == null || year == 0){
            return "";
        }
        return String.valueOf (year) + "-" + month;
    }
    public static int getYear(String date) throws ParseException {
        return Integer.parseInt (date.substring (0,date.indexOf ("年")));
    }

    public static int getMonth(String date) throws ParseException {
        return Integer.parseInt (StringUtils.substringBetween (date,"年","月"));
    }
    //获得当前年份
    public static int getCurrentYear(){
        return Integer.parseInt(DateTimeFormatter.ofPattern("yyyy").format(LocalDate.now()));
    }
    //获得当前月份
    public static int getCurrentMonth(){
        return Integer.parseInt(DateTimeFormatter.ofPattern("M").format(LocalDate.now()));
    }

    //获得给定年份每月有多少天
    public static int getDateNum(int year,int month){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,year);
        //Java月份才0开始算
        cal.set(Calendar.MONTH, month - 1);
        return cal.getActualMaximum(Calendar.DATE);
    }

}
