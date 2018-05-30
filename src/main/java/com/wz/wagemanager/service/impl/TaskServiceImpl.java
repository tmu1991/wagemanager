package com.wz.wagemanager.service.impl;

import com.wz.wagemanager.dao.TaskRepository;
import com.wz.wagemanager.entity.ActForm;
import com.wz.wagemanager.entity.ActSalary;
import com.wz.wagemanager.entity.ActTask;
import com.wz.wagemanager.entity.SysUser;
import com.wz.wagemanager.service.ActSalaryService;
import com.wz.wagemanager.service.TaskService;
import com.wz.wagemanager.service.UserService;
import com.wz.wagemanager.tools.Assert;
import com.wz.wagemanager.tools.CommonUtils;
import com.wz.wagemanager.tools.DateUtil;
import com.wz.wagemanager.tools.ExcelUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author WindowsTen
 */
@Service
@Transactional (rollbackFor = Exception.class)
public class TaskServiceImpl implements TaskService {

    @Resource
    private TaskRepository taskRepository;

    @Override
    public Page<ActTask> findByPage(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    @Override
    public List<ActTask> findBySalaryId (String salaryId) {
        return taskRepository.findBySalaryId (salaryId);
    }

    @Override
    public ActTask findById (String id) {
        return taskRepository.findOne (id);
    }

    @Override
    public BigDecimal sumBySalaryAndType (String salaryId, Integer type) {
        return taskRepository.sumBySalaryAndType (salaryId,type);
    }

    @Override
    public ActTask findByTaskDateAndWorkNoAndType(String taskDate, String workNo, Integer type) {
        return taskRepository.findByTaskDateAndWorkNoAndType(taskDate,workNo,type);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public void save(ActTask task) {
        taskRepository.save(task);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public void save(List<ActTask> tasks) {
        taskRepository.save(tasks);
    }


    @Override
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public void upload (String filePath) throws Exception {
        ExcelUtil.readForm (filePath).forEach (this::oprForm);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public void update (ActForm actForm) {
        this.oprForm (actForm);
    }

    @Override
    public List<ActTask> findByDeptId (String deptId) {
        return taskRepository.findByDeptId (deptId);
    }

    @Override
    public void charged (String deptId, List<String> ids) {
        taskRepository.updateStatus (ids,0);
    }

    @Resource
    private ActSalaryService actSalaryService;
    @Resource
    private UserService userService;

    private void oprForm(ActForm form) {

        //根据工号取到用户
        SysUser user = userService.findByWorkNo(form.getWorkNo());
        Assert.assertNotNull("工号[" + form.getWorkNo() + "]的用户不存在", user);
        Assert.assertTrue("工号[" + form.getWorkNo() + "]与用户名不符，请核实", user.getUsername().equals(form.getUsername()));
        Assert.assertTrue("工号[" + form.getWorkNo() + "]部门与当前用户部门不符，请核实",
                user.getSysDept() != null && form.getDeptName().equals(user.getSysDept().getDeptName()));
        form.setDeptId (user.getSysDept ().getId ());

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
        if(!CollectionUtils.isEmpty (formTask)){
            List<ActTask> taskList=new ArrayList<> ();
            boolean isTask=false;
            for(ActTask actTask:formTask){
                if(actTask.getAmount () == null){
                    continue;
                }
                Assert.assertTrue ("扣款日期不能为空", org.apache.commons.lang3.StringUtils.isNotBlank (actTask.getTaskDate ()));
                actTask.setTaskDate (actTask.getTaskDate ().trim ());
                if(org.apache.commons.lang3.StringUtils.isNotBlank (actTask.getNote ())){
                    actTask.setNote (actTask.getNote ().trim ());
                }
                ActTask taskDateAndWorkNoAndType = findByTaskDateAndWorkNoAndType (actTask.getTaskDate (),form.getWorkNo (),actTask.getType ());
                if(taskDateAndWorkNoAndType!=null){
                    if(taskDateAndWorkNoAndType.getAmount ().compareTo (actTask.getAmount ())==0
                            &&conNote (taskDateAndWorkNoAndType.getNote (),actTask.getNote ())){
                        continue;
                    }
                    taskDateAndWorkNoAndType.setAmount (actTask.getAmount ());
                    taskDateAndWorkNoAndType.setNote (actTask.getNote ());
                    actTask=taskDateAndWorkNoAndType;
                }else{
                    Assert.assertTrue ("扣款金额不能为空",actTask.getAmount ()!=null);
                    actTask.setDeptId (form.getDeptId ());
                    actTask.setDeptName (form.getDeptName ());
                    actTask.setUsername (form.getUsername ());
                    actTask.setWorkNo (form.getWorkNo ());
                    actTask.setSalaryId (salary.getId ());
                }
                if(actTask == null || actTask.getAmount ().compareTo (BigDecimal.ZERO) == 0){
                    taskRepository.delete (actTask);
                }else{
                    taskList.add (actTask);
                }
                isTask=true;
            }
            if(isTask){
                isChange=true;
                taskRepository.save (taskList);
                BigDecimal bigDecimal = sumBySalaryAndType (salary.getId (), 1);
                salary.setOtherDebit (bigDecimal==null?BigDecimal.ZERO:bigDecimal);
                BigDecimal decimal = sumBySalaryAndType (salary.getId (), 0);
                salary.setLoan (decimal==null?BigDecimal.ZERO:decimal);
            }
        }
        if (isChange) {
            CommonUtils.calSalary(salary, null, DateUtil.getDateNum (salary.getYear (),salary.getMonth ()));
            actSalaryService.save (salary);
        }
    }

    private boolean conNote(String note1,String note2){
        if(org.apache.commons.lang3.StringUtils.isBlank (note1)
                &&org.apache.commons.lang3.StringUtils.isNotBlank (note2)){
            return false;
        }else if(org.apache.commons.lang3.StringUtils.isBlank (note2)
                &&org.apache.commons.lang3.StringUtils.isNotBlank (note1)){
            return false;
        }else if(org.apache.commons.lang3.StringUtils.isBlank (note1)
                &&org.apache.commons.lang3.StringUtils.isBlank (note2)){
            return true;
        }
        return note1.equals (note2);
    }
}
