package com.wz.wagemanager.controller;

import com.wz.wagemanager.entity.ActForm;
import com.wz.wagemanager.entity.ActSalary;
import com.wz.wagemanager.entity.ActTask;
import com.wz.wagemanager.entity.SysUser;
import com.wz.wagemanager.service.ActSalaryService;
import com.wz.wagemanager.service.TaskService;
import com.wz.wagemanager.service.UserService;
import com.wz.wagemanager.tools.*;
import org.activiti.engine.impl.util.CollectionUtil;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
    public PageBean upload(@RequestParam("file") MultipartFile file) throws Exception {
        String originalFilename = UploadUtils.fileVerify(file);
        String filePath = DataUtil.getFilePath(originalFilename);
        File dest = DataUtil.getFile(filePath);
        //文件上传
        file.transferTo(dest);
        //从文件名中取出导入数据的年月
        List<ActTask> tasks = new ArrayList<>();
        List<ActSalary> salaries = new ArrayList<>();
        ExcelUtil.readExcel(filePath, 1, 0, TASK_PROPERTIES, ActForm.class).forEach(form -> {
            tellUser(form);
            opr(form,tasks,salaries,true);
        });
        if (!CollectionUtils.isEmpty(tasks)) {
            taskService.save(tasks);
        }
        if (!CollectionUtils.isEmpty(salaries)) {
            actSalaryService.save(salaries);
        }
        return new PageBean<> ();
    }

    @PostMapping("update.json")
    public PageBean update(@ModelAttribute ActForm form) {
        tellUser(form);
        opr(form,null,null,false);
        return new PageBean<>();
    }

    @PostMapping("salary.json")
    public PageBean<List<ActTask>> update(@RequestParam("salaryId")String salaryId) {
        return new PageBean<>(taskService.findBySalaryId (salaryId));
    }

    private void tellUser(ActForm form) {
        //根据工号取到用户
        SysUser user = userService.findByWorkNo(form.getWorkNo());
        Assert.assertNotNull("工号[" + form.getWorkNo() + "]的用户不存在", user);
        Assert.assertTrue("工号[" + form.getWorkNo() + "]与用户名不符，请核实", user.getUsername().equals(form.getUsername()));
        Assert.assertTrue("工号[" + form.getWorkNo() + "]部门与当前用户部门不符，请核实",
                user.getSysDept() != null && form.getDeptName().equals(user.getSysDept().getDeptName()));
    }

    private void opr(ActForm form, List<ActTask> tasks, List<ActSalary> salaries,Boolean isAdd) {

        //更新工资表的借款和扣款记录
        ActSalary salary = actSalaryService.findByWorkNo (form.getWorkNo());
        Assert.assertNotNull("工号[" + form.getWorkNo() + "]的用户" + salary.getYear ()+ "年" + salary.getMonth () + "月工资记录不存在，请添加工资记录后再添加借款记录", salary);
        boolean isChange=false;
        if(form.getLate ()!=null&&form.getLate().compareTo(salary.getLate()) != 0){
            salary.setLate(form.getLate());
            isChange = true;
        }
        if(form.getDue() != null&&form.getDue().compareTo(salary.getPartyDue()) != 0){
            salary.setPartyDue(form.getDue());
            isChange = true;
        }
        if(form.getOther() != null&&form.getOther().compareTo(salary.getOther()) != 0){
            salary.setOther(form.getOther());
            isChange = true;
        }
        if(form.getOtherEl() != null&&form.getOtherEl().compareTo(salary.getOtherEl()) != 0){
            salary.setOtherEl(form.getOtherEl());
            isChange = true;
        }

        List<ActTask> formTask = form.getTasks ();
        BigDecimal loan=salary.getLoan ()==null?BigDecimal.ZERO:salary.getLoan (),
                debit=salary.getOtherDebit ()==null?BigDecimal.ZERO:salary.getOtherDebit ();
        boolean isTask=false;
        if(!CollectionUtils.isEmpty (formTask)){
            List<ActTask> taskList=new ArrayList<> ();
            for(ActTask actTask:formTask){
                if(actTask.getAmount () == null){
                    continue;
                }
                actTask.setTaskDate (actTask.getTaskDate ().trim ());
                actTask.setNote (actTask.getNote ().trim ());
                if(org.apache.commons.lang3.StringUtils.isNotBlank (actTask.getId ())){
                    ActTask taskById = taskService.findById (actTask.getId ());
                    if(taskById.getAmount ().compareTo (actTask.getAmount ())==0
                            &&taskById.getTaskDate ().equals (actTask.getTaskDate ())
                            &&taskById.getNote ().equals (actTask.getNote ())
                            &&taskById.getType ().equals (actTask.getType ())){
                        continue;
                    }
                }
                actTask.setDeptId (form.getDeptId ());
                actTask.setDeptName (form.getDeptName ());
                actTask.setUsername (form.getUsername ());
                actTask.setWorkNo (form.getWorkNo ());
                actTask.setSalaryId (salary.getId ());
                if(actTask.getType ()==0){
                    loan=loan.add (actTask.getAmount ());
                }
                if(actTask.getType ()==1){
                    debit=debit.add (actTask.getAmount ());
                }
                if(isAdd){
                    tasks.add(actTask);
                }else{
                    taskList.add (actTask);
                }
                isTask=true;
            }
            if(isTask){
                salary.setOtherDebit (debit);
                salary.setLoan (loan);
                isChange=true;
                taskService.save (taskList);
            }
        }
        if (isChange) {
            CommonUtils.calSalary(salary, null, DateUtil.getDateNum (salary.getYear (),salary.getMonth ()));
            if(isAdd){
                salaries.add(salary);
            }else{
                actSalaryService.save (salary);
            }

        }
    }



    @Resource
    private ActSalaryService actSalaryService;
    @Resource
    private UserService userService;

    private final String[] TASK_PROPERTIES = new String[]{"deptName", "workNo", "username", "late", "debit", "due", "loan", "other", "otherEl", "taskDate", "note"};

    private static final String DEFAULT_SORT_FIELD = "taskDate";
}
