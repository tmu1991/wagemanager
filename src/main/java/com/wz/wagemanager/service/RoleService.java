package com.wz.wagemanager.service;

import com.wz.wagemanager.entity.SysDept;
import com.wz.wagemanager.entity.SysRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService {
    SysRole findRoleById(String id);
    SysRole findRoleByRoleName(String roleName);
    SysRole insertSysRole(SysRole sysRole);
    SysRole findByAliasAndDept(String roleAlias, SysDept dept);
    List<SysRole> findAll();

    void save(SysRole role);

    void deleteById(String roleId);

    Page<SysRole> findByPage(Pageable pageable);
}
