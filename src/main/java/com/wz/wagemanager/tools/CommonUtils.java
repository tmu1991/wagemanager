package com.wz.wagemanager.tools;

import com.wz.wagemanager.entity.ActSalary;
import com.wz.wagemanager.entity.SysUser;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommonUtils {

    /**
     * 计算扣款
     * @param actSalary
     * @return
     */
    public static BigDecimal getDebit(ActSalary actSalary){
        BigDecimal bigDecimal = BigDecimal.ZERO;
        if(isNotBlank(actSalary.getLate())){
            bigDecimal=bigDecimal.add(actSalary.getLate());
        }
        if(isNotBlank(actSalary.getOtherEl())){
            bigDecimal=bigDecimal.add(actSalary.getOtherEl());
        }
        if(isNotBlank(actSalary.getOther())){
            bigDecimal=bigDecimal.add(actSalary.getOther());
        }
        if(isNotBlank(actSalary.getPartyDue())){
            bigDecimal=bigDecimal.add(actSalary.getPartyDue());
        }
        if(isNotBlank(actSalary.getOtherDebit())){
            bigDecimal=bigDecimal.add(actSalary.getOtherDebit());
        }
        return bigDecimal;
    }
    public static boolean isNotBlank(BigDecimal bigDecimal){
        return bigDecimal!=null&&bigDecimal.compareTo(BigDecimal.ZERO)!=0;
    }

    public static void copyNullValue(ActSalary original, ActSalary novel) throws IllegalAccessException {
        Class<? extends ActSalary> aClass = original.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for(Field declaredField:declaredFields){
            declaredField.setAccessible(true);
            if(declaredField.get(original)==null){
                declaredField.set(original,declaredField.get(novel));
            }
        }
    }
    public static <T> List<T> castEntity(List<Object[]> list, Class<T> clazz,String[] properties) throws Exception {
        List<T> returnList = new ArrayList<>();
        if (list.size() == 0){
            return returnList;
        }
        for(Object[] objects:list){
            T t=clazz.newInstance();
            for(int i=0;i<properties.length;i++){
                Field declaredField = clazz.getDeclaredField(properties[i]);
                declaredField.setAccessible(true);
                Class<?> type = declaredField.getType();
                if(type.equals(String.class)){
                    declaredField.set(t,String.valueOf(String.valueOf(objects[i])));
                }else if(type.equals(BigDecimal.class)){
                    declaredField.set(t,new BigDecimal(String.valueOf(objects[i])));
                }else{
                    declaredField.set(t,objects[i]);
                }
            }
            returnList.add(t);
        }
        return returnList;
    }

    public static <T> T toEntity(String form,Class<T> clazz) throws IllegalAccessException, InstantiationException, NoSuchFieldException, UnsupportedEncodingException {
        String[] split = URLDecoder.decode(form,"utf-8").split("&");
        Assert.assertTrue("修改的数据不能为空",split.length>0);
        T t = clazz.newInstance();
        String key,value;
        for(String str:split){
            key=str.substring(0,str.indexOf("="));
            value=str.substring(str.indexOf("=")+1,str.length());
            Field declaredField = clazz.getDeclaredField(key);
            declaredField.setAccessible(true);
            Class<?> type = declaredField.getType();
            if(type.equals(String.class)){
                declaredField.set(t,String.valueOf(String.valueOf(value)));
            }else if(type.equals(BigDecimal.class)){
                declaredField.set(t,new BigDecimal(String.valueOf(value)));
            }else{
                declaredField.set(t,value);
            }
        }
        return t;
    }

    public static void calSalary(ActSalary actSalary, SysUser sysUser, int dateNum){
        if(sysUser!=null){
            actSalary.setUserId (sysUser.getId ());
            actSalary.setDeptId (sysUser.getSysDept().getId ());
            actSalary.setWorkNo(sysUser.getWorkNo());
            actSalary.setDeptName(sysUser.getSysDept ().getDeptName());
            actSalary.setUsername(sysUser.getUsername());
            actSalary.setBase(sysUser.getBase());
            actSalary.setSeniority(sysUser.getSeniority());
            actSalary.setCreditCard(sysUser.getCreditCard());
        }
        actSalary.setDailyWage(getDailyWage(actSalary,dateNum));
        actSalary.setWorkTotal(getWorkTotal(actSalary));
        actSalary.setGrossPay(getGrossPay(actSalary));
        actSalary.setIncomeTax(getIncomeTax(actSalary).setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros ());
        actSalary.setPayroll(getPayroll(actSalary));
        actSalary.setUpdateDate (new Date());
    }

    private final static int SCALE = 2;

    private static BigDecimal getPayroll(ActSalary actSalary){
        return actSalary.getGrossPay().subtract(actSalary.getInsurance()).subtract(actSalary.getAccuFund()).subtract(actSalary.getIncomeTax())
                .add(actSalary.getBonus()).subtract(actSalary.getLate()).subtract(actSalary.getOtherDebit()).subtract(actSalary.getPartyDue())
                .subtract(actSalary.getLoan()).subtract(actSalary.getOther()).subtract (actSalary.getOtherEl ()).setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros ();
    }
    private static BigDecimal getIncomeTax(ActSalary actSalary){
        BigDecimal grossPay = actSalary.getGrossPay();
        BigDecimal taxIncome = grossPay.subtract (BigDecimal.valueOf (3500)).subtract (actSalary.getAccuFund ()).subtract (actSalary.getInsurance ());
        if (taxIncome.compareTo (BigDecimal.ZERO)<=0) {
            return BigDecimal.ZERO;
        }else if (taxIncome.compareTo (BigDecimal.valueOf (1500))<=0) {
            return taxIncome.multiply (BigDecimal.valueOf (0.03));
        }else if (taxIncome.compareTo (BigDecimal.valueOf (4500))<=0) {
            return taxIncome.multiply (BigDecimal.valueOf (0.10)).subtract(BigDecimal.valueOf (105));
        }else if (taxIncome.compareTo (BigDecimal.valueOf (9000))<=0) {
            return taxIncome.multiply (BigDecimal.valueOf (0.20)).subtract(BigDecimal.valueOf (555));
        }else if (taxIncome.compareTo (BigDecimal.valueOf (35000))<=0) {
            return taxIncome.multiply (BigDecimal.valueOf (0.25)).subtract(BigDecimal.valueOf (1005));
        }else if (taxIncome.compareTo (BigDecimal.valueOf (55000))<=0) {
            return taxIncome.multiply (BigDecimal.valueOf (0.30)).subtract(BigDecimal.valueOf (2755));
        }else if (taxIncome.compareTo (BigDecimal.valueOf (80000))<=0) {
            return taxIncome.multiply (BigDecimal.valueOf (0.35)).subtract(BigDecimal.valueOf (5505));
        }else {
            return taxIncome.multiply (BigDecimal.valueOf (0.45)).subtract(BigDecimal.valueOf (13505));
        }
    }

    /**
     * 津贴 通过页面计算,后台暂时不管
     * @param actSalary
     * @return
     */
    private static BigDecimal getAllowance(ActSalary actSalary){
        return actSalary.getAllowance().multiply(actSalary.getAttendance()).setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros ();
    }

    private static BigDecimal getGrossPay(ActSalary actSalary){
        return actSalary.getDailyWage().multiply(actSalary.getWorkTotal()).add(actSalary.getSeniority()).add(actSalary.getSubDay().multiply(actSalary.getSubWork()))
                .add(actSalary.getAllowance()).setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros ();
    }
    private static BigDecimal getWorkTotal(ActSalary actSalary){
        return actSalary.getAttendance ().add(actSalary.getBusTravel()).add(actSalary.getHoliday()).setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros ();
    }

    /**
     * 日工资 基本工资*系数/天数
     * @param actSalary
     * @param dateNum
     * @return
     */
    private static BigDecimal getDailyWage(ActSalary actSalary,int dateNum){
        return actSalary.getBase().multiply(actSalary.getCoeff()).divide(new BigDecimal(String.valueOf(dateNum)),SCALE,BigDecimal.ROUND_HALF_UP).stripTrailingZeros ();
    }
}
