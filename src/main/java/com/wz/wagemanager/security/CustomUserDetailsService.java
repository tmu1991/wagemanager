package com.wz.wagemanager.security;

import com.wz.wagemanager.entity.SysUser;
import com.wz.wagemanager.exception.HandThrowException;
import com.wz.wagemanager.service.UserService;
import com.wz.wagemanager.tools.ThreadLocalUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    //业务服务类
    @Resource
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        //SysUser对应数据库中的用户表，是最终存储用户和密码的表，可自定义
        //本例使用SysUser中的name作为用户名:
        String deptId = ThreadLocalUtil.get();
        SysUser user = userService.getByUsernameAndSysDept(userName,deptId);
        ThreadLocalUtil.clear ();
        if (user == null || user.getSysDept ()!=null && !user.getSysDept ().getId ().equals (deptId)) {
//            throw new UsernameNotFoundException("用户不存在");
            throw new HandThrowException("请核实用户名和部门");
        }
//        for(SysUser sysUser:users){
//            if(deptId.equals(sysUser.getSysDept().getId())){
//                return sysUser;
//            }
//        }
        return user;
    }

}