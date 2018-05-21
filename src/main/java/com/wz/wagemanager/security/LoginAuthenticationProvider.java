package com.wz.wagemanager.security;

import com.wz.wagemanager.entity.SysDept;
import com.wz.wagemanager.entity.SysUser;
import com.wz.wagemanager.tools.Assert;
import com.wz.wagemanager.tools.ThreadLocalUtil;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;

public class LoginAuthenticationProvider extends DaoAuthenticationProvider {

    public LoginAuthenticationProvider(UserDetailsService userDetailsService){
        setUserDetailsService(userDetailsService);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        CustomUsernamePasswordToken token = (CustomUsernamePasswordToken) authentication;
//        String deptId = token.getSysUser().getSysDept().getId();
//        ThreadLocalUtil.set(deptId);
        return super.authenticate (authentication);
//        Object password = authentication.getCredentials();
//        String username = authentication.getName();
//        SysUser user = this.retrieveUser (username,authentication);
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(4);
//        if(password==null||!passwordEncoder.matches(password.toString(),user.getPassword())){
//            throw new RuntimeException("密码错误");
//        }
//        Collection<? extends GrantedAuthority> authorities = user.getAuthorities ();
//        SysDept sysDept = user.getSysDept();
//        Assert.assertTrue("请核实部门",deptId.equals(sysDept.getId()));
//        // 构建返回的用户登录成功的token
//        return new CustomUsernamePasswordToken (token, password, authorities,user);
    }
}
