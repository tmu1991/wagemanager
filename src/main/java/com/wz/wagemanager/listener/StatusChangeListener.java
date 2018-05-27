package com.wz.wagemanager.listener;

import com.wz.wagemanager.entity.SysDeclare;
import com.wz.wagemanager.entity.SysDept;
import com.wz.wagemanager.service.DeclareService;
import com.wz.wagemanager.service.DeptService;
import com.wz.wagemanager.tools.BeanUtils;
import org.activiti.engine.delegate.*;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author WindowsTen
 * @date 2018/5/9 17:29
 * @description
 */

public class StatusChangeListener implements TaskListener {

    @Override
    @Transactional
    public void notify (DelegateTask delegateTask) {
        String processInstanceId = delegateTask.getProcessInstanceId ();
        SysDeclare declare = getDeclareService ().findByProcessInstanceId (processInstanceId);
        declare.setStatus (Integer.parseInt (declareStatus.getExpressionText ()));
        getDeclareService ().save (declare);
        SysDept dept = declare.getDept();
        dept.setStatus(Integer.parseInt(deptStatus.getExpressionText()));
        getDeptService().save(dept);
    }

    private DeclareService getDeclareService(){
        return (DeclareService) BeanUtils.getObject ("declareService");
    }

    private DeptService getDeptService(){
        return (DeptService) BeanUtils.getObject ("deptService");
    }

    private Expression declareStatus;

    private Expression deptStatus;

    public StatusChangeListener(){}

    public StatusChangeListener(Expression declareStatus,Expression deptStatus){
        this.declareStatus=declareStatus;
        this.deptStatus=deptStatus;
    }
}
