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
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author WindowsTen
 */
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
    public PageBean update (
            @ModelAttribute ActSalary actSalary
    ) throws ParseException, InstantiationException, IllegalAccessException, NoSuchFieldException, UnsupportedEncodingException {
//        ActSalary actSalary = CommonUtils.toEntity (form, ActSalary.class);
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
        org.springframework.data.domain.Page<ActSalary> salaryPage = actSalaryService.findByDeptId (deptId, PageUtil.pageable (curPage, pageSize,"asc","payroll"));
        return new PageBean<> (PageUtil.getPage (salaryPage.getTotalElements (), pageSize, curPage), salaryPage.getContent ());
    }

    @PostMapping ("history.json")
    public PageBean getSalaryByYearAndMonth (
            @RequestParam (value = "year", required = false) Integer year,
            @RequestParam (value = "month", required = false) Integer month,
            @RequestParam (value = "deptId", required = false) String deptId,
            @RequestParam (value = "curPage", defaultValue = GlobalConstant.DEFUALT_CUR_PAGE) Integer curPage,
            @RequestParam (value = "pageSize", defaultValue = GlobalConstant.DEFAULT_PAGE_SIZE) Integer pageSize
    ) {
        if(year == null){
            year = hiSalaryService.getMaxYear ();
        }
        if(year == null){
            return new PageBean ();
        }
        if(month == null){
            month = hiSalaryService.getMaxMonth (year);
        }
//        Page page = PageUtil.getPage (hiSalaryService.countByYearAndMonth (year, month), pageSize, curPage);
        Pageable pageRequest = PageUtil.pageable(curPage,pageSize,GlobalConstant.DEFAULT_SORT_ORDER,DEFAULT_SORT_FIELD);
        org.springframework.data.domain.Page<HiSalary> hiSalaryPage = hiSalaryService.findByPage (year, month, deptId, pageRequest);
        return new PageBean<> (DateUtil.toDateString (year, month),
                PageUtil.getPage (hiSalaryPage.getTotalElements (),pageSize,curPage), hiSalaryPage.getContent ());
    }

    @PostMapping ("statistics.json")
    public PageBean getSalaryGroupByDept (
            @RequestParam (value = "year", required = false) Integer year,
            @RequestParam (value = "month", required = false) Integer month
    ) throws Exception {
        if(year == null){
            year = hiSalaryService.getMaxYear ();
        }
        if(year == null){
            return new PageBean ();
        }
        if(month == null){
            month = hiSalaryService.getMaxMonth (year);
        }
        List<SalaryArea> salaries = hiSalaryService.findByGroupDept (year, month);
        return new PageBean<> (DateUtil.toDateString (year, month), salaries);
    }

    @PostMapping ("search.json")
    public PageBean<List<HiSalary>> getSalaryByUser (
            @RequestParam (name = "IDNumber", required = false) String IDNumber,
            @RequestParam (name = "userName", required = false) String userName,
            @RequestParam (value = "curPage", defaultValue = GlobalConstant.DEFUALT_CUR_PAGE) Integer curPage,
            @RequestParam (value = "pageSize", defaultValue = GlobalConstant.DEFAULT_PAGE_SIZE) Integer pageSize
    ) throws Exception {
        Pageable pageable = PageUtil.pageable (curPage, pageSize, GlobalConstant.DEFAULT_SORT_ORDER,DEFAULT_SORT_FIELD);
        org.springframework.data.domain.Page<HiSalary> salaries = hiSalaryService.findByIDNumberOrUsername (IDNumber, userName, pageable);
        return new PageBean<> (PageUtil.getPage (salaries.getTotalElements (), pageSize, curPage), salaries.getContent ());
    }

    @PostMapping ("upload.json")
    @ResponseBody
    public PageBean upload (
            @RequestParam ("file") MultipartFile file
    ) throws Exception {
        String  originalFilename= UploadUtils.fileVerify (file);
        String filePath = DataUtil.getFilePath (originalFilename);
        String dateStr=UploadUtils.getDateStr (originalFilename);
        File dest = DataUtil.getFile (filePath);
        file.transferTo (dest);
        try {
            actSalaryService.upload (filePath,dateStr);
        } finally {
            dest.delete ();
        }
        return new PageBean ();
    }

    @PostMapping("loan.json")
    public PageBean<Integer> loan(
            @RequestParam(value = "declareId")String declareId,
            @RequestParam(value = "deptId")String deptId
    ) {
        return new PageBean<>(actSalaryService.findLoanStatus (declareId,deptId));
    }

    private static final String[] DEFAULT_SORT_FIELD=new String[]{ "year", "month"};


}
