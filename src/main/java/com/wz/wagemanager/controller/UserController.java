package com.wz.wagemanager.controller;

import com.wz.wagemanager.entity.SysRole;
import com.wz.wagemanager.entity.SysUser;
import com.wz.wagemanager.security.CustomUsernamePasswordToken;
import com.wz.wagemanager.service.DeptService;
import com.wz.wagemanager.service.RoleService;
import com.wz.wagemanager.service.UserService;
import com.wz.wagemanager.tools.*;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.interceptor.LogInterceptor;
import org.activiti.engine.impl.interceptor.RetryInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.security.access.method.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author WindowsTen
 */
@RestController
@RequestMapping("user")
public class UserController extends BaseExceptionController {

    @Resource
    private UserService userService;

    @PostMapping("newpsd.json")
    public PageBean newPassword(
            @RequestParam(value = "password")String password,
            @RequestParam(value = "newPassword")String newPassword,
            @RequestParam(value = "repeatPsd")String repeatPsd
    ){
        String userId=ContextHolderUtils.getPrincipal ().getId ();
        SysUser user = userService.getUserById (userId);
        Assert.assertNotNull ("用户不存在",user);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(4);
        Assert.assertTrue ("密码错误",password!=null&&passwordEncoder.matches(password,user.getPassword()));
        Assert.assertTrue ("两次密码不相等",newPassword.equals (repeatPsd));
        user.setPassword (passwordEncoder.encode (newPassword));
        userService.updateUser (user);
        return new PageBean<> ();
    }

    @PostMapping("list.json")
    public PageBean<List<SysUser>> listUser(
            @RequestParam(value = "pageSize",defaultValue = GlobalConstant.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "curPage",defaultValue = GlobalConstant.DEFUALT_CUR_PAGE) int curPage,
            @RequestParam(value = "deptId",required = false)String deptId,
            @RequestParam(value = "roleId",required = false)String roleId,
            @RequestParam(value = "username",required = false)String username
    ){
        Pageable pageRequest = PageUtil.pageable (curPage,pageSize);
        org.springframework.data.domain.Page<SysUser> userPage = userService.findByPage(deptId, roleId,username, pageRequest);
        Page page = PageUtil.getPage (userPage.getTotalElements(), pageSize, curPage);
        return new PageBean<>(page,userPage.getContent());
    }

    @PostMapping("update.json")
    public PageBean updateById(@ModelAttribute SysUser user) throws IllegalAccessException {
        userService.updateUserByProperties (user);
        return new PageBean<>();
    }

    @PostMapping("delete.json")
    public PageBean removeByIds(
            @RequestParam(value = "ids")String ids
    ){
        userService.removeByIds(ids.split(","));
        return new PageBean();
    }
    @Resource
    private RoleService roleService;
    @Resource
    private DeptService deptService;
    private static final String defaultPassword="123456";
    @PostMapping(value = "insert.json")
    public PageBean insertDept(@ModelAttribute SysUser user){
        String id = user.getId();
        if(StringUtils.isBlank(id)){
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(4);
            user.setPassword (passwordEncoder.encode (defaultPassword));
            user.setCreateDate(new Date());
            user.setCreateUser(ContextHolderUtils.getPrincipal().getUsername());
        }else{
            SysUser sysUser = userService.getUserById(id);
            sysUser.setUsername(user.getUsername());
            sysUser.setWorkNo(user.getWorkNo());
            sysUser.setUpdateDate(new Date());
            sysUser.setUpdateUser(ContextHolderUtils.getPrincipal().getUsername());
            sysUser.setStatus(user.getStatus());
            sysUser.setSysDept(user.getSysDept());
            sysUser.setSysRole(user.getSysRole());
            user=sysUser;
        }
        user.setSysRole(roleService.findRoleById(user.getSysRole().getId()));
        user.setSysDept(deptService.findById(user.getSysDept().getId()));
        userService.insertUser (user);
        return new PageBean();
    }

}
