package com.wz.wagemanager.service;

import com.wz.wagemanager.entity.SysUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService{

    SysUser getByUsernameAndSysDept(String username, String deptId);

    SysUser getUserById(String id);

    void updateUser(SysUser sysUser);
    void updateUserByProperties(SysUser sysUser) throws IllegalAccessException;

    void insertUser(SysUser sysUser);

    void deleteUser(String id);

    SysUser getUserByUsernameAndWorkNo(String username, String workNo);

    void batchInsert(List<SysUser> list);

    SysUser findByWorkNo(String workNo);

    List<SysUser> findAll();

    Page<SysUser> findByPage(String deptId, String roleId, String username, Pageable pageRequest);

    void removeByIds(String[] ids);
}
