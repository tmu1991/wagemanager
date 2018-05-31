package com.wz.wagemanager.tools;

import com.wz.wagemanager.entity.ActSalary;
import com.wz.wagemanager.entity.ActWork;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UploadUtils {
    public static String getDateStr(String originalFilename){
        String dateStr = group (DATE_PATTERN, originalFilename);
        Assert.assertTrue ("文件名格式错误,无法获取工资日期", StringUtils.isNotBlank (dateStr));
        return dateStr;
    }
    public static String fileVerify(MultipartFile file){
        Assert.assertFalse ("上传文件不能为空", file.isEmpty ());
        String originalFilename = file.getOriginalFilename ();
        Assert.assertFalse ("文件名不能为空", StringUtils.isBlank (originalFilename));
        Assert.assertTrue ("文件类型不符，只能上传xls或xlsx类型的文件！",
                originalFilename.endsWith (".xls") || originalFilename.endsWith (".xlsx"));
        return originalFilename;
    }

    public static List<ActWork> actList (String filePath) throws Exception {
        return ExcelUtil.readExcel (filePath, 1, 0, WORD_PROPERTIES, ActWork.class)
                .stream ().peek (actWork -> {
                    actWork.setUsername (DataUtil.deleteStrSpace (actWork.getUsername ()));
//                    actWork.setArrive(actWork.getArrive().replace(".0",""));
                    actWork.setCustomNo (actWork.getCustomNo ().replace (".0", ""));
//                    actWork.setReality(actWork.getReality().replace(".0",""));
                    actWork.setWorkNo (actWork.getWorkNo ().replace (".0", ""));
                }).collect (Collectors.toList ());
    }

    public static String group (Pattern pattern, String originalFilename) {
        Matcher matcher = pattern.matcher (originalFilename);
        if (matcher.find ()) {
            return matcher.group ();
        }
        return null;
    }

    public static final String[] WORD_PROPERTIES = new String[] {"deptName", "workNo", "customNo", "username", "arrive", "reality"};

    public static final Pattern DATE_PATTERN = Pattern.compile ("\\d{4}-\\d{1,2}");
}
