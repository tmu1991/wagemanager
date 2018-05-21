package com.wz.wagemanager.tools;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtil {

    public static int getYear(String group) {
        try {
            int currentYear = DateUtil.getCurrentYear();
            if (StringUtils.isNotBlank (group)) {
                int year = Integer.parseInt(group.substring(0, group.indexOf("-")));
                if(year<1990||year>currentYear){
                    throw new RuntimeException();
                }
                return year;
            } else {
                return currentYear;
            }
        } catch (Exception e) {
            throw new RuntimeException("文件名格式错误");
        }

    }

    public static int getMontoh(String group) {
        try {
            if (StringUtils.isNotBlank (group)) {
                int month = Integer.parseInt(group.substring(group.indexOf("-") + 1, group.length()));
                if(month<1||month>12){
                    throw new RuntimeException();
                }
                return month;
            } else {
                return DateUtil.getCurrentMonth();
            }
        } catch (Exception e) {
            throw new RuntimeException("文件名格式错误");
        }
    }

    public static String getFilePath(String filePath) {
        return System.getProperty("user.dir") + WORK_FILE + filePath;
    }

    private final static String WORK_FILE = "/wzwork/xls/";

    public static File getFile(String filePath) {
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            if (file.getParentFile().mkdirs()) {
                return file;
            } else {
                throw new RuntimeException("创建文件失败");
            }
        }
        if(file.exists ()){
            file.delete ();
        }
        return file;
    }

    public static String deleteStrSpace(String str) {
        if (StringUtils.isEmpty (str)) {
            return str;
        }
        int sz = str.length();
        char[] chs = new char[sz];
        int count = 0;
        for (int i = 0; i < sz; i++) {
            if (!Character.isSpaceChar (str.charAt(i))) {
                chs[count++] = str.charAt(i);
            }
        }
        if (count == sz) {
            return str;
        }
        return new String(chs, 0, count);
    }

}
