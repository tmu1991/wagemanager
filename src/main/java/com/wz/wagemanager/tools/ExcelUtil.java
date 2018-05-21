package com.wz.wagemanager.tools;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Excel文件操作工具类，包括读、写等功能
 *
 * @Version : 1.00
 * @Date    : 2014-10-29 上午12:40:44
 */
public class ExcelUtil {

    public static <T> List<T> readExcel(String filePath,int start,int end,String[] properties,Class<T> clazz) throws Exception {
        if(filePath.endsWith (".xls")){
            return readExcel2003 (filePath,start,end,properties,clazz);
        }else if(filePath.endsWith (".xlsx")){
            return readExcel2007 (filePath,start,end,properties,clazz);
        }else{
            throw new RuntimeException ("文件格式不符,只支持xls或xlsx格式文件");
        }
    }

    private static <T> List<T> readExcel2007 (String filePath, int start, int end, String[] properties, Class<T> clazz) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException ("文件名为" + file.getName() + "Excel文件不存在！");
        }
        try(FileInputStream fis= new FileInputStream(file);
            XSSFWorkbook wb = new XSSFWorkbook(fis)){
            return readSheet (wb,start,end,properties,clazz);
        }
    }
    private static  <T> List<T> readExcel2003(String filePath,int start,int end,String[] properties,Class<T> clazz)throws Exception{
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException ("文件名为" + file.getName() + "Excel文件不存在！");
        }
        try(FileInputStream fis= new FileInputStream(file);
            HSSFWorkbook wb = new HSSFWorkbook(fis)) {
            return readSheet (wb,start,end,properties,clazz);
        }
    }

    private static  <T> List<T> readSheet(Workbook wb,int start,int end,String[] properties,Class<T> clazz)throws Exception{
        List<T> list = new ArrayList<> ();
        Sheet sheet = wb.getSheetAt(0);
        AtomicInteger lastRowNum = new AtomicInteger (sheet.getLastRowNum ());
        if (lastRowNum.get () <= 0) {
            throw new RuntimeException ("文件内容为空");
        }
        Row row ;
        // 循环读取
        for (int i = start; i <= lastRowNum.get () + end; i++) {
            row = sheet.getRow(i);
            if (row != null) {
                T newInstance = clazz.newInstance ();
                list.add (newInstance);
                for(int j=0;j<properties.length;j++){
                    String value = getCellValue(row.getCell(j));
                    Field declaredField = clazz.getDeclaredField (properties[j]);
                    declaredField.setAccessible (true);
                    declaredField.set (newInstance,value);
                }
            }
        }
        return list;
    }
    public static void writeExcel (
            String sheetName, String[] headers, String[] fields, List<?> datas, String excelFile, String pattern) throws Exception {
        // 声明一个工作薄
        try(HSSFWorkbook workbook = new HSSFWorkbook ();
            OutputStream out=new FileOutputStream (excelFile)) {
            // 生成一个工作表
            HSSFSheet sheet = workbook.createSheet (sheetName);
            // 创建列首-增加样式-赋值
            settingCell (sheet,headers,fields,datas,pattern);
            workbook.write(out);
        }catch (Exception e){
            e.printStackTrace ();
        }
    }
    private static final Pattern NUMBER_REGEX = Pattern.compile ("^\\d+(\\.\\d+)?$");
    private static void settingCell(HSSFSheet sheet, String[] headers, String[] fields, List<?> datas, String pattern)throws Exception{
        HSSFRow row = sheet.createRow (0);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell titleCell = row.createCell (i);
            HSSFRichTextString text = new HSSFRichTextString (headers[i]);
            titleCell.setCellValue (text);
        }
        for (int i = 0; i < datas.size (); i++) {
            HSSFRow sheetRow = sheet.createRow (i + 1);
            Object o = datas.get(i);
            for (int j = 0; j < fields.length; j++) {
                Field declaredField = o.getClass ().getDeclaredField (fields[j]);
                declaredField.setAccessible (true);
                Object obj = declaredField.get (o);
                if (obj == null) {
                    continue;
                }
                HSSFCell cell = sheetRow.createCell (j);
                // 如果是时间类型,按照格式转换
                String textValue;
                if (obj instanceof Date) {
                    Date date = (Date) obj;
                    SimpleDateFormat sdf = new SimpleDateFormat (pattern);
                    textValue = sdf.format (date);
                } else {
                    // 其它数据类型都当作字符串简单处理
                    textValue = obj.toString ();
                }
                // 利用正则表达式判断textValue是否全部由数字组成
                if (StringUtils.isNotBlank (textValue)) {
                    Matcher matcher = NUMBER_REGEX.matcher (textValue);
                    if (matcher.matches ()) {
//                 是数字当作int处理
                        cell.setCellValue (Integer.parseInt (textValue));
                    } else {
                        // 不是数字做普通处理
                        cell.setCellValue (textValue);
                    }
                }
            }
        }
    }

    /**
     * 功能:获取单元格的值
     */
    private static String getCellValue(Cell cell) {
        Object result = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    result = cell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    result = cell.getNumericCellValue();
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    result = cell.getBooleanCellValue();
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    result = cell.getCellFormula();
                    break;
                case Cell.CELL_TYPE_ERROR:
                    result = cell.getErrorCellValue();
                    break;
                case Cell.CELL_TYPE_BLANK:
                    break;
                default:
                    break;
            }
        }
        return result.toString();
    }
}
