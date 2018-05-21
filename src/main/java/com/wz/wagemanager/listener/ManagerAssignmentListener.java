package com.wz.wagemanager.listener;

import com.wz.wagemanager.entity.SysRole;
import com.wz.wagemanager.entity.SysUser;
import com.wz.wagemanager.service.RoleService;
import com.wz.wagemanager.service.UserService;
import com.wz.wagemanager.tools.BeanUtils;
import com.wz.wagemanager.tools.ContextHolderUtils;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author WindowsTen
 * @date 2018/5/9 13:06
 * @description 指派分经理
 */

public class ManagerAssignmentListener implements TaskListener {

    private static final String roleAlias = "ROLE_MANAGER";

    @Override
    public void notify (DelegateTask delegateTask) {
        String roleId = delegateTask.getVariable ("applyRoleId").toString ();
        SysRole role = getRoleService ().findRoleById (roleId);
        SysRole sysRole = getRoleService ().findByAliasAndDept (roleAlias, role.getDept ());
        delegateTask.addCandidateGroup (sysRole.getId ());
    }

    private RoleService getRoleService(){
        return (RoleService) BeanUtils.getObject ("roleService");
    }

}
