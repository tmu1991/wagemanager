package com.wz.wagemanager.service.impl;

import com.wz.wagemanager.dao.RoleRepository;
import com.wz.wagemanager.entity.SysDept;
import com.wz.wagemanager.entity.SysRole;
import com.wz.wagemanager.service.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("roleService")
@Transactional
public class RoleServiceImpl implements RoleService {
    @Resource
    private RoleRepository roleRepository;
    @Override
    public SysRole findRoleById(String id) {
        return roleRepository.findSysRoleById(id);
    }

    @Override
    public SysRole findRoleByRoleName(String roleName) {
        return roleRepository.findSysRoleByRoleName(roleName);
    }

    @Override
    @Transactional (propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public SysRole insertSysRole(SysRole sysRole) {
        return roleRepository.save(sysRole);
    }

    @Override
    public SysRole findByAliasAndDept (String roleAlias, SysDept dept) {
        return roleRepository.findByRoleAliasAndDept (roleAlias,dept);
    }

    @Override
    public List<SysRole> findAll () {
        return roleRepository.findAll ();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public void save (SysRole role) {
        roleRepository.save (role);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public void deleteById (String roleId) {
        roleRepository.delete (roleId);
    }

    @Override
    public Page<SysRole> findByPage (Pageable pageable) {
        return roleRepository.findAll (pageable);
    }
}
