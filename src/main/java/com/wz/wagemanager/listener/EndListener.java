package com.wz.wagemanager.listener;

import com.wz.wagemanager.entity.ActSalary;
import com.wz.wagemanager.entity.HiSalary;
import com.wz.wagemanager.entity.SysDeclare;
import com.wz.wagemanager.service.ActSalaryService;
import com.wz.wagemanager.service.DeclareService;
import com.wz.wagemanager.service.HiSalaryService;
import com.wz.wagemanager.tools.BeanUtils;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.Expression;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author WindowsTen
 * @date 2018/5/17 9:50
 * @description
 */

public class EndListener implements ExecutionListener {

    @Override
    public void notify (DelegateExecution execution) {
        SysDeclare declare = getDeclareService ().findByProcessInstanceId (execution.getProcessInstanceId ());
        declare.setStatus (2);
        List<ActSalary> salaryList = getActSalaryService ().findByDeclareId (declare.getId ());
        List<HiSalary> hiSalaries=new ArrayList<> (salaryList.size ());
        salaryList.forEach (actSalary ->{
            HiSalary hiSalary = new HiSalary ();
            org.springframework.beans.BeanUtils.copyProperties (actSalary,hiSalary);
            hiSalary.setCreateDate (new Date ());
            hiSalaries.add (hiSalary);
        });
        getDeclareService().save(declare);
        getActSalaryService ().deleteAll(salaryList);
        getHiSalaryService ().mutilSave (hiSalaries);
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
}
