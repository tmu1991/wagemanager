package com.wz.wagemanager.service.impl;

import com.wz.wagemanager.annotation.OperInfo;
import com.wz.wagemanager.annotation.OperationType;
import com.wz.wagemanager.dao.UserRepository;
import com.wz.wagemanager.entity.ActSalary;
import com.wz.wagemanager.entity.SysDept;
import com.wz.wagemanager.entity.SysRole;
import com.wz.wagemanager.entity.SysUser;
import com.wz.wagemanager.service.DeptService;
import com.wz.wagemanager.service.LogService;
import com.wz.wagemanager.service.RoleService;
import com.wz.wagemanager.service.UserService;
import com.wz.wagemanager.tools.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service("userService")
@Transactional(rollbackFor = Exception.class)
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
    @Resource
    private LogService logService;
    @Override
    public SysUser getUserById(String id) {
        return userRepository.getUserById(id);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public void updateUser(SysUser sysUser) {
        userRepository.save (sysUser);
        new LogUtils (logService).save (OperationType.UPDATE,"重置密码",null);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    @OperInfo (type = OperationType.UPDATE,desc = "修改员工信息")
    public void updateUserByProperties (SysUser sysUser) throws IllegalAccessException {
        SysUser user = getUserById (sysUser.getId ());
        Map<String, Object> objectMap = CommonUtils.copyProperties (sysUser, user, updateProperties);
        if(!sysUser.getSysRole ().getId ().equals (user.getSysRole ().getId ())){
            SysRole sysRole = roleService.findRoleById (user.getSysRole ().getId ());
            objectMap.put ("角色",sysRole.getRoleName ());
        }else{
            sysUser.setSysRole(user.getSysRole ());
        }
        if(!sysUser.getSysDept ().getId ().equals (user.getSysDept ().getId ())){
            SysDept sysDept = deptService.findById (user.getSysDept ().getId ());
            objectMap.put ("部门",sysDept.getDeptName ());
        }else{
            sysUser.setSysDept (user.getSysDept ());
        }
        sysUser.setUpdateDate(new Date());
        sysUser.setUpdateUser(ContextHolderUtils.getPrincipal().getUsername());
        userRepository.save (sysUser);
        Object o = objectMap.get ("用户状态");
        if(o != null){
            objectMap.put ("用户状态",o.equals (0)?"禁用":"正常");
        }
        new LogUtils (logService).save (OperationType.UPDATE,objectMap,getOperMap (user));
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
        Assert.assertTrue ("删除用户不能为空",ids!=null&&ids.length>0);
        List<Map<String, Object>> mapList = Arrays.stream (ids).filter (StringUtils::isNotBlank)
                .map (id -> getOperMap (getUserById (id))).collect (Collectors.toList ());
        userRepository.removeByIdIn(ids);
        new LogUtils (logService).save (OperationType.DELETE,null,mapList);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public void insertUser(SysUser sysUser) {
        SysRole sysRole = sysUser.getSysRole ();
        SysDept sysDept = sysUser.getSysDept ();
        Assert.assertTrue ("员工姓名,考勤编号,角色不能为空",
                StringUtils.isNotBlank (sysUser.getUsername ())
                &&StringUtils.isNotBlank (sysUser.getWorkNo ())
                && sysRole !=null
        );
        Assert.assertNull ("工号已存在",getUserById(sysUser.getWorkNo ()));
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(4);
        sysUser.setPassword (passwordEncoder.encode (defaultPassword));
        sysUser.setCreateDate(new Date());
        sysUser.setCreateUser(ContextHolderUtils.getPrincipal().getUsername());
        sysRole = roleService.findRoleById (sysRole.getId ());
        sysUser.setSysRole(sysRole);
        sysDept = deptService.findById(sysDept.getId());
        sysUser.setSysDept(sysDept);
        userRepository.save (sysUser);
        new LogUtils (logService).save (OperationType.ADD,getOperMap (sysUser),null);
    }

    @Override
    public void updateLoginInfo(SysUser sysUser){
        userRepository.save (sysUser);
    }

    private static final String defaultPassword="123456";

    private static final List<String> updateProperties= Arrays.asList ("username","workNo","sysDept","sysRole","status");

    private Map<String,Object> getOperMap(SysUser user){
        Map<String,Object> map= new HashMap<> (3);
        map.put ("员工姓名",user.getUsername ());
        map.put ("工号",user.getWorkNo ());
        if(user.getSysDept ()!=null){
            map.put ("部门",user.getSysDept ().getDeptName ());
        }
        if(user.getSysRole ()!=null){
            map.put ("角色",user.getSysRole ().getRoleName ());
        }
        return map;
    }
}
