package com.wz.wagemanager.controller;

import com.wz.wagemanager.entity.ActSalary;
import com.wz.wagemanager.entity.ActTask;
import com.wz.wagemanager.entity.HiSalary;
import com.wz.wagemanager.entity.SysUser;
import com.wz.wagemanager.service.ActSalaryService;
import com.wz.wagemanager.service.HiSalaryService;
import com.wz.wagemanager.service.TaskService;
import com.wz.wagemanager.service.UserService;
import com.wz.wagemanager.tools.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 待办事项
 * @author WindowsTen
 */
@RestController
@RequestMapping("task")
public class TaskController extends BaseExceptionController {
    @Resource
    private TaskService taskService;

    @PostMapping("list.json")
    public PageBean<? extends Object> listTask(
            @RequestParam(value = "pageSize",defaultValue = GlobalConstant.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "curPage",defaultValue = GlobalConstant.DEFUALT_CUR_PAGE) int curPage
    ){
        Integer maxYear = taskService.getMaxYear();
        if(maxYear!=null&&maxYear!=0){
            int maxMonth = taskService.getMaxMonth(maxYear);
            Page<ActTask> taskPage = taskService.getTask (maxYear, maxMonth, PageUtil.pageable (curPage, pageSize, GlobalConstant.DEFAULT_SORT_ORDER,DEFAULT_SORT_FIELD));
            return new PageBean<> (PageUtil.getPage (taskPage.getTotalElements (),pageSize,curPage),taskPage.getContent ());
        }
        return new PageBean<> ();
    }

    @PostMapping("upload.json")
    public void upload(MultipartFile file,HttpServletResponse response) throws Exception {
        Assert.assertFalse("上传文件不能为空", file.isEmpty());
        String originalFilename = file.getOriginalFilename();
        Assert.assertFalse("文件名不能为空", StringUtils.isBlank(originalFilename));
        Assert.assertTrue("文件类型不符，只能上传xls和xlsx类型的文件！",
                originalFilename.endsWith(".xls") || originalFilename.endsWith(".xlsx"));
        String filePath = DataUtil.getFilePath(originalFilename);
        File dest = DataUtil.getFile(filePath);
        //文件上传
        file.transferTo(dest);
        //从文件名中取出导入数据的年月
        String group = group (DATE_PATTERN,originalFilename);
        Integer year=DataUtil.getYear(group);
        Integer month=DataUtil.getMontoh(group);
        //得到每月多少天
        int dateNum = DateUtil.getDateNum(year, month);
        List<ActTask> list = new ArrayList<>();
        ExcelUtil.readExcel(filePath,1,0,TASK_PROPERTIES,ActTask.class).forEach(task -> {
            //根据工号取到用户
            SysUser user = userService.findByWorkNo(task.getWorkNo());
            if(user == null){
                task.setNote("用户不存在");
                list.add(task);
            }else if(!user.getUsername().equals(task.getUsername())){
                task.setNote("工号与用户不符，请核实");
                list.add(task);
            }else if(user.getSysDept()==null||!task.getDeptName().equals(user.getSysDept().getDeptName())){
                task.setNote("部门与当前用户不符，请核实");
                list.add(task);
            }else{
                task.setMonth(month);
                task.setYear(year);
                task.setDeptId(user.getSysDept().getId());
                //根据从excel表的工号 年份 月份从已存在的待办事项表中进行查询
                ActTask actTask = taskService.findByYearAndMonthAndWorkNo(year, month, task.getWorkNo());
                //当前年月 该员工 不存在借款和扣款纪录 且导入的借款和扣款数额不为空
                if(actTask == null&&task.getDebit().compareTo(BigDecimal.ZERO)!=0||task.getLoan().compareTo(BigDecimal.ZERO)!=0){
                    //保存本条记录
                    taskService.save(task);
                    //更新工资表的借款和扣款记录
                    ActSalary salary = actSalaryService.findByYearAndMonthAndUserId(year, month, user.getId());
                    salary.setLoan(task.getLoan());
                    salary.setOtherDebit(task.getDebit());
                    //重新计算工资
                    CommonUtils.calSalary (salary,null,dateNum);
                    //更新工资记录
                    actSalaryService.save(salary);
                }else{
                    //导入的借款金额 和 数据库中记录不符
                    if(task.getDebit().compareTo(actTask.getDebit())!=0){
                        task.setNote("请核实借款金额");
                    }else if(task.getLoan().compareTo(actTask.getLoan())!=0){
                        task.setNote("请核实扣款金额");
                    }
                }
            }
        });
    }

    private void outputFile(HttpServletResponse response,String xlsName) throws IOException {
        response.reset();
        response.addHeader("Content-Disposition", "attachment;filename=" + new String(xlsName.getBytes()));
        OutputStream ous = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");
        InputStream fis = new BufferedInputStream(new FileInputStream(xlsName));
        byte[] bis = new byte[1024];
        while(-1 != fis.read(bis)){
            ous.write(bis);
        }
        ous.flush();
        ous.close();
    }

    public static final Pattern DATE_PATTERN = Pattern.compile("\\d{4}-\\d{1,2}");
    private String group (Pattern pattern, String originalFilename) {
        Matcher matcher = pattern.matcher (originalFilename);
        if(matcher.find ()){
            return matcher.group ();
        }
        return null;
    }

    private static final String[] headers=new String[]{"部门名称","工号","用户名","借款","扣款","错误事项",};
    private static final String[] fields=new String[]{"deptName","workNo","username","loan","debit","note"};
    @Resource
    private HiSalaryService hiSalaryService;
    @Resource
    private ActSalaryService actSalaryService;
    @Resource
    private UserService userService;

    private final String[] TASK_PROPERTIES = new String[]{"deptName","workNo","username","loan","debit"};

    private static final String DEFAULT_SORT_FIELD="createDate";
}
