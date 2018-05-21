package com.wz.wagemanager.tools;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class DateUtil {
    public static void main (String[] args) throws ParseException {
        System.out.println (getMonth ("2018年12月"));
        System.out.println (getYear ("2018年12月"));
    }
    public static String toDateString(int year,int month){
        return String.valueOf (year) + "年" + month + "月";
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
