package com.wz.wagemanager.controller;

import com.wz.wagemanager.entity.ActSalary;
import com.wz.wagemanager.entity.ActTask;
import com.wz.wagemanager.entity.SysUser;
import com.wz.wagemanager.service.ActSalaryService;
import com.wz.wagemanager.service.TaskService;
import com.wz.wagemanager.service.UserService;
import com.wz.wagemanager.tools.*;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 待办事项
 *
 * @author WindowsTen
 */
@RestController
@RequestMapping("task")
public class TaskController extends BaseExceptionController {
    @Resource
    private TaskService taskService;

    @PostMapping("list.json")
    public PageBean<? extends Object> listTask(
            @RequestParam(value = "pageSize", defaultValue = GlobalConstant.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "curPage", defaultValue = GlobalConstant.DEFUALT_CUR_PAGE) int curPage
    ) {
            Page<ActTask> taskPage = taskService.findByPage(PageUtil.pageable(curPage, pageSize, GlobalConstant.DEFAULT_SORT_ORDER, DEFAULT_SORT_FIELD));
            return new PageBean<>(PageUtil.getPage(taskPage.getTotalElements(), pageSize, curPage), taskPage.getContent());
    }

    @PostMapping("upload.json")
    public void upload(@RequestParam("file") MultipartFile file) throws Exception {
        String originalFilename = UploadUtils.fileVerify(file);
        String filePath = DataUtil.getFilePath(originalFilename);
        File dest = DataUtil.getFile(filePath);
        //文件上传
        file.transferTo(dest);
        //从文件名中取出导入数据的年月
        String dateStr = UploadUtils.getDateStr(originalFilename);
        List<ActTask> tasks = new ArrayList<>();
        List<ActSalary> salaries = new ArrayList<>();
        ExcelUtil.readExcel(filePath, 1, 0, TASK_PROPERTIES, ActTask.class).forEach(task -> {
            tellUser(task);
            opr(task,tasks,salaries,dateStr,true);
        });
        if (!CollectionUtils.isEmpty(tasks)) {
            taskService.save(tasks);
        }
        if (!CollectionUtils.isEmpty(salaries)) {
            actSalaryService.save(salaries);
        }
    }

    @PostMapping("update.json")
    public PageBean update(@ModelAttribute ActTask task, @RequestParam("dateStr") String dateStr) {
        tellUser(task);
        opr(task,null,null,dateStr,false);
        return new PageBean<>();
    }

    private void tellUser(ActTask task) {
        //根据工号取到用户
        SysUser user = userService.findByWorkNo(task.getWorkNo());
        Assert.assertNotNull("工号[" + task.getWorkNo() + "]的用户不存在", user);
        Assert.assertTrue("工号[" + task.getWorkNo() + "]与用户名不符，请核实", user.getUsername().equals(task.getUsername()));
        Assert.assertTrue("工号[" + task.getWorkNo() + "]部门与当前用户部门不符，请核实",
                user.getSysDept() != null && task.getDeptName().equals(user.getSysDept().getDeptName()));
        task.setDeptId(user.getSysDept().getId());
    }

    private void opr(ActTask task, List<ActTask> tasks, List<ActSalary> salaries, String dateStr,Boolean isAdd) {
        Integer year = DataUtil.getYear(dateStr);
        Integer month = DataUtil.getMontoh(dateStr);
        //得到每月多少天
        int dateNum = DateUtil.getDateNum(year, month);

        if (task.getLate() != null || task.getDue() != null || task.getOther() != null || task.getOtherEl() != null||task.getLoan() != null||task.getDebit() != null) {
            //更新工资表的借款和扣款记录
            ActSalary salary = actSalaryService.findByYearAndMonthAndWorkNo(year, month, task.getWorkNo());
            Assert.assertNotNull("工号[" + task.getWorkNo() + "]的用户" + year + "年" + month + "月工资记录不存在，请添加工资记录后再添加借款记录", salary);
            List<ActTask> taskList = salary.getTasks();
            if(taskList==null){
                taskList=new ArrayList<>();
            }
            if (task.getLoan() != null) {
                //根据从excel表的工号 年份 月份从已存在的待办事项表中进行查询
                ActTask actTask = taskService.findByTaskDateAndWorkNoAndType(task.getLoanDate(), task.getWorkNo(), 0);
                if (actTask == null) {
                    actTask = task;
                    actTask.setSalary(salary);
                }
                actTask.setAmount(task.getLoan());
                actTask.setTaskDate(task.getLoanDate());
                actTask.setNote(task.getLoanNote());
                if(isAdd){
                    tasks.add(actTask);
                }else {
                    taskService.save(actTask);
                }
                salary.setLoan(task.getLoan());
                taskList.add(actTask);
            }
            if (task.getDebit() != null) {
                ActTask actTask = taskService.findByTaskDateAndWorkNoAndType(task.getDebitDate(), task.getWorkNo(), 1);
                if (actTask == null) {
                    actTask = task;
                    actTask.setSalary(salary);
                }
                actTask.setAmount(task.getDebit());
                actTask.setTaskDate(task.getDebitDate());
                actTask.setNote(task.getDebitNote());
                if(isAdd){
                    tasks.add(actTask);
                }else {
                    taskService.save(actTask);
                }
                salary.setOtherDebit(task.getDebit());
                taskList.add(actTask);
            }
            if (task.getLate().compareTo(salary.getLate()) != 0
                    || task.getDue().compareTo(salary.getPartyDue()) != 0
                    || task.getOther().compareTo(salary.getOther()) != 0
                    || task.getOtherEl().compareTo(salary.getOtherEl()) != 0) {
                salary.setLate(task.getLate());
                salary.setPartyDue(task.getDue());
                salary.setOther(task.getOther());
                salary.setOtherEl(task.getOtherEl());
            }
            salary = cal(salary, task, dateNum);
            if(isAdd){
                salaries.add(salary);
            }else {
                actSalaryService.save(salary);
            }
        }
    }

    private ActSalary cal(ActSalary salary, ActTask task, Integer dateNum) {
        salary.setLoan(task.getLoan());
        salary.setOtherDebit(task.getDebit());
        //重新计算工资
        CommonUtils.calSalary(salary, null, dateNum);
        return salary;
    }

    @Resource
    private ActSalaryService actSalaryService;
    @Resource
    private UserService userService;

    private final String[] TASK_PROPERTIES = new String[]{"deptName", "workNo", "username", "late", "debit", "due", "loan", "other", "otherEl", "taskDate", "note"};

    private static final String DEFAULT_SORT_FIELD = "taskDate";
}
