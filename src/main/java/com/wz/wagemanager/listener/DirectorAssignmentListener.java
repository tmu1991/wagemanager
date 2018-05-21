package com.wz.wagemanager.listener;

import com.wz.wagemanager.entity.SysRole;
import com.wz.wagemanager.service.RoleService;
import com.wz.wagemanager.tools.BeanUtils;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * @author WindowsTen
 * @date 2018/5/9 13:02
 * @description 指派分厂长
 */

public class DirectorAssignmentListener implements TaskListener{

    private static final String roleAlias = "ROLE_DIRE";

    @Override
    public void notify (DelegateTask delegateTask) {
        //获取提交申请的用户
        String roleId = delegateTask.getVariable ("applyRoleId").toString ();
        SysRole role = getRoleService ().findRoleById (roleId);
        SysRole sysRole = getRoleService ().findByAliasAndDept (roleAlias, role.getDept ());
        delegateTask.addCandidateGroup (sysRole.getId ());
    }

    private RoleService getRoleService(){
        return (RoleService) BeanUtils.getObject ("roleService");
    }

}
