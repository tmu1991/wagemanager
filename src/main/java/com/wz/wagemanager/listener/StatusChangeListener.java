package com.wz.wagemanager.listener;

import com.wz.wagemanager.entity.SysDeclare;
import com.wz.wagemanager.service.DeclareService;
import com.wz.wagemanager.tools.BeanUtils;
import org.activiti.engine.delegate.*;

/**
 * @author WindowsTen
 * @date 2018/5/9 17:29
 * @description
 */

public class StatusChangeListener implements TaskListener {

    @Override
    public void notify (DelegateTask delegateTask) {
        String processInstanceId = delegateTask.getProcessInstanceId ();
        SysDeclare declare = getDeclareService ().findByProcessInstanceId (processInstanceId);
        declare.setStatus (3);
        getDeclareService ().save (declare);
    }

    private DeclareService getDeclareService(){
        return (DeclareService) BeanUtils.getObject ("declareService");
    }

}
