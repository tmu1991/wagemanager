package com.wz.wagemanager.tools;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class DataUtil {

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
