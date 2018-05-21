package com.wz.wagemanager.dao;


import com.wz.wagemanager.entity.SysDept;
import com.wz.wagemanager.entity.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<SysRole,String> {
    SysRole findSysRoleById(String id);
    SysRole findSysRoleByRoleName(String roleName);
    SysRole findByRoleAliasAndDept(String roleAlias, SysDept dept);
}
