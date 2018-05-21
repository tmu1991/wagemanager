package com.wz.wagemanager.controller;

import com.wz.wagemanager.annotation.OperInfo;
import com.wz.wagemanager.annotation.OperationType;
import com.wz.wagemanager.entity.*;
import com.wz.wagemanager.service.*;
import com.wz.wagemanager.tools.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequestMapping ("salary")
public class SalaryController extends BaseExceptionController {

    @Resource
    private HiSalaryService hiSalaryService;
    @Resource
    private ActSalaryService actSalaryService;

    @Resource
    private UserService userService;

    @PostMapping ("update.json")
    @OperInfo (type = OperationType.UPDATE)
    public PageBean update (
            @RequestParam (value = "form") String form
    ) throws ParseException, InstantiationException, IllegalAccessException, NoSuchFieldException, UnsupportedEncodingException {
        ActSalary actSalary = CommonUtils.toEntity (form, ActSalary.class);
        actSalaryService.update (actSalary);
        return new PageBean<> ();
    }

    @PostMapping ("delete.json")
    public PageBean delete (
            @RequestParam (value = "ids") String ids
    ) throws ParseException {
        Assert.assertNotNull ("员工不能为空", ids);
        actSalaryService.removeByIdIn (ids.split (","));
        return new PageBean<> ();
    }

    @PostMapping ("dept.json")
    @OperInfo (type = OperationType.QUERY)
    public PageBean<List<ActSalary>> getSalaryByDept (
            @RequestParam (value = "deptId") String deptId,
            @RequestParam (value = "curPage", defaultValue = GlobalConstant.DEFUALT_CUR_PAGE) Integer curPage,
            @RequestParam (value = "pageSize", defaultValue = GlobalConstant.DEFAULT_PAGE_SIZE) Integer pageSize
    ) throws ParseException {
        Assert.assertNotNull ("部门号不能为空", deptId);
        org.springframework.data.domain.Page<ActSalary> salaryPage = actSalaryService.findByDeptId (deptId, PageUtil.pageable (curPage, pageSize, getDefaultSort ()));
        return new PageBean<> (PageUtil.getPage (salaryPage.getTotalElements (), pageSize, curPage), salaryPage.getContent ());
//        return toReturn(date,deptId,pageSize,curPage);
//        return new PageBean<>(salaryService.findByDeptIdAndYearAndMonth(deptId,maxYear,maxMonth));
    }

    @PostMapping ("history.json")
    public PageBean getSalaryByYearAndMonth (
            @RequestParam (value = "year", defaultValue = "0") Integer year,
            @RequestParam (value = "month", defaultValue = "0") Integer month,
            @RequestParam (value = "curPage", defaultValue = GlobalConstant.DEFUALT_CUR_PAGE) Integer curPage,
            @RequestParam (value = "pageSize", defaultValue = GlobalConstant.DEFAULT_PAGE_SIZE) Integer pageSize
    ) {
        year = getYear (year);
        if (year != null && year != 0) {
            month = getMontoh (month);
            Page page = PageUtil.getPage (hiSalaryService.countByYearAndMonth (year, month), pageSize, curPage);
            PageRequest pageRequest = new PageRequest (page.getCurrentPage () - 1, pageSize, getDefaultSort ());
            List<HiSalary> salaries = hiSalaryService.findByYearAndMonth (year, month, pageRequest);
            return new PageBean<> (page, salaries);
        }
        return new PageBean ();
    }

    @PostMapping ("statistics.json")
    public PageBean getSalaryGroupByDept () throws Exception {
        Integer maxYear = hiSalaryService.getMaxYear ();
        if (maxYear != null && maxYear != 0) {
            int maxMonth = hiSalaryService.getMaxMonth (maxYear);
            List<HiSalary> salaries = hiSalaryService.findByGroupDept (maxYear, maxMonth);
            return new PageBean<> (DateUtil.toDateString (maxYear, maxMonth), salaries);
        }
        return new PageBean ();
    }

    @PostMapping ("search.json")
    public PageBean<List<HiSalary>> getSalaryByUser (
            @RequestParam (name = "workNo", required = false) String workNo,
            @RequestParam (name = "userName", required = false) String userName,
            @RequestParam (value = "curPage", defaultValue = GlobalConstant.DEFUALT_CUR_PAGE) Integer curPage,
            @RequestParam (value = "pageSize", defaultValue = GlobalConstant.DEFAULT_PAGE_SIZE) Integer pageSize
    ) throws Exception {
        Pageable pageable = PageUtil.pageable (curPage, pageSize, getDefaultSort ());
        org.springframework.data.domain.Page<HiSalary> salaries = hiSalaryService.findByWorkNoOrUsername (workNo, userName, pageable);
        return new PageBean<> (PageUtil.getPage (salaries.getTotalElements (), pageSize, curPage), salaries.getContent ());
    }

    private PageBean<List<HiSalary>> toReturn (String date, String deptId, int pageSize, int curPage) throws ParseException {
        Integer maxYear, maxMonth;
        if (StringUtils.isBlank (date)) {
            maxYear = hiSalaryService.getMaxYear ();
            if (maxYear == null || maxYear == 0) {
                return new PageBean<> ();
            }
            maxMonth = hiSalaryService.getMaxMonth (maxYear);
        } else {
            maxYear = DateUtil.getYear (date);
            maxMonth = DateUtil.getMonth (date);
        }
        if (maxYear == null || maxYear == 0) {
            return new PageBean<> ();
        }
        Page page = PageUtil.getPage (hiSalaryService.countByDeptIdAndDate (deptId, maxYear, maxMonth), pageSize, curPage);
        PageRequest pageRequest = new PageRequest (page.getCurrentPage () - 1, pageSize, getDefaultSort ());
        return new PageBean<> (DateUtil.toDateString (maxYear, maxMonth), page, hiSalaryService.findByDeptIdAndDete (deptId, maxYear, maxMonth, pageRequest));
    }

    private int getMontoh (int month) {
        if (month == 0) {
            return DateUtil.getCurrentMonth ();
        }
        return month;
    }

    private int getYear (int year) {
        if (year == 0) {
            return DateUtil.getCurrentYear ();
        }
        return year;
    }
//    @Resource
//    private RoleService roleService;
//    @Resource
//    private DeptService deptService;

    @Resource
    private DeclareService declareService;

    @PostMapping ("upload.json")
    @ResponseBody
    public PageBean upload (
            @RequestParam ("file") MultipartFile file
    ) throws Exception {
        SysUser sessionUser = ContextHolderUtils.getPrincipal ();
        SysDept sysDept = sessionUser.getSysDept ();
        Assert.assertNull ("存在未完成工资审批,请完成后重试",declareService.findNotComplete (sysDept));
        String  originalFilename= fileVerify (file);
        String filePath = DataUtil.getFilePath (originalFilename);
        String dateStr=getDateStr (originalFilename);
        File dest = DataUtil.getFile (filePath);
        file.transferTo (dest);
        try {
            Integer year = DataUtil.getYear (dateStr);
            Integer month = DataUtil.getMontoh (dateStr);
            int dateNum = DateUtil.getDateNum (year, month);
            List<ActSalary> saveList = new ArrayList<> ();
            SysDeclare declare = declareService.findNotStart (sysDept);
            for (ActWork actWork : actList (filePath)) {
                SysUser sysUser = userService.findByWorkNo (actWork.getWorkNo ());
                Assert.assertNotNull ("工号为[" + actWork.getWorkNo () + "]的员工不存在,请联系管理员添加后重试", sysUser);
                Assert.assertTrue ("部门[" + actWork.getDeptName () + "]与当前用户部门不符", sysDept.getDeptName ().equals (actWork.getDeptName ()));
                if(declare == null){
                    declare = SysDeclare.builder ()
                            .declareName (declareName (year, month, sysDept.getDeptName ()))
                            .user (sessionUser).dept (sysDept).status (0).build ();
                    declareService.save (declare);
                }
                ActSalary actSalary = actSalaryService.findByYearAndMonthAndUserId (year, month, sysUser.getId ());
                if (actSalary == null) {
                    actSalary = new ActSalary ();
                    actSalary.setMonth (month);
                    actSalary.setYear (year);
                    actSalary.setDeptId (declare.getId ());
                }
                if (StringUtils.isNotBlank (actWork.getReality ())) {
                    actSalary.setAttendance (new BigDecimal (actWork.getReality ()));
                }
                CommonUtils.calSalary (actSalary, sysUser, dateNum);
                saveList.add (actSalary);
            }
            actSalaryService.mutilSave (saveList);
        } finally {
            dest.delete ();
        }
        return new PageBean ();
    }

    private String getDateStr(String originalFilename){
        String dateStr = group (DATE_PATTERN, originalFilename);
        Assert.assertTrue ("文件名格式错误,无法获取工资日期", StringUtils.isNotBlank (dateStr));
        return dateStr;
    }
    private String fileVerify(MultipartFile file){
        Assert.assertFalse ("上传文件不能为空", file.isEmpty ());
        String originalFilename = file.getOriginalFilename ();
        Assert.assertFalse ("文件名不能为空", StringUtils.isBlank (originalFilename));
        Assert.assertTrue ("文件类型不符，只能上传xls或xlsx类型的文件！",
                originalFilename.endsWith (".xls") || originalFilename.endsWith (".xlsx"));
        return originalFilename;
    }

    private List<ActWork> actList (String filePath) throws Exception {
        return ExcelUtil.readExcel (filePath, 1, 0, WORD_PROPERTIES, ActWork.class)
                .stream ().peek (actWork -> {
                    actWork.setUsername (DataUtil.deleteStrSpace (actWork.getUsername ()));
//                    actWork.setArrive(actWork.getArrive().replace(".0",""));
                    actWork.setCustomNo (actWork.getCustomNo ().replace (".0", ""));
//                    actWork.setReality(actWork.getReality().replace(".0",""));
                    actWork.setWorkNo (actWork.getWorkNo ().replace (".0", ""));
                }).collect (Collectors.toList ());
    }

    private String declareName (int year, int month, String deptName) {
        return year + "年" + month + "月" + deptName + "工资申请";
    }

    private String group (Pattern pattern, String originalFilename) {
        Matcher matcher = pattern.matcher (originalFilename);
        if (matcher.find ()) {
            return matcher.group ();
        }
        return null;
    }

    private Sort getDefaultSort () {
        return new Sort (Sort.Direction.DESC, "year", "month", "id");
    }

    private static final String[] WORD_PROPERTIES = new String[] {"deptName", "workNo", "customNo", "username", "arrive", "reality"};


    public static final Pattern DATE_PATTERN = Pattern.compile ("\\d{4}-\\d{1,2}");

    //             ,"late", "leaveEar",
//            "absenteeism","overtime","goOut","busOut","workTime","signed","signIn","signOut","notSignIn","notSignOut", "leave",
//            "busOff","sickLeave","comLeave","homeLeave","weekday","weekend","holiday","dutyDate","dayWork","weekendWork", "holidayWork"};

}
