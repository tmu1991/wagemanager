package com.wz.wagemanager.listener;

import com.wz.wagemanager.entity.*;
import com.wz.wagemanager.service.*;
import com.wz.wagemanager.tools.BeanUtils;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.Expression;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author WindowsTen
 * @date 2018/5/17 9:50
 * @description
 */

public class EndListener implements ExecutionListener {

    @Override
    @Transactional (propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public void notify (DelegateExecution execution) {
        SysDeclare declare = getDeclareService ().findByProcessInstanceId (execution.getProcessInstanceId ());
        declare.setStatus (2);
        String declareId = declare.getId ();
        SysDept dept = declare.getDept();
        String deptId = dept.getId ();
        dept.setStatus(1);
        getDeptService().save(dept);
        getDeclareService().save(declare);
        getHiSalaryService ().mutilSave (getSalaries (declareId));
        getActSalaryService ().deleteByDeclareId (declareId);
        getTaskService ().saveHi (getTasks (deptId));
        getTaskService ().deleteByStatus (0,deptId);
    }

    private List<HiTask> getTasks(String deptId){
        return getTaskService ().findByStatus (0,deptId)
                .stream ().map (actTask ->{
                    HiTask hiTask=new HiTask ();
                    org.springframework.beans.BeanUtils.copyProperties (actTask,hiTask);
                    hiTask.setLoanDate (new Date ());
                    return hiTask;
                }).collect (Collectors.toList ());
    }

    private List<HiSalary> getSalaries(String declareId){
        return getActSalaryService ().findByDeclareId (declareId)
                .stream ().map (actSalary ->{
            HiSalary hiSalary = new HiSalary ();
            org.springframework.beans.BeanUtils.copyProperties (actSalary,hiSalary);
            hiSalary.setCreateDate (new Date ());
            return hiSalary;
        }).collect (Collectors.toList ());
    }

    private DeptService getDeptService(){
        return (DeptService) BeanUtils.getObject ("deptService");
    }

    private DeclareService getDeclareService(){
        return (DeclareService) BeanUtils.getObject ("declareService");
    }

    private HiSalaryService getHiSalaryService(){
        return (HiSalaryService) BeanUtils.getObject ("hiSalaryService");
    }

    private ActSalaryService getActSalaryService(){
        return (ActSalaryService) BeanUtils.getObject ("actSalaryService");
    }

    private TaskService getTaskService(){
        return (TaskService) BeanUtils.getObject ("taskServiceImpl");
    }
}
