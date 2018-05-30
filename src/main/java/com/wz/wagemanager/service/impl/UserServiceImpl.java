package com.wz.wagemanager.service.impl;

import com.wz.wagemanager.annotation.OperInfo;
import com.wz.wagemanager.annotation.OperationType;
import com.wz.wagemanager.dao.UserRepository;
import com.wz.wagemanager.entity.SysUser;
import com.wz.wagemanager.service.DeptService;
import com.wz.wagemanager.service.RoleService;
import com.wz.wagemanager.service.UserService;
import com.wz.wagemanager.tools.CommonUtils;
import com.wz.wagemanager.tools.ContextHolderUtils;
import com.wz.wagemanager.tools.CriteriaUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.*;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService{

    @Resource
    private UserRepository userRepository;
    @Override
    public SysUser getByUsernameAndSysDept(String username, String deptId) {
        if(deptId == null){
            return userRepository.findByUsername (username);
        }else{
            return userRepository.getByUsernameAndSysDept (username,deptId);
        }
    }

    @Override
    public SysUser getUserById(String id) {
        return userRepository.getUserById(id);
    }

    @Override
    @OperInfo (type = OperationType.UPDATE,desc = "重置密码")
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public void updateUser(SysUser sysUser) {
        userRepository.save (sysUser);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public void updateUserByProperties (SysUser sysUser) throws IllegalAccessException {
        SysUser user = getUserById (sysUser.getId ());
        CommonUtils.copyProperties (sysUser,user,updateProperties);
        sysUser.setUpdateDate(new Date());
        sysUser.setUpdateUser(ContextHolderUtils.getPrincipal().getUsername());
        sysUser.setSysRole(roleService.findRoleById(user.getSysRole().getId()));
        sysUser.setSysDept(deptService.findById(user.getSysDept().getId()));
        userRepository.save (sysUser);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public void deleteUser(String id) {
        userRepository.delete(id);
    }

    @Override
    public SysUser getUserByUsernameAndWorkNo(String username, String workNo) {
        return userRepository.findByUsernameAndWorkNo (username,workNo);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public void batchInsert(List<SysUser> list) {
        userRepository.save (list);
    }

    @Override
    public SysUser findByWorkNo (String workNo) {
        return userRepository.findByWorkNo(workNo);
    }

    @Override
    public List<SysUser> findAll() {
        return userRepository.findAll();
    }
    @Resource
    private RoleService roleService;
    @Resource
    private DeptService deptService;
    @Override
    public Page<SysUser> findByPage (String deptId, String roleId,String username, Pageable pageRequest) {
        Map<String,Object> criteraMap=new HashMap<>(3);
        criteraMap.put("sysRole",roleService.findRoleById(roleId));
        criteraMap.put("sysDept",deptService.findById(deptId));
        criteraMap.put("username",username);
        return userRepository.findAll(CriteriaUtils.getSpe(criteraMap),pageRequest);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public void removeByIds(String[] ids) {
        userRepository.deleteByIdIn(ids);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public void insertUser(SysUser sysUser) {
        userRepository.save (sysUser);
//        userRepository.insertSysUser(sysUser);
    }

    private static final List<String> updateProperties= Arrays.asList ("username","workNo","sysDept","sysRole","status");
}
